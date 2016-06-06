# Earphones - 155 (Misc)
#### Writeup by Ehsan Asdar

## Problem ##
and a chorus of [cicadas](https://raw.githubusercontent.com/TJCSec/tjctf-1516-released/master/earphones/cicadas.txt)

## Answer ##

### Overview ###
Use the [Z3](https://github.com/Z3Prover/z3) to solve for each letter.

### Details ###
Opening the text file, we see a series of equations in which various letters have taken the place of digits. At the top of the file is the encrypted flag:

```gWT ZYv za gdT WbR ZSX ZZa Ghr WWZ gbQ ZZJ te GRd Nz dnF gjI gdk gYH GYz dAA GrQ ZZb ZnC GWT Ggg Zbv dnc Wqq qrh WqO gbU grQ aa gVP dGT GnL gZE gQl dbE qZH ay Wqj dqM tu gZB gdo gds zi WYq qji Gbm qjt GWx qZF Zrn dnT gQN qQa gVd WGu qGX dgq ZQQ ZVf ZZs ZVt ty aK aO gQz dbO gYo ZWJ qgi dQD gjZ qVs dGY drd qWh dhd qrQ GGF zD Wnv qbW qhA gRZ dby ZqW gQB dGg Gqj WZl ZYY dGK WQo WAW ZQU qYL Zde ZWn zm dVL dVE Zgo ZSP
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

### Flag ###
    tjctf{wzodvbycglrhmzxnmtojiozuhxcititgjranratsqaeklmqqxjmmeuqodiqmabamkivukegnypyxqajezykojonqdviyhivnbijs}
