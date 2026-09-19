@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.data.recipe.ingredient

import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.util.HTBuilderMarker
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * [HTItemIngredient]を作成するビルダークラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
@HTBuilderMarker
class HTItemIngredientBuilder @PublishedApi internal constructor() : IngredientBuilder() {
    companion object {
        /**
         * @since 26.1.4
         */
        @JvmStatic
        inline fun build(builderAction: HTItemIngredientBuilder.() -> Unit): HTItemIngredient {
            contract {
                callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
            }
            return HTItemIngredientBuilder().apply(builderAction).build()
        }
    }

    var count: Int = 1

    fun build(): HTItemIngredient = HTItemIngredient(ingredient, count)
}
