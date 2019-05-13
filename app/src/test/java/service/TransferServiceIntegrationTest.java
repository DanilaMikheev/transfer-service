package service;

import com.fintech.domain.Transfer;
import com.fintech.web.data.TransferData;
import com.fintech.web.data.UidData;
import configuration.ApplicationContext;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author d.mikheev on 12.05.19
 */
public class TransferServiceIntegrationTest extends ApplicationContext {

    private static final int UID_LENGTH = 32;

    @After
    public void closeAll() {
        httpServer.shutdown();
        h2.dropAll();
    }

    @Test
    public void whenTransferParamsAreValid_thenTransferCreatedSuccessfully() throws Exception {
        String uid = httpClient
                .path("/api/transfers")
                .request()
                .post(Entity.entity(new TransferData(1, 1l, "40817810123456789011", "40817810123456789012", 100l),
                        MediaType.APPLICATION_JSON), UidData.class).getUid();
        Assert.assertEquals(UID_LENGTH, uid.length());

        Transfer savedTransfer = transferDAO.get(uid);
        Assert.assertEquals(savedTransfer.getAccFrom(), "40817810123456789011");
        Assert.assertEquals(savedTransfer.getAccTo(), "40817810123456789012");
        Assert.assertEquals(savedTransfer.getId(), uid);
        Assert.assertEquals(savedTransfer.getAmount().longValue(), 100l);
    }

    @Test
    public void whenNotEnougntMoneyOnAccForTransfer_thenHttpResponseStatus404() throws Exception {
        Response response = httpClient
                .path("/api/transfers/notexist")
                .request()
                .get();
        Assert.assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

    @Test
    public void whenTransferNotExist_thenHttpResponseStatus409() throws Exception {
        Response response = httpClient
                .path("/api/transfers/")
                .request()
                .post(Entity.entity(new TransferData(1, 1l, "40817810123456789011", "40817810123456789012", 2000l),
                        MediaType.APPLICATION_JSON));
        Assert.assertEquals(Response.Status.CONFLICT.getStatusCode(), response.getStatus());
    }

    @Test
    public void whenTransferNotExist_thenHttpResponseStatus403() throws Exception {
        Response response = httpClient
                .path("/api/transfers/")
                .request()
                .post(Entity.entity(new TransferData(1, 777l, "40817810123456789011", "40817810123456789012", 100l),
                        MediaType.APPLICATION_JSON));
        Assert.assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
    }
}
