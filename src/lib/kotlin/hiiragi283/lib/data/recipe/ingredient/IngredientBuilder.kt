@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.data.recipe.ingredient

import hiiragi283.lib.data.HolderAcceptor
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
 * [Ingredient]を作成するビルダークラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.7
 */
@HTBuilderMarker
abstract class IngredientBuilder {
    companion object {
        @JvmStatic
        inline fun build(builderAction: IngredientBuilder.() -> Unit): Ingredient {
            contract {
                callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
            }
            return Impl().apply(builderAction).build()
        }
    }

    protected var ingredient: Ingredient by HTDelegates.onceInitialize()
        private set

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

    @PublishedApi internal class Impl : IngredientBuilder() {
        fun build(): Ingredient = ingredient
    }
}
