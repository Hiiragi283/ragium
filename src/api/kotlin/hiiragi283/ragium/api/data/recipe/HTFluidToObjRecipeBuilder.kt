package hiiragi283.ragium.api.data.recipe

import hiiragi283.ragium.api.recipe.HTFluidToObjRecipe
import hiiragi283.ragium.api.recipe.impl.HTRefiningRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.result.HTFluidResult
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.util.RagiumConst
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import java.util.*

class HTFluidToObjRecipeBuilder<R : HTFluidToObjRecipe>(
    prefix: String,
    private val factory: Factory<R>,
    private val ingredient: HTFluidIngredient,
    private val itemResult: Optional<HTItemResult>,
    private val fluidResults: List<HTFluidResult>,
) : HTRecipeBuilder.Prefixed(prefix) {
    companion object {
        @JvmStatic
        fun refining(
            ingredient: HTFluidIngredient,
            itemResult: HTItemResult?,
            vararg fluidResults: HTFluidResult,
        ): HTFluidToObjRecipeBuilder<HTRefiningRecipe> = HTFluidToObjRecipeBuilder(
            RagiumConst.REFINING,
            ::HTRefiningRecipe,
            ingredient,
            Optional.ofNullable(itemResult),
            listOf(*fluidResults),
        )
    }

    override fun getPrimalId(): ResourceLocation {
        val id: Optional<ResourceLocation> = itemResult.map(HTItemResult::id)
        return when {
            id.isPresent -> id.get()
            else -> fluidResults[0].id
        }
    }

    override fun createRecipe(): Recipe<*> = factory.create(ingredient, itemResult, fluidResults)

    fun interface Factory<R : HTFluidToObjRecipe> {
        fun create(ingredient: HTFluidIngredient, itemResult: Optional<HTItemResult>, fluidResults: List<HTFluidResult>): R
    }
}
