package hiiragi283.ragium.api.storage.item

import hiiragi283.ragium.api.storage.HTStackSlot
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

interface HTItemSlot : HTStackSlot<HTItemStorageStack> {
    companion object {
        const val ABSOLUTE_MAX_STACK_SIZE: Long = Item.ABSOLUTE_MAX_STACK_SIZE.toLong()
    }

    /**
     * GUIにおける[Slot]を返します。
     */
    fun createContainerSlot(): Slot?

    //    Mutable    //

    abstract class Mutable :
        HTStackSlot.Mutable<HTItemStorageStack>(),
        HTItemSlot {
        final override fun getEmptyStack(): HTItemStorageStack = HTItemStorageStack.EMPTY

        final override fun isSameStack(first: HTItemStorageStack, second: HTItemStorageStack): Boolean =
            ItemStack.isSameItemSameComponents(first.stack, second.stack)
    }
}
