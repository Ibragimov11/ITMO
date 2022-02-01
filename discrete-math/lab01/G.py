n = int(input())
a = [int(s) for s in input().split()]
s = int(input())
for i in range(len(a)):
    a[i] = [int(aa) for aa in list(bin(a[i])[2:].zfill(32))]
x = [int(aa) for aa in list(bin(s)[2:].zfill(32))]
for i in range(len(x)):
    x[i] = int(x[i])
b = []
for i in range(32):
    c = []
    for j in range(n):
        c.append(a[j][i])
    b.append(c)
bool = True
for i in range(len(b)):
    if sum(b[i]) == 0 and x[i] == 1:
        bool = False
        break
while sum(b[1]) == 0:
    b.remove(b[0])
    x.remove(x[0])
for i in range(len(b)):
    for j in range(i, len(b)):
        if b[i] == b[j] and x[i] != x[j]:
            bool = False
if bool == False:
    print("Impossible")
else:
    string = ""
    for i in range(len(b)):
        if x[i] == 1:
            if len(string) != 0:
                string += "|"
            string += "("
            for j in range(n):
                if j != 0:
                    string += "&"
                if b[i][j] == 0:
                    string += "~"
                string += str(j+1)
            string += ")"
    if len(string) == 0:
        print("(1&~1)")
    else:
        print(string)
