package com.fox.game;

public class CityStorageException extends RuntimeException {
    public CityStorageException(String message, Throwable cause) {
        super(message, cause);
    }

    public CityStorageException(String message) {
        super(message);
    }
}
