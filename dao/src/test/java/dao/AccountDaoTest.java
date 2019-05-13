package dao;

import com.fintech.domain.Account;
import configuration.DaoConfig;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author d.mikheev 13.05.19
 */
public class AccountDaoTest extends DaoConfig {

    private static final String existedAcc1="40817810123456789011";
    private static final String existedAcc2="40817810123456789012";
    private static final String notExistedAcc="40817840123456789011";

    @Before
    public void init(){
        h2.createDB(TEST_DB_INIT_FILE);
    }

    @After
    public void closeAll(){
        h2.dropAll();
    }

    @Test
    public void method_get_whenAccountExist_ThenGetAccountSuccessfully(){
        Account account = accountDAO.get(existedAcc1);
        Assert.assertEquals(existedAcc1,account.getId());
        Assert.assertEquals(1,account.getClientid().longValue());
        Assert.assertEquals(1000,account.getAmount().longValue());
    }

    @Test
    public void method_get_whenAccountNotExist_ThenAccountNull(){
        Account account = accountDAO.get(notExistedAcc);
        Assert.assertEquals(null,account);
    }

    @Test
    public void method_sendFunds_whenInputAccountsExist_ThenTransferCompleteSuccessfully() {
        Account account1 = accountDAO.get(existedAcc1);
        Account account2 = accountDAO.get(existedAcc2);
        boolean success = accountDAO.sendFunds(account1, account2, 100l);
        Assert.assertEquals(true, success);
        Account afterTransfer1 = accountDAO.get(existedAcc1);
        Account afterTransfer2 = accountDAO.get(existedAcc2);
        Assert.assertEquals(900, afterTransfer1.getAmount().longValue());
        Assert.assertEquals(1100, afterTransfer2.getAmount().longValue());
    }

    @Test
    public void method_sendFunds_whenOneInputAccountNotExist_ThenReturnFalse() {
        Account account1 = accountDAO.get(existedAcc1);
        Account account2 = accountDAO.get(notExistedAcc);
        boolean success = accountDAO.sendFunds(account1, account2, 100l);
        Assert.assertEquals(false, success);
    }


}

