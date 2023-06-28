# BinaryWrapper
Android App to run Linux commands and executables on Android

## Steps to setup

1. import app to Android Studio(optional)
2. rename executable to `lib<executable>.so`. E.g `a.out` can be renamed to `liba.out.so`
3. copy all .so files to app/src/main/jniLibs/<ABI> directory. ABI can be any supported ABI. E.g. `arm64-v8a`
4. Update MainActivity.onCreate() with proper command and arguments. Users need to use `./filename`
5. In RunCommand function, set proper environment variables. (optional)
6. Update `wrap.sh` in jniLibs/<ABI> directory with desired arguments for launching app. (optional)
7. Please check dumpfile output and *logcat* for errors 
