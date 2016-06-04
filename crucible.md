### Problem ###
[crucible.py](crucible.py) [matrix.py](matrix.py) [encrypted.txt](encrypted.txt)

## Answer ##

### Overview ###
Use knowledge of linear algebra to invert the output of the program.

### Details ###

Let's first take a look at `encrypted.txt`. Looking at the file, we can see that it seems to be a list of very long (precise) Decimal objects:

```
[Decimal('2.5372...'), Decimal('10.1102...'), ...]
```

Seems like the output of the program is an encrypted "list" of numbers.

Now let's take a look at the program `crucible.py`. It seems like the program does a few things:

1. Creates a 2D array variable `matrix` and does some operations on it
2. Creates an array variable `curr` that is the ASCII values (`ord`) of the flag and uses `matrix` to do operations to it.
3. Writes the output (the final value of `curr`) to a file.

We also notice that the program also references a file `matrix.py`. Looking at it, we see it includes the function `invert` which references 'gauss-jordan elimination'. This and the rest of the file (with functions like `swap_row` and `dot` and `mult`), along with the name of the file (`matrix.py`) tells us we're dealing with Linear Algebra here.

#### Understanding `matrix` ####

Now back to `crucible.py`. We see that the program is creating `matrix`, a 2D array or a "grid" of numbers with dimensions of the length of the flag. Then it assigns some values within `matrix`. Let's examine the loop further and see exactly what it does:

``` python
for i in range(n):
    if i == 0:
        matrix[i] = [2 + 2 * r, -r] + [Decimal(0)] * (n - 2)
    elif i == n - 1:
        matrix[i] = [Decimal(0)] * (n - 2) + [-r, 2 + 2 * r]
    else:
        matrix[i] = [Decimal(0)] * (i - 1) + [-r, 2 + 2 * r, -r] + [Decimal(0)] * (n - i - 2)
```

Studying this carefully with show that it's essentially creating a matrix with [3, -.5] in the top left corner, then [-.5, 3, -.5] across the diagonal, then [-.5, 3] in the bottom corner. For example, here's what the matrix looks like if flag length is 5:

```
| 3  -.5   0   0    0 |
| -.5  3  -.5  0    0 |
| 0  -.5   3  -.5   0 |
| 0   0   -.5  3   -.5|
| 0   0   0   -.5  3  |
```

(Okay, I probably should have used LaTeX for this.)

The program then takes matrix and inverts it (using `invert` from `matrix.py`), assigning it to variable `inv`.

#### Understanding `curr` ####

Let's look at the last code block:

``` python
for step in range(100):
    temp = [curr[0]] + [Decimal(2) * curr[x + 1] + r * (curr[x] - Decimal(2) * curr[x + 1] + curr[x + 2]) for x in range(n - 2)] + [curr[n - 1]]
    curr = mult(inv, temp)
```

The key here is understanding what `temp` is and what the `mult` function does.

The syntax for constructing `temp` is a little confusing, but after some deciphering we can see that it's an array, where its first value is curr[0] (`[[curr[0]]`), the last value is cur[n - 1], and the second to penultimate values are 2 * curr[x+1] + 0.5 * (curr[x] - 2 * curr[x + 1] + curr[x + 2]). For simplicity purposes, I will write this array out as a vertical vector, using a, b, c, d, and e to represent each element in curr, assuming a length of 5. For that, `temp` would look like this:

```
| a                    |
| 2b + 0.5(a - 2b + c) |
| 2c + 0.5(b - 2c + d) |
| 2d + 0.5(c - 2d + e) |
| e                    |
```

`mult` is a function from `matrix.py` - looking at it, we can see that it computes the dot product (using the `dot` function) between each column in `matrix` and `vector`. This is also known as matrix-vector multiplication in linear algebra.

So, to summarize, `curr` is repeatedly reassigned 100 times to a new array (`temp`) generated from the previous value of `curr`, and `inv`, the inverse of `matrix`. The program then prints `curr`.

So, know we know what the program does. The question becomes, how do we reverse it?

#### Reversing `mult` ####

This is where a little linear algebra knowledge comes in handy. What we need to be able to do is reverse the process of the multiplication of `inv` and `temp` to get `curr` (`mult`). This also means we have to be able to reverse the process that creates `temp`.

A nifty thing about matrix-vector multiplication (the `mult` function) is that it is invertible. To go backwards, we simply multiply by the inverse of the matrix! Since the program already calculates the inverse of matrix before multiplying, the inverse is just the original matrix. So that's how we reverse `mult`.

#### Reversing `temp` ####

To reverse the process that creates `temp`, we can use a similar idea. Recall that the process that creates `temp` for a 5x5 matrix given `curr`=[a,b,c,d,e] is:

```
| a                    |
| 2b + 0.5(a - 2b + c) |
| 2c + 0.5(b - 2c + d) |
| 2d + 0.5(c - 2d + e) |
| e                    |
```


Simplifying:

```
| a               |
| 0.5a + b + 0.5c |
| 0.5b + c + 0.5d |
| 0.5c + d + 0.5e |
| e               |
```


Again, more linear algebra - since `curr` is [a, b, c, d, e], we can rewrite this as a linear transformation - the product of `curr` and a matrix! That looks like this:

```
| 1    0    0    0    0   |  | a |
| 0.5  1    0.5  0    0   |  | b |
| 0    0.5  1    0.5  0   |  | c |
| 0    0    0.5  1    0.5 |  | d |
| 0    0    0    0    1   |  | e |
```


Since this is a matrix-vector product, we can also reverse this by multiplying by the inverse of the matrix. The only caveat is that we have to extend this to matrices creater than 5x5 - however, one can see that there's a clear pattern in that matrix, so it's just a matter of writing code to generate it with the pattern:

``` python
def generatematrix(n):
    matrix = [[0 for x in range(n)] for y in range(n)]
    for i in range(n):
        if i == 0:
            matrix[i] = [2 + 2 * r, -r] + [Decimal(0)] * (n - 2)
        elif i == n - 1:
            matrix[i] = [Decimal(0)] * (n - 2) + [-r, 2 + 2 * r]
        else:
            matrix[i] = [Decimal(0)] * (i - 1) + [-r, 2 + 2 * r, -r] + [Decimal(0)] * (n - i - 2)
    return matrix
```

We can then multiply the inverse of the matrix generated by that function by `temp` to get the original value for `cur`. We can do this 100 times (multiplying by `matrix`) to get the original flag. See the code below. (Note that `generatematrix` is the same as the function in `crucible.py`, since that's the inverse matrix we want to multiply by).

#### Final code ####

``` python
from decimal import *
from matrix import *

getcontext().prec = 500

r = Decimal(0.5)

def generatematrix(n):
    matrix = [[0 for x in range(n)] for y in range(n)]
    for i in range(n):
        if i == 0:
            matrix[i] = [2 + 2 * r, -r] + [Decimal(0)] * (n - 2)
        elif i == n - 1:
            matrix[i] = [Decimal(0)] * (n - 2) + [-r, 2 + 2 * r]
        else:
            matrix[i] = [Decimal(0)] * (i - 1) + [-r, 2 + 2 * r, -r] + [Decimal(0)] * (n - i - 2)
    return matrix

def getreversermatrix(n):
    origmatrix = []
    origmatrix.append([2 * r] + ([Decimal(0)] * (n-1)))
    for i in range(1, n - 1):
        dibber = [Decimal(0)] * n
        dibber[i] = 2 * r
        dibber[i - 1] = r
        dibber[i + 1] = r
        origmatrix.append(dibber)
    origmatrix.append([Decimal(0)] * (n-1) + [2 * r])
    reversermatrix = invert(origmatrix)
    return reversermatrix

file = open("encrypted_b781aa7039a27df2ec6c7babd263f32841141dbcff2645066c22128f28ceea11.txt", "r")

nums = eval(file.readline())
for i in range(0, len(nums)):
    nums[i] = Decimal(nums[i])
print str(nums)
matrix = generatematrix(len(nums))
reversermatrix = getreversermatrix(len(nums))

for step in range(100):
    temporig = mult(matrix, nums)
    nums = mult(reversermatrix, temporig)

flag = ""
for i in nums:
    flag += chr(int(round(i)))
print flag
```