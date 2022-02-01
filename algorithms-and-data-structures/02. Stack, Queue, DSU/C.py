import sys

a = []
for i in range(int(sys.stdin.readline())):
    s = sys.stdin.readline()
    b = int(s.split(' ', 1)[0])
    if b == 1:
        a.append(int(s.split(' ', 1)[1]))
    elif b == 2:
        a.pop(0)
    elif b == 3:
        a.pop()
    elif b == 4:
        sys.stdout.write(str(a.index(int(s.split(' ', 1)[1]))) + str('\n'))
    elif b == 5:
        sys.stdout.write(str(a[0]) + str('\n'))
