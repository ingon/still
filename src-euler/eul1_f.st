val limit = 1000

fn eul(i, t) :: Integer -> Integer -> Integer
    if i >= limit
        return t

    val newT = if i % 3 == 0 || i % 5 == 0: t + i else t
    return eul(i + 1, newT)

eul(0, 0)
