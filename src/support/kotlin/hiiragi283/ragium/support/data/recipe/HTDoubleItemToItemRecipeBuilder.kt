@file:OptIn(ExperimentalContracts::class)

package hiiragi283.ragium.support.data.recipe

import hiiragi283.core.api.data.recipe.HTItemResultBuilder
import hiiragi283.core.api.data.recipe.HTProgressRecipeBuilder
import hiiragi283.core.api.data.recipe.IngredientBuilder
import hiiragi283.core.api.recipe.base.HTProgressData
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.util.HTDelegates
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe

class HTDoubleItemToItemRecipeBuilder<out RECIPE : Recipe<*>>(prefix: String, private val factory: Factory<RECIPE>) : HTProgressRecipeBuilder<RECIPE>(prefix) {
    @PublishedApi internal var primary: HTItemIngredient by HTDelegates.onceInitialize()

    @PublishedApi internal var secondary: HTItemIngredient by HTDelegates.onceInitialize()

    @PublishedApi internal var result: HTItemResult by HTDelegates.onceInitialize()

    inline fun primary(builderAction: IngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        primary = IngredientBuilder().apply(builderAction).buildSized()
    }

    inline fun secondary(builderAction: IngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        secondary = IngredientBuilder().apply(builderAction).buildSized()
    }

    inline fun result(builderAction: HTItemResultBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        result = HTItemResultBuilder().apply(builderAction).build()
    }

    override fun getPrimalId(): ResourceLocation = result.getId()

    override fun createRecipe(): RECIPE = factory.create(primary, secondary, result, progressData)

    //    Factory    //

    fun interface Factory<out RECIPE : Any> {
        fun create(primary: HTItemIngredient, secondary: HTItemIngredient, result: HTItemResult, progressData: HTProgressData): RECIPE
    }
}
