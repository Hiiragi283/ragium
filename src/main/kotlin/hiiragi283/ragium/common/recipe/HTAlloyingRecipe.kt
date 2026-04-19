package hiiragi283.ragium.common.recipe

import hiiragi283.core.api.recipe.base.HTProcessingRecipe
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType

class HTAlloyingRecipe(
    val primary: HTItemIngredient,
    val secondary: HTItemIngredient,
    val tertiary: HTItemIngredient?,
    val result: HTItemResult,
    override val time: Int,
) : HTProcessingRecipe.Serializable<HTAlloyingRecipe.Input> {
    constructor(ingredients: List<HTItemIngredient>, result: HTItemResult, time: Int) : this(
        ingredients[0],
        ingredients[1],
        ingredients.getOrNull(2),
        result,
        time,
    )

    override fun test(input: Input): Boolean {
        val (first: ItemStack, second: ItemStack, third: ItemStack) = input
        if (!primary.test(first) || !secondary.test(second)) return false
        return tertiary?.test(third) ?: third.isEmpty
    }

    override fun assemble(input: Input, preview: Boolean): ItemStack = result.getOrEmpty(preview)

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.ALLOYING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.ALLOYING.get()

    @JvmRecord
    data class Input(val first: ItemStack, val second: ItemStack, val third: ItemStack) : RecipeInput {
        constructor(items: List<ItemStack>) : this(items[0], items[1], items[2])

        override fun getItem(index: Int): ItemStack = when (index) {
            0 -> first
            1 -> second
            2 -> third
            else -> error("No item for index $index")
        }

        override fun size(): Int = 3
    }
}
