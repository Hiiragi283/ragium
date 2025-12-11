package hiiragi283.ragium.common.recipe

import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.input.HTRecipeInput
import hiiragi283.ragium.api.recipe.result.HTComplexResult
import hiiragi283.ragium.common.recipe.base.HTBasicComplexRecipe
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level

class HTRefiningRecipe(val ingredient: HTFluidIngredient, results: HTComplexResult) : HTBasicComplexRecipe(results) {
    override fun matches(input: HTRecipeInput, level: Level): Boolean = input.testFluid(0, ingredient)

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.REFINING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.REFINING.get()

    override fun getRequiredCount(index: Int): Int = 0

    override fun getRequiredAmount(index: Int): Int = when (index) {
        0 -> ingredient.getRequiredAmount()
        else -> 0
    }
}
