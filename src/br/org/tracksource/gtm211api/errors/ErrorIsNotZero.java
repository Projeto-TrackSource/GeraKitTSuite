/*
 * ErrorIsNotZero.java
 *
 * Criada em 24 de Dezembro de 2008, 11:33
 *
 */

package br.org.tracksource.gtm211api.errors;

/**
 * Error thrown when a GTM file with a parameter supposed to be zero is loaded as non-zero.
 *
 * @author Paulo Roberto Moura de Carvalho <a href="mailto:paulo.r.m.carvalho@gmail.com">paulo.r.m.carvalho@gmail.com</a>
 */
public class ErrorIsNotZero extends ErrorGTM {
    
    /** Default constructor. */
    public ErrorIsNotZero() {
        super("Attempt to set a numeric paramater that should be zero.");
    }
    
    /** Constructor with a message. */
    public ErrorIsNotZero(String message) {
        super("Attempt to set a numeric paramater that should be zero.\n" + message);
    }
}
