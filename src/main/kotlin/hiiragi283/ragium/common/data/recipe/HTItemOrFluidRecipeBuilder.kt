package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.api.HTBuilderMarker
import hiiragi283.core.api.data.recipe.builder.HTProcessingRecipeBuilder
import hiiragi283.core.api.monad.Either
import hiiragi283.core.api.monad.Ior
import hiiragi283.core.api.monad.toIorOrThrow
import hiiragi283.core.api.recipe.HTProcessingRecipe
import hiiragi283.core.api.recipe.ingredient.HTFluidIngredient
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTFluidResult
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.recipe.HTFreezingRecipe
import hiiragi283.ragium.common.recipe.HTMeltingRecipe
import hiiragi283.ragium.common.recipe.base.HTItemOrFluidRecipe
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation

class HTItemOrFluidRecipeBuilder(prefix: String, private val factory: Factory<*>) : HTProcessingRecipeBuilder(prefix) {
    companion object {
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
    }

    val ingredient: IorHolder = IorHolder()
    val result: EitherHolder<HTItemResult, HTFluidResult> = EitherHolder()

    inner class IorHolder {
        private var item: HTItemIngredient? = null
        private var fluid: HTFluidIngredient? = null

        @JvmName("setItem")
        operator fun plusAssign(left: HTItemIngredient) {
            check(this.item == null) { "Left value has already initialized" }
            this.item = left
        }

        @JvmName("setFluid")
        operator fun plusAssign(right: HTFluidIngredient) {
            check(this.fluid == null) { "Right value has already initialized" }
            this.fluid = right
        }

        fun toIor(): Ior<HTItemIngredient, HTFluidIngredient> = (item to fluid).toIorOrThrow()
    }

    inner class EitherHolder<A, B> {
        lateinit var content: Either<A, B>
            private set

        @JvmName("setLeft")
        operator fun plusAssign(left: A) {
            check(!::content.isInitialized) { "Either has already initialized" }
            this.content = Either.Left(left)
        }

        @JvmName("setRight")
        operator fun plusAssign(right: B) {
            check(!::content.isInitialized) { "Either has already initialized" }
            this.content = Either.Right(right)
        }
    }

    override fun getPrimalId(): ResourceLocation = result.content.map(HTItemResult::getId, HTFluidResult::getId)

    override fun createRecipe(): HTItemOrFluidRecipe = factory.create(
        ingredient.toIor(),
        result.content,
        subParameters(),
    )

    //    Factory    //

    fun interface Factory<RECIPE : HTItemOrFluidRecipe> {
        fun create(
            ingredient: Ior<HTItemIngredient, HTFluidIngredient>,
            result: Either<HTItemResult, HTFluidResult>,
            parameters: HTProcessingRecipe.SubParameters,
        ): RECIPE
    }
}
