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
/**
 * 
 */
public class UnknownException extends Exception {
	public UnknownException() {
		super();
	}
	public UnknownException(Throwable e) {
		super(e);
	}
}

/*
 * 
 * File History
 * ==============
 * $Log: UnknownException.java,v $
 * Revision 1.2  2009/05/13 08:36:22  aknuth
 * TW20000: InvalidAttributeValueException wird nun auch gefangen
 *
 * Revision 1.1  2009/04/16 15:56:46  aknuth
 * TW20000: LDAP Absicherung eingebaut
 *
 */