/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package machine.exception;

/**
 *
 * @author chaupdm
 */
public class InternalException extends RuntimeException {

    public InternalException() {
    }

    public InternalException(String string) {
        super(string);
    }
    
}
