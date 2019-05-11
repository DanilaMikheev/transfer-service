package com.fintech.core;

import com.fintech.core.exception.InsufficientFundsException;
import com.fintech.core.exception.NoPermissionException;
import com.fintech.model.Account;

/**
 * @author d.mikheev on 09.05.19
 */
public interface TransferService {

    public String processTransfer(Long clientId, String accFrom, String accTo, Long amount) throws NoPermissionException, InsufficientFundsException;
    public void save();
    public Account get(String id);
}
