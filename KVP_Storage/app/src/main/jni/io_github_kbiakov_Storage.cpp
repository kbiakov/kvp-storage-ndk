#include "Storage.h"
#include <string.h>

using namespace std;

static STStorage gStorage = { {}, 0 };

extern "C" {
    JNIEXPORT jboolean JNICALL
    Java_io_github_kbiakov_kvp_1storage_storage_Storage_getBoolean(JNIEnv * pEnv, jobject pInstance,
                                                                   jstring pKey) {

        STEntity * lEntity = findEntity(pEnv, &gStorage, pKey, JNI_FALSE);

        if (isValid(pEnv, lEntity, STBoolean)) {
            return (jboolean) (lEntity->mValue.mBoolean ? 1 : 0);
        } else {
            return JNI_FALSE;
        }
    }

    JNIEXPORT void JNICALL
    Java_io_github_kbiakov_kvp_1storage_storage_Storage_putBoolean(JNIEnv * pEnv, jobject pInstance,
                                                                   jstring pKey, jboolean pValue) {

        STEntity * lEntity = allocEntity(pEnv, &gStorage, pKey);

        if (lEntity != NULL) {
            lEntity -> mType = STBoolean;
            lEntity -> mValue.mBoolean = pValue;
        }
    }

    JNIEXPORT jint JNICALL
    Java_io_github_kbiakov_kvp_1storage_storage_Storage_getInteger(JNIEnv * pEnv, jobject pInstance,
                                                                   jstring pKey) {

        STEntity * lEntity = findEntity(pEnv, &gStorage, pKey, JNI_FALSE);

        if (isValid(pEnv, lEntity, STInteger)) {
            return lEntity -> mValue.mInteger;
        } else {
            return 0;
        }
    }

    JNIEXPORT void JNICALL
    Java_io_github_kbiakov_kvp_1storage_storage_Storage_putInteger(JNIEnv * pEnv, jobject pInstance,
                                                                   jstring pKey, jint pValue) {

        STEntity * lEntity = allocEntity(pEnv, &gStorage, pKey);

        if (lEntity != NULL) {
            lEntity -> mType = STInteger;
            lEntity -> mValue.mInteger = pValue;
        }
    }

    JNIEXPORT jfloat JNICALL
    Java_io_github_kbiakov_kvp_1storage_storage_Storage_getFloat(JNIEnv * pEnv, jobject pInstance,
                                                                 jstring pKey) {

        STEntity * lEntity = findEntity(pEnv, &gStorage, pKey, JNI_FALSE);

        if (isValid(pEnv, lEntity, STFloat)) {
            return lEntity -> mValue.mFloat;
        } else {
            return 0.0f;
        }
    }

    JNIEXPORT void JNICALL
    Java_io_github_kbiakov_kvp_1storage_storage_Storage_putFloat(JNIEnv * pEnv, jobject pInstance,
                                                                 jstring pKey, jfloat pValue) {

        STEntity * lEntity = allocEntity(pEnv, &gStorage, pKey);

        if (lEntity != NULL) {
            lEntity -> mType = STFloat;
            lEntity -> mValue.mFloat = pValue;
        }
    }

    JNIEXPORT jstring JNICALL
    Java_io_github_kbiakov_kvp_1storage_storage_Storage_getString(JNIEnv * pEnv, jobject pInstance,
                                                                  jstring pKey) {

        STEntity * lEntity = findEntity(pEnv, &gStorage, pKey, JNI_FALSE);

        if (isValid(pEnv, lEntity, STString)) {
            return pEnv -> NewStringUTF(lEntity -> mValue.mString);
        } else {
            return NULL;
        }
    }

    JNIEXPORT void JNICALL
    Java_io_github_kbiakov_kvp_1storage_storage_Storage_putString(JNIEnv * pEnv, jobject pInstance,
                                                                  jstring pKey, jstring pValue) {

        const char * lStringTmp = pEnv -> GetStringUTFChars(pValue, NULL);
        if (lStringTmp == NULL) {
            return;
        }

        STEntity * lEntity = allocEntity(pEnv, &gStorage, pKey);
        if (lEntity != NULL) {
            lEntity -> mType = STString;
            jsize lStringLength = pEnv -> GetStringUTFLength(pValue);
            lEntity -> mValue.mString = (char *) malloc(sizeof(char) * (lStringLength + 1));
            strcpy(lEntity -> mValue.mString, lStringTmp);
        }

        pEnv -> ReleaseStringUTFChars(pValue, lStringTmp);
    }
}
