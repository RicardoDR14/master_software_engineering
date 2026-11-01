package pt.isec.a2022147797.p03teste

object MyObject {
    private var _my_value = 0
    val my_value : Int
        get() = ++_my_value
}