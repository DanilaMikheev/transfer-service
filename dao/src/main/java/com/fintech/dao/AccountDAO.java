package com.fintech.dao;

import com.fintech.domain.Account;

/**
 * Account DAO
 *
 * @author d.mikheev on 11.05.19
 */
public interface AccountDAO {

    /**
     * Get account
     *
     * @param id account uid
     * @return account entity
     */
    public Account get(String id);

    /**
     * Send funds "from" account to "to" account
     *
     * @param from sender account entity
     * @param to receiver account entity
     * @param amount transfer amount
     * @return transfer result, true - successful.
     */
    public boolean sendFunds(Account from, Account to, Long amount);
}
