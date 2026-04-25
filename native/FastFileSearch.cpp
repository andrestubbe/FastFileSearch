#include <jni.h>
#include <string>
#include <vector>
#include <unordered_map>
#include <algorithm>
#include <cctype>

// Simple Java-only implementation for MVP
// Native layer can be added later for performance optimization

// JNI: Build search structures from index
extern "C" JNIEXPORT void JNICALL Java_fastfilesearch_FastFileSearch_build(JNIEnv* env, jclass, jstring indexPath) {
    // MVP: Just store the index path, actual implementation will be in Java
    const char* path = env->GetStringUTFChars(indexPath, NULL);
    // TODO: Load FastFileIndex and build search structures
    env->ReleaseStringUTFChars(indexPath, path);
}

// JNI: Prefix search
extern "C" JNIEXPORT jobjectArray JNICALL Java_fastfilesearch_FastFileSearch_prefixSearch(JNIEnv* env, jclass, jstring prefix, jobject options) {
    // MVP: Return empty array, will be implemented in Java
    jclass resultClass = env->FindClass("fastfilesearch/FastFileSearch$SearchResult");
    jmethodID constructor = env->GetMethodID(resultClass, "<init>", "(Ljava/lang/String;DJJ)V");
    
    jobjectArray results = env->NewObjectArray(0, resultClass, NULL);
    return results;
}

// JNI: Fuzzy search
extern "C" JNIEXPORT jobjectArray JNICALL Java_fastfilesearch_FastFileSearch_fuzzySearch(JNIEnv* env, jclass, jstring query, jobject options) {
    // MVP: Return empty array, will be implemented in Java
    jclass resultClass = env->FindClass("fastfilesearch/FastFileSearch$SearchResult");
    jmethodID constructor = env->GetMethodID(resultClass, "<init>", "(Ljava/lang/String;DJJ)V");
    
    jobjectArray results = env->NewObjectArray(0, resultClass, NULL);
    return results;
}

// JNI: Exact search
extern "C" JNIEXPORT jobjectArray JNICALL Java_fastfilesearch_FastFileSearch_exactSearch(JNIEnv* env, jclass, jstring filename, jobject options) {
    // MVP: Return empty array, will be implemented in Java
    jclass resultClass = env->FindClass("fastfilesearch/FastFileSearch$SearchResult");
    jmethodID constructor = env->GetMethodID(resultClass, "<init>", "(Ljava/lang/String;DJJ)V");
    
    jobjectArray results = env->NewObjectArray(0, resultClass, NULL);
    return results;
}

// JNI: Update search structures incrementally
extern "C" JNIEXPORT void JNICALL Java_fastfilesearch_FastFileSearch_update(JNIEnv* env, jclass, jstring path, jint type) {
    // MVP: No-op for now
}

// JNI: Cleanup
extern "C" JNIEXPORT void JNICALL Java_fastfilesearch_FastFileSearch_cleanup(JNIEnv* env, jclass) {
    // MVP: No-op for now
}
