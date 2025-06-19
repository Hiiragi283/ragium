package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.storage.item.HTItemSlot
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput

class HTListedRecipeInput(private val stacks: List<ItemStack>) : RecipeInput {
    companion object {
        @JvmStatic
        fun fromStacks(vararg stacks: ItemStack): HTListedRecipeInput = HTListedRecipeInput(listOf(*stacks))

        @JvmStatic
        fun fromSlots(slots: List<HTItemSlot>): HTListedRecipeInput = HTListedRecipeInput(slots.map(HTItemSlot::stack))
    }

    operator fun get(index: Int): ItemStack = getItem(index)

    override fun getItem(index: Int): ItemStack = stacks.getOrNull(index) ?: ItemStack.EMPTY

    override fun size(): Int = stacks.size
}
