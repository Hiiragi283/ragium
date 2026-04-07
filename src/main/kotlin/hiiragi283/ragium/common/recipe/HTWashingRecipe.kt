package hiiragi283.ragium.common.recipe

import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.ragium.impl.recipe.HTBasicItemFluidMultiOutputRecipe
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType

class HTWashingRecipe(
    itemIngredient: HTItemIngredient,
    fluidIngredient: HTFluidIngredient,
    results: List<HTItemResult>,
    time: Int,
) : HTBasicItemFluidMultiOutputRecipe(itemIngredient, fluidIngredient, results, time) {
    companion object {
        @JvmField
        val OUTPUT_RANGE: IntRange = 1..4
    }

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.WASHING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.WASHING.get()
}
