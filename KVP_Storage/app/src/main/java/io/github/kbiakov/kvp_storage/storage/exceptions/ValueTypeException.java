package io.github.kbiakov.kvp_storage.storage.exceptions;

/**
 * @class ValueTypeException
 *
 * Thrown when value type does not match the specified type
 */
public class ValueTypeException extends Exception {
    public ValueTypeException() {
        super("Value type does not match the specified type!");
    }
}
