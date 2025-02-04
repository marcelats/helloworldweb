package com.asda;

import com.asda.utils.Md5Hash;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Felipe Osorio Thomé From the book: Web Development with JavaServer
 * Pages
 */
public class CommandToken {

    public static void set(HttpServletRequest req) {
        HttpSession session = req.getSession(true);
        long systime = System.currentTimeMillis();
        String id = session.getId();
        String time = new Long(systime).toString();

        StringBuilder buffer = new StringBuilder();
        buffer.append(id);
        buffer.append(time);
        byte[] message = buffer.toString().getBytes();

        String token = Md5Hash.generator(message);
        req.setAttribute("token", token);
        session.setAttribute("token", token);
    }

    public static boolean isValid(HttpServletRequest req) {
        HttpSession session = req.getSession(true);
        String requestToken = req.getParameter("token");
        String sessionToken = (String) session.getAttribute("token");
        if (requestToken == null || sessionToken == null) {
            return false;
        } else {
            return requestToken.equals(sessionToken);
        }
    }
}
