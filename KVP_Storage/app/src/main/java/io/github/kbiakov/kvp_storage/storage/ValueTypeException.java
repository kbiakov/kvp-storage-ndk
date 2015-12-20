package io.github.kbiakov.kvp_storage.storage;

public class ValueTypeException extends NumberFormatException {
    public ValueTypeException() {
        super("Value type does not match the specified type!");
    }
}
