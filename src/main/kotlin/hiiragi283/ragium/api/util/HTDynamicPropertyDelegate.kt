package hiiragi283.ragium.api.util

import net.minecraft.screen.PropertyDelegate

class HTDynamicPropertyDelegate(
    private val size: Int,
    private val getter: (Int) -> Int,
    private val setter: (Int, Int) -> Unit = { _: Int, _: Int -> },
) : PropertyDelegate {
    override fun get(index: Int): Int = getter(index)

    override fun set(index: Int, value: Int) {
        setter(index, value)
    }

    override fun size(): Int = size
}
