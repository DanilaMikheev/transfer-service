package com.fintech.app;

import com.fintech.config.DBCPDataSource;
import com.fintech.core.SimpleTransferService;
import com.fintech.core.TransferService;
import com.fintech.dao.impl.AccountDAOJdbc;
import com.fintech.web.controller.TransferController;
import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ext.RuntimeDelegate;
import java.io.IOException;

/**
 * @author d.mikheev on 09.05.19
 */
public class Application {
    private HttpServer server;

    public static void main(String[] args) throws IOException {
        Application application = new Application();
        application.run();
        System.out.println("Application has started. For exit press 'Enter'");
        System.in.read();
        application.shoutDown();
    }

    public void run() {
        DBCPDataSource h2 = new DBCPDataSource();
        ResourceConfig resourceConfig = new ResourceConfig();
        resourceConfig.register(JacksonFeature.class);
        resourceConfig.register(TransferController.class);
        resourceConfig.register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(new SimpleTransferService(new AccountDAOJdbc(h2.getDataSource()))).to(TransferService.class);
            }
        });

        HttpHandler endpoint = RuntimeDelegate.getInstance().createEndpoint(resourceConfig, HttpHandler.class);
        server = HttpServer.createSimpleServer("http://localhost", 8080);
        server.getServerConfiguration().addHttpHandler(endpoint);
        try {
            h2.createDB();
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void shoutDown() {
        server.shutdown();
    }
}
