import sys

a = []
mn = []
for t in range(int(sys.stdin.readline())):
    s = sys.stdin.readline()
    b = int(s.split(' ', 1)[0])
    if b == 1:
        a.append(int(s.split(' ', 1)[1]))
        if len(mn) == 0:
            mn.append(int(s.split(' ', 1)[1]))
        else:
            mn.append(min(int(s.split(' ', 1)[1]), mn[len(mn) - 1]))
    elif b == 2:
        a.pop()
        mn.pop()
    else:
        sys.stdout.write(str(mn[len(mn) - 1]) + str('\n'))
