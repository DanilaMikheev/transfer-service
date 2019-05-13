package com.fintech.core;

import com.fintech.core.exception.InsufficientFundsException;
import com.fintech.core.exception.NoPermissionException;
import com.fintech.enums.TransferStatus;

/**
 * Transfer service
 *
 * @author d.mikheev on 09.05.19
 */
public interface TransferService {

    /**
     * Process transfer
     *
     * @param clientId client id
     * @param accFrom client account number
     * @param accTo receiver account number
     * @param amount transfer amount
     * @return unique transfer uid
     * @throws NoPermissionException if clientId not match with accFrom account owner
     * @throws InsufficientFundsException if amount greater than balance accFrom account number
     */
    public String processTransfer(Long clientId, String accFrom, String accTo, Long amount) throws NoPermissionException, InsufficientFundsException;

    /**
     * Get transfer
     *
     * @param uid unique transfer id
     * @return status of the existed transfer
     */
    public TransferStatus get(String uid);
}
