package hiiragi283.ragium.api.recipe

import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput

class HTListedRecipeInput(private val stacks: List<ItemStack>) : RecipeInput {
    constructor(vararg stacks: ItemStack) : this(listOf(*stacks))

    operator fun get(index: Int): ItemStack = getItem(index)

    override fun getItem(index: Int): ItemStack = stacks.getOrNull(index) ?: ItemStack.EMPTY

    override fun size(): Int = stacks.size
}
