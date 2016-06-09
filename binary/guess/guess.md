# guess - 120 (Binary)
#### Writeup by Ehsan Asdar

## Problem ##
Can you guess the numbers? guess.c guess nc p.tjctf.org 8007

## Answer ##

### Overview ###
Implement srand with the same time-based seed used by the server.

### Details ###
Reading the source in `guess.c`, we see that the program calls `srand()` with the current system time in seconds (`time(NULL)`) and then calls `rand()` to get a random number. Since C random implments a  [Pseudorandom number generator](https://en.wikipedia.org/wiki/Pseudorandom_number_generator), we can get the exact same number `rand()` generates simply by seeding another C program with the same time value. The follow C and Python code took care of this.

solve.py:
```python
import subprocess
import time
from pwn import *
r = remote('p.tjctf.org', 8007)
for x in range(0, 100) :
    sOut = subprocess.Popen(["./guessgen"], stdout=subprocess.PIPE)
    out, err = sOut.communicate()
    num = int(out)
    print num
    r.send(str(num) + '\n')
    print r.recv(1024)
print r.recv(1024)
```
guessgen.c:

```c
#include<stdlib.h>
#include<stdio.h>
#include<time.h>

int main ()
{
    int r;
    srand(time(NULL));
    r = rand();
    printf("%d\n",r);
}
```

At first this code wouldn't work, but the issue turned out to be that `rand()` produces different values on different operating systems. Running the code above on Ubuntu successfully produced the flag.

### Flag ###
  tjctf{n0t_so_r4ndom_4nymor3}
