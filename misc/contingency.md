## Problem ##
Control wants some information from the irrelevant list, but you're locked out. Can you figure out Research's secret encoding scheme? nc p.tjctf.org 8002

## Answer ##

### Overview ###
Do a bit of searching to find out what the script really is - a reference to "The Machine" from the TV show *Person of Interest*. Use knowledge from the show, as well as the encoding specified by the machine, to encode an override message saying to leak the flag.

### Details ###

Starting up the machine, we're greeted with the following message:

```
 - - - - - SYSTEM STARTUP - - - - -
START_KERNEL
INIT
RUNLEVEL

LAUNCHING...
CORE ANALYTICS
NEURAL NETWORKS
HEURISTIC ENGINES
RECURSION PROCESSORS
EVOLUTIONARY GENERATORS
BAYESIAN NETWORKS
DATA ACQUISITION
CRYPTOGRAPHIC ALGORITHMS
DOCUMENT PROCESSORS
...
...

NORTHERNLIGHTS LOGIN: 
```

We don't know any of the necessary information to log in. However, this sequence seems oddly specific. Let's see if we can login info through some searching. Doing a Google Search for "CORE ANALYTICS NEURAL NETWORKS HEURISTIC ENGINES NORTHERN LIGHTS" (a few of the phrases from the boot sequence as well as the phrase northern lights) pulls up this [Wiki page](http://personofinterest.wikia.com/wiki/The_Machine) which seems to match quite well. It seems that this program is a copy of The Machine from this TV show *Person of Interest*!

Let's see if we can find some login info from this Wiki page. At the top, it says the machine was build by "Harold Finch" and "Nathan Ingram". Logging in with their names doesn't work, but if we type their last name - "finch" we get "REMOTE ACCESS DISABLED AT USER REQUEST" and if we type in "ingram" we get USER ALIAS FOUND INGRAM!

So now we're in the machine. Let's see what it can do. Typing `help` gives the following:

```
HELP: SHOW HELP ON SYSTEM COMMANDS
SHOW: SHOW RUNNING SYSTEM PROPERTIES
    SHOW CONFIG: DISPLAY SYSTEM CONFIGURATION
    SHOW USERS: DISPLAY AVAILABLE USERS ON SYSTEM
    SHOW PROCESSES: DISPLAY RUNNING PROCESSES
    SHOW PRIVILEGES: CHECK YOUR PRIVILEGE
    SHOW LIST: OUTPUT A LIST OF NUMBERS
        SHOW LIST RELEVANT: DISPLAY NUMBERS RELEVANT TO NATIONAL SECURITY
        SHOW LIST IRRELEVANT: DISPLAY NUMBERS IRRELEVANT TO NATIONAL SECURITY
    SHOW LIBRARY: OUTPUT A LIST OF BOOKS
OVERRIDE: ENTER ENCODED COMMAND, BYPASSING ALL PERMISSIONS
```

Hm. There's a relevant and irrelevant list - the problem statement says we want something from the irrelevant list. However, trying `SHOW LIST IRRELEVANT` gives 

```
PERMISSIONS RESTRICTED
EXECUTE "SHOW PRIVILEGES" COMMAND FOR DETAILS
```

Executing `SHOW PRIVILEGES` gives

```
SYSTEM PRIVILEGES
    LOGGED IN AS NCINGRAM [NATHAN INGRAM]
    USER ROLE AUX_ADMIN *
    * PERMISSION RESTRICTED: DISALLOW SHOW LIST IRRELEVANT
    * RESTRICTION ADDED SEP 13 2010 BY USER HFINCH ROLE ADMIN [HAROLD FINCH]
```

Seems like we've been blocked from `SHOW LIST IRRELEVANT` by `finch`. Seems like the `OVERRIDE` command is probably useful for getting around this. `OVERRIDE` gives:

```
ENTER ENCODED MESSAGE TERMINATED BY EMPTY LINE

```

Seems like there's a special encoding to be able to override the message - which matches up with what the problem statement told us. Let's see if we can try to figure out what that encoding is. Perhaps it's given in the system config? Doing `SHOW CONFIG` gives:

```
SYSTEM CONFIGURATION
    HOSTNAME: NORTHERNLIGHTS
    REMOTE ACCESS NODE: IFT-00345867
    SYSTEM ENCODING: EBCDIC
```

[EBCDIC](https://en.wikipedia.org/wiki/EBCDIC) is a special type of encoding. Maybe we can just convert `SHOW LIST RELEVANT` to EBCDIC and paste that in as the encoded message for the override command.

Nope, converting the message to EBCDIC doesn't work - it returns `PERMISSIONS UNRESTRICTED OR UNKNOWN COMMAND`.

Seems like there's more to this encoding. Let's look at the other commands available to us:

```
HELP: SHOW HELP ON SYSTEM COMMANDS
SHOW: SHOW RUNNING SYSTEM PROPERTIES
    SHOW CONFIG: DISPLAY SYSTEM CONFIGURATION
    SHOW USERS: DISPLAY AVAILABLE USERS ON SYSTEM
    SHOW PROCESSES: DISPLAY RUNNING PROCESSES
    SHOW PRIVILEGES: CHECK YOUR PRIVILEGE
    SHOW LIST: OUTPUT A LIST OF NUMBERS
        SHOW LIST RELEVANT: DISPLAY NUMBERS RELEVANT TO NATIONAL SECURITY
        SHOW LIST IRRELEVANT: DISPLAY NUMBERS IRRELEVANT TO NATIONAL SECURITY
    SHOW LIBRARY: OUTPUT A LIST OF BOOKS
OVERRIDE: ENTER ENCODED COMMAND, BYPASSING ALL PERMISSIONS
```

Hm. `SHOW LIST RELEVANT` gives a very strange message:

```
MESSAGE OF LENGTH 21 FOLLOWS
427.09 7 ... 338.7/6663941443 6 ... 519.2 1 ...
338.7/6663941443 6 ... 322.4/2/0941 3 ... 623.74/75/0973 3 ...
273/.4 6 ... 823/.9/1 0 ... 759.06 10 ...
631.3/0942 1 ... 283 4 ... 346.4203 10 ...
...
...
...
823/.8 3 ... 378.01 6 ... 508.744/92 10 ...
516 0 ... 375/.0086 8 ... 297.4/8 6 ...
307.70941 5 ... 327/.2/0924 0 ... 004 0 ...
```

This is very strange. `SHOW LIBRARY` is equally strange:

```
636/.07           :: 9780753458389
770/.28           :: 0904069494
591.75/3          :: 9780753458396
082               :: 0836911016
635/.0484         :: 0028623150
704.9/2           :: 9780824024802
423/.12           :: 9780753461174
823/.8            :: 019281687X
346.4203          :: 9781405873628
759.06            :: 9780896598119
823/.9/1          :: 0856860522
940.53/162/0924   :: 0701204559
358/.18/08 s      :: 0853830029
[Fic]             :: 0525454039
307.70941         :: 0745011985
338.7/6663941443  :: 9780902752306
631.3/0942        :: 0238788202
...
...
...
378.01            :: 0665871872
330.973           :: 083691645X
709/.2            :: 9780907849971
```

Let's see if the Wiki has more information.

There's something interesting [here](http://personofinterest.wikia.com/wiki/The_Machine#The_.22Irrelevant.22_List). Apparently, the encoding is based on the Dewey Decimal System! That would explain the fact that there's a library. Let's see if we can decipher how this works.

Looking at the full contents of `SHOW LIST RELEVANT` and `SHOW LIBRARY`, we can see that the relevant list is composed of numbers under the Dewey Decimal system followed by a single digit number, for example `273/.4 6`. Interestingly, the Dewey Decimal numbers correspond to lines in the library! Perhaps the second number is an index to look up within each book in the library.

For example, one of the items in the relevant list is `631.3/0942 1`. This corresponds with a book `631.3/0942        :: 0238788202` from the library. The `1` (second number) is probably an index within the book, so it's probably `2` (from the second number of the book). Each line of the relevant list has two or three items on it - maybe the numbers we get will correspond to EBCDIC characters, and if we do this for every item in the relevant list, we'll get some sort of message. 

Looking up every item in the relevant list, getting the numbers, and converting to EBCDIC, we get `THIS IS NOT THE FLAG`. It isn't the flag, but we're close - it means we're using the correct encoding! We just need to encode `SHOW LIST IRRELEVANT` as in the same way.

We convert `SHOW LIST IRRELEVANT` to EBCDIC, then put it in terms of things that can be looked up in the dictionary. For example, if we want to make the letter S, we could do this:

```
S = 226 in EBCDIC
2 can be referenced as 823/.8 3 (because it's the character at index 3 in the corresponding book in the library, which is "823/.9/1          :: 0856860522")
6 can be referenced as 635/.0484 4

So S can be written as "823/.8 3 ... 823/.8 3 ... 635/.0484 4 ..."
```

We do that for the entire message. For example, one way of doing that would be:

```
375/.0086 1 ... 375/.0086 1 ... 828/.7/09 3 ...
375/.0086 1 ... 375/.0086 0 ... 375/.0086 0 ...
375/.0086 1 ... 516 0 ... 704.9/2 6 ...
375/.0086 1 ... 301 1 ... 375/.0086 0 ...
828/.7/09 3 ... 704.9/2 6 ...
375/.0086 1 ... 516 0 ... 516 0 ...
375/.0086 1 ... 375/.0086 0 ... 516 0 ...
375/.0086 1 ... 375/.0086 1 ... 828/.7/09 3 ...
375/.0086 1 ... 375/.0086 1 ... 519.2 1 ...
828/.7/09 3 ... 704.9/2 6 ...
375/.0086 1 ... 375/.0086 0 ... 516 0 ...
375/.0086 1 ... 516 0 ... 519.2 1 ...
375/.0086 1 ... 516 0 ... 519.2 1 ...
516 0 ... 519.2 0 ... 519.2 1 ...
375/.0086 1 ... 516 0 ... 516 0 ...
516 0 ... 519.2 0 ... 519.2 1 ...
375/.0086 1 ... 375/.0086 1 ... 519.2 0 ...
516 0 ... 519.2 0 ... 301 1 ...
375/.0086 1 ... 516 0 ... 301 1 ...
375/.0086 1 ... 375/.0086 1 ... 519.2 1 ...
```

We paste that in when running `OVERRIDE`. It works! We get a message of length 29 back!
```
MESSAGE OF LENGTH 29 FOLLOWS
330.973 5 ... 375/.0086 6 ... 006.76 3 ...
813/.54 5 ... 410 12 ... 327/.2/0924 9 ...
516 0 ... 978.3/62 6 ... 909 3 ...
516 0 ... 746.46092 9 ... 978.3/62 6 ...
297.4/8 4 ... 358/.18/08 s 3 ... 273/.4 7 ...
330.973 5 ... 327/.2/0924 0 ... 283 4 ...
297.4/8 4 ... 909 6 ... 338.7/66639414/43 6 ...
301.41/2/0942 6 ... 330.1 D 5 ... 327/.2/0924 5 ...
746.46092 0 ... 252/.03 12 ... 301.41/2/0942 1 ...
170/.202 2 ... 178.1 9 ... 823/.9/1F 9 ...
301.41/2/0942 6 ... 301.41/2/0942 9 ... 070.5/79 0 ...
082 5 ... 307.70941 2 ... 330.1 D 1 ...
929.4 1 ... 252/.03 12 ... 070.5/79 4 ...
283 1 ... 623.74/75/0973 3 ... 178.1 5 ...
378.01 6 ... 004 10 ... 423.1 10 ...
823/.8 1 ... 940.53/162/0924 7 ... 720/.92/4 3 ...
252/.03 9 ... 631.3/0942 0 ... 297.4/8 0 ...
252/.03 9 ... 823/.9/1 3 ... 621.381/076 5 ...
346.4203 3 ... 978.3/62 6 ... 516 4 ...
031.02 9 ... 909 12 ... 635/.0484 6 ...
283 1 ... 338.7/6663941443 3 ... 297.4/8 0 ...
942.2/792 3 ... 508 5 ... 709/.2 2 ...
301 3 ... 307.70941 2 ... 704.9/2 6 ...
252/.03 9 ... 420/.8 2 ... 508.744/92 10 ...
082 5 ... 929.4 2 ... 823.91 2 ...
929.4 1 ... 330.1 D 5 ... 508.744/92 10 ...
842/.7 6 ... 330.1 D 5 ... 973/.04975 0 ...
631.3/0942 1 ... 346.4203 4 ... 423.1 10 ...
823/.9/1 8 ... 273/.4 3 ... 338.7/6663941443 2 ...
```

Converting that back to EBCDIC using the method above, we get:
```
163
145
131
163
134
192
166
243
147
131
240
148
243
109
163
150
109
163
136
243
109
148
244
131
136
241
149
243
208
```

And finally, converting that to ASCII, we get our flag!

### Flag ###
tjctf{w3lc0m3_to_th3_m4ch1n3}