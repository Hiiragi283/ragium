@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.data.recipe.builder

import hiiragi283.lib.collection.Nel
import hiiragi283.lib.collection.toNel
import hiiragi283.lib.data.recipe.ingredient.HTItemIngredientBuilder
import hiiragi283.lib.data.recipe.result.HTItemResultBuilder
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.lib.util.HTDelegates
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import net.minecraft.resources.Identifier
import net.minecraft.world.item.crafting.Recipe
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * 2種類または3種類のアイテムから1種類のアイテムを作成するレシピ向けの[HTProgressRecipeBuilder]の実装クラスです。
 * @param RECIPE 生成するレシピのクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.7
 */
class HTTripleItemToItemRecipeBuilder<out RECIPE : Recipe<*>>(prefix: String, private val factory: Factory<RECIPE>) :
    HTProgressRecipeBuilder<RECIPE>(prefix) {
    override fun getRecipeId(): Identifier? = result.getId()

    override fun createRecipe(): RECIPE = factory.create(
        ingredient,
        extras.toNel(),
        result,
        progressData
    )

    // Ingredient
    @PublishedApi internal var ingredient: HTItemIngredient by HTDelegates.onceInitialize()

    operator fun HTItemIngredient.unaryPlus() {
        ingredient = this
    }

    inline fun ingredient(builderAction: HTItemIngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        +HTItemIngredientBuilder.build(builderAction)
    }

    // Extras
    @PublishedApi internal var extras: MutableList<HTItemIngredient> = ObjectArrayList()

    inline fun extra(builderAction: HTItemIngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        extras += HTItemIngredientBuilder.build(builderAction)
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
     * @since 26.1.7
     */
    fun interface Factory<out RECIPE : Any> {
        fun create(
            ingredient: HTItemIngredient,
            extras: Nel<HTItemIngredient>,
            result: HTItemResult,
            progressData: HTProgressData
        ): RECIPE
    }
}
