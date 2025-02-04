package com.asda.controller.home;

import com.asda.utils.FlowControl;
import java.io.IOException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Felipe Osorio Thomé
 */
public class HomeServlet extends HttpServlet {
    
    private static final String WELCOME_PAGE = "/WEB-INF/view/index.jsp";

    @Override
    public void service(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        FlowControl.forward(WELCOME_PAGE, req, res);
        
        /***/
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("ASDA_JSPPU");
        EntityManager manager = factory.createEntityManager();
        manager.close();
        factory.close();
        /***/
    }
}
