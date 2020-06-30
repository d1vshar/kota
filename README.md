# kota :tea: [WIP]

Kota is a HTTP/1.1 implementation of a concurrent static web server in Kotlin.

> Work in progress. Many important features like caching are not implemented.

Since it's a static web server (like GitHub pages), it only supports GET requests. 

## Building

Build using these -

```shell script
git clone https://github.com/l0llygag/kota
cd kota
gradle build
```

Two jars will be built in `build/libs/` - `kota-x.x.x.jar` and `kota-x.x.x-all.jar` (with dependencies)

## Usage

Currently, the server supports only one configuration option, the port.

All static files that need to be hosted should be in the `public/` sub-folder (relative to the jar)
```
-- root/
    |-- public/
    |    |-- index.html
    |    |-- subfolders/
    |-- kota-x.x.x-all.jar
```

To run the jar:

`java -jar build/libs/kota-x.x.x-all.jar --port 8000`

> You will need JDK 8+

In case you want to use it as a library, you can include the jar in your class-path and use
```kotlin
Server(port).listen()
```

## License

MIT