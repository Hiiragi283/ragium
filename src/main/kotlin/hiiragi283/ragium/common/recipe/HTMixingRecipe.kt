package hiiragi283.ragium.common.recipe

import hiiragi283.core.api.monad.Ior
import hiiragi283.core.api.recipe.HTViewProcessingRecipe
import hiiragi283.core.api.recipe.HTViewRecipeInput
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTComplexResult
import hiiragi283.core.util.HTShapelessRecipeHelper
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import net.neoforged.neoforge.fluids.FluidStack

class HTMixingRecipe(
    val ingredients: Ior<List<HTItemIngredient>, List<HTFluidIngredient>>,
    val result: HTComplexResult,
    parameters: SubParameters,
) : HTViewProcessingRecipe(parameters) {
    companion object {
        const val MAX_FLUID_INPUT = 2
        const val MAX_ITEM_INPUT = 3
    }

    fun getResultFluid(provider: HolderLookup.Provider): FluidStack =
        result.getRight()?.getStackResult(provider)?.value() ?: FluidStack.EMPTY

    override fun matches(input: HTViewRecipeInput, level: Level): Boolean = ingredients.fold(
        { HTShapelessRecipeHelper.shapelessMatch(it, input.items).isNotEmpty() },
        { HTShapelessRecipeHelper.shapelessMatch(it, input.fluids).isNotEmpty() },
        { items: List<HTItemIngredient>, fluids: List<HTFluidIngredient> ->
            val bool1: Boolean = HTShapelessRecipeHelper.shapelessMatch(items, input.items).isNotEmpty()
            val bool2: Boolean = HTShapelessRecipeHelper.shapelessMatch(fluids, input.fluids).isNotEmpty()
            bool1 && bool2
        },
    )

    override fun getResultItem(registries: HolderLookup.Provider): ItemStack =
        result.getLeft()?.getStackResult(registries)?.value() ?: ItemStack.EMPTY

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.MIXING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.MIXING.get()
}
