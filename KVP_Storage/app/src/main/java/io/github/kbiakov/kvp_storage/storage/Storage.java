package io.github.kbiakov.kvp_storage.storage;

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
                if (!value.equals("true") || !value.equals("false")) {
                    throw new ValueTypeException();
                } else {
                    putBoolean(key, Boolean.parseBoolean(value));
                }
                break;
            case Integer:
                try {
                    putInteger(key, Integer.parseInt(value));
                } catch (NumberFormatException e) {
                    throw new ValueTypeException();
                }
                break;
            case Float:
                try {
                    putFloat(key, Float.parseFloat(value));
                } catch (NumberFormatException e) {
                    throw new ValueTypeException();
                }
                break;
            case String:
                putString(key, value);
                break;
        }
    }
}
