from decimal import *
getcontext().prec = 200
n = int(input())
dictionary = ["a", "b", "c", "d", "e", "f", "g", "h",
              "i", "j", "k", "l", "m", "n", "o", "p", "q",
              "r", "s", "t", "u", "v", "w", "x", "y", "z"]
dictionary = dictionary[:n]
counts = [int(x) for x in input().split()]
s = str(input())
lg = sum(counts)
ver = [0] * n
for i in range(n):
    ver[i] = Decimal(counts[i]) / Decimal(lg)
    if i > 0:
        ver[i] += ver[i - 1]
ver.insert(0, 0)
q = len(s)
t = 2**q
p = 0
s = s[::-1]
for i in range(q):
    p += int(s[i]) * 2**i
k = Decimal(p) / Decimal(t)
z = ""
for i in range(lg):
    j = 0
    while ver[j] <= k:
        j += 1
    z += dictionary[j - 1]
    k = Decimal(k - ver[j - 1]) / Decimal(ver[j] - ver[j - 1])
print(z)
