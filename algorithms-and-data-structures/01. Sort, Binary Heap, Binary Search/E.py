n = int(input())
a = [int(s) for s in input().split()]

def mn(x):
    global a
    l = -1
    r = len(a)
    while l < r - 1:
        m = (l + r) // 2
        if x > a[m]:
            l = m
        else:
            r = m
    return l

def mx(x):
    global a
    l = -1
    r = len(a)
    while l < r - 1:
        m = (l + r) // 2
        if x < a[m]:
            r = m
        else:
            l = m
    return r

a.sort()
k = int(input())
for t in range(k):
    l, r = map(int, input().split())
    ll = mn(l)
    rr = mx(r)
    print(rr - ll - 1, end=" ")
