#include <elf.h>//
// VirtualApp Native Project
//
#include <Foundation/IOUniformer.h>
#include <fb/include/fb/Build.h>
#include <fb/include/fb/ALog.h>
#include <fb/include/fb/fbjni.h>
#include <Substrate/SubstrateHook.h>
#include "VAJni.h"
#include <android/log.h>
#include <map>
#include <unistd.h>

using namespace facebook::jni;
using namespace std;
map<const char *,const char *> ghashMapInfo;
static void jni_nativeLaunchEngine(alias_ref<jclass> clazz, JArrayClass<jobject> javaMethods,
                                   jstring packageName,
                                   jboolean isArt, jint apiLevel, jint cameraMethodType) {
    hookAndroidVM(javaMethods, packageName, isArt, apiLevel, cameraMethodType);
}

static void jni_disableJit(alias_ref<jclass> clazz, jint apiLevel) {
    disableJit(apiLevel);
}

static void jni_nativeEnableIORedirect(alias_ref<jclass>, jstring selfSoPath, jint apiLevel,
                                       jint preview_api_level) {
    ScopeUtfString so_path(selfSoPath);
    IOUniformer::startUniformer(so_path.c_str(), apiLevel, preview_api_level);
}

static void jni_nativeIOWhitelist(alias_ref<jclass> jclazz, jstring _path) {
    ScopeUtfString path(_path);
    IOUniformer::whitelist(path.c_str());
}

static void jni_nativeIOForbid(alias_ref<jclass> jclazz, jstring _path) {
    ScopeUtfString path(_path);
    IOUniformer::forbid(path.c_str());
}


static void jni_nativeIORedirect(alias_ref<jclass> jclazz, jstring origPath, jstring newPath) {
    ScopeUtfString orig_path(origPath);
    ScopeUtfString new_path(newPath);
    IOUniformer::redirect(orig_path.c_str(), new_path.c_str());

}

static jstring jni_nativeGetRedirectedPath(alias_ref<jclass> jclazz, jstring origPath) {
    ScopeUtfString orig_path(origPath);
    const char *redirected_path = IOUniformer::query(orig_path.c_str());
    if (redirected_path != NULL) {
        return Environment::current()->NewStringUTF(redirected_path);
    }
    return NULL;
}

static jstring jni_nativeReverseRedirectedPath(alias_ref<jclass> jclazz, jstring redirectedPath) {
    ScopeUtfString redirected_path(redirectedPath);
    const char *orig_path = IOUniformer::reverse(redirected_path.c_str());
    return Environment::current()->NewStringUTF(orig_path);
}

jstring stoJstring(JNIEnv* env, const char* pat)
{
    jclass strClass = env->FindClass("Ljava/lang/String;");
    jmethodID ctorID = env->GetMethodID(strClass, "<init>", "([BLjava/lang/String;)V");
    jbyteArray bytes = env->NewByteArray(strlen(pat));
    env->SetByteArrayRegion(bytes, 0, strlen(pat), (jbyte*)pat);
    jstring encoding = env->NewStringUTF("utf-8");
    return (jstring)env->NewObject(strClass, ctorID, bytes, encoding);
}


HOOK_DEF(int,__system_property_get, const char *key, char *value) {

    int ret = orig___system_property_get(key, value);


//    __android_log_print(ANDROID_LOG_INFO, "replaceVal", "replaceinfo:%s------%s-----",key,value);



    map<const char *,const char *>::iterator it;
    it = ghashMapInfo.begin();


    //=========================
    //  1. 输出所有 Pair 元素
    //=========================
    // 迭代器遍历 map
    for (it; it != ghashMapInfo.end(); it++)
    {
        // map的迭代器，可以用 first 访问std::pair的第一个成员(Type1)，second 访问第二个成员 (Type2)
//        __android_log_print(ANDROID_LOG_INFO, "replaceVal", "replaceinfo:%s-----%d",it->first,strcmp(key,it->first));

        if(strcmp(key,it->first)==0)
        {
            strcpy((char *)value,(char *)it->second);
//            __android_log_print(ANDROID_LOG_INFO, "replaceVal", "-----------------------------------------------------------------");
        }
    }



    return strlen(value);
}




static inline void
hook_function(void *handle, const char *symbol, void *new_func, void **old_func) {
    void *addr = dlsym(handle, symbol);
    if (addr == NULL) {
        return;
    }
    MSHookFunction(addr, new_func, old_func);
}

void hookPropertySymbol()
{
    void *android_runtime = dlopen("libcutils.so",RTLD_NOW);

    if(android_runtime)
    {
        void *property_get = dlsym(android_runtime,"__system_property_get");

        if(property_get)
        {
            HOOK_SYMBOL(android_runtime,__system_property_get);
        }
        else
        {
            __android_log_assert("gctech","android_runtime","can't have property_get symbol");
        }

    }
    else
    {
        __android_log_assert("android_runtime","android_runtime null","an't have property_get libcutils.so");
    }
}

const char *cpuinfo;


HOOK_DEF(int, read, int fd, void * buf, size_t count) {

    int ret = orig_read(fd, buf, count);

    char *buf_char = (char *) buf;

//    __android_log_print(ANDROID_LOG_INFO,"architecture","------------------------------------%s------",buf_char);
    if (strstr(buf_char, "Processor") && strstr(buf_char, "Features") && strstr(buf_char, "CPU implementer\t:")) {
//        char *buf_char_tmp[354];
////
//        buf = buf_char_tmp;
//        reset(&buf);

//        buf = ;

        if(strstr(buf_char,"Features\t: swp half thumb fastmult vfp edsp neon vfpv3 tls vfpv4 idiva idivt"))
        {
            __android_log_print(ANDROID_LOG_INFO,"architecture","have");
            return ret;
        }
        __android_log_print(ANDROID_LOG_INFO,"architecture","--------------------------NO----------%s------",buf_char);

//        if(strlen((char *)buf)>strlen(cpuinfo))
//        {
//            memset( buf, 0, sizeof(buf) );
//        }

        int fd1 =  open("/sdcard/cpuinfo", O_RDWR);

        ret = orig_read(fd1, buf, count);



        close(fd1);



//        return strlen(cpuinfo)-1;
//        int len = strlen(cpuinfo)>strlen(buf_char)?strlen(buf_char):strlen(cpuinfo);

//        char *buf_char_tmp[500];


//        strcpy((char *) buf,cpuinfo);

//        buf = buf_char_tmp;
//        char *cpu = (char *)cpuinfo;
//        for (int i = 0;i < len;i++)
//        {
//            buf_char[i] =  cpu[i];
//        }
//        strcpy((char *)(buf),cpuinfo);
//        __android_log_assert("gctech","architecture","------------------------------------%s------",cpuinfo);
//        return strlen(cpuinfo);
    }


    return ret;
}

HOOK_DEF( int,open, const char *pathname,int flags) {
    if(strcmp(pathname,"/proc/cpuinfo")==0)
    {
        pathname = "/sdcard/cpuinfo";
    }
    ALOGD("open","openopenopenopenopen");

    return orig_open(pathname,flags);
}

HOOK_DEF( FILE *,fopen, char *path,char *mode) {


    __android_log_print(ANDROID_LOG_INFO,"fopen","fopenfopenfopen:%s",path);
    FILE *fp = orig_fopen(path, mode);



    return fp;
}

HOOK_DEF( FILE *,freopen, const char * filename, const char * mode, FILE * stream) {

    __android_log_print(ANDROID_LOG_INFO,"fileno","freopenfreopenfreopenfreopen:%s",filename);
    FILE * fp = orig_freopen(filename,mode,stream);



    return fp;
}



static void hookcpuinfo(alias_ref<jclass> jclazz,jstring cpuinfostr)
{ void *handle = dlopen("libc.so", RTLD_NOW);

    if (handle) {
//        HOOK_SYMBOL(handle, read);
//        HOOK_SYMBOL(handle, freopen);
//        HOOK_SYMBOL(handle, fopen);
//        HOOK_SYMBOL(handle, open);
        jboolean* isCopy = JNI_FALSE;
        cpuinfo = Environment::current()->GetStringUTFChars(cpuinfostr,isCopy);
    }
    dlclose(handle);
}
static void addMap(alias_ref<jclass> jclazz,jstring key,jstring val)
{
//    __android_log_print(ANDROID_LOG_INFO, "addMap", "addMap start");
    const  char *key_char = Environment::current()->GetStringUTFChars(key,0);
    const char *val_str = Environment::current()->GetStringUTFChars(val,0);
    ghashMapInfo[key_char] = val_str;

//    __android_log_print(ANDROID_LOG_INFO, "addMap", "addMap--------%s:%s end",key_char,val_str);
}

static void hookProperty(alias_ref<jclass> jclazz,jobject hashMapInfo) {

    hookPropertySymbol();
    __android_log_print(ANDROID_LOG_INFO, "hookProperty", "hookProperty sucess");
}


static jstring getSerious(alias_ref<jclass> jclazz) {

    char val[PROP_VALUE_MAX];
   __system_property_get("ro.serialno",val);
    __android_log_print(ANDROID_LOG_INFO, "getSerious", "getSerious sucess:%s",val);

    return Environment::current()->NewStringUTF(val);
}


HOOK_DEF(int ,sqlite3_open_v2, const char *filename,   /* Database filename (UTF-8) */
         void **ppDb  ) {

    int ret = orig_sqlite3_open_v2(filename,ppDb);

    __android_log_print(ANDROID_LOG_INFO, "nativeExecuteForString", "nativeExecuteForString");
    return ret;
}

static void hookSqlite()
{
    void *android_runtime = dlopen("libandroid_runtime.so",RTLD_NOW);

    if(android_runtime)
    {
        void *nativeExecuteForStringSy = dlsym(android_runtime,"sqlite3_open_v2");

        if(nativeExecuteForStringSy)
        {
            HOOK_SYMBOL(android_runtime,sqlite3_open_v2);
        }
        else
        {
            __android_log_assert("gctech","sqlite3_open","can't have sqlite3_open symbol");
        }

    }
    else
    {
        __android_log_assert("android_runtime","android_runtime null","an't have nativeExecuteForString libcutils.so");
    }
 }






alias_ref<jclass> nativeEngineClass;

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *) {


    return initialize(vm, [] {
        nativeEngineClass = findClassStatic("com/lody/virtual/client/NativeEngine");
        nativeEngineClass->registerNatives({
                        makeNativeMethod("nativeEnableIORedirect",
                                         jni_nativeEnableIORedirect),
                        makeNativeMethod("getSerious",
                                         getSerious),
                        makeNativeMethod("hookProperty",
                                         hookProperty),
                        makeNativeMethod("addMap",addMap),
                        makeNativeMethod("nativeIOWhitelist",
                                         jni_nativeIOWhitelist),
                        makeNativeMethod("nativeIOForbid",
                                         jni_nativeIOForbid),
                        makeNativeMethod("nativeIORedirect",
                                         jni_nativeIORedirect),
                        makeNativeMethod("nativeGetRedirectedPath",
                                         jni_nativeGetRedirectedPath),
                        makeNativeMethod("nativeReverseRedirectedPath",
                                         jni_nativeReverseRedirectedPath),
                        makeNativeMethod("nativeLaunchEngine",
                                         jni_nativeLaunchEngine),
                        makeNativeMethod("disableJit", jni_disableJit)
                }


        );
    });
}

extern "C" __attribute__((constructor)) void _init(void) {
    IOUniformer::init_env_before_all();
}


