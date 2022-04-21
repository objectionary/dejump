# EliminationOfJumps
## Goal:
#### In this section we consider the possibility to get rid of GOTO object in code (atom object of EO).
## Action plan:
1. As an input we get an *EO* program ***X*** (as XMIR-file), which contains atom-Objects "GOTO";
2. We replace pieces of code containing "goto" with semantically equivalent counterparts;
3. Now we have new ***X'*** program with the same logic, as an input program, but without "GOTO". Then we must prove that ***X'*** program
with the same input data as for the ***X*** program at the output gets the same results.

## Example:
We have an ***EO*** program ***X*** with GOTO object:

```
[x] > f
  memory > r
  seq > @
    r.write 0
    goto 
      [g]
        seq > @
          if.
            x.eq 0
            g.forward
            TRUE
          r.write (42.div x)
    r
```
Which is the same as this ***C++*** program:
```
int f(int x) {
  int r = 0;
  if (x == 0) goto exit;
  r = 42 / x;
  exit:
  return r;
}
```
----------------------------------------
After replacing the GOTO object, we get:
```
[x] > f
  memory > r
  seq > @
    r.write 0
      seq > @
        if.
          x.eq 0
          r                       // How just to return here?
          TRUE
        r.write (42.div x)
    r
```
The same on ***C++***:
```
int f(int x) {
  int r = 0;
  if (x == 0) return r;
  else {
    r = 42 / x;
    return r;
  }
}
```
