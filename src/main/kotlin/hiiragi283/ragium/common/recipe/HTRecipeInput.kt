package hiiragi283.ragium.common.recipe

import hiiragi283.ragium.common.Ragium
import net.minecraft.item.ItemStack
import net.minecraft.recipe.input.RecipeInput

class HTRecipeInput(private val list: List<ItemStack>) : RecipeInput {

    constructor(builderAction: MutableList<ItemStack>.() -> Unit) : this(buildList(builderAction))

    fun matches(maps: Map<Int, WeightedIngredient>): Boolean =
        maps.all { (slot: Int, ing: WeightedIngredient) -> ing.test(getStackInSlot(slot)) }

    fun matches(vararg pairs: Pair<Int, WeightedIngredient>): Boolean = matches(mapOf(*pairs))

    fun matches(list: List<WeightedIngredient>): Boolean {
        Ragium.log {
            info("===")
            list.forEach { info("Input; $it") }
        }
        return list
            .filterIndexed { index: Int, ing: WeightedIngredient -> !ing.test(getStackInSlot(index)) }.isEmpty()
    }

    //    RecipeInput    //

    override fun getStackInSlot(slot: Int): ItemStack = list.getOrNull(slot) ?: ItemStack.EMPTY

    override fun getSize(): Int = list.size

    override fun isEmpty(): Boolean = list.isEmpty() || list.all(ItemStack::isEmpty)
}