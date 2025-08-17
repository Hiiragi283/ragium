package hiiragi283.ragium.api.storage.item

import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.storage.HTContentListener
import hiiragi283.ragium.api.storage.HTTransferIO
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.common.util.INBTSerializable
import net.neoforged.neoforge.items.IItemHandler
import net.neoforged.neoforge.items.IItemHandlerModifiable
import java.util.Optional
import kotlin.math.min

interface HTItemHandler :
    IItemHandlerModifiable,
    INBTSerializable<CompoundTag>,
    HTContentListener {
    val isEmpty: Boolean
    val inputSlots: IntArray
    val outputSlots: IntArray

    //    Extensions    //

    val slotRange: IntRange get() = (0 until slots)

    fun toFiltered(): IItemHandler = HTFilteredItemHandler(this, inputSlots, outputSlots, null, HTTransferIO.Provider.ALWAYS)

    fun toFilteredReverse(): IItemHandler = HTFilteredItemHandler(this, outputSlots, inputSlots, null, HTTransferIO.Provider.ALWAYS)

    /**
     * @see [mekanism.api.fluid.IExtendedFluidTank.setStackSize]
     */
    fun setStackSize(slot: Int, count: Int, simulate: Boolean): Int {
        val stack: ItemStack = getStackInSlot(slot)
        if (stack.isEmpty) {
            return 0
        } else if (count <= 0) {
            if (!simulate) {
                setStackInSlot(slot, ItemStack.EMPTY)
            }
            return 0
        }
        val countLimit: Int = getSlotLimit(slot)
        val fixedCount: Int = min(countLimit, countLimit)
        if (stack.count == fixedCount || simulate) {
            return fixedCount
        }
        setStackInSlot(slot, stack.copyWithCount(fixedCount))
        return fixedCount
    }

    /**
     * @see [mekanism.api.fluid.IExtendedFluidTank.growStack]
     */
    fun growStack(slot: Int, count: Int, simulate: Boolean): Int {
        val stack: ItemStack = getStackInSlot(slot)
        val current: Int = stack.count
        if (current == 0 || count == 0) return 0
        val fixedCount: Int = min(count, getSlotLimit(slot) - current)
        val newCount: Int = setStackSize(slot, current + fixedCount, simulate)
        return newCount - current
    }

    /**
     * @see [mekanism.api.fluid.IExtendedFluidTank.shrinkStack]
     */
    fun shrinkStack(slot: Int, count: Int, simulate: Boolean): Int = -growStack(slot, -count, simulate)

    fun shrinkStack(slot: Int, ingredient: HTItemIngredient, simulate: Boolean): Int {
        val stackIn: ItemStack = getStackInSlot(slot)
        return shrinkStack(slot, ingredient.getRequiredAmount(stackIn), simulate)
    }

    fun shrinkStack(slot: Int, catalyst: Optional<HTItemIngredient>, simulate: Boolean): Int {
        val stackIn: ItemStack = getStackInSlot(slot)
        return catalyst
            .map { ingredient: HTItemIngredient -> shrinkStack(slot, ingredient.getRequiredAmount(stackIn), simulate) }
            .orElse(0)
    }

    fun getStackView(): Iterable<ItemStack>

    fun hasStack(filter: (ItemStack) -> Boolean): Boolean {
        for (stackIn: ItemStack in getStackView()) {
            if (filter(stackIn)) {
                return true
            }
        }
        return false
    }

    operator fun get(slot: Int): ItemStack = getStackInSlot(slot)
}
