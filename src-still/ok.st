fun distance(x, y)
    return x * x + y * y
print(distance(3, 4) == 25)

fun area(x, y)
    return x * y
print(area(3, 4) == 12)

fun new-distance(x, y)
    fun pow2(z)
        return z * z
    return pow2(x) + pow2(y)
print(new-distance(3, 4) == 25)

fun pow(x, t)
    if t == 1
        return x
    else
        return x * pow(x, t - 1)
print(pow(2, 4) == 16)

fun powr(x, t)
    return if t == 1 
        x 
    else 
        x * powr(x, t - 1)
print(powr(2, 4) == 16)

fun powx(x, t)
    if t == 1
        x
    else
        x * powx(x, t - 1)
print(powx(2, 4) == 16)

fun powm(x, t)
    t == 1 ? x : x * powm(x, t - 1)
print(powm(2, 4) == 16)

fun powz(x, t)
    return t == 1 ? x : x * powz(x, t - 1)
print(powz(2, 4) == 16)

fun times(x, t)
    x()
    if t != 1
        times(x, t - 1)
        
fun hello()
    print(9)

times(hello, 3)
times(fun print(8), 3)

print([1, 2, 3])

print(hello() + hello() == 18)

fun map(l, f)
    empty?(l) ? [] : f(head(l)) :: map(tail(l), f)
    
print(map([1, 2, 3], fun(x) x * x) == [1, 4, 9])

fun a()
    return if 0 == 0
        return 1
        
print(a() == 1)

fun mapLiteral()
    return {1 = [1], 2 = [2]}
        
print(mapLiteral())
