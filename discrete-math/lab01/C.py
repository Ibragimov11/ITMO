n = int(input())
a = []
for i in range(n):
    a.append([0])
gl = 0
for i in range(n):
    b = [int(s) for s in input().split()]
    if b[0] != 0:
        mx = a[b[1] - 1][0]
        for j in range(1, len(b)):
            if a[b[j] - 1][0] > mx:
                mx = [b[j] - 1][0]
        a[i][0] = mx + 1
        k = 2**(len(b) - 1)
        a[i].append([int(s) for s in input().split()])
    if a[i][0] > gl:
        gl = a[i][0]
print(gl)
d = [0]*(2**n)
