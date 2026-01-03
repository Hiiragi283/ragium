package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.api.data.recipe.builder.HTProcessingRecipeBuilder
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.storage.item.HTItemResourceType
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.recipe.HTPlantingRecipe
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.ItemLike

class HTPlantingRecipeBuilder(private val seed: HTItemResourceType, private val soil: HTItemIngredient, private val crop: HTItemResult) :
    HTProcessingRecipeBuilder<HTPlantingRecipeBuilder>(RagiumConst.PLANTING) {
    companion object {
        @JvmStatic
        fun create(seed: ItemLike, soil: HTItemIngredient, crop: HTItemResult): HTPlantingRecipeBuilder =
            create(HTItemResourceType.of(seed), soil, crop)

        @JvmStatic
        fun create(seed: HTItemResourceType, soil: HTItemIngredient, crop: HTItemResult): HTPlantingRecipeBuilder =
            HTPlantingRecipeBuilder(seed, soil, crop)
    }

    override fun getPrimalId(): ResourceLocation = crop.getId()

    override fun createRecipe(): HTPlantingRecipe = HTPlantingRecipe(
        seed,
        soil,
        crop,
        time,
        exp,
    )
}
