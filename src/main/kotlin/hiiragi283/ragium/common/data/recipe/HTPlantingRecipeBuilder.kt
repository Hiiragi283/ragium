package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.impl.data.recipe.builder.HTMultiOutputRecipeBuilder
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.recipe.HTPlantingRecipe
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

    lateinit var plant: Ingredient
    lateinit var soil: Ingredient

    override fun createRecipe(): HTPlantingRecipe = HTPlantingRecipe(plant, soil, results, time)
}
