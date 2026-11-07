@file:OptIn(ExperimentalContracts::class)

package hiiragi283.ragium.api.data.recipe.builder

import hiiragi283.lib.collection.toNel
import hiiragi283.lib.collection.toNelOrNull
import hiiragi283.lib.data.recipe.builder.HTItemCatalystAcceptor
import hiiragi283.lib.data.recipe.builder.HTItemOrFluidResultAcceptor
import hiiragi283.lib.data.recipe.builder.HTProgressRecipeBuilder
import hiiragi283.lib.data.recipe.ingredient.HTFluidIngredientBuilder
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.recipe.ingredient.HTItemCatalyst
import hiiragi283.lib.recipe.result.HTFluidResult
import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.lib.util.HTDelegates
import hiiragi283.lib.util.Ior
import hiiragi283.lib.util.toOptional
import hiiragi283.ragium.api.RagiumConstants
import hiiragi283.ragium.api.recipe.RTFluidReactingRecipe
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import net.minecraft.resources.Identifier
import java.util.Optional
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

class RTFluidReactingRecipeBuilder :
    HTProgressRecipeBuilder<RTFluidReactingRecipe>(RagiumConstants.REACTING),
    HTItemCatalystAcceptor,
    HTItemOrFluidResultAcceptor {
    override fun getRecipeId(): Identifier? = result.getId()

    override fun createRecipe(): RTFluidReactingRecipe = RTFluidReactingRecipe(ingredients.toNel(), catalyst, result, progressData)

    // Ingredients
    private val ingredients: MutableList<HTFluidIngredient> = ObjectArrayList()

    operator fun HTFluidIngredient.unaryPlus() {
        ingredients += this
    }

    inline fun ingredient(builderAction: HTFluidIngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        +HTFluidIngredientBuilder.build(builderAction)
    }

    // Catalyst
    @PublishedApi internal var catalyst: HTItemCatalyst by HTDelegates.onceInitialize(HTItemCatalyst::EMPTY)

    override operator fun HTItemCatalyst.unaryPlus() {
        catalyst = this
    }

    // Results
    @PublishedApi internal var itemResult: Optional<HTItemResult> by HTDelegates.optionalInitialize()

    @PublishedApi internal var fluidResults: MutableList<HTFluidResult> = ObjectArrayList()

    val result: RTFluidReactingRecipe.Result by lazy {
        Ior.fromNullable(fluidResults.toNelOrNull().toOptional(), itemResult).orElseThrow().let(RTFluidReactingRecipe::Result)
    }

    override operator fun HTItemResult.unaryPlus() {
        itemResult = Optional.of(this)
    }

    override operator fun HTFluidResult.unaryPlus() {
        fluidResults += this
    }
}
