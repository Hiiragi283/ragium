package hiiragi283.ragium.api.util

import net.minecraft.screen.PropertyDelegate

class HTDynamicPropertyDelegate(private val size: Int, private val getter: (Int) -> Int) : PropertyDelegate {
    override fun get(index: Int): Int = getter(index)

    override fun set(index: Int, value: Int) {}

    override fun size(): Int = size
}
