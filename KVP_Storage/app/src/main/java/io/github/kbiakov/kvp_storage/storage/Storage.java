package io.github.kbiakov.kvp_storage.storage;

import java.util.ArrayList;

import io.github.kbiakov.kvp_storage.models.PairEntity;
import io.github.kbiakov.kvp_storage.storage.exceptions.KeyNotFoundException;
import io.github.kbiakov.kvp_storage.storage.exceptions.ValueTypeException;

/**
 * @class Storage
 *
 * Key-value pair storage as singleton using JNI
 */
public class Storage {

    // On Demand Holder idiom singleton
    public static class SingletonHolder {
        public static final Storage HOLDER_INSTANCE = new Storage();
    }

    /**
     * Get singleton instance of storage
     *
     * @return Storage
     */
    public static Storage getInstance() {
        return SingletonHolder.HOLDER_INSTANCE;
    }

    // Static initialization block for library loading
    static {
        System.loadLibrary("io_github_kbiakov_Storage");
    }

    // Temp. pair entities
    private ArrayList<PairEntity> mPairEntities;

    // Main constructor
    public Storage() {
        this.mPairEntities = new ArrayList<>();
    }

    /**
     * Put KV-pair to storage
     *
     * @param key
     * @param value
     * @param type
     * @throws ValueTypeException Throws if value type does not match the specified type
     */
    public void putPairToStorage(String key, String value, StoreType type) throws ValueTypeException{
        switch (type) {
            case Boolean:
                if (value.equals("true") || value.equals("false")) {
                    putBoolean(key, Boolean.parseBoolean(value));
                    mPairEntities.add(new PairEntity(key, value, type));
                } else {
                    throw new ValueTypeException();
                }
                break;
            case Integer:
                try {
                    putInteger(key, Integer.parseInt(value));
                    mPairEntities.add(new PairEntity(key, value, type));
                } catch (NumberFormatException e) {
                    throw new ValueTypeException();
                }
                break;
            case Float:
                try {
                    putFloat(key, Float.parseFloat(value));
                    mPairEntities.add(new PairEntity(key, value, type));
                } catch (NumberFormatException e) {
                    throw new ValueTypeException();
                }
                break;
            case String:
                putString(key, value);
                mPairEntities.add(new PairEntity(key, value, type));
                break;
        }
    }

    /**
     * Get stored KV-pair entities from storage
     *
     * @return Stored pair entities
     */
    public ArrayList<PairEntity> getStoredPairEntities()
            throws KeyNotFoundException, ValueTypeException {

        ArrayList<PairEntity> storedPairEntities = new ArrayList<>();

        for (PairEntity p: mPairEntities) {
            String key = p.getKey();
            StoreType type = p.getType();
            String value;

            switch (type) {
                case Boolean:
                    value = String.valueOf(getBoolean(key));
                    break;
                case Integer:
                    value = String.valueOf(getInteger(key));
                    break;
                case Float:
                    value = String.valueOf(getFloat(key));
                    break;
                case String: default:
                    value = getString(key);
                    break;
            }
            storedPairEntities.add(new PairEntity(key, value, type));
        }

        return storedPairEntities;
    }

    // Main JNI-methods (put & get) for storage

    private native void putBoolean(String key, boolean value);
    private native void putInteger(String key, int value);
    private native void putFloat(String key, float value);
    private native void putString(String key, String value);

    private native boolean getBoolean(String key) throws
            KeyNotFoundException, ValueTypeException;

    private native int getInteger(String key) throws
            KeyNotFoundException, ValueTypeException;

    private native float getFloat(String key) throws
            KeyNotFoundException, ValueTypeException;

    private native String getString(String key) throws
            KeyNotFoundException, ValueTypeException;
}
