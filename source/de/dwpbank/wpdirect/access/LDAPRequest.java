/*
 * (c) 2007 by dwpbank Deutsche WertpapierService Bank AG
 */
/**
 * Actual Version
 * ==============
 * @version $Revision: 1.5 $
 * @author $Author: aknuth $ 
 * For a detailed history of this file see bottom !
 */
package de.dwpbank.wpdirect.access;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.directory.DirContext;
import javax.naming.ldap.LdapContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * 
 */
public class LDAPRequest {

	public static final LDAPRequest ldapRequest = new LDAPRequest();

	private static Logger log = Logger.getLogger("trace." + LDAPRequest.class.getName());

	private LDAPRequest() {

	}

	public void checkAuthentication(String user, String password) throws InvalidUserException, InvalidPasswordException, ExpiredPasswordException, UnknownException, ExceedPasswordRetryLimitException {
		Hashtable env = new Hashtable();

		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		// env.put(Context.PROVIDER_URL, "ldap://97.0.242.240:3890/"); // SET
		// YOUR SERVER AND STARTING CONTEXT HERE
		if (isTestMode(user)){
			//Präprod
			env.put(Context.PROVIDER_URL, "ldap://97.0.242.247:3890/");
		} else {
			//Prod
			env.put(Context.PROVIDER_URL, "ldap://97.0.242.240:3890/"); // SET YOUR
		}
		// SERVER
		// AND
		// STARTING
		// CONTEXT
		// HERE
		env.put(Context.SECURITY_PRINCIPAL, "dwpuniquekey=00000-"+user+",ou=People,o=00000,dc=dwp-bank,dc=net"); // SET
		// USER
		env.put(Context.SECURITY_CREDENTIALS, password); // SET PASSWORD HERE
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put("com.sun.jndi.ldap.read.timeout", "1000");
		// env.put(Context.SECURITY_PROTOCOL, "ssl");

		// THE LOCATION OF THE CACERTS MUST BE SPECIFIED
		// java.security.Security.addProvider(new
		// com.sun.net.ssl.internal.ssl.Provider());
		// System.setProperty("javax.net.ssl.keyStore",
		// "JAVAHOME\\lib\\security\\cacerts");
		// System.setProperty("javax.net.ssl.trustStore","JAVAHOME\\lib\\security\\cacerts");

		env.put(LdapContext.CONTROL_FACTORIES, "com.sun.jndi.ldap.ControlFactory");
		DirContext ctx = null;
//		try {
//			ctx = new InitialDirContext(env);
//		} catch (javax.naming.AuthenticationException authEx) {
//			handleAuthenticationException(authEx, env);
//		} catch (InvalidAttributeValueException e) {
//			handleAuthenticationException(e, env);
//		} catch (javax.naming.NamingException e) {
//			throw new RuntimeException(e);
//		} catch (Throwable th) {
//			log.fatal(th);
//			throw new RuntimeException(th);
//		} finally {
//			if (ctx != null) {
//				try {
//					ctx.close();
//				} catch (javax.naming.NamingException e) {
//					// Kann ignoriert werden
//					log.warn(e);
//				}
//			}
//		}

	}

	private void handleAuthenticationException(javax.naming.NamingException authEx, Hashtable env) throws InvalidUserException, InvalidPasswordException, ExpiredPasswordException, UnknownException, ExceedPasswordRetryLimitException {
		log.debug(authEx);
		String explaination = authEx.getExplanation();
		log.debug("LDAP-AuthEx explaination=" + explaination);
		if (explaination == null) {
			log.fatal("LDAP-Authentication Exception. Grund unbekannt", authEx);
			throw new RuntimeException();
		}
		if (explaination.indexOf("No Such Object") != -1) {
			throw new InvalidUserException();
		} else if (explaination.indexOf("Invalid Credentials") != -1) {
			throw new InvalidPasswordException();
		} else if (explaination.indexOf("password expired") != -1) {
			throw new ExpiredPasswordException();
		} else if (explaination.indexOf("Exceed password retry limit") != -1) {
			throw new ExceedPasswordRetryLimitException();
		} else {
			throw new UnknownException();
		}
	}
	
	static boolean isTestMode(String user){
		boolean result = false;
		if (StringUtils.isEmpty(System.getProperty("test_not_available"))){
			result = user!=null?user.toLowerCase().endsWith("t"):true;
		}
		return result;
	}
	
	public static void main(String[] args) throws Exception {
		LDAPRequest request = LDAPRequest.ldapRequest;
		//request.checkAuthentication("DWP4923", "XDR5CFT6",false);
		//request.checkAuthentication("DWP4923", "CFT6VGZ7",false);
		request.checkAuthentication("DWP4923t", "WEST1234");
	}
}

/*
 * 
 * File History ============== $Log: LDAPRequest.java,v $
 * File History ============== Revision 1.3  2009/09/07 14:47:13  aknuth
 * File History ============== TW20000: Neue Version Versionspflege
 * File History ==============
 * File History ============== Revision 1.2  2009/09/02 08:22:19  aknuth
 * File History ============== TW20000: idm testmode eingebaut
 * File History ==============
 * File History ============== Revision 1.1  2009/08/25 15:59:10  aknuth
 * File History ============== TW20000: Erste Version Releaseplaner
 * File History ==============
 * File History ============== Revision 1.2  2009/05/13 08:36:21  aknuth
 * File History ============== TW20000: InvalidAttributeValueException wird nun auch gefangen
 * File History ==============
 * File History ============== Revision 1.1  2009/04/16 15:56:46  aknuth
 * File History ============== TW20000: LDAP Absicherung eingebaut
 * File History ============== Revision 1.2
 * 2009/04/09 08:59:56 aknuth TW20000: ip vom load balancer eingetragen
 * 
 * Revision 1.1 2009/03/27 14:24:40 aknuth TW20000: allegmeines Update
 */