package hiiragi283.ragium.common.recipe

import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.ragium.common.recipe.base.HTFluidWithItemRecipe
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.neoforge.fluids.FluidStack

class HTArcSmeltingRecipe(
    fluidIngredient: HTFluidIngredient,
    itemIngredient: HTItemIngredient,
    result: HTFluidResult,
    parameters: SubParameters,
) : HTFluidWithItemRecipe<HTFluidResult>(fluidIngredient, itemIngredient, result, parameters) {
    fun getResultFluid(provider: HolderLookup.Provider): FluidStack = result.getStackOrEmpty(provider)

    override fun getResultItem(registries: HolderLookup.Provider): ItemStack = ItemStack.EMPTY

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.ARC_SMELTING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.ARC_FURNACE.get()
}
