
Takes as an input a program in a strictly object-oriented programming language and converts it to a semantically equivalent program, excluding the use of ***goto*** object.
The input data is a program written in EO in **.XMIR** format, the output program also returns the program in **.XMIR**.

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
