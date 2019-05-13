package configuration;

import com.fintech.config.DBCPDataSource;
import com.fintech.core.SimpleTransferService;
import com.fintech.core.TransferService;
import com.fintech.dao.AccountDAO;
import com.fintech.dao.TransferDAO;
import com.fintech.dao.impl.AccountDAOJdbc;
import com.fintech.dao.impl.TransferDAOJdbc;
import com.fintech.web.controller.TransferController;
import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.Before;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.ext.RuntimeDelegate;
import java.io.IOException;

/**
 * @author d.mikheev on 12.05.19
 */
public class ApplicationContext {
    private static final String ROOT = "http://localhost";
    private static final int PORT = 8090;
    private static final String TEST_DB_INIT_FILE = "test-data.sql";

    protected HttpServer httpServer;
    protected WebTarget httpClient;
    protected AccountDAO accountDAO;
    protected TransferDAO transferDAO;
    protected DBCPDataSource h2;

    public ApplicationContext(){
        initContext();
        initDB();
    }

    public void initContext(){
        this.h2 = new DBCPDataSource();
        ResourceConfig resourceConfig = new ResourceConfig();
        resourceConfig.register(JacksonFeature.class);
        resourceConfig.register(TransferController.class);
        this.transferDAO  =  new TransferDAOJdbc(h2.getDataSource());
        this.accountDAO = new AccountDAOJdbc(h2.getDataSource());
        resourceConfig.register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(new SimpleTransferService(accountDAO,transferDAO)).to(TransferService.class);
            }
        });

        HttpHandler endpoint = RuntimeDelegate.getInstance().createEndpoint(resourceConfig, HttpHandler.class);
        this.httpServer = HttpServer.createSimpleServer(ROOT, PORT);
        this.httpServer.getServerConfiguration().addHttpHandler(endpoint);
        try {
            this.httpServer.start();
            this.httpClient = ClientBuilder.newClient().target(ROOT+":"+PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initDB(){
        h2.createDB(TEST_DB_INIT_FILE);
    }

}
