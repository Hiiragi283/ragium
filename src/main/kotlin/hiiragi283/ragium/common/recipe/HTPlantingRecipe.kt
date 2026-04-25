package hiiragi283.ragium.common.recipe

import hiiragi283.core.api.recipe.base.HTDoubleMultiOutputRecipe
import hiiragi283.core.api.recipe.input.HTDoubleRecipeInput
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.impl.recipe.HTBasicMultiOutputRecipe
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType

class HTPlantingRecipe(
    val plant: Ingredient,
    val soil: Ingredient,
    results: List<HTItemResult>,
    time: Int,
) : HTBasicMultiOutputRecipe<HTDoubleRecipeInput>(results, time),
    HTDoubleMultiOutputRecipe.Serializable {
    companion object {
        @JvmField
        val OUTPUT_RANGE: IntRange = 1..4
    }

    override fun test(input: HTDoubleRecipeInput): Boolean {
        val (first: ItemStack, second: ItemStack) = input
        return plant.test(first) && soil.test(second)
    }

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.PLANTING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.PLANTING.get()

    override fun getBaseAmount(input: HTDoubleRecipeInput): Int = when {
        plant.test(input.first) -> 1
        else -> 0
    }

    override fun getAdditionAmount(input: HTDoubleRecipeInput): Int = 0
}
