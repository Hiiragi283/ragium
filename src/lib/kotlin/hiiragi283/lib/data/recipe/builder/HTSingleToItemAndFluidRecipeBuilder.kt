@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.data.recipe.builder

import hiiragi283.lib.data.recipe.ingredient.HTFluidIngredientBuilder
import hiiragi283.lib.data.recipe.ingredient.HTItemIngredientBuilder
import hiiragi283.lib.data.recipe.result.HTItemOrFluidResultBuilder
import hiiragi283.lib.recipe.base.HTBasicSingleRecipe
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.recipe.ingredient.HTIngredient
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.result.HTItemOrFluidResult
import hiiragi283.lib.util.HTDelegates
import net.minecraft.resources.Identifier
import net.minecraft.world.item.crafting.Recipe
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * 1種類の材料から1種類アイテムと液体の完成品を作成するレシピ向けの[HTProgressRecipeBuilder]の実装クラスです。
 * @param ING レシピの材料を判定するクラス
 * @param RECIPE 生成するレシピのクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.8
 */
class HTSingleToItemAndFluidRecipeBuilder<ING : HTIngredient<*, *>, out RECIPE : Recipe<*>>(
    prefix: String,
    private val factory: HTBasicSingleRecipe.Factory<ING, HTItemOrFluidResult, RECIPE>,
) : HTProgressRecipeBuilder<RECIPE>(prefix),
    HTItemOrFluidResultBuilder.Holder {
    override fun getRecipeId(): Identifier? = result.getId()

    override fun createRecipe(): RECIPE = factory.create(ingredient, result, progressData)

    // Ingredient
    var ingredient: ING by HTDelegates.onceInitialize()

    operator fun ING.unaryPlus() {
        ingredient = this
    }

    // Results
    override val builder: HTItemOrFluidResultBuilder = HTItemOrFluidResultBuilder()
}

//    Extensions    //

/**
 * @param RECIPE 生成するレシピのクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.8
 */
typealias HTFluidToItemAndFluidRecipeBuilder<RECIPE> = HTSingleToItemAndFluidRecipeBuilder<HTFluidIngredient, RECIPE>

/**
 * @param RECIPE 生成するレシピのクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.8
 */
typealias HTItemToItemAndFluidRecipeBuilder<RECIPE> = HTSingleToItemAndFluidRecipeBuilder<HTItemIngredient, RECIPE>

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.8
 */
@JvmName("itemIngredient")
fun HTSingleToItemAndFluidRecipeBuilder<HTFluidIngredient, *>.ingredient(builderAction: HTFluidIngredientBuilder.() -> Unit) {
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
fun HTSingleToItemAndFluidRecipeBuilder<HTItemIngredient, *>.ingredient(builderAction: HTItemIngredientBuilder.() -> Unit) {
    contract {
        callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
    }
    +HTItemIngredientBuilder.build(builderAction)
}
