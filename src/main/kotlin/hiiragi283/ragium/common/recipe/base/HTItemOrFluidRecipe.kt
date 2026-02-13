package hiiragi283.ragium.common.recipe.base

import hiiragi283.core.api.monad.Either
import hiiragi283.core.api.monad.Ior
import hiiragi283.core.api.recipe.HTFluidRecipe
import hiiragi283.core.api.recipe.HTProcessingRecipe
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.input.HTItemAndFluidRecipeInput
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.neoforged.neoforge.fluids.FluidStack

abstract class HTItemOrFluidRecipe(
    val ingredient: Ior<HTItemIngredient, HTFluidIngredient>,
    val result: Either<HTItemResult, HTFluidResult>,
    parameters: SubParameters,
) : HTProcessingRecipe<HTItemAndFluidRecipeInput>(parameters),
    HTFluidRecipe {
    final override fun matches(input: HTItemAndFluidRecipeInput, level: Level): Boolean = ingredient.fold(
        { it.test(input.item) },
        { it.test(input.fluid) },
        { itemIng: HTItemIngredient, fluidIng: HTFluidIngredient ->
            itemIng.test(input.item) && fluidIng.test(input.fluid)
        },
    )

    final override fun getResultItem(registries: HolderLookup.Provider): ItemStack =
        result.getLeft()?.getStackResult(registries)?.value() ?: ItemStack.EMPTY

    final override fun getResultFluid(registries: HolderLookup.Provider): FluidStack =
        result.getRight()?.getStackResult(registries)?.value() ?: FluidStack.EMPTY
}
