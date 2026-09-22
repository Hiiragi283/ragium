@file:OptIn(ExperimentalContracts::class)

package hiiragi283.ragium.api.data.recipe.builder

import hiiragi283.lib.data.recipe.builder.HTProgressRecipeBuilder
import hiiragi283.lib.data.recipe.ingredient.HTBiomeConditionBuilder
import hiiragi283.lib.data.recipe.result.HTFluidResultBuilder
import hiiragi283.lib.recipe.ingredient.HTBiomeCondition
import hiiragi283.lib.recipe.result.HTFluidResult
import hiiragi283.lib.util.HTDelegates
import hiiragi283.ragium.api.RagiumConstants
import hiiragi283.ragium.api.recipe.RTResourceExtractingRecipe
import net.minecraft.resources.Identifier
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

class RTResourceExtractingRecipeBuilder :
    HTProgressRecipeBuilder<RTResourceExtractingRecipe>(RagiumConstants.RESOURCE_EXTRACTING) {
    override fun getRecipeId(): Identifier? = result.getId()

    override fun createRecipe(): RTResourceExtractingRecipe =
        RTResourceExtractingRecipe(condition, result, progressData)

    // Biome Condition
    var condition: HTBiomeCondition by HTDelegates.onceInitialize()

    operator fun HTBiomeCondition.unaryPlus() {
        condition = this
    }

    inline fun biomes(builderAction: HTBiomeConditionBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        +HTBiomeConditionBuilder.build(builderAction)
    }

    // Result
    var result: HTFluidResult by HTDelegates.onceInitialize()

    operator fun HTFluidResult.unaryPlus() {
        result = this
    }

    inline fun result(builderAction: HTFluidResultBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        +HTFluidResultBuilder.build(builderAction)
    }
}
