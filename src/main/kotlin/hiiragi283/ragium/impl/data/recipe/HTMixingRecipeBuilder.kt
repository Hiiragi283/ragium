package hiiragi283.ragium.impl.data.recipe

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.recipe.HTRecipeBuilder
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTFluidResult
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.util.wrapOptional
import hiiragi283.ragium.impl.recipe.HTMixingRecipe
import net.minecraft.resources.ResourceLocation

class HTMixingRecipeBuilder : HTRecipeBuilder<HTMixingRecipeBuilder>(RagiumConst.MIXING) {
    companion object {
        @JvmStatic
        fun create(): HTMixingRecipeBuilder = HTMixingRecipeBuilder()
    }

    private val itemIngredients: MutableList<HTItemIngredient> = mutableListOf()
    private val fluidIngredients: MutableList<HTFluidIngredient> = mutableListOf()
    private var itemResult: HTItemResult? = null
    private var fluidResult: HTFluidResult? = null

    fun addIngredient(ingredient: HTItemIngredient): HTMixingRecipeBuilder = apply {
        check(itemIngredients.size <= 2) { "Item ingredients already initialized!" }
        itemIngredients.add(ingredient)
    }

    fun addIngredient(ingredient: HTFluidIngredient): HTMixingRecipeBuilder = apply {
        check(fluidIngredients.size <= 2) { "Fluid ingredients already initialized!" }
        fluidIngredients.add(ingredient)
    }

    fun setResult(result: HTItemResult): HTMixingRecipeBuilder = apply {
        check(this.itemResult == null) { "Item result already initialized!" }
        this.itemResult = result
    }

    fun setResult(result: HTFluidResult): HTMixingRecipeBuilder = apply {
        check(this.fluidResult == null) { "Fluid result already initialized!" }
        this.fluidResult = result
    }

    override fun createRecipe(): HTMixingRecipe = HTMixingRecipe(
        itemIngredients,
        fluidIngredients,
        itemResult.wrapOptional(),
        fluidResult.wrapOptional(),
    )

    override fun getPrimalId(): ResourceLocation = fluidResult?.id ?: itemResult?.id ?: error("Either item or fluid result required")
}
