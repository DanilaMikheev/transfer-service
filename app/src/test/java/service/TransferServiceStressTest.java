package service;

import com.fintech.domain.Account;
import com.fintech.web.data.TransferData;
import com.fintech.web.data.UidData;
import configuration.ApplicationContext;
import org.junit.*;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author d.mikheev 13.05.19
 */
public class TransferServiceStressTest extends ApplicationContext {

    @Before
    public void init() {
        initContext();
        initDB();
    }

    @After
    public void closeAll(){
        httpServer.shutdown();
        h2.dropAll();
    }

    @Test
    @Ignore
    public void stress_test_delay3sec() throws Exception {
        List<String> uids = new ArrayList<>();
        for (int i=0;i<300;i++){ // create 300 transfers between to accounts
            uids.add(httpClient
                    .path("/api/transfers")
                    .request()
                    .post(Entity.entity(new TransferData( 1l, "40817810123456789011", "40817810123456789012", 1l),
                            MediaType.APPLICATION_JSON), UidData.class).getUid());
        }
        Thread.sleep(3000); // wait before task complete
        uids.stream()
                .map(e->transferDAO.get(e))
                .collect(Collectors.toList());
        Account account = accountDAO.get("40817810123456789011");
        Assert.assertEquals(700,account.getAmount().longValue());
    }
}
