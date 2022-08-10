<img alt="logo" src="https://www.objectionary.com/cactus.svg" height="100px" />

Takes as an input a program in [EO](https://www.eolang.org/) and converts it to a semantically equivalent program, excluding the use of ***goto*** object.
The input data is a program in **.XMIR** format, the output program also returns the program in **.XMIR**.

## Quick start
To use this program, the first step is to clone this repository to your device locally. This can be done via the command line by writing:
```
git clone https://github.com/MikhailLipanin/EliminationOfJumps.git
```
To compile a program, you can do it through the command line by running it from the directory where the project was cloned:
```
javac Main.java
```
And run with:
```
java Main arg0
```
Where *arg0* will be the path to the **.XMIR** file you want to convert.

The converted file will be located in the same directory where the repository was cloned.

## How to run tests

You can run tests just like this:
```
mvn clean test
```

There are 2 types of tests:
1) Validating XSL Transformations

The *before* `eo` code is taken from `yaml` file, applies XSL-transformations, and compares it to `eo` code *after*.

To add more tests, put your `yaml` file to `src/test/resources/org/eolang/jump/packs`.

2) Check for equivalent output for the program BEFORE transformation and AFTER it.

We take the `eo` code, transform it to `XMIR` and apply all the XSL-transformations to it, then transform it back to `eo`.
After that we create new `eo` file, which has the next structure:
```
[] > name_of_test
  assert-that > @
    {original eo-code}
    $.equal-to
      {eo-code after transformations}
```
Then, this new `eo`-file executes and if exception didn't throwed, output is equal.

To add more tests, put your `eo` file to `src/test/eo/org/eolang/jump`.
