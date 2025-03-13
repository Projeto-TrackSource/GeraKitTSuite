/*
 * ErrorGTM.java
 *
 * Created in Dec 24th, 2008. 11:13
 *
 */

package br.org.tracksource.gtm211api.errors;

/**
 * Base class for exception class hierarchy.
 *
 * @author Paulo Roberto Moura de Carvalho <a href="mailto:paulo.r.m.carvalho@gmail.com">paulo.r.m.carvalho@gmail.com</a>
 */
public class ErrorGTM extends Exception{
    
    /** Default constructor. */
    public ErrorGTM() {
        super();
    }
    
    /** Constructor with a message. */
    public ErrorGTM(String message) {
        super(message);
    }
}
