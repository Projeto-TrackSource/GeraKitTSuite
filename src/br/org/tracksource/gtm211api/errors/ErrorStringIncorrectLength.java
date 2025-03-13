/*
 * ErrorStringIncorrectLength.java
 *
 * Criada em 28 de Dezembro de 2008, 16:05
 *
 */

package br.org.tracksource.gtm211api.errors;

/**
 * Error thrown when a GTM file with a text parameter supposed to be of determined length is loaded longer or shorter than the expected lenght.
 *
 * @author Paulo Roberto Moura de Carvalho <a href="mailto:paulo.r.m.carvalho@gmail.com">paulo.r.m.carvalho@gmail.com</a>
 */
public class ErrorStringIncorrectLength extends ErrorGTM {
    
    /** Constructor. */
    public ErrorStringIncorrectLength(int expected_length, int actual_length) {
        super("Attempt to store a text of length " + String.valueOf(actual_length) + " into a field expected to have " + String.valueOf(expected_length) + " in length.");
    }
    
}
