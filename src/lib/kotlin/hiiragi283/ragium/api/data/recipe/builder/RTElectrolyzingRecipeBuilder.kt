@file:OptIn(ExperimentalContracts::class)

package hiiragi283.ragium.api.data.recipe.builder

import hiiragi283.lib.collection.toNel
import hiiragi283.lib.data.recipe.builder.HTProgressRecipeBuilder
import hiiragi283.lib.data.recipe.ingredient.HTFluidIngredientBuilder
import hiiragi283.lib.data.recipe.result.HTFluidResultBuilder
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.recipe.result.HTFluidResult
import hiiragi283.lib.util.HTDelegates
import hiiragi283.ragium.api.RagiumConstants
import hiiragi283.ragium.api.recipe.RTElectrolyzingRecipe
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import net.minecraft.resources.Identifier
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

class RTElectrolyzingRecipeBuilder : HTProgressRecipeBuilder<RTElectrolyzingRecipe>(RagiumConstants.ELECTROLYZING) {
    override fun getRecipeId(): Identifier? = results.firstOrNull()?.getId()

    override fun createRecipe(): RTElectrolyzingRecipe =
        RTElectrolyzingRecipe(fluidIngredient, results.toNel(), progressData)

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
