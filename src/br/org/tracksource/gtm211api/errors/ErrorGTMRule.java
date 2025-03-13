/*
 * ErrorGTMRule.java
 *
 * Criada em 25 de Dezembro de 2008, 09:41
 *
 */

package br.org.tracksource.gtm211api.errors;

/**
 * Error thrown when some specific GTM 211 format rule is broken, for example: if <code>rectangular</code> field of
 * the <i>header</i> record is false, the <code>ngrid</code> field of the <i>datum</i> record must be zero.
 *
 * @author Paulo Roberto Moura de Carvalho <a href="mailto:paulo.r.m.carvalho@gmail.com">paulo.r.m.carvalho@gmail.com</a>
 */
public class ErrorGTMRule extends ErrorGTM  {
    
    /** Default constructor. */
    public ErrorGTMRule() {
        super();
    }
    
    /** Constructor with a message. */
    public ErrorGTMRule(String message) {
        super(message);
    }
}
