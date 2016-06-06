from z3 import *
import string
bitvecstring = 'a b c d e f g h i j k l m n o p q r s t u v w x y z A B C D E F G H I J K L M N O P Q R S T U V W X Y Z'
ma = [i for i in bitvecstring.split(' ')]
d = BitVecs('a b c d e f g h i j k l m n o p q r s t u v w x y z A B C D E F G H I J K L M N O P Q R S T U V W X Y Z',32)
s = Solver()

digit_constraints = [ And(0 <= d[i], d[i] <= 9)  for i in range(0,len(d)) ]
s.add(digit_constraints)

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
    return out
def gen_test(line):
    split = line.split(' ')
    for i in range(0,len(split),2):
        split[i] = convertNum(split[i].strip('\r\n'))
    split[0] = '('*10 + split[0] + ')'
    for i in range(2,len(split)-2,2):
        split[i] = split[i] + ')'
    split[len(split)-2] = '=='
    return ' '.join(split)
rules = []
f = open('text.txt','r')
lines = f.readlines()
for line in lines:
    rules.append(And(eval(gen_test(line))))
s.add(rules)
s.add(And(d[0] > 0))
if s.check() == sat:
    print "sat"
    print s.model()
