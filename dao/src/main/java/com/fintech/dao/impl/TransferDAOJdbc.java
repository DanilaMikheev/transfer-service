package com.fintech.dao.impl;

import com.fintech.dao.TransferDAO;
import com.fintech.domain.Transfer;
import com.fintech.enums.TransferStatus;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * @author d.mikheev on 11.05.19
 */
public class TransferDAOJdbc implements TransferDAO {

    private static final String FIND_BY_ID = "SELECT * FROM Transfer WHERE id = ?";
    private static final String FIND_STATUS_BY_ID = "SELECT status FROM Transfer WHERE id = ?";
    private final static String INSERT_NEW = "INSERT INTO Transfer (id,accFrom,accTo,amount, status,updated) values (?,?,?,?,?,?)";
    private final static String UPDATE_STATUS_BY_UID = "UPDATE Transfer SET status=?, updated=? where id=?";
    private DataSource dataSource;

    public TransferDAOJdbc(DataSource ds) {
        this.dataSource = ds;
    }

    @Override
    /**
     * @inheritDoc
     */
    public boolean init(String uid, String accFrom, String accTo, Long amount) {
        QueryRunner run = new QueryRunner(dataSource);
        try {
            int insert = run.update(INSERT_NEW, uid, accFrom, accTo, amount, TransferStatus.INIT.getVal(), new Timestamp(System.currentTimeMillis()));
            return insert == 1;
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return false;
        }
    }

    @Override
    /**
     * @inheritDoc
     */
    public boolean updateStatus(String uid, int status) {
        QueryRunner run = new QueryRunner(dataSource);
        try {
            int updated = run.update(UPDATE_STATUS_BY_UID, status, new Timestamp(System.currentTimeMillis()+1000000l), uid);
            return updated == 1;
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            return false;
        }
    }

    @Override
    /**
     * @inheritDoc
     */
    public Transfer get(String uid) {
        QueryRunner run = new QueryRunner(dataSource);
        ResultSetHandler<Transfer> h = new BeanHandler<>(Transfer.class);
        try {
            return run.query(FIND_BY_ID, h, uid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
