@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.data.recipe.builder

import hiiragi283.lib.data.recipe.ingredient.HTItemIngredientBuilder
import hiiragi283.lib.data.recipe.result.HTItemResultBuilder
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.lib.util.HTDelegates
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import net.minecraft.world.item.crafting.Recipe
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.13
 */
class HTItemCombineRecipeBuilder<out RECIPE : Recipe<*>>(prefix: String, private val factory: Factory<RECIPE>) :
    HTProgressRecipeBuilder.SingleResult<RECIPE>(prefix) {
    override fun createRecipe(): RECIPE {
        val size: Int = ingredients.size
        check(size == 2) { "Requires 2 ingredients but given $size" }
        return factory.create(ingredients, result, progressData)
    }

    // Ingredients
    private val ingredients: MutableList<HTItemIngredient> = ObjectArrayList()

    operator fun HTItemIngredient.unaryPlus() {
        ingredients += this
    }

    inline fun ingredient(builderAction: HTItemIngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        +HTItemIngredientBuilder.build(builderAction)
    }

    // Result
    override var result: HTItemResult by HTDelegates.onceInitialize()

    operator fun HTItemResult.unaryPlus() {
        result = this
    }

    inline fun result(builderAction: HTItemResultBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        +HTItemResultBuilder.build(builderAction)
    }

    //    Factory    //

    /**
     * @author Hiiragi Tsubasa
     * @since 26.1.13
     */
    fun interface Factory<out RECIPE : Any> {
        fun create(ingredients: List<HTItemIngredient>, result: HTItemResult, progressData: HTProgressData): RECIPE =
            create(ingredients[0], ingredients[1], result, progressData)

        fun create(firstIngredient: HTItemIngredient, secondIngredient: HTItemIngredient, result: HTItemResult, progressData: HTProgressData): RECIPE
    }
}
