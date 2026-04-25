package hiiragi283.ragium.common.recipe

import hiiragi283.core.api.recipe.base.HTDoubleMultiOutputRecipe
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.input.HTDoubleRecipeInput
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.util.HTShapelessRecipeHelper
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType

class HTImplodingRecipe(
    val ingredient: HTItemIngredient,
    val explosive: HTItemIngredient,
    val results: List<HTItemResult>,
    override val time: Int,
) : HTDoubleMultiOutputRecipe.Serializable {
    companion object {
        @JvmField
        val OUTPUT_RANGE: IntRange = 1..2
    }

    override fun assembleItems(input: HTDoubleRecipeInput, preview: Boolean): List<ItemStack> =
        results.map { it.getOrEmpty(preview) }.let(HTShapelessRecipeHelper::mergeStacks)

    override fun test(input: HTDoubleRecipeInput): Boolean {
        val (first: ItemStack, second: ItemStack) = input
        return ingredient.test(first) && explosive.test(second)
    }

    override fun getBaseAmount(input: HTDoubleRecipeInput): Int = ingredient.getRequiredAmount(input.first)

    override fun getAdditionAmount(input: HTDoubleRecipeInput): Int = explosive.getRequiredAmount(input.second)

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.IMPLODING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.IMPLODING.get()
}
