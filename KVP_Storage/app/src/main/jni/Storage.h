#ifndef KVP_STORAGE_STORE_H
#define KVP_STORAGE_STORE_H

#include "jni.h"
#include <stdint.h>

#define ST_CAPACITY 64
#define ST_JEXS_PATH "io/github/kbiakov/kvp_storage/storage/exceptions/"

// STType: Predefined storage entities types
typedef enum {
    STBoolean, STInteger, STFloat, STString
} STType;

// STValue: Value can be one of the following types
typedef union {
    bool mBoolean;
    int mInteger;
    float mFloat;
    char * mString;
} STValue;

// STEntity: Typed key-value pair
typedef struct {
    char * mKey;
    STValue mValue;
    STType mType;
} STEntity;

// STStorage: All stored KV-pairs
typedef struct {
    STEntity mEntities[ST_CAPACITY];
    int8_t mLength;
} STStorage;

// Main functions
bool isValid(JNIEnv * pEnv, STEntity * pEntity, STType pType);
STEntity * allocEntity(JNIEnv * pEnv, STStorage * pStorage, jstring pKey);
STEntity * findEntity(JNIEnv * pEnv, STStorage * pStorage, jstring pKey, jboolean * pError);
STEntity * releaseEntity(STEntity * pEntity);

// Exceptions
void throwValueTypeException(JNIEnv * pEnv);
void throwKeyNotFoundException(JNIEnv * pEnv);
void throwStorageFullException(JNIEnv * pEnv);

#endif //KVP_STORAGE_STORE_H
