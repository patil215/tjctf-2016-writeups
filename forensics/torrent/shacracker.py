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
