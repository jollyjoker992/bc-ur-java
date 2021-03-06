# Installation for UR Java
This document gives the instruction for installing the Blockchain Commons UR Java library.

## Dependencies
We use [bc-ur](https://github.com/BlockchainCommons/bc-ur) as sub-module so make sure you clone this repo correctly.

> We add utility script for installing all dependencies, you can find it at `scripts/install_deps.sh`
If you want to do it manually by yourself, make sure all of following dependencies are installed correctly. 

### Linux
> Following packages can be installed via `apt-get`

- automake
- make
- libc++-10-dev
- libc++abi-10-dev
- openjdk-8-jdk
- clang-10

### MacOS
- automake
- make
- [Adopt Open JDK 1.8](https://github.com/AdoptOpenJDK/openjdk8-binaries/releases)

## Android
> Working directory: `/android`

### Testing
```console
./gradlew clean connectedDebugAndroidTest
```

### Bundling
```console
./gradlew clean assembleRelease
```

> The `app-release.aar` file would be found in `app/build/outputs/aar`. You can compile it as a library in your project.

> Notice that we distribute a prebuilt aar file at `prebuilt` directory.


## Java (Web app/ Desktop app)
> Working directory: `/java`

### Testing
```console
./gradlew clean test
```

### Bundling

Run following command for building the dynamic library file.
```console
./scripts/build.sh
```

> If you are working on mac OS, we recommend run the command with `sudo` permission.

> The dynamic library file would be found at `build/release`. You need to install it into `java.library.path` for JVM can load it at runtime.

The `jar` file will be bundled by running
```console
./gradlew assemble
```

> `jar` file just contain all `.class` files for running pure Java, no dynamic library is carried with.