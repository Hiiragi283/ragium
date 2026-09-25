@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.data.recipe.builder.vanilla

import hiiragi283.lib.data.recipe.ingredient.IngredientBuilder
import hiiragi283.lib.util.HTDelegates
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.Recipe
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * アイテムを1:1で変換するレシピ向けの[HTVanillaRecipeBuilder]の実装クラスです。
 *
 * 参照 : [Minecraft - SingleItemRecipeBuilder][net.minecraft.data.recipes.SingleItemRecipeBuilder]
 * @param RECIPE 生成するレシピのクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
abstract class HTSingleItemRecipeBuilder<out RECIPE : Recipe<*>>(prefix: String) :
    HTVanillaRecipeBuilder<RECIPE>(prefix) {
    // Ingredient
    var ingredient: Ingredient by HTDelegates.onceInitialize()

    operator fun Ingredient.unaryPlus() {
        ingredient = this
    }

    inline fun ingredient(builderAction: IngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        ingredient = IngredientBuilder.build(builderAction)
    }
}
