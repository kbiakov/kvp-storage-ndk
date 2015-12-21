package io.github.kbiakov.kvp_storage.storage.exceptions;

/**
 * @class KeyNotFoundException
 *
 * Thrown when key has no associated value
 */
public class KeyNotFoundException extends Exception {
    public KeyNotFoundException() {
        super("This key has no associated value!");
    }
}
