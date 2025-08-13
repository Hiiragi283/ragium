package hiiragi283.ragium.api.recipe.base

import hiiragi283.ragium.api.recipe.HTFluidRecipe
import hiiragi283.ragium.api.recipe.HTItemWithFluidToObjRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.input.HTItemWithFluidRecipeInput
import hiiragi283.ragium.api.recipe.result.HTFluidResult
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack
import java.util.*

abstract class HTItemWithFluidToFluidRecipe(
    override val itemIngredient: Optional<HTItemIngredient>,
    override val fluidIngredient: Optional<HTFluidIngredient>,
    override val result: HTFluidResult,
) : HTItemWithFluidToObjRecipe<HTFluidResult>,
    HTFluidRecipe<HTItemWithFluidRecipeInput> {
    final override fun assembleFluid(input: HTItemWithFluidRecipeInput, registries: HolderLookup.Provider): FluidStack = result.getOrEmpty()

    final override fun getResultItem(registries: HolderLookup.Provider): ItemStack = ItemStack.EMPTY
}
