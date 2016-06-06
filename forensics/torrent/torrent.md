# Torrent - 90 (Forensics)
#### Writeup by Ashwin Gupta

## Problem ##
Help, someone's sharing [flags](flag.torrent). 

## Answer ##

### Overview ###
Brute force the 14 Sha1 hashes inside the torrent file and put them together to get the flag.

### Details ###

After taking a look at flag.torrent and reading about the structure of .torrent files, we see that this is a torrent for a single file named flag. The flag has 28 characters. Additionally, the file has 280 pieces, and a piece length of 2. "Pieces" are a concatenation of Sha1 hashes of each part and are of length 20. This means that there are 14 total parts that need to be put together to get the flag. Since each of the 14 parts is 2 long, we can brute force every combination of 2 characters and check that against the Sha1 hash we have. If the Sha1 hash matches our calculated hash, we can add those two letters to the flag.

Here is my script that does exactly that:

``` python
import hashlib
import itertools
import string

f = open("flagstuff2.txt", 'rb')

def bruteforce(charset, maxlength):
    return (''.join(candidate)
        for candidate in itertools.chain.from_iterable(itertools.product(charset, repeat=i)
        for i in range(1, maxlength + 1)))

flag = ""

for i in range(0, 14):
    shacheck = ""

    shacheck = f.read(20)

    for y in (bruteforce(string.ascii_letters+string.digits+"{}_", 2)):
        if hashlib.sha1(y).hexdigest() == shacheck.encode('hex'):
            flag = flag + y

print flag
```

### Flag ###
	tjctf{pls_2_n0t_fl4g_share}