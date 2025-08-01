package hiiragi283.ragium.api.storage.item

import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.common.util.INBTSerializable
import net.neoforged.neoforge.items.IItemHandler
import java.util.Optional

interface HTItemHandler :
    IItemHandler,
    INBTSerializable<CompoundTag> {
    val isEmpty: Boolean
    val slotRange: IntRange get() = (0 until slots)

    fun consumeStackInSlot(slot: Int, catalyst: Optional<HTItemIngredient>, applyDamage: Boolean) {
        catalyst.ifPresent { ingredient: HTItemIngredient -> consumeStackInSlot(slot, ingredient, applyDamage) }
    }

    fun consumeStackInSlot(slot: Int, ingredient: HTItemIngredient, applyDamage: Boolean)

    fun consumeStackInSlot(slot: Int, count: Int, applyDamage: Boolean)

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
