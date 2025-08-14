package hiiragi283.ragium.api.data.recipe.impl

import hiiragi283.ragium.api.data.recipe.HTRecipeBuilder
import hiiragi283.ragium.api.recipe.HTItemWithFluidToObjRecipe
import hiiragi283.ragium.api.recipe.impl.HTInfusingRecipe
import hiiragi283.ragium.api.recipe.impl.HTMixingRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTFluidResult
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.recipe.result.HTRecipeResult
import hiiragi283.ragium.api.util.RagiumConst
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import java.util.*

class HTItemWithFluidToObjRecipeBuilder<R1 : HTRecipeResult<*, *>, R2 : HTItemWithFluidToObjRecipe<R1>>(
    prefix: String,
    private val factory: Factory<R1, R2>,
    private val itemIngredient: Optional<HTItemIngredient>,
    private val fluidIngredient: Optional<HTFluidIngredient>,
    private val result: R1,
) : HTRecipeBuilder.Prefixed(prefix) {
    companion object {
        @JvmStatic
        fun infusing(
            itemIngredient: HTItemIngredient,
            fluidIngredient: HTFluidIngredient,
            result: HTItemResult,
        ): HTItemWithFluidToObjRecipeBuilder<HTItemResult, HTInfusingRecipe> = HTItemWithFluidToObjRecipeBuilder(
            RagiumConst.INFUSING,
            ::HTInfusingRecipe,
            Optional.of(itemIngredient),
            Optional.of(fluidIngredient),
            result,
        )

        @JvmStatic
        fun mixing(
            itemIngredient: HTItemIngredient,
            fluidIngredient: HTFluidIngredient,
            result: HTFluidResult,
        ): HTItemWithFluidToObjRecipeBuilder<HTFluidResult, HTMixingRecipe> = HTItemWithFluidToObjRecipeBuilder(
            RagiumConst.MELTING,
            ::HTMixingRecipe,
            Optional.of(itemIngredient),
            Optional.of(fluidIngredient),
            result,
        )
    }

    override fun getPrimalId(): ResourceLocation = result.id

    override fun createRecipe(): Recipe<*> = factory.create(itemIngredient, fluidIngredient, result)

    fun interface Factory<R1 : HTRecipeResult<*, *>, R2 : HTItemWithFluidToObjRecipe<R1>> {
        fun create(itemIngredient: Optional<HTItemIngredient>, fluidIngredient: Optional<HTFluidIngredient>, result: R1): R2
    }
}
