package hiiragi283.ragium.api.storage.item

import hiiragi283.ragium.api.storage.HTStackSlot
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack

/**
 * [ImmutableItemStack]向けの[HTStackSlot]の拡張インターフェース
 */
interface HTItemSlot : HTStackSlot<ImmutableItemStack> {
    /**
     * GUIにおける[Slot]を返します。
     */
    fun createContainerSlot(): Slot?

    //    Mutable    //

    /**
     * [ImmutableItemStack]向けの[HTStackSlot.Mutable]の拡張クラス
     */
    abstract class Mutable :
        HTStackSlot.Mutable<ImmutableItemStack>(),
        HTItemSlot {
        final override fun getEmptyStack(): ImmutableItemStack = ImmutableItemStack.EMPTY

        final override fun isSameStack(first: ImmutableItemStack, second: ImmutableItemStack): Boolean =
            ItemStack.isSameItemSameComponents(first.stack, second.stack)
    }
}
