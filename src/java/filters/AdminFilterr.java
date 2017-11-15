/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filters;

import businesslogic.UserService;
import domainmodel.User;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author 679810
 */
public class AdminFilterr implements Filter {

    private FilterConfig filterConfig = null;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpSession session = ((HttpServletRequest)request).getSession();
        UserService us = new UserService();
        String username = (String) session.getAttribute("username");
        if (username != null) {
            User user = null;
            try {
                user = us.get(username);
            } catch (Exception ex) {
                Logger.getLogger(AdminFilterr.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (user != null) {
                if (user.getRole().getRoleName().equals("admin")) {
                    // yes, go onwards to the servlet or next filter
                    chain.doFilter(request, response);  
                } else {
                    ((HttpServletResponse)response).sendRedirect("home");
                }
            } else {
                ((HttpServletResponse)response).sendRedirect("login");
            }
        } else {
            // get out of here!
            ((HttpServletResponse)response).sendRedirect("login");
        }
    }

    @Override
    public void destroy() {
    }
    
}
