package hiiragi283.ragium.api.storage.item

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.stack.maxStackSize
import hiiragi283.ragium.api.storage.HTStackSlot
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack
import kotlin.math.min

/**
 * [ImmutableItemStack]向けの[HTStackSlot]の拡張インターフェース
 * @see [mekanism.api.inventory.IInventorySlot]
 */
interface HTItemSlot : HTStackSlot<ImmutableItemStack> {
    companion object {
        @JvmStatic
        fun getMaxStackSize(stack: ImmutableItemStack, limit: Int = RagiumConst.ABSOLUTE_MAX_STACK_SIZE): Int =
            if (stack.isEmpty()) limit else min(limit, stack.maxStackSize())
    }

    /**
     * GUIにおける[Slot]を返します。
     */
    fun createContainerSlot(): Slot? = null

    override fun isSameStack(other: ImmutableItemStack): Boolean = ItemStack.isSameItemSameComponents(this.getItemStack(), other.stack)

    //    Mutable    //

    /**
     * [ImmutableItemStack]向けの[HTStackSlot.Mutable]の拡張クラス
     */
    abstract class Mutable :
        HTStackSlot.Mutable<ImmutableItemStack>(),
        HTItemSlot {
        final override fun getEmptyStack(): ImmutableItemStack = ImmutableItemStack.EMPTY
    }
}
