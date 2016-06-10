# Basic Images - 90 (Misc)
#### Writeup by GenericNickname

## Problem ##
Check out my <a href="shades.png">shades</a>! If you want the flag, I guess you should be basic like me. Good luck!

## Answer ##

### Overview ###

Continue decrypting base64 images until you reach text.

### Details ###

Both the title and "I guess you should be basic like me" should hint towards that the solution has something to do with bases, and the name of the file is shades, so we should take a look at the shades of the colors first.

This can be done in python using the PIL imaging library:

```
from PIL import Image

i = Image.open('shades.png')
dat = i.load()
res = ''
for y in range(0, i.height) :
    for x in range(0, i.width) :
            res += chr(dat[x,y][0])
o = open('shades-out.txt', 'wb')
o.write(res)
o.close()
```

After inspecting the output file, it should be noticed that the text all seems to fit into what something encoded with Base64 would look like. Some testing will show that the Base64 decodes to a PNG header, so we need to save the image.

This python will decode the Base64 and output the new image file:
```
from PIL import Image
import base64

i = Image.open('shades.png')
dat = i.load()
res = ''
for y in range(0, i.height) :
    for x in range(0, i.width) :
            res += chr(dat[x,y][0])
o = open('shades1.png', 'wb')
o.write(base64.b64decode(res))
o.close()
```

It will then be realized that the new image looks almost exactly like the old image, just smaller. The flag can be obtained by repeating the process above and getting progressively smaller images, until you eventually get some plaintext output, which happens to be the flag.

### Flag ###
  tjctf{asc11_c0l0r_inc3pt1on}
