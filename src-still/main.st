import java.util.List
import java.util.ArrayList

print(isEmpty([]))

op +> (infix, 80, 79) (i, l) :: a -> List a -> List a
    add(l, 0, i)
    return l

op ! (prefix) (a) :: boolean -> boolean
    return if a: false else true

fn head(l) :: List a -> a
    get(l, 0)

fn tail(l) :: List a -> List a
    subList(l, 1, size(l))

fn map(l, f) :: List a -> (a -> b) -> List b
    return if isEmpty(l): [] else f(head(l)) +> map(tail(l), f)

print(map)

print(map([1, 2, 3], fn(x) x * x - (x + -- x)))

print("Heio" + "XXX")
