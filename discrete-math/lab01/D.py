n = int(input())
k = 2**n
c = []
d = []
for i in range(k):
    c.append([0]*(n + 1))
for i in range(k):
    s, x = map(str, input().split())
    c[i][n] = int(x)
    b = list(s)
    for j in range(len(b)):
        c[i][j] = int(b[j])
t = 0
w = n
for i in range(k):
    t += c[i][n]
if t == 0:
    print(n + 2)
    print(1, 1)
    print(2, 1, n + 1)
else:
    print(t - 1 + t*(n-1) + 2 * n)
    for i in range(n):
        print(1, i+1)
        w += 1
    for i in range(k):
        if c[i][n] == 1:
            for j in range(n-1):
                if j == 0:
                    z1 = j + 1
                    z2 = j + 2
                    if c[i][j] == 0:
                        z1 += n
                    if c[i][j + 1] == 0:
                        z2 += n
                    print(2, z1, z2)
                    w += 1
                else:
                    z2 = j + 2
                    if c[i][j + 1] == 0:
                        z2 += n
                    print(2, w, z2)
                    w += 1
            d.append(w)
    if len(d) == 0:
        print(0)
    for i in range(len(d) - 1):
        if i == 0:
            print(3, d[0], d[1])
            w += 1
        else:
            print(3, w, d[i + 1])
            w += 1
