@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.data.recipe

import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.lib.util.HTDelegates
import net.minecraft.resources.Identifier
import net.minecraft.world.item.crafting.Recipe
import java.util.Optional
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * 1種類のアイテムから2種類のアイテムを作成するレシピ向けの[HTProgressRecipeBuilder]の実装クラスです。
 * @param RECIPE 生成するレシピのクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
class HTItemToDoubleItemRecipeBuilder<out RECIPE : Recipe<*>>(prefix: String, private val factory: Factory<RECIPE>) :
    HTProgressRecipeBuilder<RECIPE>(prefix) {
    override fun getPrimalId(): Identifier = primary.getId()

    override fun createRecipe(): RECIPE = factory.create(ingredient, primary, secondary, progressData)

    // Ingredient
    @PublishedApi internal var ingredient: HTItemIngredient by HTDelegates.onceInitialize()

    operator fun HTItemIngredient.unaryPlus() {
        ingredient = this
    }

    inline fun ingredient(builderAction: IngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        +IngredientBuilder.buildSized(builderAction)
    }

    // Result
    @PublishedApi internal var primary: HTItemResult by HTDelegates.onceInitialize()

    @PublishedApi internal var secondary: Optional<HTItemResult> by HTDelegates.onceInitialize { Optional.empty() }

    inline fun primary(builderAction: HTItemResultBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        primary = HTItemResultBuilder.build(builderAction)
    }

    inline fun secondary(builderAction: HTItemResultBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        secondary = Optional.of(HTItemResultBuilder.build(builderAction))
    }

    //    Factory    //

    /**
     * @param RECIPE 生成するレシピのクラス
     * @author Hiiragi Tsubasa
     * @since 26.1.0
     */
    fun interface Factory<out RECIPE : Any> {
        fun create(
            ingredient: HTItemIngredient,
            primary: HTItemResult,
            secondary: Optional<HTItemResult>,
            progressData: HTProgressData
        ): RECIPE
    }
}
