package com.asda.model.accountsCommands;

import com.asda.Command;
import com.asda.CommandException;
import com.asda.CommandResponse;
import com.asda.beans.AccountBean;
import com.asda.utils.Md5Hash;
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
public class LogInCommand implements Command {

    private static final String WELCOME_PAGE = "/qnetwork?cmd=init";
    private CommandResponse aResponse;
    private EntityManagerFactory factory;
    private EntityManager manager;

    @Override
    public CommandResponse execute(HttpServletRequest req, HttpServletResponse res)
            throws CommandException {
        //se quiser testar a interface sem login é só comentar as linhas a seguir até as linhas comentadas
        //e ai descomenta as comentadas
        AccountBean account;

        String user = req.getParameter("user");
        if (user == null) {
            throw new CommandException("No user specified.");
        }
        String password = req.getParameter("password");
        if (password == null) {
            throw new CommandException("No password specified.");
        }

        factory = Persistence.createEntityManagerFactory("ASDA_JSPPU");
        manager = factory.createEntityManager();

        account = findUser(user, password);

        if (account != null) {
            doLogInSuccess(req, account);
        }

        manager.close();
        factory.close();
        //aResponse = new CommandResponse();
        //aResponse.setAjax(true);
        //aResponse.setPage(WELCOME_PAGE);
        return aResponse;
    }

    private AccountBean findUser(String email, String password)
            throws CommandException {
        AccountBean account = null;
        String passwordHash = Md5Hash.generator(password.getBytes());

        try {
            account = (AccountBean) manager.createNamedQuery("users.findUser")
                    .setParameter("email", email)
                    .setParameter("password", passwordHash)
                    .getSingleResult();
        } catch (NoResultException e) {
            manager.close();
            factory.close();
            throw new CommandException("The email or password is invalid.");

        } catch (Exception e) {
            manager.close();
            factory.close();
            throw new CommandException("An error occurred.");
        }

        return account;
    }

    private void doLogInSuccess(HttpServletRequest req, AccountBean account) {
        HttpSession session = req.getSession(true);
        UserSessionManager sessionMgr = UserSessionManager.getInstance();
        String targetPage = sessionMgr.getLoginTarget(req, true);
        //sessionMgr.setSessionUser(session, account);

        aResponse = new CommandResponse();
        aResponse.setAjax(true);
        aResponse.setPage(WELCOME_PAGE);
        if (targetPage != null) {
            aResponse.setPage(targetPage);
        }
    }
}