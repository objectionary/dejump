<img alt="logo" src="https://www.objectionary.com/cactus.svg" height="100px" />

[![EO principles respected here](https://www.elegantobjects.org/badge.svg)](https://www.elegantobjects.org)
[![DevOps By Rultor.com](http://www.rultor.com/b/objectionary/eo)](http://www.rultor.com/p/objectionary/eo)
[![We recommend IntelliJ IDEA](https://www.elegantobjects.org/intellij-idea.svg)](https://www.jetbrains.com/idea/)

[![mvn-linux](https://github.com/objectionary/dejump/actions/workflows/build.yml/badge.svg)](https://github.com/objectionary/dejump/actions/workflows/build.yml)
[![PDD status](http://www.0pdd.com/svg?name=objectionary/dejump)](http://www.0pdd.com/p?name=objectionary/dejump)
[![Maven Central](https://img.shields.io/maven-central/v/org.eolang/dejump.svg)](https://maven-badges.herokuapp.com/maven-central/org.eolang/dejump)
[![Hits-of-Code](https://hitsofcode.com/github/objectionary/dejump?branch=master)](https://hitsofcode.com/github/objectionary/dejump/view?branch=master)
[![Lines of code](https://img.shields.io/tokei/lines/github/objectionary/dejump)](https://img.shields.io/tokei/lines/github/objectionary/dejump)
[![License](https://img.shields.io/badge/license-MIT-green.svg)](https://github.com/objectionary/dejump/blob/master/LICENSE.txt)

Takes as an input a program in [EO](https://www.eolang.org/) and converts it to a semantically equivalent program, excluding the use of `goto` object.
The input data is a program in `.eo`/`.xmir` format, as the output the program returns `.eo`/`.xmir` respectively.

## Usage
To use this program, the first step is to download the JAR. Then:

```
$ java -jar dejump-0.0.0-jar-with-dependencies.jar --help
```

An application has a CLI (Command line interface), which appears as:

```
Usage: dejump [-hV] [--eo] <file>
Replaces objects GOTO with semantically equivalent
      <file>      Absolute path of file to transform
      --eo        treat input file as EO program (not XMIR)
  -h, --help      Show this help message and exit.
  -V, --version   Print version information and exit.
```

Depending on the input file format, the output file format will be similar.
By default, the input file format is `.xmir`.

In the directory that contains the file passed as a parameter, a new directory `generated` is created, in which, after the program is executed, the transformed file will be located.

## Example

For the following `.eo`-code, which appears at `temp/example.eo`:

```
[] > example
  memory 0 > x
  goto > @
    [g]
      seq > @
        x.write (x.plus 1)
        if.
          x.eq 5
          g.forward x
          QQ.io.stdout "Again\n"
        g.backward
```

After the application is executed, the file `temp/generated/example_transformed.eo` will be created:

```
[] > example
  memory 0 > x
  seq > @
    flag_fwd.write -1
    flag_bwd.write 0
    while. > g_rem!
      or.
        flag_fwd.eq -1
        flag_bwd.eq 0
      [i]
        seq > @
          flag_fwd.write 0
          flag_bwd.write 1
          seq
            x.write (x.plus 1)
            if.
              x.eq 5
              flag_fwd.write 2
              QQ.io.stdout "Again\n"
            if.
              flag_fwd.eq 0
              flag_bwd.write 0
              TRUE
    if.
      flag_fwd.eq 2
      x
      g_rem
  memory -1 > flag_fwd
  memory 0 > flag_bwd
```

## How to Contribute

You will need JDK 11+ and Maven 3.8+. Clone the repo and run the build like this:

```
$ mvn clean install -Pqulice
```

There are `.eo` files in [`src/test/eo/org/eolang/dejump`](https://github.com/objectionary/dejump/tree/master/src/test/eo/org/eolang/dejump), which you can edit or add your own source there. There are the following build steps that process these files:

1. At `generate-test-sources` Maven phase, `gmavenplus-plugin` takes all `.eo` sources and generates new ones. The files that it generates are not just `.eo` programs, but EO tests, which combine the code originally taken from original EO programs with the EO code generated. Thus, for `src/test/eo/org/eolang/dejump/alpha.eo` a modified EO source will be saved into `target/eo-after/process_alpha-test.eo`.

2. At `process-test-sources` phase, `eo-maven-plugin` process generated `.eo` files from `target/eo-after` directory and runs them as junit-tests.
