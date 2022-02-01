a = [int (x) for x in input().split()]
b = []
count = 0
for i in range(len(a)):
    b.append(a[i])
    if b[len(b) - 1] != b[len(b) - 2]:
        j = len(b) - 2
        c = 1
        while b[j] == b[j - 1]:
            c += 1
            j -= 1
        if c >= 3:
            count += c
            for k in range(c):
                b.pop(len(b) - 2)
j = len(b) - 1
c = 1
while b[j] == b[j - 1]:
    j -= 1
    c += 1
    if j == 0:
        break
if c >= 3:
    count += c
print(count)
