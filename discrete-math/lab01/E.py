a = []
c = []
n = int(input())
k = 2**n
for i in range(k):
    b = [s for s in input().split()]
    c.append(b[0])
    a.append(int(b[1]))
for i in range(k):
    for j in range(k - 1, i, -1):
        a[j] = (a[j] + a[j - 1]) % 2
for i in range(k):
    print(c[i], end=" ")
    print(a[i])
