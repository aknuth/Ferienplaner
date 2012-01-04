package de.dwpbank.wpdirect.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;


public class InitServlet extends HttpServlet {

    public void init() throws ServletException {
        System.out.println("SamplesInitServlet.init()");

        //VelocityInitializer.init();
    }
}
