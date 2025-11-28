package hiiragi283.ragium.impl.recipe

import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.input.HTMultiRecipeInput
import hiiragi283.ragium.api.recipe.result.HTComplexResult
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.impl.recipe.base.HTBasicComplexRecipe
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import java.util.Optional

class HTRefiningRecipe(val itemIngredient: Optional<HTItemIngredient>, val fluidIngredient: HTFluidIngredient, results: HTComplexResult) :
    HTBasicComplexRecipe(results) {
    override fun isIncompleteIngredient(): Boolean {
        val bool1: Boolean = itemIngredient.isPresent && itemIngredient.get().hasNoMatchingStacks()
        val bool2: Boolean = fluidIngredient.hasNoMatchingStacks()
        return bool1 || bool2
    }

    override fun test(input: HTMultiRecipeInput): Boolean {
        val bool1: Boolean = itemIngredient.isEmpty || itemIngredient.get().test(input.getItem(0))
        val bool2: Boolean = fluidIngredient.test(input.getFluid(0))
        return bool1 && bool2
    }

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.REFINING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.REFINING.get()

    override fun getRequiredCount(index: Int, stack: ImmutableItemStack): Int = 0

    override fun getRequiredAmount(index: Int, stack: ImmutableFluidStack): Int = when (index) {
        0 -> fluidIngredient.getRequiredAmount(stack)
        else -> 0
    }
}
