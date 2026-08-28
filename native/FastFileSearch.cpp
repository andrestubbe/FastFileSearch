#include <jni.h>
#include <windows.h>
#include <string>
#include <vector>
#include <algorithm>
#include <cctype>

using namespace std;

struct FileEntry {
    uint64_t id;
    uint64_t parentId;
    uint64_t size;
    uint64_t modified;
    uint32_t type;
    string path;
};

typedef void* (*GetEntriesFn)();

static string toLower(const string& str) {
    string lower = str;
    transform(lower.begin(), lower.end(), lower.begin(), [](unsigned char c) { return (char)tolower(c); });
    return lower;
}

extern "C" {

JNIEXPORT jobject JNICALL Java_fastfilesearch_FastFileSearch_fromIndexNative(JNIEnv* env, jclass clazz, jlong indexHandle, jobject options) {
    jmethodID constructor = env->GetMethodID(clazz, "<init>", "(J)V");
    if (constructor == NULL) return NULL;
    return env->NewObject(clazz, constructor, (jlong)0x12345678);
}

JNIEXPORT void JNICALL Java_fastfilesearch_FastFileSearch_close(JNIEnv* env, jobject obj) {
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
    if (limit <= 0) limit = 10000;

    jclass resultClass = env->FindClass("fastfilesearch/SearchResult");
    if (resultClass == NULL) return NULL;

    jmethodID resultConstructor = env->GetMethodID(resultClass, "<init>", "(Ljava/lang/String;DJJ)V");
    if (resultConstructor == NULL) return NULL;

    // Direct C++ memory access: obtain pointer to g_entries from fastfileindex.dll
    vector<FileEntry>* pEntries = nullptr;
    HMODULE hIndexDll = GetModuleHandleA("fastfileindex.dll");
    if (hIndexDll) {
        GetEntriesFn getFn = (GetEntriesFn)GetProcAddress(hIndexDll, "FastFileIndex_getNativeEntriesHandle");
        if (getFn) {
            pEntries = (vector<FileEntry>*)getFn();
        }
    }

    vector<jobject> matchedResults;
    matchedResults.reserve(min(limit, 500));

    if (pEntries) {
        // Fast direct C++ scan: 0 JNI cross-boundary overhead during traversal!
        const vector<FileEntry>& entries = *pEntries;
        size_t total = entries.size();
        for (size_t i = 0; i < total && (int)matchedResults.size() < limit; i++) {
            const FileEntry& e = entries[i];
            const string& pathStr = e.path;

            size_t lastSlash = pathStr.find_last_of("/\\");
            string filename = (lastSlash == string::npos) ? pathStr : pathStr.substr(lastSlash + 1);
            string filenameLower = toLower(filename);

            if (filenameLower.find(queryLower) == 0 || pathStr.find(queryStr) != string::npos) {
                jstring jpath = env->NewStringUTF(pathStr.c_str());
                double score = (filenameLower.find(queryLower) == 0) ? 1.0 : 0.8;
                jobject resObj = env->NewObject(resultClass, resultConstructor, jpath, (jdouble)score, (jlong)e.size, (jlong)e.modified);
                matchedResults.push_back(resObj);
                env->DeleteLocalRef(jpath);
            }
        }
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