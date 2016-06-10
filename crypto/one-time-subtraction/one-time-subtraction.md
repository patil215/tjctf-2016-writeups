# One Time Subtraction - 20 (Cryptography)
#### Writeup by Ashwin Gupta

## Problem ##
241 231 224 241 227 248 173 235 176 220 223 246 241 176 220 174 240 220 235 173 241 220 176 235 173 242 228 229 250 135

## Answer ##

### Overview ###
Brute force the one byte long key.

### Details ###
The problem description tells you that the key is one byte long, which means that there are only 255 possible combinations. I wrote a small script that adds the possible key to each number (modding by 256 to keep the numbers in range) and then converted each letter to ascii. After printing out every combination, I looked through the list and found the flag.

Here is the script:
```python
stringToXOR = "241 231 224 241 227 248 173 235 176 220 223 246 241 176 220 174 240 220 235 173 241 220 176 235 173 242 228 229 250 135"
thing = stringToXOR.split(" ")
print thing

for i in range (0, 255):
    string = ""
    for x in thing:
        string = string + chr((int(x) + i) % 256)
    print string
```

###Flag###
	tjctf{0n3_byt3_1s_n0t_3n0ugh}