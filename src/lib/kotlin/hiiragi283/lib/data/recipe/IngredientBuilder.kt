@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.data.recipe

import hiiragi283.lib.data.HolderAcceptor
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.util.Either
import hiiragi283.lib.util.HTBuilderMarker
import hiiragi283.lib.util.HTDelegates
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import net.minecraft.core.HolderSet
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.common.crafting.CompoundIngredient
import net.neoforged.neoforge.common.crafting.ICustomIngredient
import net.neoforged.neoforge.registries.holdersets.OrHolderSet

/**
 * [Ingredient]および[HTItemIngredient]を作成するビルダークラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.1
 */
@HTBuilderMarker
class IngredientBuilder {
    private var contents: Either<ICustomIngredient, HolderSet<Item>> by HTDelegates.onceInitialize()
    var count: Int = 1

    operator fun ICustomIngredient.unaryPlus() {
        contents = Either.Left(this)
    }

    @JvmName("unaryPlusCompound")
    operator fun List<Ingredient>.unaryPlus() {
        +CompoundIngredient(this)
    }

    operator fun HolderSet<Item>.unaryPlus() {
        contents = Either.Right(this)
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

    fun build(): Ingredient = contents.fold(ICustomIngredient::toVanilla, Ingredient::of)

    fun buildSized(): HTItemIngredient = HTItemIngredient(build(), count)
}
