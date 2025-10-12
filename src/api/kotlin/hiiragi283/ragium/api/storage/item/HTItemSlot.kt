package hiiragi283.ragium.api.storage.item

import hiiragi283.ragium.api.storage.HTStackSlot
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack

/**
 * [HTItemStorageStack]向けの[HTStackSlot]の拡張インターフェース
 */
interface HTItemSlot : HTStackSlot<HTItemStorageStack> {
    /**
     * GUIにおける[Slot]を返します。
     */
    fun createContainerSlot(): Slot?

    //    Mutable    //

    /**
     * [HTItemStorageStack]向けの[HTStackSlot.Mutable]の拡張クラス
     */
    abstract class Mutable :
        HTStackSlot.Mutable<HTItemStorageStack>(),
        HTItemSlot {
        final override fun getEmptyStack(): HTItemStorageStack = HTItemStorageStack.EMPTY

        final override fun isSameStack(first: HTItemStorageStack, second: HTItemStorageStack): Boolean =
            ItemStack.isSameItemSameComponents(first.stack, second.stack)
    }
}
