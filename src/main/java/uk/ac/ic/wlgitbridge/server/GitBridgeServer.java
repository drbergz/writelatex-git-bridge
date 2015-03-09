package uk.ac.ic.wlgitbridge.server;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.log.Log;
import uk.ac.ic.wlgitbridge.application.Config;
import uk.ac.ic.wlgitbridge.application.jetty.NullLogger;
import uk.ac.ic.wlgitbridge.bridge.BridgeAPI;
import uk.ac.ic.wlgitbridge.git.exception.InvalidRootDirectoryPathException;
import uk.ac.ic.wlgitbridge.git.servlet.WLGitServlet;
import uk.ac.ic.wlgitbridge.snapshot.base.SnapshotAPIRequest;
import uk.ac.ic.wlgitbridge.util.Util;

import javax.servlet.ServletException;
import java.io.File;
import java.net.BindException;

/**
 * Created by Winston on 02/11/14.
 */

/**
 * Class for the actual server.
 */
public class GitBridgeServer {

    private final BridgeAPI bridgeAPI;

    private final Server jettyServer;

    private final int port;
    private String rootGitDirectoryPath;
    private String apiBaseURL;

    /**
     * Constructs an instance of the server.
     * @param port the port number to listen on
     * @param rootGitDirectoryPath the root directory path containing the git repositories
     * @throws ServletException if the servlet throws an exception
     */
    private GitBridgeServer(final int port, String rootGitDirectoryPath) throws ServletException, InvalidRootDirectoryPathException {
        Log.setLog(new NullLogger());
        this.port = port;
        this.rootGitDirectoryPath = rootGitDirectoryPath;
        bridgeAPI = new BridgeAPI(rootGitDirectoryPath);
        jettyServer = new Server(port);
        configureJettyServer();
    }

    public GitBridgeServer(Config config) throws ServletException, InvalidRootDirectoryPathException {
        this(config.getPort(), config.getRootGitDirectory());
        SnapshotAPIRequest.setBasicAuth(config.getUsername(), config.getPassword());
        apiBaseURL = config.getAPIBaseURL();
        SnapshotAPIRequest.setBaseURL(apiBaseURL);
        Util.setServiceName(config.getServiceName());
        Util.setPostbackURL(config.getPostbackURL());
        Util.setPort(config.getPort());
    }

    /**
     * Starts the server on the port given on construction.
     */
    public void start() {
        try {
            jettyServer.start();
            Util.sout(Util.getServiceName() + "-Git Bridge server started");
            Util.sout("Listening on port: " + port);
            Util.sout("Bridged to: " + apiBaseURL);
            Util.sout("Postback base URL: " + Util.getPostbackURL());
            Util.sout("Root git directory path: " + rootGitDirectoryPath);
        } catch (BindException e) {
            Util.printStackTrace(e);
        } catch (Exception e) {
            Util.printStackTrace(e);
        }
    }

    public void stop() {
        try {
            jettyServer.stop();
        } catch (Exception e) {
            Util.printStackTrace(e);
        }
    }

    private void configureJettyServer() throws ServletException, InvalidRootDirectoryPathException {
        HandlerCollection handlers = new HandlerCollection();
        handlers.setHandlers(new Handler[] {
                initResourceHandler(),
                new PostbackHandler(bridgeAPI),
                initGitHandler()
        });
        jettyServer.setHandler(handlers);
    }

    private Handler initGitHandler() throws ServletException, InvalidRootDirectoryPathException {
        final ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        servletContextHandler.setContextPath("/");
        servletContextHandler.addServlet(
                new ServletHolder(
                        new WLGitServlet(servletContextHandler, bridgeAPI, rootGitDirectoryPath)),
                "/*"
        );
        return servletContextHandler;
    }

    private Handler initResourceHandler() {
        ResourceHandler resourceHandler = new FileServlet(bridgeAPI);
        resourceHandler.setResourceBase(new File(rootGitDirectoryPath, ".wlgb/atts").getAbsolutePath());
        return resourceHandler;
    }

}