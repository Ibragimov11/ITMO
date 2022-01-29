a = [1]*5
n = int(input())
for i in range(n):
    if sum(a) == 0:
        break
    s, x = map(str, input().split())
    b = list(x)
    s = int(s)
    k = 2**s
    for j in range(k):
        b[j] = int(b[j])

    if b[0] == 1:
        a[0] = 0

    if b[k - 1] == 0:
        a[1] = 0
    if k == 1:
        a[2] = 0
    else:
        for j in range(k // 2):
            if b[j] == b[k - 1 - j]:
                a[2] = 0
                break

    for p in range(k - 1):
        if a[3] == 0:
            break
        for q in range(p + 1, k):
            if a[3] == 0:
                break
            if b[p] != b[q]:
                w = 1
                c1 = list(bin(p)[2:].zfill(5))
                c2 = list(bin(q)[2:].zfill(5))
                for r in range(5):
                    c1[r] = int(c1[r])
                    c2[r] = int(c2[r])
                for r in range(5):
                    if c1[r] > c2[r]:
                        w = 0
                if w == 1 and b[p] > b[q]:
                    a[3] = 0

    for p in range(k):
        for q in range(k - 1, p, -1):
            b[q] = (b[q] + b[q - 1]) % 2
    t = 1
    for j in range(k):
        if j > 1:
            if j == 2 * t:
                t = j
            elif b[j] == 1:
                a[4] = 0
                break
if sum(a) == 0:
    print("YES")
else:
    print("NO")
