package hiiragi283.ragium.api.storage.item

import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.storage.HTTransferIOHolder
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.common.util.INBTSerializable
import net.neoforged.neoforge.items.IItemHandler
import net.neoforged.neoforge.items.IItemHandlerModifiable
import java.util.Optional

interface HTItemHandler :
    IItemHandlerModifiable,
    INBTSerializable<CompoundTag> {
    val isEmpty: Boolean
    val inputSlots: IntArray
    val outputSlots: IntArray

    //    Extensions    //

    val slotRange: IntRange get() = (0 until slots)

    fun toFiltered(): IItemHandler = HTFilteredItemHandler(this, inputSlots, outputSlots, null, HTTransferIOHolder.ALWAYS)

    fun toFilteredReverse(): IItemHandler = HTFilteredItemHandler(this, outputSlots, inputSlots, null, HTTransferIOHolder.ALWAYS)

    fun extractItem(slot: Int, catalyst: Optional<HTItemIngredient>, simulate: Boolean): ItemStack {
        val stackIn: ItemStack = getStackInSlot(slot)
        return catalyst
            .map { ingredient: HTItemIngredient -> extractItem(slot, ingredient.getRequiredAmount(stackIn), simulate) }
            .orElse(ItemStack.EMPTY)
    }

    fun extractItem(slot: Int, ingredient: HTItemIngredient, simulate: Boolean): ItemStack {
        val stackIn: ItemStack = getStackInSlot(slot)
        return extractItem(slot, ingredient.getRequiredAmount(stackIn), simulate)
    }

    fun getRemainingStack(slot: Int, count: Int): ItemStack {
        val stackIn: ItemStack = getStackInSlot(slot)
        if (stackIn.hasCraftingRemainingItem()) {
            val remain: ItemStack = stackIn.craftingRemainingItem
            stackIn.shrink(count)
            return remain
        } else {
            stackIn.shrink(count)
            return ItemStack.EMPTY
        }
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
