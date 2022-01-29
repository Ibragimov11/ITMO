w, h, n = map(int, input().split())
l = 0
r = max(h, w) * n
while r - l > 1:
    m = (r + l) // 2
    if (m // h) * (m // w) >= n:
        r = m
    else:
        l = m
print(r)
