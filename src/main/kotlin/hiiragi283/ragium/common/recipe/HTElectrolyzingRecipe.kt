package hiiragi283.ragium.common.recipe

import hiiragi283.core.api.recipe.base.HTFluidRecipe
import hiiragi283.core.api.recipe.base.HTProcessingRecipe
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.input.HTSingleFluidRecipeInput
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.util.Ior
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.neoforge.fluids.FluidStack

class HTElectrolyzingRecipe(
    val ingredient: HTFluidIngredient,
    val result: HTFluidResult,
    val extraResult: Ior<HTItemResult, HTFluidResult>,
    override val time: Int,
) : HTProcessingRecipe.Serializable<HTSingleFluidRecipeInput>,
    HTFluidRecipe<HTSingleFluidRecipeInput> {
    override fun test(input: HTSingleFluidRecipeInput): Boolean = ingredient.test(input.fluid)

    override fun assemble(input: HTSingleFluidRecipeInput, registries: HolderLookup.Provider): ItemStack =
        extraResult.getLeft()?.getStackResult(registries)?.value() ?: ItemStack.EMPTY

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.ELECTROLYZING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.ELECTROLYZING.get()

    override fun assembleFluid(input: HTSingleFluidRecipeInput, registries: HolderLookup.Provider): FluidStack =
        result.getStackOrEmpty(registries)

    fun assembleExtraFluid(input: HTSingleFluidRecipeInput, registries: HolderLookup.Provider): FluidStack =
        extraResult.getRight()?.getStackResult(registries)?.value() ?: FluidStack.EMPTY
}
