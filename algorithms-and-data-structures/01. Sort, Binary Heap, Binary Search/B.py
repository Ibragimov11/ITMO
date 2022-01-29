n = int(input())
a = [int(s) for s in input().split()]
mx = a[0]
for i in range(n):
    if a[i] > mx:
        mx = a[i]
c = [0] * (mx + 1)
for i in range(n):
    c[a[i]] += 1
i = 0
for j in range(mx + 1):
    if c[j] != 0:
        while c[j] > 0:
            a[i] = j
            i += 1
            c[j] -= 1
for i in range(n):
    print(a[i], end=' ')