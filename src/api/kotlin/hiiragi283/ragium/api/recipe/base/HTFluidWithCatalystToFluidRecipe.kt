package hiiragi283.ragium.api.recipe.base

import hiiragi283.ragium.api.recipe.HTFluidRecipe
import hiiragi283.ragium.api.recipe.HTFluidWithCatalystToObjRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.input.HTItemWithFluidRecipeInput
import hiiragi283.ragium.api.recipe.result.HTFluidResult
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack
import java.util.*

abstract class HTFluidWithCatalystToFluidRecipe(
    override val ingredient: HTFluidIngredient,
    override val catalyst: Optional<HTItemIngredient>,
    override val result: HTFluidResult,
) : HTFluidWithCatalystToObjRecipe<HTFluidResult>,
    HTFluidRecipe<HTItemWithFluidRecipeInput> {
    final override fun assembleFluid(input: HTItemWithFluidRecipeInput, registries: HolderLookup.Provider): FluidStack =
        if (test(input)) result.getOrEmpty(registries) else FluidStack.EMPTY

    final override fun getResultItem(registries: HolderLookup.Provider): ItemStack = ItemStack.EMPTY
}
