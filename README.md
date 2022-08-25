<img alt="logo" src="https://www.objectionary.com/cactus.svg" height="100px" />

[![EO principles respected here](https://www.elegantobjects.org/badge.svg)](https://www.elegantobjects.org)
[![We recommend IntelliJ IDEA](https://www.elegantobjects.org/intellij-idea.svg)](https://www.jetbrains.com/idea/)

[![mvn-linux](https://github.com/objectionary/dejump/actions/workflows/build.yml/badge.svg)](https://github.com/objectionary/dejump/actions/workflows/build.yml)
[![License](https://img.shields.io/badge/license-MIT-green.svg)](https://github.com/objectionary/dejump/blob/master/LICENSE.txt)

Takes as an input a program in [EO](https://www.eolang.org/) and converts it to a semantically equivalent program, excluding the use of `goto` object.
The input data is a program in `.eo`/`.xmir` format, as the output the program returns `.eo`/`.xmir` respectively.

## Quick Start

To use this program, the first step is to download the JAR. Then:

```
$ java -jar dejump-0.0.0-jar-with-dependencies.jar --help
```

Read its output and proceed.

## CLI

An application has a CLI (Command line interface), which appears as:


## How to Contribute

You will need JDK 11+ and Maven 3.8+. Clone the repo and run the build like this:

```
$ mvn clean install
```

There are `.eo` files in `src/test/eo/org/eolang/dejump`, which you can edit or add your own source there. There are the following build steps that process these files:

  1. At `generate-test-sources` Maven phase, `gmavenplus-plugin` takes all `.eo` sources and generates new ones. The files that it generates are not just `.eo` programs, but EO tests, which combine the code originally taken from original EO programs with the EO code generated. Thus, for `src/test/eo/org/eolang/dejump/alpha.eo` a modified EO source will be saved into `target/eo-after/process_alpha-test.eo`.

  2. At `process-test-sources` phase, `eo-maven-plugin` process generated `.eo` files from `target/eo-after` directory and runs them as junit-tests. 
