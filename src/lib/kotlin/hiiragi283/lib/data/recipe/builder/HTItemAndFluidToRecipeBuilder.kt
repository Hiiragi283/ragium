@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.data.recipe.builder

import hiiragi283.lib.data.recipe.result.HTFluidResultBuilder
import hiiragi283.lib.data.recipe.result.HTItemResultBuilder
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.result.HTFluidResult
import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.lib.recipe.result.HTRecipeResult
import hiiragi283.lib.util.HTDelegates
import net.minecraft.resources.Identifier
import net.minecraft.world.item.crafting.Recipe
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * 1種類のアイテムと液体から1種類の完成品を作成するレシピ向けの[HTProgressRecipeBuilder]の実装クラスです。
 * @param RECIPE 生成するレシピのクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
open class HTItemAndFluidToRecipeBuilder<RESULT : HTRecipeResult<*>, out RECIPE : Recipe<*>>(
    prefix: String,
    private val factory: Factory<RESULT, RECIPE>,
) : HTProgressRecipeBuilder<RECIPE>(prefix),
    HTItemOrFluidIngredientAcceptor {
    final override fun getRecipeId(): Identifier? = result.getId()

    final override fun createRecipe(): RECIPE = factory.create(itemIngredient, fluidIngredient, result, progressData)

    // Ingredients
    @PublishedApi internal var itemIngredient: HTItemIngredient by HTDelegates.onceInitialize()

    @PublishedApi internal var fluidIngredient: HTFluidIngredient by HTDelegates.onceInitialize()

    override operator fun HTItemIngredient.unaryPlus() {
        itemIngredient = this
    }

    override operator fun HTFluidIngredient.unaryPlus() {
        fluidIngredient = this
    }

    // Result
    var result: RESULT by HTDelegates.onceInitialize()

    operator fun RESULT.unaryPlus() {
        result = this
    }

    //    Factory    //

    /**
     * @param RESULT レシピの完成品となるクラス
     * @param RECIPE 生成するレシピのクラス
     * @author Hiiragi Tsubasa
     * @since 26.1.0
     */
    fun interface Factory<RESULT : HTRecipeResult<*>, out RECIPE : Any> {
        fun create(itemIngredient: HTItemIngredient, fluidIngredient: HTFluidIngredient, result: RESULT, progressData: HTProgressData): RECIPE

        fun create(ingredient: Pair<HTItemIngredient, HTFluidIngredient>, result: RESULT, progressData: HTProgressData): RECIPE =
            create(ingredient.first, ingredient.second, result, progressData)
    }
}

//    Extensions    //

/**
 * @param RECIPE 生成するレシピのクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.13
 */
typealias HTItemAndFluidToFluidRecipeBuilder<RECIPE> = HTItemAndFluidToRecipeBuilder<HTFluidResult, RECIPE>

/**
 * @param RECIPE 生成するレシピのクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.13
 */
typealias HTItemAndFluidToItemRecipeBuilder<RECIPE> = HTItemAndFluidToRecipeBuilder<HTItemResult, RECIPE>

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.13
 */
@JvmName("itemResult")
fun HTItemAndFluidToFluidRecipeBuilder<*>.result(builderAction: HTFluidResultBuilder.() -> Unit) {
    contract {
        callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
    }
    +HTFluidResultBuilder.build(builderAction)
}

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.13
 */
@JvmName("fluidResult")
fun HTItemAndFluidToItemRecipeBuilder<*>.result(builderAction: HTItemResultBuilder.() -> Unit) {
    contract {
        callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
    }
    +HTItemResultBuilder.build(builderAction)
}
