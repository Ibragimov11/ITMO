s = str(input())
n = len(s)
a = [""] * n
for i in range(n):
    for j in range(n):
        a[j] = s[j] + a[j]
    a.sort()
print(a[0])
