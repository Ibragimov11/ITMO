from math import sqrt
c = float(input())
l = 0
r = c
t = 0
while r - l > 0.0000001:
    m = (r + l) / 2
    if m * m + sqrt(m) > c:
        r = m
    elif m * m + sqrt(m) == c:
        print(m)
        t = 1
        break
    else:
        l = m
if t != 1:
    print(l)
