/*
 * ErrorIsNotFalse.java
 *
 * Criada em 24 de Dezembro de 2008, 13:27
 *
 */

package br.org.tracksource.gtm211api.errors;

/**
 * Error thrown when a GTM file with a boolean parameter supposed to be false is loaded as true.
 *
 * @author Paulo Roberto Moura de Carvalho <a href="mailto:paulo.r.m.carvalho@gmail.com">paulo.r.m.carvalho@gmail.com</a>
 */
public class ErrorIsNotFalse extends ErrorGTM {
    
    /** Default constructor. */
    public ErrorIsNotFalse() {
        super("Attempt to set a boolean paramater that should be false.");
    }
    
    /** Constructor with a message. */
    public ErrorIsNotFalse(String message) {
        super("Attempt to set a boolean paramater that should be false.\n" + message);
    }
}
