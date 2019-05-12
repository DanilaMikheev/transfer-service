package service.transfer;

import com.fintech.domain.Account;
import com.fintech.domain.Transfer;
import com.fintech.web.data.TransferData;
import configuration.ApplicationContext;
import org.junit.*;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author d.mikheev on 12.05.19
 */
public class TransferServiceIntegrationTest extends ApplicationContext {

    private static final int UID_LENGTH = 32;

    @After
    public void closeAll(){
        httpServer.shutdown();
    }

    @Test
    public void whenTransferParamsAreValid_thenTransferCreatedSuccessfully() throws Exception {

        String uid = httpClient
                .path("/api/transfers")
                .request()
                .post(Entity.entity(new TransferData(1, 1l, "40817810123456789011", "40817810123456789012", 100l),
                        MediaType.APPLICATION_JSON), String.class);
        Assert.assertEquals(UID_LENGTH, uid.length());

        Transfer savedTransfer = transferDAO.get(uid);
        Assert.assertEquals(savedTransfer.getAccFrom(),"40817810123456789011");
        Assert.assertEquals(savedTransfer.getAccTo(),"40817810123456789012");
        Assert.assertEquals(savedTransfer.getId(),uid);
        Assert.assertEquals(savedTransfer.getAmount().longValue(),100l);
    }

    @Test
    public void stress() throws Exception {
        initDB();
        List<String> uids = new ArrayList<>();
        for (int i=0;i<500;i++){
            uids.add(httpClient
                    .path("/api/transfers")
                    .request()
                    .post(Entity.entity(new TransferData(1, 1l, "40817810123456789011", "40817810123456789012", 1l),
                            MediaType.APPLICATION_JSON), String.class));
        }
        Thread.sleep(3000); // wait before task complete

        uids.stream()
                .map(e->transferDAO.get(e))
                .map(e->e.getStatus())
                .peek(e-> System.out.println(e))
                .collect(Collectors.toList());
        Account account = accountDAO.get("40817810123456789011");
        Assert.assertEquals(500,account.getAmount().longValue());
    }
}
