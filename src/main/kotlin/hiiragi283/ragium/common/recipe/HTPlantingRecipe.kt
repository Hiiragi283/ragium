package hiiragi283.ragium.common.recipe

import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.impl.recipe.HTBasicDoubleMultiOutputRecipe
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import java.util.Optional

class HTPlantingRecipe(
    base: HTItemIngredient,
    addition: Optional<HTItemIngredient>,
    results: List<HTItemResult>,
    time: Int,
) : HTBasicDoubleMultiOutputRecipe(base, addition, results, time) {
    companion object {
        @JvmField
        val OUTPUT_RANGE: IntRange = 1..4
    }

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.PLANTING

    override fun getType(): RecipeType<*> = RagiumRecipeTypes.PLANTING.get()
}
