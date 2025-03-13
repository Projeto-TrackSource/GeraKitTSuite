/*
 * ErrorObjectNotDefined.java
 *
 * Criada em 25 de Dezembro de 2008, 09:56
 *
 */

package br.org.tracksource.gtm211api.errors;

/**
 * Error thrown when an object object supposed to be instantiated is null.
 *
 * @author Paulo Roberto Moura de Carvalho <a href="mailto:paulo.r.m.carvalho@gmail.com">paulo.r.m.carvalho@gmail.com</a>
 */
public class ErrorObjectNotDefined extends ErrorGTM {
    
    /** Default constructor. */
    public ErrorObjectNotDefined() {
        super();
    }
    
    /** Constructor with a message. */
    public ErrorObjectNotDefined(String message) {
        super(message);
    }
}
