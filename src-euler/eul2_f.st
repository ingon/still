val lim = 4000000

fn eul(f1, f2, total) :: Integer -> Integer -> Integer
    val f3 = f1 + f2
    if f3 > lim
        return total
    val newT = if f3 % 2 == 0: total + f3 else total
    return eul(f2, f3, newT)
    
eul(1, 2, 2)
