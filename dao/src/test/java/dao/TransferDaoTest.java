package dao;

import com.fintech.domain.Transfer;
import configuration.DaoConfig;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author d.mikheev 13.05.19
 */
public class TransferDaoTest extends DaoConfig {
    private static final String existedAcc1="40817810123456789011";
    private static final String existedAcc2="40817810123456789012";
    private static final String existedTransferUid="d9bbad7e0cea42b9bcd933ae524230db";
    private static final String notExistedTransferUid="cd933ae524230dbd9bbad7e0cea42b9b";

    @Before
    public void init(){
        h2.createDB(TEST_DB_INIT_FILE);
    }

    @After
    public void closeAll(){
        h2.dropAll();
    }

    @Test
    public void method_init_whenInvokeWithValidParams_thenTransferSavedSuccessfully(){
        transferDAO.init(notExistedTransferUid,existedAcc1,existedAcc2,100l);
        Transfer transfer = transferDAO.get(notExistedTransferUid);
        Assert.assertEquals(existedAcc1,transfer.getAccFrom());
        Assert.assertEquals(existedAcc2,transfer.getAccTo());
        Assert.assertEquals(notExistedTransferUid,transfer.getId());
        Assert.assertEquals(100,transfer.getAmount().longValue());
        Assert.assertEquals(0,transfer.getStatus());
    }

    @Test
    public void method_get_whenTransferExist_thenTransferGetSuccessfully(){
        Transfer transfer = transferDAO.get(existedTransferUid);
        Assert.assertEquals(existedAcc1,transfer.getAccFrom());
        Assert.assertEquals(existedAcc2,transfer.getAccTo());
        Assert.assertEquals(existedTransferUid,transfer.getId());
        Assert.assertEquals(100,transfer.getAmount().longValue());
        Assert.assertEquals(0,transfer.getStatus());
    }

    @Test
    public void method_get_whenTransferNotExist_thenTransferNull(){
        Transfer transfer = transferDAO.get(notExistedTransferUid);
        Assert.assertEquals(null,transfer);
    }

    @Test
    public void method_updateStatus_whenTransferExist_thenUpdateTransferStatusSuccessfully(){
        boolean update =  transferDAO.updateStatus(existedTransferUid,1);
        Transfer transfer = transferDAO.get(existedTransferUid);
        Assert.assertEquals(true,update);
        Assert.assertEquals(1,transfer.getStatus());
    }

    @Test
    public void method_updateStatus_whenTransferNotExist_thenReturnFalse(){
        boolean update =  transferDAO.updateStatus(notExistedTransferUid,1);
        Assert.assertEquals(false,update);
    }


}
