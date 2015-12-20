package io.github.kbiakov.kvp_storage.storage;

import java.util.ArrayList;

import io.github.kbiakov.kvp_storage.models.PairEntity;

/**
 * @class Storage
 *
 * Key-value pair storage as singleton using JNI
 */
public class Storage {

    public static class SingletonHolder {
        public static final Storage HOLDER_INSTANCE = new Storage();
    }

    public static Storage getInstance() {
        return SingletonHolder.HOLDER_INSTANCE;
    }

    static {
        System.loadLibrary("io_github_kbiakov_Storage");
    }

    private ArrayList<PairEntity> mPairEntities;

    public Storage() {
        this.mPairEntities = new ArrayList<>();
    }

    public native boolean getBoolean(String key);
    public native void putBoolean(String key, boolean value);

    public native int getInteger(String key);
    public native void putInteger(String key, int value);

    public native float getFloat(String key);
    public native void putFloat(String key, float value);

    public native String getString(String key);
    public native void putString(String key, String value);

    public void putKVPair(String key, String value, StoreType type) throws ValueTypeException{
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

    public ArrayList<PairEntity> getStoredPairEntities() {
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
                    value = getString(p.getKey());
                    break;
            }
            storedPairEntities.add(new PairEntity(key, value, type));
        }

        return storedPairEntities;
    }
}
