package hiiragi283.ragium.common.recipe

import hiiragi283.core.api.data.recipe.HTIngredientCreator
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.impl.recipe.HTBasicMultiOutputRecipe
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.neoforge.fluids.FluidStack

/**
 * @see hiiragi283.core.impl.recipe.HTBasicSingleMultiOutputRecipe
 */
class HTWashingRecipe(val ingredient: HTItemIngredient, results: List<HTItemResult>, time: Int) :
    HTBasicMultiOutputRecipe<HTItemAndFluidRecipeInput>(results, time) {
    companion object {
        @JvmField
        val WATER_INGREDIENT: HTFluidIngredient = HTIngredientCreator.water(250)

        @JvmField
        val OUTPUT_RANGE: IntRange = 1..4
    }

    override fun test(input: HTItemAndFluidRecipeInput): Boolean {
        val (item: ItemStack, fluid: FluidStack) = input
        return ingredient.test(item) && WATER_INGREDIENT.test(fluid)
    }

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.WASHING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.WASHING.get()
}
