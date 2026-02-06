package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.api.data.recipe.builder.HTProcessingRecipeBuilder
import hiiragi283.core.api.recipe.result.HTChancedItemResult
import hiiragi283.core.api.recipe.result.HTItemResult
import net.minecraft.resources.ResourceLocation

abstract class HTChancedRecipeBuilder(prefix: String) : HTProcessingRecipeBuilder(prefix) {
    lateinit var result: HTItemResult
    val extraResults: MutableList<HTChancedItemResult> = mutableListOf()

    final override fun getPrimalId(): ResourceLocation = result.getId()
}
