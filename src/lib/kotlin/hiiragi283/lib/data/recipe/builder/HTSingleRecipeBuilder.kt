@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.data.recipe.builder

import hiiragi283.lib.data.recipe.ingredient.HTFluidIngredientBuilder
import hiiragi283.lib.data.recipe.ingredient.HTItemIngredientBuilder
import hiiragi283.lib.data.recipe.result.HTFluidResultBuilder
import hiiragi283.lib.data.recipe.result.HTItemResultBuilder
import hiiragi283.lib.recipe.base.HTBasicSingleRecipe
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.recipe.ingredient.HTIngredient
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
 * 1種類の材料から1種類の完成品を作成するレシピ向けの[HTProgressRecipeBuilder]の実装クラスです。
 * @param ING レシピの材料を判定するクラス
 * @param RES レシピの完成品を提供するクラス
 * @param RECIPE 生成するレシピのクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.8
 */
open class HTSingleRecipeBuilder<ING : HTIngredient<*, *>, RES : HTRecipeResult<*>, out RECIPE : Recipe<*>>(
    prefix: String,
    private val factory: HTBasicSingleRecipe.Factory<ING, RES, RECIPE>
) : HTProgressRecipeBuilder<RECIPE>(prefix) {
    final override fun getRecipeId(): Identifier? = result.getId()

    final override fun createRecipe(): RECIPE = factory.create(ingredient, result, progressData)

    // Ingredient
    var ingredient: ING by HTDelegates.onceInitialize()

    operator fun ING.unaryPlus() {
        ingredient = this
    }

    // Result
    var result: RES by HTDelegates.onceInitialize()

    operator fun RES.unaryPlus() {
        result = this
    }
}

//    Extensions    //

/**
 * @param RECIPE 生成するレシピのクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.8
 */
typealias HTFluidToFluidRecipeBuilder<RECIPE> = HTSingleRecipeBuilder<HTFluidIngredient, HTFluidResult, RECIPE>

/**
 * @param RECIPE 生成するレシピのクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.8
 */
typealias HTFluidToItemRecipeBuilder<RECIPE> = HTSingleRecipeBuilder<HTFluidIngredient, HTItemResult, RECIPE>

/**
 * @param RECIPE 生成するレシピのクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.8
 */
typealias HTItemToFluidRecipeBuilder<RECIPE> = HTSingleRecipeBuilder<HTItemIngredient, HTFluidResult, RECIPE>

/**
 * @param RECIPE 生成するレシピのクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.8
 */
typealias HTItemToItemRecipeBuilder<RECIPE> = HTSingleRecipeBuilder<HTItemIngredient, HTItemResult, RECIPE>

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.8
 */
@JvmName("itemIngredient")
fun HTSingleRecipeBuilder<HTFluidIngredient, *, *>.ingredient(builderAction: HTFluidIngredientBuilder.() -> Unit) {
    contract {
        callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
    }
    +HTFluidIngredientBuilder.build(builderAction)
}

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.8
 */
@JvmName("fluidIngredient")
fun HTSingleRecipeBuilder<HTItemIngredient, *, *>.ingredient(builderAction: HTItemIngredientBuilder.() -> Unit) {
    contract {
        callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
    }
    +HTItemIngredientBuilder.build(builderAction)
}

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.8
 */
@JvmName("itemResult")
fun HTSingleRecipeBuilder<*, HTFluidResult, *>.result(builderAction: HTFluidResultBuilder.() -> Unit) {
    contract {
        callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
    }
    +HTFluidResultBuilder.build(builderAction)
}

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.8
 */
@JvmName("fluidResult")
fun HTSingleRecipeBuilder<*, HTItemResult, *>.result(builderAction: HTItemResultBuilder.() -> Unit) {
    contract {
        callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
    }
    +HTItemResultBuilder.build(builderAction)
}
