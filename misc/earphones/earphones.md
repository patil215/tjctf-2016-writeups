# Earphones - 155 (Misc)
#### Writeup by Ehsan Asdar

## Problem ##
and a chorus of [cicadas](https://raw.githubusercontent.com/TJCSec/tjctf-1516-released/master/earphones/cicadas.txt)

## Answer ##

### Overview ###
Use [Z3](https://github.com/Z3Prover/z3) to solve for the numerical value of each letter.

### Details ###
Opening the text file, we see a series of equations in which various letters have taken the place of digits. At the top of the file is the encrypted flag:

```
gWT ZYv za gdT WbR ZSX ZZa Ghr WWZ gbQ ZZJ te GRd Nz dnF gjI gdk gYH GYz dAA GrQ ZZb ZnC GWT Ggg Zbv dnc Wqq qrh WqO gbU grQ aa gVP dGT GnL gZE gQl dbE qZH ay Wqj dqM tu gZB gdo gds zi WYq qji Gbm qjt GWx qZF Zrn dnT gQN qQa gVd WGu qGX dgq ZQQ ZVf ZZs ZVt ty aK aO gQz dbO gYo ZWJ qgi dQD gjZ qVs dGY drd qWh dhd qrQ GGF zD Wnv qbW qhA gRZ dby ZqW gQB dGg Gqj WZl ZYY dGK WQo WAW ZQU qYL Zde ZWn zm dVL dVE Zgo ZSP
```

In order to decipher the flag, we are going to have to figure out what digit corresponds to each letter in the flag text. For this task I turned to the good old [Z3](https://github.com/Z3Prover/z3).

#### A Brief introduction to Z3 ####
"What is Z3?" you might ask. Z3 is a high performance and easy to use [SMT](https://en.wikipedia.org/wiki/Satisfiability_modulo_theories) solver. Simply put, if you give it a number of variables and some constraints, it attempts to find values of the variables that fit within the constraints. It's a lot like the algebraic system of equations solver on modern calculators, but much much more powerful.

#### Implementing Z3 ####
For ease of use, I used the Python bindings of Z3. This allows us to programmatically generate "rules" to feed into Z3, which will become very useful in a moment. Let's get started! The first thing to tell Z3 our variables, which in this case are a-z and A-Z. Then, we want to tell Z3 that each variable is between 0 and 9 (since each letter represents a single digit). The following lines do that:

```python
from z3 import *
s = Solver()

d = BitVecs('a b c d e f g h i j k l m n o p q r s t u v w x y z A B C D E F G H I J K L M N O P Q R S T U V W X Y Z',32)

digit_constraints = [ And(0 <= d[i], d[i] <= 9)  for i in range(0,len(d)) ]
s.add(digit_constraints)

```

The array `d` now contains references to all our variables. The only thing left to do is convert the rest of the math expressions into a form Z3 can read. This can be split into two steps.

1. Convert numbers into a Z3 understandable form (composed of digits represented by letters)
2. Make sure Z3 understands that order of operations does not apply.

Z3 won't inherently recognize multiple letters in a row as digits that form larger numbers. Instead, Z3 only recognizes each discrete letter as a number between 0 and 9. To solve this I parsed every "number" and rewrote it using multiplication by powers of 10 to preserve place. For example, the number `abc` would become `a*10^2 + b*10^1 + c*10^0`. The following python code took care of converting each number to the form I just described.
```python
def convertNum(num):
    out = ""
    for j in range(0,len(num)):
        for i in range(0,len(ma)):
            if j + 1 == len(num):
                if ma[i] == num[j]:
                    out += 'd[' + str(i) + '] * 10**' + str(len(num) - j -1)
            else:
                if ma[i] == num[j].strip('\r\n'):
                    out += 'd[' + str(i) + '] * 10**' + str(len(num) - j - 1) + ' + '
```

To make Z3 ignore order of operations I wrapped the expressions in layers of parentheses. This python code took care of doing that (there were exactly the same number of operations performed for each example):
```python
def gen_test(line):
    split = line.split(' ')
    for i in range(0,len(split),2):
        split[i] = convertNum(split[i].strip('\r\n'))
    split[0] = '('*10 + split[0] + ')'
    for i in range(2,len(split)-2,2):
        split[i] = split[i] + ')'
    split[len(split)-2] = '=='
    return ' '.join(split)
```

Putting this all together, Z3 works its magic and we get the following output
```
sat
[r = 2,
 x = 3,
 W = 1,
 c = 5,
 v = 6,
 Z = 1,
 L = 5,
 l = 3,
 a = 9,
 Q = 0,
 h = 2,
 k = 4,
 y = 7,
 p = 6,
 T = 6,
 R = 2,
 I = 8,
 A = 2,
 g = 1,
 s = 3,
 i = 7,
 U = 4,
 V = 0,
 n = 0,
 d = 1,
 N = 9,
 C = 9,
 F = 3,
 O = 7,
 B = 6,
 m = 8,
 b = 0,
 X = 3,
 j = 0,
 u = 7,
 J = 8,
 G = 1,
 t = 9,
 K = 8,
 H = 4,
 S = 2,
 z = 9,
 M = 4,
 E = 6,
 Y = 0,
 q = 1,
 f = 5,
 D = 7,
 P = 5,
 o = 5,
 e = 8,
 w = 4]
```

I wrote another small script that used that data to decode the flag.

[Final Solve Script](earphonesolve.py)

### Flag ###
    tjctf{wzodvbycglrhmzxnmtojiozuhxcititgjranratsqaeklmqqxjmmeuqodiqmabamkivukegnypyxqajezykojonqdviyhivnbijs}
