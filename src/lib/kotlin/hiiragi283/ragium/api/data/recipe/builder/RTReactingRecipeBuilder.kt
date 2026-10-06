@file:OptIn(ExperimentalContracts::class)

package hiiragi283.ragium.api.data.recipe.builder

import hiiragi283.lib.collection.toNel
import hiiragi283.lib.data.recipe.builder.HTProgressRecipeBuilder
import hiiragi283.lib.data.recipe.ingredient.HTFluidIngredientBuilder
import hiiragi283.lib.data.recipe.ingredient.IngredientBuilder
import hiiragi283.lib.data.recipe.result.HTFluidResultBuilder
import hiiragi283.lib.data.recipe.result.HTItemResultBuilder
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.recipe.ingredient.HTItemCatalyst
import hiiragi283.lib.recipe.result.HTFluidResult
import hiiragi283.lib.recipe.result.HTItemOrFluidResult
import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.lib.util.HTDelegates
import hiiragi283.lib.util.Ior
import hiiragi283.ragium.api.RagiumConstants
import hiiragi283.ragium.api.recipe.RTReactingRecipe
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import net.minecraft.resources.Identifier
import net.minecraft.world.item.crafting.Ingredient
import java.util.Optional
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

class RTReactingRecipeBuilder : HTProgressRecipeBuilder<RTReactingRecipe>(RagiumConstants.REACTING) {
    override fun getRecipeId(): Identifier? = result.getId()

    override fun createRecipe(): RTReactingRecipe =
        RTReactingRecipe(fluidIngredients.toNel(), catalyst, result, progressData)

    // Ingredients
    private val fluidIngredients: MutableList<HTFluidIngredient> = ObjectArrayList()

    operator fun HTFluidIngredient.unaryPlus() {
        fluidIngredients += this
    }

    inline fun ingredient(builderAction: HTFluidIngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        +HTFluidIngredientBuilder.build(builderAction)
    }

    // Catalyst
    var catalyst: HTItemCatalyst by HTDelegates.onceInitialize(HTItemCatalyst::EMPTY)

    operator fun HTItemCatalyst.unaryPlus() {
        catalyst = this
    }

    operator fun Ingredient.unaryPlus() {
        +HTItemCatalyst(this)
    }

    inline fun catalyst(builderAction: IngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        +IngredientBuilder.build(builderAction)
    }

    // Results
    var itemResult: Optional<HTItemResult> by HTDelegates.optionalInitialize()

    var fluidResult: Optional<HTFluidResult> by HTDelegates.optionalInitialize()

    val result: HTItemOrFluidResult get() = Ior.fromNullable(itemResult, fluidResult)
        .map(::HTItemOrFluidResult)
        .orElseThrow { error("Either item or fluid result required") }

    operator fun HTItemResult.unaryPlus() {
        itemResult = Optional.of(this)
    }

    operator fun HTFluidResult.unaryPlus() {
        fluidResult = Optional.of(this)
    }

    inline fun itemResult(builderAction: HTItemResultBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        +HTItemResultBuilder.build(builderAction)
    }

    inline fun fluidResult(builderAction: HTFluidResultBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        +HTFluidResultBuilder.build(builderAction)
    }
}
