#include "Storage.h"
#include <string.h>

// Check entity is valid
jboolean isValid(STEntity * pEntity, STType pType) {
    if (pEntity != NULL && pEntity -> mType == pType) {
        return JNI_TRUE;
    }
    return JNI_FALSE;
}

// Allocate memory for entity in storage
STEntity * allocEntity(JNIEnv* pEnv, STStorage * pStorage, jstring pKey) {
    jboolean lError = JNI_FALSE;
    STEntity * lEntity = findEntity(pEnv, pStorage, pKey, &lError);

    if (lEntity != NULL) {
        releaseEntity(lEntity);
    } else if (!lError) {
        if (pStorage -> mLength >= ST_CAPACITY) {
            return NULL;
        }
        lEntity = pStorage -> mEntities + pStorage -> mLength;

        const char* lKeyTmp = (*pEnv) -> GetStringUTFChars(pEnv, pKey, NULL);

        if (lKeyTmp == NULL) {
            return NULL;
        }

        lEntity -> mKey = (char*) malloc(strlen(lKeyTmp));
        strcpy(lEntity -> mKey, lKeyTmp);
        (*pEnv) -> ReleaseStringUTFChars(pEnv, pKey, lKeyTmp);

        ++pStorage -> mLength;
    }

    return lEntity;
}

// Find entity in storage
STEntity * findEntity(JNIEnv* pEnv, STStorage * pStorage, jstring pKey, jboolean* pError) {
    STEntity * lEntity = pStorage -> mEntities;
    STEntity * lEntityLast = lEntity + pStorage -> mLength;

    const char* lKeyTmp = (*pEnv) -> GetStringUTFChars(pEnv, pKey, NULL);

    if (lKeyTmp == NULL) {
        *pError = JNI_TRUE;
        return NULL;
    }

    while (lEntity < lEntityLast && strcmp(lEntity -> mKey, lKeyTmp) != 0) {
        ++lEntity;
    }
    (*pEnv) -> ReleaseStringUTFChars(pEnv, pKey, lKeyTmp);

    return lEntity == lEntityLast ? NULL : lEntity;
}

// Release memory for stored entity
STEntity * releaseEntity(STEntity * pEntity) {
    switch (pEntity -> mType) {
        case STBoolean:
        case STInteger:
        case STFloat:
            break;
        case STString:
            free(pEntity -> mValue.mString);
            break;
    }
}
