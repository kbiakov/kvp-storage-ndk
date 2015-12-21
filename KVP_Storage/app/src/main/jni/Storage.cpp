#include "Storage.h"
#include <string.h>

/* * * * * * * * * *
 * Main functions. *
 * * * * * * * * * */

// Check entity is valid
bool isValid(JNIEnv * pEnv, STEntity * pEntity, STType pType) {
    if (pEntity == NULL) {
        throwKeyNotFoundException(pEnv);
    } else if (pEntity -> mType != pType) {
        throwValueTypeException(pEnv);
    }
    return !pEnv -> ExceptionCheck();
}

// Allocate memory for entity in storage
STEntity * allocEntity(JNIEnv * pEnv, STStorage * pStorage, jstring pKey) {
    jboolean lError = JNI_FALSE;
    STEntity * lEntity = findEntity(pEnv, pStorage, pKey, &lError);

    if (lEntity != NULL) {
        releaseEntity(lEntity);
    } else if (!lError) {
        if (pStorage -> mLength >= ST_CAPACITY) {
            throwStorageFullException(pEnv);
        }

        lEntity = pStorage -> mEntities + pStorage -> mLength;

        const char * lKeyTmp = pEnv -> GetStringUTFChars(pKey, NULL);
        if (lKeyTmp == NULL) {
            return NULL;
        }

        lEntity -> mKey = (char *) malloc(strlen(lKeyTmp));
        strcpy(lEntity -> mKey, lKeyTmp);
        pEnv -> ReleaseStringUTFChars(pKey, lKeyTmp);

        ++pStorage -> mLength;
    }

    return lEntity;
}

// Find entity in storage
STEntity * findEntity(JNIEnv * pEnv, STStorage * pStorage, jstring pKey, jboolean * pError) {
    STEntity * lEntity = pStorage -> mEntities;
    STEntity * lEntityLast = lEntity + pStorage -> mLength;

    const char * lKeyTmp = pEnv -> GetStringUTFChars(pKey, NULL);
    if (lKeyTmp == NULL) {
        * pError = JNI_TRUE;
        return NULL;
    }

    while (lEntity < lEntityLast && strcmp(lEntity -> mKey, lKeyTmp) != 0) {
        ++lEntity;
    }
    pEnv -> ReleaseStringUTFChars(pKey, lKeyTmp);

    return lEntity == lEntityLast ? NULL : lEntity;
}

// Release memory for stored entity
STEntity * releaseEntity(STEntity * pEntity) {
    switch (pEntity -> mType) {
        case STBoolean:
        case STInteger:
        case STFloat:
            // nothing to do: primitive types
            break;
        case STString:
            free(pEntity -> mValue.mString);
            break;
    }
}

/* * * * * * * *
 * Exceptions. *
 * * * * * * * */

// Throw ValueTypeException
void throwValueTypeException(JNIEnv * pEnv) {
    jclass clazz = pEnv -> FindClass(ST_JEXS_PATH "ValueTypeException");
    if (clazz != NULL) {
        pEnv -> ThrowNew(clazz, "Key does not exist.");
    }
    pEnv -> DeleteLocalRef(clazz);
}

// Throw KeyNotFoundException
void throwKeyNotFoundException(JNIEnv * pEnv) {
    jclass clazz = pEnv -> FindClass(ST_JEXS_PATH "KeyNotFoundException");
    if (clazz != NULL) {
        pEnv -> ThrowNew(clazz, "Key does not exist.");
    }
    pEnv -> DeleteLocalRef(clazz);
}

// Throw StorageFullException
void throwStorageFullException(JNIEnv * pEnv) {
    jclass clazz = pEnv -> FindClass(ST_JEXS_PATH "StorageFullException");
    if (clazz != NULL) {
        pEnv -> ThrowNew(clazz, "Key does not exist.");
    }
    pEnv -> DeleteLocalRef(clazz);
}
