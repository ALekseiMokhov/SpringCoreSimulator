package org.springframework.beanz.factory.Exceptions;

public class LocalNoSuchABeanException extends Exception {

    public LocalNoSuchABeanException(String message) {
       message = "The named bean cannot be found or doesn't exist!" ;
       System.out.println(message);
    }
}
