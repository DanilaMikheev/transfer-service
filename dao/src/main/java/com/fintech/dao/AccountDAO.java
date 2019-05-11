package com.fintech.dao;

import com.fintech.model.Account;

/**
 * @author d.mikheev on 11.05.19
 */
public interface AccountDAO {

    public Account get(String id);
    public void save(Account account);
    public boolean sendFunds(Account from, Account to, Long amount);
}
