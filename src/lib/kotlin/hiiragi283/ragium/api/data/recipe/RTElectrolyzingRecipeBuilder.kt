@file:OptIn(ExperimentalContracts::class)

package hiiragi283.ragium.api.data.recipe

import hiiragi283.lib.data.recipe.FluidIngredientBuilder
import hiiragi283.lib.data.recipe.HTFluidResultBuilder
import hiiragi283.lib.data.recipe.HTProgressRecipeBuilder
import hiiragi283.lib.data.recipe.IngredientBuilder
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.result.HTFluidResult
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
    override fun getPrimalId(): Identifier = results.first().getId()

    override fun createRecipe(): RTElectrolyzingRecipe =
        RTElectrolyzingRecipe(itemIngredient, fluidIngredient, results, progressData)

    // Ingredient
    @PublishedApi internal var itemIngredient: Optional<HTItemIngredient> by HTDelegates.onceInitialize {
        Optional.empty()
    }

    @PublishedApi internal var fluidIngredient: HTFluidIngredient by HTDelegates.onceInitialize()

    operator fun HTItemIngredient.unaryPlus() {
        itemIngredient = Optional.of(this)
    }

    operator fun HTFluidIngredient.unaryPlus() {
        fluidIngredient = this
    }

    inline fun itemIngredient(builderAction: IngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        +IngredientBuilder.buildSized(builderAction)
    }

    inline fun fluidIngredient(builderAction: FluidIngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        +FluidIngredientBuilder.buildSized(builderAction)
    }

    // Result
    @PublishedApi internal var results: MutableList<HTFluidResult> = ObjectArrayList()

    operator fun HTFluidResult.unaryPlus() {
        results += this
    }

    inline fun result(builderAction: HTFluidResultBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        +HTFluidResultBuilder.build(builderAction)
    }
}
