@file:OptIn(ExperimentalContracts::class)

package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.api.data.recipe.HTItemResultBuilder
import hiiragi283.core.api.data.recipe.HTProgressRecipeBuilder
import hiiragi283.core.api.data.recipe.IngredientBuilder
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.progress.HTProgressData
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.util.HTDelegates
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.recipe.HTAlloyingRecipe
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe

class HTAlloyingRecipeBuilder<out RECIPE : Recipe<*>>(prefix: String, private val factory: Factory<RECIPE>) : HTProgressRecipeBuilder<RECIPE>(prefix) {
    companion object {
        @JvmStatic
        inline fun create(builderAction: HTAlloyingRecipeBuilder<HTAlloyingRecipe>.() -> Unit): HTAlloyingRecipeBuilder<HTAlloyingRecipe> {
            contract {
                callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
            }
            return HTAlloyingRecipeBuilder(RagiumConst.ALLOYING, ::HTAlloyingRecipe).apply(builderAction)
        }
    }

    @PublishedApi internal val ingredients: MutableList<HTItemIngredient> = mutableListOf()

    @PublishedApi internal var result: HTItemResult by HTDelegates.onceInitialize()

    operator fun HTItemIngredient.unaryPlus() {
        ingredients += this
    }

    inline fun ingredient(builderAction: IngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        ingredients += IngredientBuilder().apply(builderAction).buildSized()
    }

    inline fun result(builderAction: HTItemResultBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        result = HTItemResultBuilder().apply(builderAction).build()
    }

    override fun getPrimalId(): ResourceLocation = result.getId()

    override fun createRecipe(): RECIPE = factory.create(ingredients, result, progressData)

    //    Factory    //

    fun interface Factory<out RECIPE : Any> {
        fun create(ingredients: List<HTItemIngredient>, result: HTItemResult, progressData: HTProgressData): RECIPE
    }
}
