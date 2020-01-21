package com.mzherdev.accounts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mzherdev.accounts.exception.AccountAppExceptionMapper;
import com.mzherdev.accounts.util.DBHelper;
import org.apache.http.client.HttpClient;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

public abstract class AbstractControllerTest {
    private static final String TEST_HOST = "localhost";
    private static final String TEST_PORT = "9000";

    protected static Server server = null;

    protected static HttpClient client;
    protected ObjectMapper mapper = new ObjectMapper();
    protected URIBuilder builder = new URIBuilder().setScheme("http").setHost(TEST_HOST + ":" + TEST_PORT);


    @BeforeAll
    public static void setup() throws Exception {
        startServer();

        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(200);
        connectionManager.setDefaultMaxPerRoute(100);
        client = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .setConnectionManagerShared(true)
                .build();
    }

    @BeforeEach
    public void beforeEach() {
        DBHelper.populateTestData();
    }

    @AfterAll
    public static void closeClient() {
        HttpClientUtils.closeQuietly(client);
    }

    private static void startServer() throws Exception {
        if (server == null) {
            server = new Server(Integer.parseInt(TEST_PORT));
            ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
            context.setContextPath("/");
            server.setHandler(context);
            ServletHolder servletHolder = context.addServlet(ServletContainer.class, "/*");
            servletHolder.setInitParameter("jersey.config.server.provider.classnames",
                    AccountController.class.getCanonicalName() + ","
                            + AccountOwnerController.class.getCanonicalName() + ","
                            + AccountAppExceptionMapper.class.getCanonicalName());
            server.start();
        }
    }
}
