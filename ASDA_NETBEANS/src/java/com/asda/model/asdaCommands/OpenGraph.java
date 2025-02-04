package com.asda.model.asdaCommands;

import com.asda.Command;
import com.asda.CommandException;
import com.asda.CommandResponse;
import com.asda.beans.AccountBean;
import com.asda.beans.GraphBean;
import com.asda.model.accountsCommands.UserSessionManager;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Felipe Osorio Thomé
 */
public class OpenGraph implements Command {

    private static final String PERSISTENCE_UNIT = "ASDA_JSPPU";
    private CommandResponse aResponse;
    private EntityManagerFactory factory;
    private EntityManager manager;

    @Override
    public CommandResponse execute(HttpServletRequest req, HttpServletResponse res)
            throws CommandException {
        HttpSession session = req.getSession();
        UserSessionManager sessionMgr = UserSessionManager.getInstance();
        AccountBean account = sessionMgr.getAccountUser(session);

        String graphName = req.getParameter("graphName");

        if (graphName != null) {
            factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
            manager = factory.createEntityManager();

            GraphBean graph = findGraph(account, graphName);

            if (graph != null) {
                String graphJson = graph.getGraphJson();
                
                res.setContentType("application/json");    
                PrintWriter out;
                try {
                    out = res.getWriter();
                } catch (IOException ex) {
                    throw new CommandException("IO Exception (response PrintWriter).");
                }
                out.print(graphJson);
                out.flush();
            }

            manager.close();
            factory.close();
        }
        return aResponse;
    }

    private GraphBean findGraph(AccountBean account, String graphName)
            throws CommandException {
        GraphBean graph = null;

        try {
            graph = (GraphBean) manager.createNamedQuery("graphs.findGraph")
                    .setParameter("user", account)
                    .setParameter("name", graphName)
                    .getSingleResult();
        } catch (NoResultException e) {
            manager.close();
            factory.close();
            throw new CommandException("The graph name is invalid.");

        } catch (Exception e) {
            manager.close();
            factory.close();
            throw new CommandException("An error occurred.");
        }

        return graph;
    }
}
