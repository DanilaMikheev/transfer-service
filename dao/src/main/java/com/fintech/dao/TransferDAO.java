package com.fintech.dao;

import com.fintech.domain.Transfer;

/**
 * Transfer DAO
 *
 * @author d.mikheev on 11.05.19
 */
public interface TransferDAO {

    /**
     * Create transfer entity (for history)
     *
     * @param uid transfer unique uid
     * @param accFrom sender account number
     * @param accTo receiver account number
     * @param amount transfer amount
     * @return true if transfer created, else false
     */
    public boolean init(String uid, String accFrom, String accTo, Long amount);

    /**
     * Update transfer status
     *
     * @param uid transfer unique uid
     * @param status transfer status id
     * @return true if successfully update, else false
     */
    public boolean updateStatus(String uid, int status);

    /**
     * Get saved transfer
     *
     * @param uid transfer unique uid
     * @return transfer
     */
    public Transfer get(String uid);

}
