#!/bin/bash

source scripts/helper.sh

lib_name="libbc-ur.dylib"
out_dir=build/release
jni_md_dir="darwin"
deps=(automake make)
if ! is_osx; then
  # Additional dependencies for Linux
  deps+=(clang wget libc++-10-dev libc++abi-10-dev openjdk-8-jdk)

  lib_name="libbc-ur.so"
  jni_md_dir="linux"
fi

# Check and install missing dependencies
for dep in "${deps[@]}"; do
  check_dep $dep
done

# check and install missing dependencies for Linux/MacOS
if ! is_osx; then
  if clang-10 -v >/dev/null; then
    echo "clang-10 already installed"
  else
    echo "Installing LLVM..."
    wget https://apt.llvm.org/llvm.sh
    chmod +x llvm.sh
    ./llvm.sh 10 || exit
    rm llvm.sh
  fi

  export CC="clang-10"
  export CXX="clang++-10"
  export JAVA_HOME="/usr/lib/jvm/java-8-openjdk-amd64"
else
  java_home=$(/usr/libexec/java_home 2>/dev/null)
  if [ "$java_home" == "" ]; then
    echo "Installing JDK..."
    install_java8_mac || exit
    java_home="/Library/Java/JavaVirtualMachines/adoptopenjdk-8.jdk/Contents/Home"
  else
    echo "JDK has been installed at $java_home"
  fi
  export JAVA_HOME=$java_home
  export CC="clang"
  export CXX="clang++"
fi

# Install base bc-ur lib
pushd ../deps/bc-ur || exit
./configure
make clean
make CPPFLAGS=-fPIC check
popd || exit

# Install jni bc-ur lib
echo "Building $lib_name"
mkdir -p $out_dir
$CXX -I$JAVA_HOME/include -I$JAVA_HOME/include/$jni_md_dir -fexceptions -frtti -std=c++17 -stdlib=libc++ -shared -fPIC src/main/jniLibs/bc-ur.cpp ../deps/bc-ur/src/libbc-ur.a -o $out_dir/$lib_name || exit
echo "Checkout the release file at $out_dir/$lib_name"
