#ifndef KVP_STORAGE_STORE_H
#define KVP_STORAGE_STORE_H

#include "jni.h"
#include <stdint.h>

#define ST_CAPACITY 64

// STType: Predefined storage entities types
typedef enum {
    STBoolean, STInteger, STFloat, STString
} STType;

// STValue: Value can be one of the following types
typedef union {
    jboolean mBoolean;
    jint mInteger;
    jfloat mFloat;
    jcharArray mString;
} STValue;

// STEntity: Typed key-value pair
typedef struct {
    jcharArray mKey;
    STValue mValue;
    STType mType;
} STEntity;

// STStorage: All stored KV-pairs
typedef struct {
    STEntity mEntities[ST_CAPACITY];
    jint mLength;
} STStorage;

// Main functions
jboolean isValid(STEntity * pEntity, STType pType);
STEntity * allocEntity(JNIEnv* pEnv, STStorage * pStorage, jstring pKey);
STEntity * findEntity(JNIEnv* pEnv, STStorage * pStorage, jstring pKey, jboolean* pError);
STEntity * releaseEntity(STEntity * pEntity);

#endif //KVP_STORAGE_STORE_H
