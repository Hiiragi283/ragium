package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.api.HTBuilderMarker
import hiiragi283.core.api.data.recipe.builder.HTProcessingRecipeBuilder
import hiiragi283.core.api.function.identityLeft
import hiiragi283.core.api.monad.Ior
import hiiragi283.core.api.monad.toIorOrThrow
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.recipe.HTCanningRecipe
import hiiragi283.ragium.common.recipe.HTFreezingRecipe
import hiiragi283.ragium.common.recipe.HTMeltingRecipe
import hiiragi283.ragium.common.recipe.HTPyrolyzingRecipe
import hiiragi283.ragium.common.recipe.base.HTBasicItemOrFluidRecipe
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation

class HTItemOrFluidRecipeBuilder(prefix: String, private val factory: Factory<*>) : HTProcessingRecipeBuilder(prefix) {
    companion object {
        @HTBuilderMarker
        @JvmStatic
        inline fun canning(output: RecipeOutput, builderAction: HTItemOrFluidRecipeBuilder.() -> Unit) {
            HTItemOrFluidRecipeBuilder(RagiumConst.CANNING, ::HTCanningRecipe)
                .apply { time /= 2 }
                .apply(builderAction)
                .save(output)
        }

        @HTBuilderMarker
        @JvmStatic
        inline fun freezing(output: RecipeOutput, builderAction: HTItemOrFluidRecipeBuilder.() -> Unit) {
            HTItemOrFluidRecipeBuilder(RagiumConst.FREEZING, ::HTFreezingRecipe).apply(builderAction).save(output)
        }

        @HTBuilderMarker
        @JvmStatic
        inline fun melting(output: RecipeOutput, builderAction: HTItemOrFluidRecipeBuilder.() -> Unit) {
            HTItemOrFluidRecipeBuilder(RagiumConst.MELTING, ::HTMeltingRecipe).apply(builderAction).save(output)
        }

        @HTBuilderMarker
        @JvmStatic
        inline fun pyrolyzing(output: RecipeOutput, builderAction: HTItemOrFluidRecipeBuilder.() -> Unit) {
            HTItemOrFluidRecipeBuilder(RagiumConst.PYROLYZING, ::HTPyrolyzingRecipe)
                .apply { time *= 3 }
                .apply(builderAction)
                .save(output)
        }
    }

    val ingredient: IorHolder<HTItemIngredient, HTFluidIngredient> = IorHolder()
    val result: IorHolder<HTItemResult, HTFluidResult> = IorHolder()

    inner class IorHolder<ITEM : Any, FLUID : Any> {
        private var item: ITEM? = null
        private var fluid: FLUID? = null

        @JvmName("setItem")
        operator fun plusAssign(left: ITEM) {
            check(this.item == null) { "Item value has already initialized" }
            this.item = left
        }

        @JvmName("setFluid")
        operator fun plusAssign(right: FLUID) {
            check(this.fluid == null) { "Fluid value has already initialized" }
            this.fluid = right
        }

        fun toIor(): Ior<ITEM, FLUID> = (item to fluid).toIorOrThrow()
    }

    override fun getPrimalId(): ResourceLocation = result.toIor().map(HTItemResult::getId, HTFluidResult::getId, identityLeft())

    override fun createRecipe(): HTBasicItemOrFluidRecipe = factory.create(
        ingredient.toIor(),
        result.toIor(),
        time,
    )

    //    Factory    //

    fun interface Factory<RECIPE : HTBasicItemOrFluidRecipe> {
        fun create(ingredient: Ior<HTItemIngredient, HTFluidIngredient>, result: Ior<HTItemResult, HTFluidResult>, time: Int): RECIPE
    }
}
