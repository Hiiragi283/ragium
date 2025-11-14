package hiiragi283.ragium.impl.recipe

import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.input.HTMultiRecipeInput
import hiiragi283.ragium.api.recipe.multi.HTComplexRecipe
import hiiragi283.ragium.api.recipe.multi.HTMultiInputsToObjRecipe
import hiiragi283.ragium.api.recipe.result.HTFluidResult
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import java.util.Optional

class HTMixingRecipe(
    val itemIngredients: List<HTItemIngredient>,
    val fluidIngredients: List<HTFluidIngredient>,
    val itemResult: Optional<HTItemResult>,
    val fluidResult: Optional<HTFluidResult>,
) : HTComplexRecipe {
    override fun getRequiredCount(index: Int, stack: ImmutableItemStack): Int = itemIngredients[index].getRequiredAmount(stack)

    override fun getRequiredAmount(index: Int, stack: ImmutableFluidStack): Int = fluidIngredients[index].getRequiredAmount(stack)

    override fun test(input: HTMultiRecipeInput): Boolean {
        val bool1: Boolean = HTMultiInputsToObjRecipe.hasMatchingSlots(itemIngredients, input.items)
        val bool2: Boolean = HTMultiInputsToObjRecipe.hasMatchingSlots(fluidIngredients, input.fluids)
        return bool1 && bool2
    }

    override fun assembleItem(input: HTMultiRecipeInput, provider: HolderLookup.Provider): ImmutableItemStack? =
        getItemResult(input, provider, itemResult)

    override fun assembleFluid(input: HTMultiRecipeInput, provider: HolderLookup.Provider): ImmutableFluidStack? =
        getFluidResult(input, provider, fluidResult)

    override fun isIncomplete(): Boolean {
        val bool1: Boolean = itemIngredients.isEmpty() || itemIngredients.any(HTItemIngredient::hasNoMatchingStacks)
        val bool2: Boolean = fluidIngredients.isEmpty() || fluidIngredients.any(HTFluidIngredient::hasNoMatchingStacks)
        val bool3: Boolean = itemResult.map { it.hasNoMatchingStack() }.orElse(false)
        val bool4: Boolean = fluidResult.map { it.hasNoMatchingStack() }.orElse(false)
        return bool1 || bool2 || bool3 || bool4
    }

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.MIXING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.MIXING.get()
}
