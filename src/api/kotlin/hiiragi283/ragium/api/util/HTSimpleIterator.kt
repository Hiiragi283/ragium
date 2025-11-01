package hiiragi283.ragium.api.util

class HTSimpleIterator<T>(private val maxCount: Int, private val getter: (Int) -> T) : Iterator<T> {
    private var index = 0

    override fun next(): T {
        val element: T = getter(index)
        index++
        return element
    }

    override fun hasNext(): Boolean = index < maxCount
}
