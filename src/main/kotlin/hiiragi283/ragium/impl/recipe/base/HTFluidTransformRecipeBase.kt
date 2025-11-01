package hiiragi283.ragium.impl.recipe.base

import hiiragi283.ragium.api.recipe.HTFluidTransformRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.input.HTItemWithFluidRecipeInput
import hiiragi283.ragium.api.recipe.result.HTFluidResult
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.ImmutableItemStack
import net.minecraft.core.HolderLookup
import java.util.Optional

abstract class HTFluidTransformRecipeBase : HTFluidTransformRecipe {
    abstract val fluidIngredient: HTFluidIngredient
    abstract val itemIngredient: Optional<HTItemIngredient>
    abstract val itemResult: Optional<HTItemResult>
    abstract val fluidResult: Optional<HTFluidResult>

    final override fun getRequiredCount(stack: ImmutableItemStack): Int = itemIngredient.map { it.getRequiredAmount(stack) }.orElse(0)

    final override fun getRequiredAmount(stack: ImmutableFluidStack): Int = fluidIngredient.getRequiredAmount(stack)

    final override fun test(input: HTItemWithFluidRecipeInput): Boolean {
        val bool1: Boolean = fluidIngredient.test(input.fluid)
        val bool2: Boolean = itemIngredient
            .map { ingredient: HTItemIngredient -> ingredient.testOnlyType(input.item) }
            .orElse(input.item.isEmpty)
        return bool1 && bool2
    }

    final override fun assembleItem(input: HTItemWithFluidRecipeInput, registries: HolderLookup.Provider): ImmutableItemStack? =
        getItemResult(input, registries, itemResult)

    final override fun assembleFluid(input: HTItemWithFluidRecipeInput, registries: HolderLookup.Provider): ImmutableFluidStack? =
        getFluidResult(input, registries, fluidResult)

    final override fun isIncomplete(): Boolean {
        if (itemResult.isEmpty && fluidResult.isEmpty) return true
        val bool1: Boolean = fluidIngredient.hasNoMatchingStacks()
        val bool2: Boolean = itemIngredient.map(HTItemIngredient::hasNoMatchingStacks).orElse(false)
        val bool3: Boolean = itemResult.map(HTItemResult::hasNoMatchingStack).orElse(false)
        val bool4: Boolean = fluidResult.map(HTFluidResult::hasNoMatchingStack).orElse(false)
        return bool1 || bool2 || bool3 || bool4
    }
}
