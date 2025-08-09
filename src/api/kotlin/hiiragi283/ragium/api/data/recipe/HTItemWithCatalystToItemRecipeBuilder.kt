package hiiragi283.ragium.api.data.recipe

import hiiragi283.ragium.api.recipe.base.HTItemWithCatalystToItemRecipe
import hiiragi283.ragium.api.recipe.impl.HTPressingRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.util.RagiumConst
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import java.util.*

class HTItemWithCatalystToItemRecipeBuilder<R : HTItemWithCatalystToItemRecipe>(
    prefix: String,
    private val factory: Factory<R>,
    private val ingredient: HTItemIngredient,
    private val catalyst: Optional<HTItemIngredient>,
    private val result: HTItemResult,
) : HTRecipeBuilder.Prefixed(prefix) {
    companion object {
        @JvmStatic
        fun pressing(
            ingredient: HTItemIngredient,
            catalyst: HTItemIngredient?,
            result: HTItemResult,
        ): HTItemWithCatalystToItemRecipeBuilder<HTPressingRecipe> = HTItemWithCatalystToItemRecipeBuilder(
            RagiumConst.PRESSING,
            ::HTPressingRecipe,
            ingredient,
            Optional.ofNullable(catalyst),
            result,
        )
    }

    override fun getPrimalId(): ResourceLocation = result.id

    override fun createRecipe(): Recipe<*> = factory.create(ingredient, catalyst, result)

    fun interface Factory<R : HTItemWithCatalystToItemRecipe> {
        fun create(ingredient: HTItemIngredient, catalyst: Optional<HTItemIngredient>, result: HTItemResult): R
    }
}
