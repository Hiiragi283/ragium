@file:OptIn(ExperimentalContracts::class)

package hiiragi283.ragium.api.data.recipe.builder

import hiiragi283.lib.collection.toNelOrNull
import hiiragi283.lib.data.recipe.builder.HTProgressRecipeBuilder
import hiiragi283.lib.data.recipe.ingredient.HTFluidIngredientBuilder
import hiiragi283.lib.data.recipe.ingredient.HTItemIngredientBuilder
import hiiragi283.lib.data.recipe.ingredient.IngredientBuilder
import hiiragi283.lib.data.recipe.result.HTFluidResultBuilder
import hiiragi283.lib.data.recipe.result.HTItemResultBuilder
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.recipe.ingredient.HTItemCatalyst
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.result.HTFluidResult
import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.lib.util.HTDelegates
import hiiragi283.lib.util.Ior
import hiiragi283.ragium.api.RagiumConstants
import hiiragi283.ragium.api.recipe.RTReactingRecipe
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import net.minecraft.resources.Identifier
import net.minecraft.world.item.crafting.Ingredient
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

class RTReactingRecipeBuilder : HTProgressRecipeBuilder<RTReactingRecipe>(RagiumConstants.REACTING) {
    override fun getRecipeId(): Identifier? = result.getId()

    override fun createRecipe(): RTReactingRecipe = RTReactingRecipe(
        Ior.fromNullable(itemIngredients.toNelOrNull(), fluidIngredients.toNelOrNull())
            ?.let(RTReactingRecipe::Ingredients)
            ?: error("Either item or fluid ingredient required"),
        catalyst,
        result,
        progressData,
    )

    // Ingredients
    private val itemIngredients: MutableList<HTItemIngredient> = ObjectArrayList()
    private val fluidIngredients: MutableList<HTFluidIngredient> = ObjectArrayList()

    operator fun HTItemIngredient.unaryPlus() {
        itemIngredients += this
    }

    operator fun HTFluidIngredient.unaryPlus() {
        fluidIngredients += this
    }

    inline fun itemIngredient(builderAction: HTItemIngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        +HTItemIngredientBuilder.build(builderAction)
    }

    inline fun fluidIngredient(builderAction: HTFluidIngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        +HTFluidIngredientBuilder.build(builderAction)
    }

    // Catalyst
    @PublishedApi internal var catalyst: HTItemCatalyst by HTDelegates.onceInitialize(HTItemCatalyst::EMPTY)

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
    private val itemResults: MutableList<HTItemResult> = ObjectArrayList()
    private val fluidResults: MutableList<HTFluidResult> = ObjectArrayList()

    val result: RTReactingRecipe.Result by lazy {
        Ior.fromNullable(itemResults.toNelOrNull(), fluidResults.toNelOrNull())
            ?.let(RTReactingRecipe::Result)
            ?: error("Either item or fluid result required")
    }

    operator fun HTItemResult.unaryPlus() {
        itemResults += this
    }

    operator fun HTFluidResult.unaryPlus() {
        fluidResults += this
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
