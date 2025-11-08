package hiiragi283.ragium.impl.data.recipe

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.recipe.HTRecipeBuilder
import hiiragi283.ragium.api.recipe.HTFluidTransformRecipe
import hiiragi283.ragium.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTFluidResult
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.impl.recipe.HTRefiningRecipe
import net.minecraft.resources.ResourceLocation
import java.util.*

class HTFluidTransformRecipeBuilder(
    prefix: String,
    private val factory: Factory<*>,
    private val fluidIngredient: HTFluidIngredient,
    private val itemIngredient: Optional<HTItemIngredient>,
    private val itemResult: Optional<HTItemResult>,
    private val fluidResult: Optional<HTFluidResult>,
) : HTRecipeBuilder.Prefixed(prefix) {
    companion object {
        @JvmStatic
        fun mixing(
            itemIngredient: HTItemIngredient,
            fluidIngredient: HTFluidIngredient,
            fluidResult: HTFluidResult,
        ): HTFluidTransformRecipeBuilder = HTFluidTransformRecipeBuilder(
            RagiumConst.MIXING,
            ::HTRefiningRecipe,
            fluidIngredient,
            Optional.of(itemIngredient),
            Optional.empty(),
            Optional.of(fluidResult),
        )

        @JvmStatic
        fun refining(
            fluidIngredient: HTFluidIngredient,
            fluidResult: HTFluidResult,
            itemIngredient: HTItemIngredient?,
            itemResult: HTItemResult?,
        ): HTFluidTransformRecipeBuilder = HTFluidTransformRecipeBuilder(
            RagiumConst.REFINING,
            ::HTRefiningRecipe,
            fluidIngredient,
            Optional.ofNullable(itemIngredient),
            Optional.ofNullable(itemResult),
            Optional.of(fluidResult),
        )

        @JvmStatic
        fun solidifying(
            itemIngredient: HTItemIngredient?,
            fluidIngredient: HTFluidIngredient,
            itemResult: HTItemResult,
        ): HTFluidTransformRecipeBuilder = HTFluidTransformRecipeBuilder(
            RagiumConst.SOLIDIFYING,
            ::HTRefiningRecipe,
            fluidIngredient,
            Optional.ofNullable(itemIngredient),
            Optional.of(itemResult),
            Optional.empty(),
        )
    }

    override fun getPrimalId(): ResourceLocation = when {
        fluidResult.isPresent -> fluidResult.get().id
        itemResult.isPresent -> itemResult.get().id
        else -> error("Either item or fluid result required!")
    }

    override fun createRecipe(): HTFluidTransformRecipe = factory.create(fluidIngredient, itemIngredient, itemResult, fluidResult)

    fun interface Factory<RECIPE : HTFluidTransformRecipe> {
        fun create(
            fluidIngredient: HTFluidIngredient,
            itemIngredient: Optional<HTItemIngredient>,
            itemResult: Optional<HTItemResult>,
            fluidResult: Optional<HTFluidResult>,
        ): RECIPE
    }
}
