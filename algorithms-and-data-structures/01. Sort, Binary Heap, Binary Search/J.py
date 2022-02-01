from math import sqrt
def check(x):
    sf = sqrt(a**2 + (1 - x)**2)
    sp = sqrt((1 - a)**2 + x**2)
    t = sp/vp + sf/vf
    return t

vp, vf = map(int, input().split())
a = float(input())
l = 0
r = 1
while (r - l > 1e-4):
    m1 = (2*l + r) / 3
    m2 = (2*r + l) / 3
    if check(m1) > check(m2):
        l = m1
    else:
        r = m2
print(r)