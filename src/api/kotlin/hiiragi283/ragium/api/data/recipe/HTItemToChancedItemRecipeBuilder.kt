package hiiragi283.ragium.api.data.recipe

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

    private val results: MutableList<HTItemResult> = mutableListOf()
    private val chances: MutableList<Float> = mutableListOf()

    fun addResult(result: HTItemResult, chance: Float = 1f): HTItemToChancedItemRecipeBuilder<R> = apply {
        this.results.add(result)
        this.chances.add(chance)
    }

    override fun getPrimalId(): ResourceLocation = results[0].id

    override fun createRecipe(): Recipe<*> = factory.create(ingredient, results, chances)

    fun interface Factory<R : HTItemToChancedItemRecipe> {
        fun create(ingredient: HTItemIngredient, results: List<HTItemResult>, chances: List<Float>): R
    }
}
