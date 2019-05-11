package com.fintech.core;

import com.fintech.core.exception.InsufficientFundsException;
import com.fintech.core.exception.NoPermissionException;
import com.fintech.dao.AccountDAO;
import com.fintech.dao.TransferDAO;
import com.fintech.domain.Account;
import com.fintech.enums.TransferStatus;

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
    public static int EXECUTOR_SHUTDOWN_DELAY_SEC = 10;
    private AccountDAO accountDAO;
    private TransferDAO transferDAO;
    private ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);

    public SimpleTransferService(AccountDAO accountDAO, TransferDAO transferDAO) {
        this.accountDAO = accountDAO;
        this.transferDAO = transferDAO;
    }

    public TransferStatus get(String id) {
        return TransferStatus.valueOf(transferDAO.get(id).getStatus());
    }


    private Account checkSender(Long clientId, String acc, Long amount) throws NoPermissionException, InsufficientFundsException {
        Account account = accountDAO.get(acc);
        if (account == null)
            throw new NoSuchElementException(String.format("Sender account %s doesn't exist", acc));
        if (!account.getClientid().equals(clientId))
            throw new NoPermissionException(String.format("Operation not avalible clientId %s", clientId));
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

        String uuid = UUID.randomUUID().toString();
        transferDAO.init(uuid, accFrom, accTo, amount);

        Runnable transferTask = () -> {
            boolean success = accountDAO.sendFunds(sender, receiver, amount);
            if (success) {
                transferDAO.updateStatus(uuid, TransferStatus.SUCCESSFUL.getVal());
                return;
            }
            transferDAO.updateStatus(uuid, TransferStatus.FAILED.getVal());
        };

        executor.execute(transferTask);
        return uuid;
    }

    @PreDestroy
    public void finish() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(EXECUTOR_SHUTDOWN_DELAY_SEC, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }

    }

}
