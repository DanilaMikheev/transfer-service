package com.fintech.dao;

import com.fintech.domain.Transfer;
import com.fintech.enums.TransferStatus;

/**
 * @author d.mikheev on 11.05.19
 */
public interface TransferDAO {

    public boolean init(String uid, String accFrom, String accTo, Long amount);

    public boolean updateStatus(String uid, int status);

    public Transfer get(String uid);

}
