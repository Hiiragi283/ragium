@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.data.recipe

import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.result.HTFluidResult
import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.lib.util.HTDelegates
import hiiragi283.lib.util.Ior
import hiiragi283.lib.util.Option
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import net.minecraft.resources.Identifier
import net.minecraft.world.item.crafting.Recipe

class HTItemOrFluidRecipeBuilder<out RECIPE : Recipe<*>>(prefix: String, private val factory: Factory<RECIPE>) : HTProgressRecipeBuilder<RECIPE>(prefix) {
    @PublishedApi internal var itemIngredient: Option<HTItemIngredient> by HTDelegates.onceInitialize { Option.none() }

    @PublishedApi internal var fluidIngredient: Option<HTFluidIngredient> by HTDelegates.onceInitialize { Option.none() }

    @PublishedApi internal var itemResult: Option<HTItemResult> by HTDelegates.onceInitialize { Option.none() }

    @PublishedApi internal var fluidResult: Option<HTFluidResult> by HTDelegates.onceInitialize { Option.none() }

    operator fun HTItemIngredient.unaryPlus() {
        itemIngredient = Option.some(this)
    }

    operator fun HTFluidIngredient.unaryPlus() {
        fluidIngredient = Option.some(this)
    }

    operator fun HTItemResult.unaryPlus() {
        itemResult = Option.some(this)
    }

    operator fun HTFluidResult.unaryPlus() {
        fluidResult = Option.some(this)
    }

    inline fun itemIngredient(builderAction: IngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        +IngredientBuilder().apply(builderAction).buildSized()
    }

    inline fun fluidIngredient(builderAction: FluidIngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        +FluidIngredientBuilder().apply(builderAction).buildSized()
    }

    inline fun itemResult(builderAction: HTItemResultBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        +HTItemResultBuilder().apply(builderAction).build()
    }

    inline fun fluidResult(builderAction: HTFluidResultBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        +HTFluidResultBuilder().apply(builderAction).build()
    }

    private fun toIorResult(): Ior<HTItemResult, HTFluidResult> = Ior.fromOption(itemResult, fluidResult).getOrNull() ?: error("Either item or fluid result required")

    override fun getPrimalId(): Identifier = toIorResult().swap().map(HTFluidResult::getId, HTItemResult::getId)

    override fun createRecipe(): RECIPE = factory.create(
        Ior.fromOption(itemIngredient, fluidIngredient).getOrNull() ?: error("Either item or fluid ingredient required"),
        toIorResult(),
        progressData,
    )

    //    Factory    //

    fun interface Factory<out RECIPE : Any> {
        fun create(ingredient: Ior<HTItemIngredient, HTFluidIngredient>, result: Ior<HTItemResult, HTFluidResult>, progressData: HTProgressData): RECIPE
    }
}
