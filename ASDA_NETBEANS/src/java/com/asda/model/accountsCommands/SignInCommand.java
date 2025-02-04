package com.asda.model.accountsCommands;

import com.asda.beans.AccountBean;
import com.asda.Command;
import com.asda.CommandException;
import com.asda.CommandResponse;
import java.util.Calendar;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Felipe Osorio Thomé
 */
public class SignInCommand implements Command {

    private static final String PERSISTENCE_UNIT = "ASDA_JSPPU";
    private CommandResponse aResponse;
    private EntityManagerFactory factory;
    private EntityManager manager;

    @Override
    public CommandResponse execute(HttpServletRequest req, HttpServletResponse res)
            throws CommandException {
        AccountBean account = new AccountBean();

        if(!req.getParameter("si_name").equals("")) {
            account.setName(req.getParameter("si_name"));
        }
        if(!req.getParameter("si_email").equals("")) {
            account.setEmail(req.getParameter("si_email"));
        }
        if(!req.getParameter("si_course").equals("")) {
            account.setCourse(req.getParameter("si_course"));
        }
        if(!req.getParameter("si_class").equals("")) {
            account.setUserClass(req.getParameter("si_class"));
        }
        if(!req.getParameter("si_password").equals("")) {
            account.setPasswordHash(req.getParameter("si_password"));
        }
        account.setRegistrationDate(Calendar.getInstance());

        factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
        manager = factory.createEntityManager();

        manager.getTransaction().begin();
        manager.persist(account);
        manager.getTransaction().commit();

        manager.close();
        factory.close();

        return aResponse;
    }
}