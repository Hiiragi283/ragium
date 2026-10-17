@file:OptIn(ExperimentalContracts::class)

package hiiragi283.ragium.api.data.recipe.builder

import hiiragi283.lib.data.recipe.builder.HTProgressRecipeBuilder
import hiiragi283.lib.data.recipe.ingredient.HTFluidIngredientBuilder
import hiiragi283.lib.data.recipe.result.HTFluidResultBuilder
import hiiragi283.lib.data.recipe.result.HTItemResultBuilder
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.recipe.result.HTFluidResult
import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.lib.util.HTDelegates
import hiiragi283.ragium.api.RagiumConstants
import hiiragi283.ragium.api.recipe.RTElectrolyzingRecipe
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import net.minecraft.resources.Identifier
import java.util.Optional
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

class RTElectrolyzingRecipeBuilder : HTProgressRecipeBuilder<RTElectrolyzingRecipe>(RagiumConstants.ELECTROLYZING) {
    override fun getRecipeId(): Identifier? = null

    override fun createRecipe(): RTElectrolyzingRecipe = RTElectrolyzingRecipe(
        fluidIngredient,
        RTElectrolyzingRecipe.Result(
            fluidResults[0],
            fluidResults[1],
            itemResult
        ),
        progressData
    )

    // Ingredient
    @PublishedApi internal var fluidIngredient: HTFluidIngredient by HTDelegates.onceInitialize()

    operator fun HTFluidIngredient.unaryPlus() {
        fluidIngredient = this
    }

    inline fun ingredient(builderAction: HTFluidIngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        +HTFluidIngredientBuilder.build(builderAction)
    }

    // Result
    @PublishedApi internal var itemResult: Optional<HTItemResult> by HTDelegates.optionalInitialize()

    @PublishedApi internal var fluidResults: MutableList<HTFluidResult> = ObjectArrayList()

    operator fun HTItemResult.unaryPlus() {
        itemResult = Optional.of(this)
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
