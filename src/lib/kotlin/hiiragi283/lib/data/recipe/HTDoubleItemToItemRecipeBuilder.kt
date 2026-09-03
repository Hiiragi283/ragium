@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.data.recipe

import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.lib.util.HTDelegates
import net.minecraft.resources.Identifier
import net.minecraft.world.item.crafting.Recipe
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * 2種類のアイテムから1種類のアイテムを作成するレシピ向けの[HTProgressRecipeBuilder]の実装クラスです。
 * @param RECIPE 生成するレシピのクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
class HTDoubleItemToItemRecipeBuilder<out RECIPE : Recipe<*>>(prefix: String, private val factory: Factory<RECIPE>) :
    HTProgressRecipeBuilder<RECIPE>(prefix) {
    override fun getPrimalId(): Identifier = result.getId()

    override fun createRecipe(): RECIPE = factory.create(primary, secondary, result, progressData)

    // Ingredient
    @PublishedApi internal var primary: HTItemIngredient by HTDelegates.onceInitialize()

    @PublishedApi internal var secondary: HTItemIngredient by HTDelegates.onceInitialize()

    inline fun primary(builderAction: IngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        primary = IngredientBuilder.buildSized(builderAction)
    }

    inline fun secondary(builderAction: IngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        secondary = IngredientBuilder.buildSized(builderAction)
    }

    // Result
    @PublishedApi internal var result: HTItemResult by HTDelegates.onceInitialize()

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
     * @param RECIPE 生成するレシピのクラス
     * @author Hiiragi Tsubasa
     * @since 26.1.0
     */
    fun interface Factory<out RECIPE : Any> {
        fun create(
            primary: HTItemIngredient,
            secondary: HTItemIngredient,
            result: HTItemResult,
            progressData: HTProgressData
        ): RECIPE
    }
}
