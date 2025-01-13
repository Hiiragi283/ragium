package hiiragi283.ragium.api.storage

import net.minecraft.core.Direction
import net.minecraft.world.SimpleContainer
import net.minecraft.world.WorldlyContainer
import net.minecraft.world.item.ItemStack

object EmptyWorldlyContainer : SimpleContainer(0), WorldlyContainer {
    override fun getSlotsForFace(side: Direction): IntArray = intArrayOf()

    override fun canPlaceItemThroughFace(index: Int, itemStack: ItemStack, direction: Direction?): Boolean = false

    override fun canTakeItemThroughFace(index: Int, stack: ItemStack, direction: Direction): Boolean = false
}
