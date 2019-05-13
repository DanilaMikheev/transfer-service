package com.fintech.dao.impl;

import com.fintech.dao.AccountDAO;
import com.fintech.domain.Account;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author d.mikheev 08.05.19
 */
public class AccountDAOJdbc implements AccountDAO {

    private static final String FIND_BY_ID = "SELECT * FROM account WHERE id = ?";
    private static final String UPDATE_AMOUNT_BY_ID_AND_AMOUNT = "Update account set amount = ? WHERE id = ? and amount = ?";
    private DataSource dataSource;

    public AccountDAOJdbc(DataSource ds) {
        this.dataSource = ds;
    }

    @Override
    public Account get(String id) {
        QueryRunner run = new QueryRunner(dataSource);
        ResultSetHandler<Account> h = new BeanHandler<>(Account.class);
        try {
            return run.query(FIND_BY_ID, h, id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean sendFunds(Account from, Account to, Long amount) {
        if ((from==null)||(to==null))
            return false;
        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);
            QueryRunner run = new QueryRunner(dataSource);
            try {
                int decrease = run.update(UPDATE_AMOUNT_BY_ID_AND_AMOUNT, from.getAmount() - amount, from.getId(), from.getAmount());
                int increase = run.update(UPDATE_AMOUNT_BY_ID_AND_AMOUNT, to.getAmount() + amount, to.getId(), to.getAmount());
                if ((decrease == 1) && (increase == 1)) {
                    conn.commit();
                    return true;
                }
            } catch (Exception e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
