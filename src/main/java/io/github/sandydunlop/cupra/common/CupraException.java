package io.github.sandydunlop.cupra.common;

public class CupraException extends Exception {
    private String message;

    public CupraException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    

}
