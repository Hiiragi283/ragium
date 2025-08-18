package hiiragi283.ragium.api.data.recipe.impl

import hiiragi283.ragium.api.data.recipe.HTRecipeBuilder
import hiiragi283.ragium.api.recipe.HTItemToChancedItemRecipe
import hiiragi283.ragium.api.recipe.impl.HTCrushingRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.util.RagiumConst
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe

class HTItemToChancedItemRecipeBuilder<R : HTItemToChancedItemRecipe>(
    prefix: String,
    private val factory: Factory<R>,
    val ingredient: HTItemIngredient,
) : HTRecipeBuilder.Prefixed(prefix) {
    companion object {
        @JvmStatic
        fun crushing(ingredient: HTItemIngredient): HTItemToChancedItemRecipeBuilder<HTCrushingRecipe> =
            HTItemToChancedItemRecipeBuilder(RagiumConst.CRUSHING, ::HTCrushingRecipe, ingredient)
    }

    private val results: MutableList<HTItemToChancedItemRecipe.ChancedResult> = mutableListOf()

    fun addResult(result: HTItemResult, chance: Float = 1f): HTItemToChancedItemRecipeBuilder<R> = apply {
        check(chance in (0f..1f)) { "Chance of result must be within 0f to 1f!" }
        this.results.add(HTItemToChancedItemRecipe.ChancedResult(result, chance))
    }

    override fun getPrimalId(): ResourceLocation = results[0].id

    override fun createRecipe(): Recipe<*> = factory.create(ingredient, results)

    fun interface Factory<R : HTItemToChancedItemRecipe> {
        fun create(ingredient: HTItemIngredient, results: List<HTItemToChancedItemRecipe.ChancedResult>): R
    }
}
