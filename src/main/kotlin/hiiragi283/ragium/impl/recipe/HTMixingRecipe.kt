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

class HTMixingRecipe(
    val itemIngredients: List<HTItemIngredient>,
    val fluidIngredients: List<HTFluidIngredient>,
    results: HTComplexResult,
) : HTBasicComplexRecipe(results) {
    override fun matches(input: HTRecipeInput, level: Level): Boolean {
        val bool1: Boolean = HTRecipeInput.hasMatchingSlots(itemIngredients, input.items)
        val bool2: Boolean = HTRecipeInput.hasMatchingSlots(fluidIngredients, input.fluids)
        return bool1 && bool2
    }

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.MIXING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.MIXING.get()

    override fun getRequiredCount(index: Int, stack: ImmutableItemStack): Int = itemIngredients[index].getRequiredAmount(stack)

    override fun getRequiredAmount(index: Int, stack: ImmutableFluidStack): Int = fluidIngredients[index].getRequiredAmount(stack)
}
