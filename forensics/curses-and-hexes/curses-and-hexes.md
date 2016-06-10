# Curses and Hex-es - 65 (Forensics)
#### Writeup by GenericNickname

## Problem ##
Wow! I think I just magicked a flag into <a href="curses_and_hexes.png">this image</a>.

## Answer ##

### Overview ###

Make characters using the RGB values of the image to recover the flag.

### Details ###

The name of the problem and the fact that Hex-es has Hex isolated implies that the hex values are important. A really simple way to encode data using the hex values of an image would be to make each value (R, G, or B) correspond to a character, enabling each pixel to contain three characters.

The PIL library can be used to decode the image under this assumption using the following code:

```
from PIL import Image

i = Image.open('curses_and_hexes.png')
dat = i.load()
res = ''
for y in range(0, i.height) :
    for x in range(0, i.width) :
            res += chr(dat[x,y][0]) + chr(dat[x,y][1]) + chr(dat[x,y][2])
o = open('curses_and_hexes-out.txt', 'wb')
o.write(res)
o.close()
```

Opening this output file will reveal a wall of readable text, and the flag can be found by searching it for "tjctf{"

### Flag ###
  tjctf{y0u'r3_a_w1z4rd_H4rry!!}
