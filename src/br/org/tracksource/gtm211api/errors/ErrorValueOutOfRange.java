/*
 * ErrorValueOutOfRange.java
 *
 * Criada em 24 de Dezembro de 2008, 12:00
 *
 */

package br.org.tracksource.gtm211api.errors;

/**
 * Error thrown upon attempt to set a value out of allowed range.
 *
 * @author Paulo Roberto Moura de Carvalho <a href="mailto:paulo.r.m.carvalho@gmail.com">paulo.r.m.carvalho@gmail.com</a>
 */
public class ErrorValueOutOfRange extends ErrorGTM {
    
    /**
     *@param expected_range A textual description of the allowed range, for example: "0 to 100".
     *@param attempted_value The value outside the range converted to <code>String</code>.
     */
    public ErrorValueOutOfRange(String expected_range, String attempted_value) {
        super("Attempt to set " + attempted_value + " when the allowed range is " + expected_range + ".");
    }
}
