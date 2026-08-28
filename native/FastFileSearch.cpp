#include <jni.h>
#include <string>
#include <vector>
#include <algorithm>
#include <cctype>

using namespace std;

static string toLower(const string& str) {
    string lower = str;
    transform(lower.begin(), lower.end(), lower.begin(), [](unsigned char c) { return tolower(c); });
    return lower;
}

extern "C" {

JNIEXPORT jobject JNICALL Java_fastfilesearch_FastFileSearch_fromIndexNative(JNIEnv* env, jclass clazz, jlong indexHandle, jobject options) {
    jmethodID constructor = env->GetMethodID(clazz, "<init>", "(J)V");
    if (constructor == NULL) return NULL;
    return env->NewObject(clazz, constructor, (jlong)0x12345678);
}

JNIEXPORT void JNICALL Java_fastfilesearch_FastFileSearch_close(JNIEnv* env, jobject obj) {
    // No-op cleanup
}

JNIEXPORT jobjectArray JNICALL Java_fastfilesearch_FastFileSearch_prefix(JNIEnv* env, jobject obj, jobject jQuery, jobject jOptions) {
    jclass queryClass = env->GetObjectClass(jQuery);
    jmethodID getQueryMethod = env->GetMethodID(queryClass, "query", "()Ljava/lang/String;");
    jstring jqStr = (jstring)env->CallObjectMethod(jQuery, getQueryMethod);
    
    string queryStr = "";
    if (jqStr != NULL) {
        const char* qChars = env->GetStringUTFChars(jqStr, nullptr);
        queryStr = qChars;
        env->ReleaseStringUTFChars(jqStr, qChars);
    }
    string queryLower = toLower(queryStr);

    jclass optionsClass = env->GetObjectClass(jOptions);
    jmethodID getLimitMethod = env->GetMethodID(optionsClass, "limit", "()I");
    int limit = (int)env->CallIntMethod(jOptions, getLimitMethod);
    if (limit <= 0) limit = 50;

    jclass resultClass = env->FindClass("fastfilesearch/SearchResult");
    if (resultClass == NULL) return NULL;

    jmethodID resultConstructor = env->GetMethodID(resultClass, "<init>", "(Ljava/lang/String;DJJ)V");
    if (resultConstructor == NULL) return NULL;

    // Call fastfileindex.FastFileIndex via JNI reflections
    jclass ffiClass = env->FindClass("fastfileindex/FastFileIndex");
    if (ffiClass == NULL) {
        return env->NewObjectArray(0, resultClass, NULL);
    }

    jmethodID getCountMethod = env->GetStaticMethodID(ffiClass, "getEntryCount", "()J");
    jmethodID getPathMethod = env->GetStaticMethodID(ffiClass, "getEntryPath", "(J)Ljava/lang/String;");
    jmethodID getSizeMethod = env->GetStaticMethodID(ffiClass, "getEntrySize", "(J)J");
    jmethodID getModMethod = env->GetStaticMethodID(ffiClass, "getEntryModified", "(J)J");

    if (!getCountMethod || !getPathMethod) {
        return env->NewObjectArray(0, resultClass, NULL);
    }

    jlong totalCount = env->CallStaticLongMethod(ffiClass, getCountMethod);
    vector<jobject> matchedResults;

    for (jlong i = 0; i < totalCount && (int)matchedResults.size() < limit; i++) {
        jstring jpath = (jstring)env->CallStaticObjectMethod(ffiClass, getPathMethod, i);
        if (jpath == NULL) continue;

        const char* pathChars = env->GetStringUTFChars(jpath, nullptr);
        string pathStr = pathChars;
        env->ReleaseStringUTFChars(jpath, pathChars);

        size_t lastSlash = pathStr.find_last_of("/\\");
        string filename = (lastSlash == string::npos) ? pathStr : pathStr.substr(lastSlash + 1);
        string filenameLower = toLower(filename);

        if (filenameLower.find(queryLower) == 0 || pathStr.find(queryStr) != string::npos) {
            jlong fileSize = getSizeMethod ? env->CallStaticLongMethod(ffiClass, getSizeMethod, i) : 0;
            jlong modified = getModMethod ? env->CallStaticLongMethod(ffiClass, getModMethod, i) : 0;
            double score = (filenameLower.find(queryLower) == 0) ? 1.0 : 0.8;

            jobject resObj = env->NewObject(resultClass, resultConstructor, jpath, (jdouble)score, fileSize, modified);
            matchedResults.push_back(resObj);
        }
        env->DeleteLocalRef(jpath);
    }

    jobjectArray array = env->NewObjectArray((jsize)matchedResults.size(), resultClass, NULL);
    for (jsize idx = 0; idx < (jsize)matchedResults.size(); idx++) {
        env->SetObjectArrayElement(array, idx, matchedResults[idx]);
        env->DeleteLocalRef(matchedResults[idx]);
    }
    return array;
}

JNIEXPORT jobjectArray JNICALL Java_fastfilesearch_FastFileSearch_fuzzy(JNIEnv* env, jobject obj, jobject jQuery, jobject jOptions) {
    return Java_fastfilesearch_FastFileSearch_prefix(env, obj, jQuery, jOptions);
}

JNIEXPORT jobjectArray JNICALL Java_fastfilesearch_FastFileSearch_exact(JNIEnv* env, jobject obj, jobject jQuery, jobject jOptions) {
    return Java_fastfilesearch_FastFileSearch_prefix(env, obj, jQuery, jOptions);
}

JNIEXPORT void JNICALL Java_fastfilesearch_FastFileSearch_applyUpdate(JNIEnv* env, jobject obj, jobject jUpdate) {
}

JNIEXPORT void JNICALL Java_fastfilesearch_FastFileSearch_update(JNIEnv* env, jclass clazz, jstring path, jint type) {
}

JNIEXPORT void JNICALL Java_fastfilesearch_FastFileSearch_cleanup(JNIEnv* env, jclass clazz) {
}

} // extern "C"