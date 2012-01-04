/*
 * (c) 2007 by dwpbank Deutsche WertpapierService Bank AG
 */
/**
 * Actual Version
 * ==============
 * @version $Revision: 1.2 $
 * @author $Author: aknuth $ 
 * For a detailed history of this file see bottom !
 */
package de.dwpbank.wpdirect.access;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.MDC;

import sun.misc.BASE64Decoder;

/**
 * 
 */
public class AccessFilter implements Filter {

	private static Logger logger = Logger.getLogger("trace." + AccessFilter.class);

	@Override
	public void destroy() {
		logger.info("init()");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		String authorization = ((HttpServletRequest)request).getHeader("Authorization");
		String user = "";
		String password = "";
		if (request.getParameter("dakar_usecase_name")!=null && request.getParameter("dakar_usecase_name").equals("log.ug0001logs.uc0001showlogs")){
			if (authorization == null) {
				askForPassword((HttpServletResponse)response);
			} else {
				String userInfo = authorization.substring(6).trim();
				BASE64Decoder decoder = new BASE64Decoder();
				String nameAndPassword = new String(decoder.decodeBuffer(userInfo));
				// Decoded part looks like "username:password".
				int index = nameAndPassword.indexOf(":");
				user = nameAndPassword.substring(0, index);
				password = nameAndPassword.substring(index + 1);

				try {
					LDAPRequest.ldapRequest.checkAuthentication(user,password);
			        MDC.put("username", user);
					chain.doFilter(request, response);
				} catch (InvalidUserException e) {
					logger.warn(user+" :InvalidUserException");
					askForPassword((HttpServletResponse)response);
				} catch (InvalidPasswordException e) {
					logger.warn(user+" :InvalidPasswordException");
					askForPassword((HttpServletResponse)response);
				} catch (ExpiredPasswordException e) {
					logger.warn(user+" :ExpiredPasswordException");
					redirectToIdentityManager((HttpServletResponse)response);
				} catch (UnknownException e) {
					logger.fatal(ExceptionUtils.getStackTrace(e));
					redirectToIdentityManager((HttpServletResponse)response);
				} catch (ExceedPasswordRetryLimitException e) {
					logger.warn(user+" :ExpiredPasswordException");
					redirectToIdentityManager((HttpServletResponse)response);
				}

			}
		} else {
			chain.doFilter(request, response);
		}
	}

	private void askForPassword(HttpServletResponse response) {
    	response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // I.e., 401
    	response.setHeader("WWW-Authenticate", "BASIC realm=\"LogFile Analyse\"");
   }
	private void redirectToIdentityManager(HttpServletResponse response) {
    	response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY); // I.e., 302
    	response.setHeader("Location", "https://idm.dwpbank.crednet.de/idm/user/login.jsp");
   }
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		logger.info("destroy()");
	}

}

/*
 * 
 * File History ============== $Log$
 */