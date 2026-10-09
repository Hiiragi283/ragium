@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.data.recipe.builder

import hiiragi283.lib.data.recipe.ingredient.HTFluidIngredientBuilder
import hiiragi283.lib.data.recipe.ingredient.HTItemIngredientBuilder
import hiiragi283.lib.data.recipe.result.HTFluidResultBuilder
import hiiragi283.lib.data.recipe.result.HTItemResultBuilder
import hiiragi283.lib.recipe.ingredient.HTFluidIngredient
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.recipe.result.HTFluidResult
import hiiragi283.lib.recipe.result.HTItemOrFluidResult
import hiiragi283.lib.recipe.result.HTItemResult
import hiiragi283.lib.util.HTDelegates
import hiiragi283.lib.util.Ior
import net.minecraft.resources.Identifier
import net.minecraft.world.item.crafting.Recipe
import java.util.Optional
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * 1種類のアイテムと液体から1種類のアイテムと液体を作成するレシピ向けの[HTProgressRecipeBuilder]の実装クラスです。
 * @param RECIPE 生成するレシピのクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.9
 */
class HTItemOrFluidRecipeBuilder<out RECIPE : Recipe<*>>(
    prefix: String,
    private val factory: HTItemAndFluidToRecipeBuilder.Factory<HTItemOrFluidResult, RECIPE>
) : HTProgressRecipeBuilder<RECIPE>(prefix) {
    override fun getRecipeId(): Identifier? = result.getId()

    override fun createRecipe(): RECIPE = factory.create(itemIngredient, fluidIngredient, result, progressData)

    // Ingredients
    var itemIngredient: HTItemIngredient by HTDelegates.onceInitialize()
    var fluidIngredient: HTFluidIngredient by HTDelegates.onceInitialize()

    operator fun HTItemIngredient.unaryPlus() {
        itemIngredient = this
    }

    operator fun HTFluidIngredient.unaryPlus() {
        fluidIngredient = this
    }

    inline fun itemIngredient(builderAction: HTItemIngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        +HTItemIngredientBuilder.build(builderAction)
    }

    inline fun fluidIngredient(builderAction: HTFluidIngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        +HTFluidIngredientBuilder.build(builderAction)
    }

    // Results
    var itemResult: Optional<HTItemResult> by HTDelegates.optionalInitialize()

    var fluidResult: Optional<HTFluidResult> by HTDelegates.optionalInitialize()

    val result: HTItemOrFluidResult get() = Ior.fromNullable(itemResult, fluidResult)
        .map(::HTItemOrFluidResult)
        .orElseThrow { error("Either item or fluid result required") }

    operator fun HTItemResult.unaryPlus() {
        itemResult = Optional.of(this)
    }

    operator fun HTFluidResult.unaryPlus() {
        fluidResult = Optional.of(this)
    }

    inline fun itemResult(builderAction: HTItemResultBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        +HTItemResultBuilder.build(builderAction)
    }

    inline fun fluidResult(builderAction: HTFluidResultBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        +HTFluidResultBuilder.build(builderAction)
    }
}
