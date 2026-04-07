package hiiragi283.ragium.impl.recipe

import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.impl.recipe.HTBasicMultiOutputRecipe
import hiiragi283.ragium.api.recipe.base.HTItemFluidMultiOutputRecipe
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack

abstract class HTBasicItemFluidMultiOutputRecipe(
    val itemIngredient: HTItemIngredient,
    val fluidIngredient: HTFluidIngredient,
    results: List<HTItemResult>,
    time: Int,
) : HTBasicMultiOutputRecipe<HTItemAndFluidRecipeInput>(results, time),
    HTItemFluidMultiOutputRecipe.Serializable {
    override fun getRequiredCount(input: HTItemAndFluidRecipeInput): Int = itemIngredient.amount

    override fun getRequiredAmount(input: HTItemAndFluidRecipeInput): Int = fluidIngredient.amount

    final override fun test(input: HTItemAndFluidRecipeInput): Boolean {
        val (item: ItemStack, fluid: FluidStack) = input
        return itemIngredient.test(item) && fluidIngredient.test(fluid)
    }
}
