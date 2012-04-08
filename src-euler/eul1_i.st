val limit = 1000
var total = 0

for i in 1..limit
    if i % 3 == 0 || i % 5 == 0
        total = total + i

print(total)
