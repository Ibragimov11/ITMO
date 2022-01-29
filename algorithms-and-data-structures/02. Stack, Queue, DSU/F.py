n = int(input())
bl = True
a = [int(x) for x in input().split()]
for i in range(n):
    if bl:
        c = []
        for j in range(i):
            if a[j] > a[i]:
                c.append(a[j])
        if len(c) > 1:
            for j in range(len(c) - 1):
                if c[j] < c[j + 1]:
                    bl = False
                    print("impossible")
                    break
if bl:
    b = []
    for i in range(n):
        b.append(a[i])
        print("push")
        while b[len(b) - 1] == min(a):
            print("pop")
            b.pop()
            a[a.index(min(a))] = 1e15
            if len(b) == 0:
                break
