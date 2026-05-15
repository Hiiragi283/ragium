package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.api.util.HTDelegates
import hiiragi283.core.impl.data.recipe.builder.HTMultiOutputRecipeBuilder
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.recipe.RTPlantingRecipe
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.world.item.crafting.Ingredient

class HTPlantingRecipeBuilder : HTMultiOutputRecipeBuilder(RagiumConst.PLANTING) {
    companion object {
        @JvmStatic
        inline fun create(output: RecipeOutput, builderAction: HTPlantingRecipeBuilder.() -> Unit) {
            HTPlantingRecipeBuilder().apply(builderAction).save(output)
        }
    }

    init {
        time /= 2
    }

    var plant: Ingredient by HTDelegates.onceInitialize()
    var soil: Ingredient by HTDelegates.onceInitialize()

    override fun createRecipe(): RTPlantingRecipe = RTPlantingRecipe(plant, soil, createList(), progressData)
}
