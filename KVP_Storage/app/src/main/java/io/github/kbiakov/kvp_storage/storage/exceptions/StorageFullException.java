package io.github.kbiakov.kvp_storage.storage.exceptions;

/**
 * @class StorageFullException
 *
 * Thrown when exceeds maximum capacity of storage
 */
public class StorageFullException extends RuntimeException {
    public StorageFullException() {
        super("Exceeds maximum capacity of storage");
    }
}
