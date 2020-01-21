package com.mzherdev.accounts;

import com.mzherdev.accounts.controller.AccountController;
import com.mzherdev.accounts.controller.AccountOwnerController;
import com.mzherdev.accounts.exception.AccountAppExceptionMapper;
import com.mzherdev.accounts.util.DBHelper;
import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

public class AccountApplication {
    private static Logger log = Logger.getLogger(AccountApplication.class);

    public static void main(String[] args) {
        DBHelper.populateTestData();
        startServer();
    }

    private static void startServer() {
        Server server = new Server(8080);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        ServletHolder servletHolder = context.addServlet(ServletContainer.class, "/*");
        servletHolder.setInitParameter("jersey.config.server.provider.classnames",
                AccountController.class.getCanonicalName() + ","
                        + AccountOwnerController.class.getCanonicalName() + ","
                        + AccountAppExceptionMapper.class.getCanonicalName());
        try {
            server.start();
            server.join();
        } catch (Exception e) {
            log.error("Some error occurred during server start: {}" + e.getMessage());
        } finally {
            server.destroy();
        }
    }
}
