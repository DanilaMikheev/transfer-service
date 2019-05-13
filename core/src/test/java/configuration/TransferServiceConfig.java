package configuration;

import com.fintech.config.DBCPDataSource;
import com.fintech.core.SimpleTransferService;
import com.fintech.core.TransferService;
import com.fintech.dao.AccountDAO;
import com.fintech.dao.TransferDAO;
import com.fintech.dao.impl.AccountDAOJdbc;
import com.fintech.dao.impl.TransferDAOJdbc;

/**
 * @author d.mikheev 13.05.19
 */
public class TransferServiceConfig {
    private static final String TEST_DB_INIT_FILE = "test-data.sql";

    protected AccountDAO accountDAO;
    protected TransferDAO transferDAO;
    protected DBCPDataSource h2;
    protected TransferService transferService;

    public TransferServiceConfig() {
        this.h2 = new DBCPDataSource();
        h2.createDB(TEST_DB_INIT_FILE);
        this.transferDAO = new TransferDAOJdbc(h2.getDataSource());
        this.accountDAO = new AccountDAOJdbc(h2.getDataSource());
        this.transferService = new SimpleTransferService(accountDAO, transferDAO);
    }

}
