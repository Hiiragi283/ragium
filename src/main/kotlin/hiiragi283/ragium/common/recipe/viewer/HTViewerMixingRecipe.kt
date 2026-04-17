package hiiragi283.ragium.common.recipe.viewer

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.recipe.base.HTProcessingRecipe
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.serialization.codec.MapBiCodec
import hiiragi283.ragium.api.recipe.base.HTMixingRecipe
import hiiragi283.ragium.api.recipe.input.HTMixingRecipeInput
import net.minecraft.core.HolderLookup
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack

@JvmRecord
data class HTViewerMixingRecipe(
    val itemIngredients: List<HTItemIngredient>,
    val fluidIngredients: List<HTFluidIngredient>,
    val itemResults: List<HTItemResult>,
    val fluidResults: List<HTFluidResult>,
    override val time: Int,
) : HTMixingRecipe {
    companion object {
        @JvmField
        val CODEC: MapBiCodec<RegistryFriendlyByteBuf, HTViewerMixingRecipe> = MapBiCodec.composite(
            HTItemIngredient.CODEC
                .listOf(0, 2)
                .optionalFieldOf(HTConst.ITEM_INGREDIENT, emptyList())
                .forGetter(HTViewerMixingRecipe::itemIngredients),
            HTFluidIngredient.CODEC
                .listOf(0, 2)
                .optionalFieldOf(HTConst.FLUID_INGREDIENT, emptyList())
                .forGetter(HTViewerMixingRecipe::fluidIngredients),
            HTItemResult.CODEC
                .listOf(0, 2)
                .optionalFieldOf(HTConst.ITEM_RESULT, emptyList())
                .forGetter(HTViewerMixingRecipe::itemResults),
            HTFluidResult.CODEC
                .listOf(0, 2)
                .optionalFieldOf(HTConst.FLUID_RESULT, emptyList())
                .forGetter(HTViewerMixingRecipe::fluidResults),
            HTProcessingRecipe.timeCodec(),
            ::HTViewerMixingRecipe,
        )
    }

    override fun getRequiredAmounts(input: HTMixingRecipeInput): HTMixingRecipe.RequiredAmounts = HTMixingRecipe.RequiredAmounts.EMPTY

    override fun assembleFluids(input: HTMixingRecipeInput, registries: HolderLookup.Provider): List<FluidStack> = emptyList()

    override fun assembleItems(input: HTMixingRecipeInput, registries: HolderLookup.Provider): List<ItemStack> = emptyList()

    override fun test(input: HTMixingRecipeInput): Boolean = false
}
