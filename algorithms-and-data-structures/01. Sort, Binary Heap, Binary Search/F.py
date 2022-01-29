n, k = map(int, input().split())
a = [int(s) for s in input().split()]
b = [int(s) for s in input().split()]
for x in b:
    l = q = 0
    r = n - 1
    while r - l > 1:
        if a[l] == x:
            print(x)
            q = 1
            break
        if a[r] == x:
            print(x)
            q = 1
            break
        m = (r + l) // 2
        if a[m] == x:
            print(x)
            q = 1
            break
        if a[m] < x:
            l = m
        else:
            r = m
    if q == 0:
        if abs(x - a[l]) <= abs(x - a[r]):
            print(a[l])
        else:
            print(a[r])
