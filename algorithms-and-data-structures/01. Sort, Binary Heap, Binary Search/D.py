a = []

def sift_up(a):
    c = a
    i = len(a) - 1
    while i > 0 and a[i] > a[(i - 1) // 2]:
        a[i], a[(i - 1) // 2] = a[(i - 1) // 2], a[i]
        i = (i - 1) // 2
    return c

def remove_max(b):
    a = b
    res = a[0]
    a[0] = a[len(a) - 1]
    a.pop()
    i = 0
    while True:
        j = i
        if 2*i + 1 < len(a) and a[2*i + 1] > a[j]:
            j = 2*i + 1
        if 2*i + 2 < len(a) and a[2*i + 2] > a[j]:
            j = 2*i + 2
        if j == i:
            break
        a[i], a[j] = a[j], a[i]
        i = j
    return res

n = int(input())
for i in range(n):
    b = [int(s) for s in input().split()]
    if b[0] == 0:
        a.append(b[1])
        a = sift_up(a)
    else:
        k = remove_max(a)
        print(k)