package com.fintech.core;

import com.fintech.core.exception.InsufficientFundsException;
import com.fintech.core.exception.NoPermissionException;
import com.fintech.dao.AccountDAO;
import com.fintech.dao.TransferDAO;
import com.fintech.domain.Account;
import com.fintech.domain.Transfer;
import com.fintech.enums.TransferStatus;
import lombok.AllArgsConstructor;

import javax.annotation.PreDestroy;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author d.mikheev 08.05.19
 */
public class SimpleTransferService implements TransferService {
    public static int SHUTDOWN_DELAY_SEC = 10;
    public static int REPEAT_TRANSACTION_ATTEMPTS = 10;
    private AccountDAO accountDAO;
    private TransferDAO transferDAO;
    private ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);

    public SimpleTransferService(AccountDAO accountDAO, TransferDAO transferDAO) {
        this.accountDAO = accountDAO;
        this.transferDAO = transferDAO;
    }

    public TransferStatus get(String id) {
        Transfer transfer = transferDAO.get(id);
        if (transfer==null)
            throw new NoSuchElementException(String.format("Transfer with id %s not exists",id));
        return TransferStatus.valueOf(transfer.getStatus());
    }

    private Account checkSender(Long clientId, String acc, Long amount) throws NoPermissionException, InsufficientFundsException {
        Account account = accountDAO.get(acc);
        if (account == null)
            throw new NoSuchElementException(String.format("Sender account %s doesn't exist", acc));
        if (!account.getClientid().equals(clientId))
            throw new NoPermissionException(String.format("Operation not avalible for clientId %s", clientId));
        if (account.getAmount() < amount)
            throw new InsufficientFundsException(String.format("Not enought funds, avalible %s, required %s", account.getAmount(), amount));
        return account;
    }

    private Account checkReceiver(String acc) {
        Account account = accountDAO.get(acc);
        if (account == null)
            throw new NoSuchElementException(String.format("Receiver account %s doesn't exist", acc));
        return account;
    }

    @Override
    public String processTransfer(Long clientId, String accFrom, String accTo, Long amount) throws NoPermissionException, InsufficientFundsException {
        Account sender = checkSender(clientId, accFrom, amount);
        Account receiver = checkReceiver(accTo);

        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        transferDAO.init(uuid, accFrom, accTo, amount);

        executor.execute(new TransferTask(uuid, sender, receiver, amount, 1));
        return uuid;
    }

    @PreDestroy
    public void finish() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(SHUTDOWN_DELAY_SEC, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }

    }

    @AllArgsConstructor
    private class TransferTask implements Runnable {
        private String uuid;
        private Account sender;
        private Account receiver;
        private Long amount;
        private int attempt;

        @Override
        public void run() {
            boolean success = accountDAO.sendFunds(sender, receiver, amount);
            if (success) {
                transferDAO.updateStatus(uuid, TransferStatus.SUCCESSFUL.getVal());
                return;
            }
            transferDAO.updateStatus(uuid, TransferStatus.FAILED.getVal());
            if (attempt < REPEAT_TRANSACTION_ATTEMPTS) // infinity check
            {
                try {
                    executor.execute(new TransferTask(uuid, checkSender(sender.getClientid(),sender.getId(),amount), checkReceiver(receiver.getId()), amount, attempt + 1));
                } catch (NoPermissionException e) {
                } catch (InsufficientFundsException e) {
                    //other transaction "steal" money from acc
                }
            }
        }
    }

}
