@file:OptIn(ExperimentalContracts::class)

package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.api.data.recipe.HTItemResultBuilder
import hiiragi283.core.api.data.recipe.HTProgressRecipeBuilder
import hiiragi283.core.api.data.recipe.IngredientBuilder
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.util.HTDelegates
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.recipe.HTPrintingRecipe
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Ingredient

class HTPrintingRecipeBuilder : HTProgressRecipeBuilder<HTPrintingRecipe>(RagiumConst.PRINTING) {
    companion object {
        @JvmStatic
        inline fun create(builderAction: HTPrintingRecipeBuilder.() -> Unit): HTPrintingRecipeBuilder {
            contract {
                callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
            }
            return HTPrintingRecipeBuilder().apply(builderAction)
        }
    }

    @PublishedApi internal var ingredient: HTItemIngredient by HTDelegates.onceInitialize()

    @PublishedApi internal var press: Ingredient by HTDelegates.onceInitialize()

    @PublishedApi internal var result: HTItemResult by HTDelegates.onceInitialize()

    operator fun HTItemIngredient.unaryPlus() {
        ingredient = this
    }

    operator fun Ingredient.unaryPlus() {
        press = this
    }

    operator fun HTItemResult.unaryPlus() {
        result = this
    }

    inline fun ingredient(builderAction: IngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        +IngredientBuilder().apply(builderAction).buildSized()
    }

    inline fun press(builderAction: IngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        +IngredientBuilder().apply(builderAction).build()
    }

    inline fun result(builderAction: HTItemResultBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        +HTItemResultBuilder().apply(builderAction).build()
    }

    override fun getPrimalId(): ResourceLocation = result.getId()

    override fun createRecipe(): HTPrintingRecipe = HTPrintingRecipe(ingredient, press, result, progressData)
}
