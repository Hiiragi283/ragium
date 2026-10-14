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
 * 複数のアイテムから1種類のアイテムを作成するレシピ向けの[HTProgressRecipeBuilder]の実装クラスです。
 * @param RECIPE 生成するレシピのクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.11
 */
class HTSizedShapelessRecipeBuilder<out RECIPE : Recipe<*>>(prefix: String, private val factory: Factory<RECIPE>) :
    HTProgressRecipeBuilder<RECIPE>(prefix) {
    override fun getRecipeId(): Identifier? = result.getId()

    override fun createRecipe(): RECIPE = factory.create(ingredients.toNel(), result, progressData)

    // Ingredients
    val ingredients: MutableList<HTItemIngredient> = ObjectArrayList()

    operator fun HTItemIngredient.unaryPlus() {
        ingredients += this
    }

    inline fun ingredient(builderAction: HTItemIngredientBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        +HTItemIngredientBuilder.build(builderAction)
    }

    // Result
    var result: HTItemResult by HTDelegates.onceInitialize()

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
     * @since 26.1.11
     */
    fun interface Factory<out RECIPE : Any> {
        fun create(ingredients: Nel<HTItemIngredient>, result: HTItemResult, progressData: HTProgressData): RECIPE
    }
}
