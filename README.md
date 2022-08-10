<img alt="logo" src="https://www.objectionary.com/cactus.svg" height="100px" />

Takes as an input a program in [EO](https://www.eolang.org/) and converts it to a semantically equivalent program, excluding the use of ***goto*** object.
The input data is a program in **.XMIR** format, the output program also returns the program in **.XMIR**.

## Quick Start

To use this program, the first step is to download the JAR. Then:

```
$ java -jar dejump-0.0.0-jar-with-dependencies.jar --help
```

Read its output and proceed.

## How to Contribute

You will need JDK 8+ and Maven 3.8+. Clone the repo and run the build like this:

```
$ mvn clean install -Pqulice
```

There are `.eo` files in `src/test/eo/org/eolang/dejump`, which you can edit or add your own source there. There are the following build steps that process these files:

  1. At `process-test-sources` Maven phase, `eo-maven-plugin` processes all `.eo` sources and generates `.xmir` files in `target/eo-test` directory.

  2. At `test` phase, `PacksTest` generates new `.eo` files from generated XMIR documents and saves them to `target/eo-after` directory. The files that it generates are not just `.eo` programs, but EO tests, which combine the code originally taken from original EO programs with the EO code generated. Thus, for `src/test/eo/org/eolang/dejump/alpha.eo` a modified EO source will be saved into `target/eo-itest/org/eolang/dejump/alpha-test.eo`.

  3. At `package` phase, a `.jar` is packaged.

  4. At `integration-test` phase, `maven-invoker-plugin` runs a new Maven instance with a new `pom.xml`, where all `.eo` tests from `target/eo-itest` are compiled and executed.
