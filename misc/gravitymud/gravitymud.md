# gravitymud - 90 (Misc)
#### Writeup by GenericNickname

## Problem ##
Wanna hear a joke? north, south, east, west, up, down, examine item<br/>nc p.tjctf.org 8006

## Answer ##

### Overview ###

Explore the map and inspect all the items you run across until you have gathered the entire flag.

### Details ###

gravitymud is simply a map that we can explore via netcat using the commands north, south, easy, west, up, down, and examine <item_to_examine>. The solution is to just explore the entire map, inspecting all the items that you come across.

For example, after inspecting the rug in the giftshop, this portion of the flag can be recovered: tjctf{y0u_m1ght_h@v3_ . There are also some items that upon being interacted with indicate what steps to take next, such as the paintedeye in the exhibithall which opens a stairway. The amount of paths is small enough that the entire thing can be explored "by hand" by using python socket commands.

The code below is the path that I took to find the last portion of the flag that I recovered.
```
#flag found so far:
#tjctf{y0u_m1ght_h@v3_b33n_m1$sing_t3h_fl4g_but_YOUR_A1M_IS_G3TT1NG_B3TT3R}
import socket
hostname = 'p.tjctf.org'
port = 8006
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.connect((hostname, port))
data = s.recv(1024)
print data
s.send('north\n')
data = s.recv(1024)
print data
s.send('west\n')
data = s.recv(1024)
print data
s.send('examine journal2\n')
data = s.recv(1024)
print data
s.send('east\n')
data = s.recv(1024)
print data
s.send('east\n')
data = s.recv(1024)
print data
s.send('east\n')
data = s.recv(1024)
print data
s.send('examine fireplace\n')
data = s.recv(1024)
print data
s.send('examine paintedeye\n')
data = s.recv(1024)
print data
s.send('down\n')
data = s.recv(1024)
print data
s.send('examine book\n')
data = s.recv(1024)
print data
s.send('up\n')
data = s.recv(1024)
print data
s.send('west\n')
data = s.recv(1024)
print data
s.send('west\n')
data = s.recv(1024)
print data
s.send('south\n')
data = s.recv(1024)
print data
s.send('examine tree\n')
data = s.recv(1024)
print data
s.close()
```


### Flag ###
  tjctf{y0u_m1ght_h@v3_b33n_m1$sing_t3h_fl4g_but_YOUR_A1M_IS_G3TT1NG_B3TT3R}
