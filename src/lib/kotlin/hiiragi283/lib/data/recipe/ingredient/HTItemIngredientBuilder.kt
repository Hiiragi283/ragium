@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.data.recipe.ingredient

import hiiragi283.lib.data.HolderAcceptor
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.util.HTBuilderMarker
import hiiragi283.lib.util.HTDelegates
import net.minecraft.core.HolderSet
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.common.crafting.ICustomIngredient
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * [Ingredient]および[HTItemIngredient]を作成するビルダークラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
@HTBuilderMarker
class HTItemIngredientBuilder @PublishedApi internal constructor() {
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

    private var ingredient: Ingredient by HTDelegates.onceInitialize()
    var count: Int = 1

    operator fun Ingredient.unaryPlus() {
        ingredient = this
    }

    operator fun ICustomIngredient.unaryPlus() {
        ingredient = this.toVanilla()
    }

    operator fun HolderSet<Item>.unaryPlus() {
        ingredient = Ingredient.of(this)
    }

    inline fun items(builderAction: HolderAcceptor.ItemSetBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        +HolderAcceptor.buildItemSet(builderAction)
    }

    fun build(): HTItemIngredient = HTItemIngredient(ingredient, count)
}
