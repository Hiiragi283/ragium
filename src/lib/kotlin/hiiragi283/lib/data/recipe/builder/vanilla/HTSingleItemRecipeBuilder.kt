@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.data.recipe.builder.vanilla

import hiiragi283.lib.data.recipe.ingredient.IngredientBuilder
import hiiragi283.lib.util.HTDelegates
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.StonecutterRecipe
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
    companion object {
        @JvmStatic
        inline fun stonecutting(builderAction: Stonecutting.() -> Unit): Stonecutting {
            contract {
                callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
            }
            return Stonecutting().apply(builderAction)
        }
    }

    // Ingredient
    @PublishedApi internal var ingredient: Ingredient by HTDelegates.onceInitialize()

    operator fun Ingredient.unaryPlus() {
        ingredient = this
    }

    inline fun ingredient(builderAction: IngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        ingredient = IngredientBuilder.build(builderAction)
    }

    //    Stonecutting    //

    /**
     * 石切台レシピ向けの[HTSingleItemRecipeBuilder]の実装クラスです。
     * @author Hiiragi Tsubasa
     * @since 26.1.7
     */
    class Stonecutting : HTSingleItemRecipeBuilder<StonecutterRecipe>("stonecutting") {
        override fun createRecipe(): StonecutterRecipe = StonecutterRecipe(commonInfo(true), ingredient, result)
    }
}
