package com.fintech.core;

import com.fintech.core.exception.InsufficientFundsException;
import com.fintech.core.exception.NoPermissionException;
import com.fintech.dao.AccountDAO;
import com.fintech.dao.impl.AccountDAOJdbc;
import com.fintech.model.Account;

import javax.annotation.PostConstruct;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author d.mikheev 08.05.19
 */
public class SimpleTransferService implements TransferService {
    private AccountDAO accountDAO;
    private ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);

    public SimpleTransferService(AccountDAOJdbc accountDAO) {
        this.accountDAO = accountDAO;
    }

    public Account get(String id) {
        return accountDAO.get(id);
    }

    @Override
    public void save() {
        try {
//            accountDAO.save(new Account("acc1",100l,null));
//            Account acc = accountDAO.find("acc1");
//            System.out.println(acc.getAmount());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Account checkSender(Long clientId, String acc, Long amount) throws NoPermissionException, InsufficientFundsException {
        Account account = accountDAO.get(acc);
        if (account == null)
            throw new NoSuchElementException(String.format("Account %s doesn't exist", acc));
        if (!account.getClientid().equals(clientId))
            throw new NoPermissionException(String.format("Operation not avalible clientId %s", clientId));
        if (account.getAmount() < amount)
            throw new InsufficientFundsException(String.format("Not enought funds, avalible %s, required %s", account.getAmount(), amount));
        return account;
    }

    private Account checkReceiver(String acc) {
        Account account = accountDAO.get(acc);
        if (account == null)
            throw new NoSuchElementException(String.format("Account %s doesn't exist", acc));
        return account;
    }

    @Override
    public String processTransfer(Long clientId, String accFrom, String accTo, Long amount) throws NoPermissionException, InsufficientFundsException {
        Account sender = checkSender(clientId, accFrom, amount);
        Account receiver = checkReceiver(accTo);

        String uuid = UUID.randomUUID().toString();
        //TODO:Save transfer

        Runnable transferTask = () -> {
            accountDAO.sendFunds(sender, receiver, amount);
        };

        executor.execute(transferTask);
        return uuid;
    }

    /**
     * Initialize service.
     */
    @PostConstruct
    public void init() {
    }

    /**
     * Terminate service.
     */
//    @PreDestroy
//    public void shutdown() {
//        executor.shutdownNow();
//        transferQueue.clear();
//    }
//
//    private void processQueue() {
//        while (transferQueue.size() > 0) {
////            List<Transfer> transfers = getBatch();
//
//        }
//    }
//
//    public void startTransaction(Transfer transfer) {
//        transferQueue.add(transfer);
//    }
//
//    public void addNewTransfer(Transfer transfer) {
//        transferQueue.add(transfer);
//    }


//    private List<Transfer> getBatch() {
//        List<Transfer> batch = new ArrayList<>();
//        Transfer nextTransfer;
//        for (int i = 0; i < BATCH_SIZE; i++) {
//            nextTransfer = transferQueue.poll();
//            if (nextTransfer == null)
//                break;
//            batch.add(nextTransfer);
//        }
//        return batch;
//    }

}
