package hiiragi283.ragium.impl.recipe

import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.input.HTRecipeInput
import hiiragi283.ragium.api.recipe.result.HTComplexResult
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.impl.recipe.base.HTBasicComplexRecipe
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level

class HTSimpleMixingRecipe(val itemIngredient: HTItemIngredient, val fluidIngredient: HTFluidIngredient, results: HTComplexResult) :
    HTBasicComplexRecipe(results) {
    override fun matches(input: HTRecipeInput, level: Level): Boolean {
        val bool1: Boolean = input.testItem(0, itemIngredient)
        val bool2: Boolean = input.testFluid(0, fluidIngredient)
        return bool1 && bool2
    }

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.MIXING_SIMPLE

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.MIXING.get()

    override fun getRequiredCount(index: Int, stack: ImmutableItemStack): Int = when (index) {
        0 -> itemIngredient.getRequiredAmount(stack)
        else -> 0
    }

    override fun getRequiredAmount(index: Int, stack: ImmutableFluidStack): Int = when (index) {
        0 -> fluidIngredient.getRequiredAmount(stack)
        else -> 0
    }
}
