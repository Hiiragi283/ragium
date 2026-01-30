package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.api.HTBuilderMarker
import hiiragi283.core.api.data.recipe.builder.HTProcessingRecipeBuilder
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.registry.HTItemHolderLike
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.recipe.HTPlantingRecipe
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.ItemLike

class HTPlantingRecipeBuilder : HTProcessingRecipeBuilder(RagiumConst.PLANTING) {
    companion object {
        @HTBuilderMarker
        @JvmStatic
        inline fun create(output: RecipeOutput, builderAction: HTPlantingRecipeBuilder.() -> Unit) {
            HTPlantingRecipeBuilder().apply(builderAction).save(output)
        }
    }

    lateinit var seed: ItemLike
    lateinit var soil: HTItemIngredient
    lateinit var crop: HTItemResult

    override fun getPrimalId(): ResourceLocation = crop.getId()

    override fun createRecipe(): HTPlantingRecipe = HTPlantingRecipe(
        HTItemHolderLike.of(seed),
        soil,
        crop,
        time,
        exp,
    )
}
