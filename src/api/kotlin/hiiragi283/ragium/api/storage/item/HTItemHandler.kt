package hiiragi283.ragium.api.storage.item

import hiiragi283.ragium.api.RagiumAPI
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
    companion object {
        @JvmField
        val EMPTY: HTItemHandler = Builder(0).build()
    }

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
        val fixedCount: Int = min(count, countLimit)
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
        if (current == 0) return 0
        val fixedCount: Int = when {
            count > 0 -> min(count, getSlotLimit(slot) - current)
            else -> count
        }
        val newCount: Int = setStackSize(slot, current + fixedCount, simulate)
        return newCount - current
    }

    /**
     * @see [mekanism.api.fluid.IExtendedFluidTank.shrinkStack]
     */
    fun shrinkStack(slot: Int, count: Int, simulate: Boolean): Int = -growStack(slot, -count, simulate)

    fun shrinkStack(slot: Int, ingredient: HTItemIngredient, simulate: Boolean): Int {
        val stackIn: ItemStack = getStackInSlot(slot)
        if (stackIn.hasCraftingRemainingItem() && stackIn.count == 1) {
            setStackInSlot(slot, stackIn.craftingRemainingItem)
            return 0
        }
        return shrinkStack(slot, ingredient.getRequiredAmount(stackIn), simulate)
    }

    fun shrinkStack(slot: Int, catalyst: Optional<HTItemIngredient>, simulate: Boolean): Int = catalyst
        .map { ingredient: HTItemIngredient -> shrinkStack(slot, ingredient.getRequiredAmount(getStackInSlot(slot)), simulate) }
        .orElse(0)

    fun getStackView(): Iterable<ItemStack>

    fun hasStack(filter: (ItemStack) -> Boolean): Boolean {
        for (stackIn: ItemStack in getStackView()) {
            if (filter(stackIn)) {
                return true
            }
        }
        return false
    }

    //    Builder    //

    class Builder(private val size: Int) {
        private val inputSlots: MutableList<Int> = mutableListOf()
        private val outputSlots: MutableList<Int> = mutableListOf()

        fun addInput(vararg slots: Int): Builder = apply {
            inputSlots.addAll(slots.toTypedArray())
        }

        fun addInput(slots: IntRange): Builder = apply {
            inputSlots.addAll(slots)
        }

        fun addOutput(vararg slots: Int): Builder = apply {
            outputSlots.addAll(slots.toTypedArray())
        }

        fun addOutput(slots: IntRange): Builder = apply {
            outputSlots.addAll(slots)
        }

        fun build(): HTItemHandler = build(null)

        fun build(callback: HTContentListener?): HTItemHandler =
            RagiumAPI.getInstance().createItemHandler(size, inputSlots.toIntArray(), outputSlots.toIntArray(), callback)
    }
}
