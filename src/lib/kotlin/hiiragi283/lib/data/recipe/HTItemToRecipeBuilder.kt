@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.data.recipe

import hiiragi283.lib.recipe.base.HTProgressData
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

abstract class HTItemToRecipeBuilder<RESULT : HTRecipeResult<*>, out RECIPE : Recipe<*>>(
    prefix: String,
    private val factory: Factory<RESULT, RECIPE>
) : HTProgressRecipeBuilder<RECIPE>(prefix) {
    override fun getPrimalId(): Identifier = result.getId()

    override fun createRecipe(): RECIPE = factory.create(ingredient, result, progressData)

    // Ingredient
    @PublishedApi internal var ingredient: HTItemIngredient by HTDelegates.onceInitialize()

    operator fun HTItemIngredient.unaryPlus() {
        ingredient = this
    }

    inline fun ingredient(builderAction: IngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        +IngredientBuilder.buildSized(builderAction)
    }

    // Result
    @PublishedApi internal var result: RESULT by HTDelegates.onceInitialize()

    operator fun RESULT.unaryPlus() {
        result = this
    }

    //    ToFluid    //

    /**
     * 1種類のアイテムと液体から1種類の液体を作成するレシピ向けの[HTProgressRecipeBuilder]の実装クラスです。
     * @param RECIPE 生成するレシピのクラス
     * @author Hiiragi Tsubasa
     * @since 26.1.0
     */
    class ToFluid<out RECIPE : Recipe<*>>(prefix: String, factory: Factory<HTFluidResult, RECIPE>) :
        HTItemToRecipeBuilder<HTFluidResult, RECIPE>(prefix, factory) {
        inline fun result(builderAction: HTFluidResultBuilder.() -> Unit) {
            contract {
                callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
            }
            +HTFluidResultBuilder.build(builderAction)
        }
    }

    //    ToItem    //

    /**
     * 1種類のアイテムと液体から1種類のアイテムを作成するレシピ向けの[HTProgressRecipeBuilder]の実装クラスです。
     * @param RECIPE 生成するレシピのクラス
     * @author Hiiragi Tsubasa
     * @since 26.1.0
     */
    class ToItem<out RECIPE : Recipe<*>>(prefix: String, factory: Factory<HTItemResult, RECIPE>) :
        HTItemToRecipeBuilder<HTItemResult, RECIPE>(prefix, factory) {
        inline fun result(builderAction: HTItemResultBuilder.() -> Unit) {
            contract {
                callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
            }
            +HTItemResultBuilder.build(builderAction)
        }
    }

    //    Factory    //

    /**
     * @param RECIPE 生成するレシピのクラス
     * @author Hiiragi Tsubasa
     * @since 26.1.5
     */
    fun interface Factory<RESULT : HTRecipeResult<*>, out RECIPE : Any> {
        fun create(ingredient: HTItemIngredient, result: RESULT, progressData: HTProgressData): RECIPE
    }
}
