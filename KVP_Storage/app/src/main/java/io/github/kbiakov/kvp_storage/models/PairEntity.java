package io.github.kbiakov.kvp_storage.models;

import java.io.Serializable;

import io.github.kbiakov.kvp_storage.storage.StoreType;

public class PairEntity implements Serializable {

    private String mKey;
    private String mValue;
    private StoreType mType;

    public PairEntity(String mKey, String mValue, StoreType mType) {
        this.mKey = mKey;
        this.mValue = mValue;
        this.mType = mType;
    }

    public String getKey() {
        return mKey;
    }

    public String getValue() {
        return mValue;
    }

    public StoreType getType() {
        return mType;
    }
}
