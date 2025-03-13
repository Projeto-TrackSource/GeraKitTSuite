/*
 * ErrorStringNotEmpty.java
 *
 * Criada em 24 de Dezembro de 2008, 13:39
 *
 */

package br.org.tracksource.gtm211api.errors;

/**
 * Error thrown when a GTM file with a text parameter supposed to be empty is loaded otherwise.
 *
 * @author Paulo Roberto Moura de Carvalho <a href="mailto:paulo.r.m.carvalho@gmail.com">paulo.r.m.carvalho@gmail.com</a>
 */
public class ErrorStringNotEmpty extends ErrorGTM {
    
    /** Default constructor. */
    public ErrorStringNotEmpty() {
        super("Attempt to set a text paramater that should be empty.");
    }
    
    /** Constructor with a message. */
    public ErrorStringNotEmpty(String message) {
        super("Attempt to set a text paramater that should be empty.\n" + message);
    }
}
