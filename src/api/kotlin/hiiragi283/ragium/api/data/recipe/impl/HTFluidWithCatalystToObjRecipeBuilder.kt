package hiiragi283.ragium.api.data.recipe.impl

import hiiragi283.ragium.api.data.recipe.HTRecipeBuilder
import hiiragi283.ragium.api.recipe.HTFluidWithCatalystToObjRecipe
import hiiragi283.ragium.api.recipe.impl.HTSolidifyingRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.recipe.result.HTRecipeResult
import hiiragi283.ragium.api.util.RagiumConst
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import java.util.*

class HTFluidWithCatalystToObjRecipeBuilder<R1 : HTRecipeResult<*>, R2 : HTFluidWithCatalystToObjRecipe<R1>>(
    prefix: String,
    private val factory: Factory<R1, R2>,
    private val ingredient: HTFluidIngredient,
    private val catalyst: Optional<HTItemIngredient>,
    private val result: R1,
) : HTRecipeBuilder.Prefixed(prefix) {
    companion object {
        @JvmStatic
        fun solidifying(
            itemIngredient: HTItemIngredient?,
            fluidIngredient: HTFluidIngredient,
            result: HTItemResult,
        ): HTFluidWithCatalystToObjRecipeBuilder<HTItemResult, HTSolidifyingRecipe> = HTFluidWithCatalystToObjRecipeBuilder(
            RagiumConst.SOLIDIFYING,
            ::HTSolidifyingRecipe,
            fluidIngredient,
            Optional.ofNullable(itemIngredient),
            result,
        )
    }

    override fun getPrimalId(): ResourceLocation = result.id

    override fun createRecipe(): Recipe<*> = factory.create(ingredient, catalyst, result)

    fun interface Factory<R1 : HTRecipeResult<*>, R2 : HTFluidWithCatalystToObjRecipe<R1>> {
        fun create(ingredient: HTFluidIngredient, catalyst: Optional<HTItemIngredient>, result: R1): R2
    }
}
