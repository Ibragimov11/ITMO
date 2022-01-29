import math
from decimal import *
getcontext().prec = 200
n = int(input())
s = str(input())
dictionary = ["a", "b", "c", "d", "e", "f", "g", "h",
              "i", "j", "k", "l", "m", "n", "o", "p", "q",
              "r", "s", "t", "u", "v", "w", "x", "y", "z"]
dictionary = dictionary[:n]
counts = [0] * n
lg = len(s)
for i in range(lg):
    counts[dictionary.index(s[i])] += 1
ver = [0] * n
for i in range(n):
    ver[i] = Decimal(counts[i]) / Decimal(lg)
    if i > 0:
        ver[i] += ver[i - 1]
ver.insert(0, 0)
l = 0
r = 1
for _s_ in s:
    i = dictionary.index(_s_)
    l, r = Decimal(l) + Decimal((r - l)) * Decimal(ver[i]), Decimal(l) + Decimal((r - l)) * Decimal(ver[i + 1])
q = 0
p = 0
while True:
    if math.ceil(l * (2**q)) >= r * (2**q):
        q += 1
    else:
        p = math.ceil(l * (2**q))
        break
z = ""
while q > 0:
    z += (str(p % 2))
    p //= 2
    q -= 1
z = z[::-1]
print(n)
for x in counts:
    print(x, end=" ")
if z == "":
    z = "0"
print('\n' + z)
