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

To add more tests, put your .YAML file to `src/test/resources/org/eolang/jump/packs`

2) Check for equivalent output for the program BEFORE transformation and AFTER it.
