package service;

import com.fintech.core.exception.InsufficientFundsException;
import com.fintech.core.exception.NoPermissionException;
import com.fintech.domain.Transfer;
import com.fintech.enums.TransferStatus;
import configuration.TransferServiceConfig;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.util.NoSuchElementException;

/**
 * @author d.mikheev 13.05.19
 */
public class TransferServiceTest extends TransferServiceConfig {

    private static final String existedAcc1 = "40817810123456789011";
    private static final String existedAcc2 = "40817810123456789012";
    private static final String existedTransferUid = "d9bbad7e0cea42b9bcd933ae524230db";
    private static final String notExistedTransferUid = "cd933ae524230dbd9bbad7e0cea42b9b";

    @After
    public void closeAll() {
        h2.dropAll();
    }

    @Test
    public void method_get_whenTransferExist_thenTransferStatusGetSuccessfully() {
        TransferStatus status = transferService.get(existedTransferUid);
        Assert.assertEquals(TransferStatus.INIT, status);
    }

    @Test(expected = NoSuchElementException.class)
    public void method_get_whenTransferNotExist_thenNoSuchElementException() {
        transferService.get(notExistedTransferUid);
    }

    @Test
    public void method_processTransfer_whenInvokeWithValidParams_thenTransferSavedSuccessfully() throws NoPermissionException, InsufficientFundsException {
        String uid = transferService.processTransfer(1l, existedAcc1, existedAcc2, 100l);
        Transfer transfer = transferDAO.get(uid);
        Assert.assertEquals(existedAcc1, transfer.getAccFrom());
        Assert.assertEquals(existedAcc2, transfer.getAccTo());
        Assert.assertEquals(32, transfer.getId().length());
        Assert.assertEquals(100, transfer.getAmount().longValue());
        Assert.assertEquals(0, transfer.getStatus());
    }

    @Test(expected = NoPermissionException.class)
    public void method_processTransfer_whenInvokeWithInvalidClientId_thenNoPermissionException() throws NoPermissionException, InsufficientFundsException {
        String uid = transferService.processTransfer(7l, existedAcc1, existedAcc2, 100l);
    }

    @Test(expected = InsufficientFundsException.class)
    public void method_processTransfer_whenInvokeWithAmountGreaterThenAmountOnAccount_thenNoPermissionException() throws NoPermissionException, InsufficientFundsException {
        String uid = transferService.processTransfer(1l, existedAcc1, existedAcc2, 2000l);
    }

}
