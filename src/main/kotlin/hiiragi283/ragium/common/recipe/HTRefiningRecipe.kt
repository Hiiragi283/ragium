package hiiragi283.ragium.common.recipe

import hiiragi283.core.api.recipe.HTProcessingRecipe
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.input.HTSingleFluidRecipeInput
import hiiragi283.core.api.recipe.result.HTComplexResult
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import net.neoforged.neoforge.fluids.FluidStack
import org.apache.commons.lang3.math.Fraction

class HTRefiningRecipe(
    val ingredient: HTFluidIngredient,
    val result: HTFluidResult,
    val extraResult: HTComplexResult,
    time: Int,
    exp: Fraction,
) : HTProcessingRecipe<HTSingleFluidRecipeInput>(time, exp) {
    fun getResultFluid(provider: HolderLookup.Provider): FluidStack = result.getStackOrEmpty(provider)

    fun getExtraFluid(provider: HolderLookup.Provider): FluidStack = extraResult.getRight()?.getStackOrEmpty(provider) ?: FluidStack.EMPTY

    override fun matches(input: HTSingleFluidRecipeInput, level: Level): Boolean = ingredient.test(input.fluid)

    override fun getResultItem(registries: HolderLookup.Provider): ItemStack =
        extraResult.getLeft()?.getStackOrEmpty(registries) ?: ItemStack.EMPTY

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.REFINING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.REFINING.get()
}
