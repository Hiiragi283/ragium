@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.data.recipe

import hiiragi283.lib.data.HolderAcceptor
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.util.HTBuilderMarker
import hiiragi283.lib.util.HTDelegates
import net.minecraft.core.HolderSet
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.common.crafting.CompoundIngredient
import net.neoforged.neoforge.common.crafting.ICustomIngredient
import net.neoforged.neoforge.registries.holdersets.OrHolderSet
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * [Ingredient]および[HTItemIngredient]を作成するビルダークラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
@HTBuilderMarker
class IngredientBuilder @PublishedApi internal constructor() {
    companion object {
        @JvmStatic
        inline fun build(builderAction: IngredientBuilder.() -> Unit): Ingredient {
            contract {
                callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
            }
            return IngredientBuilder().apply(builderAction).build()
        }

        @JvmStatic
        inline fun buildSized(builderAction: IngredientBuilder.() -> Unit): HTItemIngredient {
            contract {
                callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
            }
            return IngredientBuilder().apply(builderAction).buildSized()
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

    @JvmName("unaryPlusCompound")
    operator fun List<Ingredient>.unaryPlus() {
        +CompoundIngredient(this)
    }

    operator fun HolderSet<Item>.unaryPlus() {
        ingredient = Ingredient.of(this)
    }

    @JvmName("unaryPlusOr")
    operator fun List<HolderSet<Item>>.unaryPlus() {
        +OrHolderSet(this)
    }

    inline fun items(builderAction: HolderAcceptor.ItemSetBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        +HolderAcceptor.ItemSetBuilder().apply(builderAction).build()
    }

    fun build(): Ingredient = ingredient

    fun buildSized(): HTItemIngredient = HTItemIngredient(ingredient, count)
}
