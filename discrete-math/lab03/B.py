s = str(input())
n = len(s)
a = []
for i in range(n):
    a.append(s[i:] + s[:i])
a.sort()
b_u = ""
for i in range(n):
    b_u += a[i][n - 1]
print(b_u)
