@file:OptIn(ExperimentalContracts::class)

package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.api.data.recipe.HTItemResultBuilder
import hiiragi283.core.api.data.recipe.HTProgressRecipeBuilder
import hiiragi283.core.api.data.recipe.IngredientBuilder
import hiiragi283.core.api.recipe.base.HTProgressData
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTItemResult
import hiiragi283.core.api.util.HTDelegates
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.recipe.HTAlloyingRecipe
import hiiragi283.ragium.common.recipe.HTAssemblingRecipe
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe

class HTCombiningRecipeBuilder<out RECIPE : Recipe<*>>(prefix: String, private val factory: Factory<RECIPE>) : HTProgressRecipeBuilder<RECIPE>(prefix) {
    companion object {
        @JvmStatic
        inline fun alloying(builderAction: HTCombiningRecipeBuilder<HTAlloyingRecipe>.() -> Unit): HTCombiningRecipeBuilder<HTAlloyingRecipe> {
            contract {
                callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
            }
            return HTCombiningRecipeBuilder(RagiumConst.ALLOYING, ::HTAlloyingRecipe).apply(builderAction)
        }

        @JvmStatic
        inline fun assembling(builderAction: HTCombiningRecipeBuilder<HTAssemblingRecipe>.() -> Unit): HTCombiningRecipeBuilder<HTAssemblingRecipe> {
            contract {
                callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
            }
            return HTCombiningRecipeBuilder(RagiumConst.ASSEMBLING, ::HTAssemblingRecipe).apply(builderAction)
        }
    }

    @PublishedApi internal val ingredients: MutableList<HTItemIngredient> = mutableListOf()

    @PublishedApi internal var result: HTItemResult by HTDelegates.onceInitialize()

    operator fun HTItemIngredient.unaryPlus() {
        ingredients += this
    }

    operator fun HTItemResult.unaryPlus() {
        result = this
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
