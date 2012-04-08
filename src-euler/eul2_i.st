val lim = 100
var f1 = 1
var f2 = 2
var sum = 2

while true
    val f3 = f1 + f2
    if f3 > lim
        break
    if f3 % 2 == 0
        sum = sum + f3
    f1 = f2
    f2 = f3

print(sum)
