@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.data.recipe

import hiiragi283.lib.data.ConditionBuilder
import hiiragi283.lib.recipe.RecipeKey
import hiiragi283.lib.util.HTBuilderMarker
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.resources.Identifier
import net.minecraft.world.item.crafting.CraftingRecipe
import net.minecraft.world.item.crafting.Recipe
import net.neoforged.neoforge.common.conditions.ICondition

/**
 * Hiiragi Seriesで使用される[Recipe]のビルダークラスです。
 * @param RECIPE 生成するレシピのクラス
 * @param prefix レシピIDに使われる前置詞
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
@HTBuilderMarker
abstract class HTRecipeBuilder<out RECIPE : Recipe<*>>(private val prefix: String) {
    fun commonInfo(showNotification: Boolean): Recipe.CommonInfo = RecipeBuilder.createCraftingCommonInfo(showNotification)

    fun bookInfo(category: RecipeCategory, group: String?): CraftingRecipe.CraftingBookInfo = RecipeBuilder.createCraftingBookInfo(category, group)

    //    Conditions    //

    /**
     * [ICondition]を保持するインスタンス
     */
    @PublishedApi
    internal val conditions: MutableList<ICondition> = mutableListOf()

    /**
     * @since 26.1.1
     */
    inline fun condition(builderAction: ConditionBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        ConditionBuilder(conditions).apply(builderAction)
    }

    //    Save    //

    /**
     * レシピ[ID][Identifier]を保持するインスタンス
     */
    val recipeId: RecipeId by lazy(::RecipeId)

    inner class RecipeId {
        /**
         * 保持している[ID][Identifier]
         */
        var id: Identifier = getPrimalId()
            private set

        /**
         * 現在の[ID][Identifier]にプレフィックスを追加します。
         */
        infix fun prefix(prefix: String) {
            id = id.withPrefix(prefix)
        }

        /**
         * 現在の[ID][Identifier]にサフィックスを追加します。
         */
        infix fun suffix(suffix: String) {
            id = id.withSuffix(suffix)
        }

        /**
         * 現在の[ID][Identifier]を[newId]で置換します。
         */
        infix fun replace(newId: Identifier) {
            id = newId
        }
    }

    /**
     * レシピを生成します。
     * @param exporter 生成したレシピの出力先
     */
    open fun save(exporter: HTRecipeExporter) {
        this.save { id: Identifier, recipe: RECIPE -> exporter.accept(RecipeKey(id), recipe, conditions) }
    }

    /**
     * 生成したレシピを処理します。
     * @param consumer 生成されたレシピIDとレシピを処理するブロック
     */
    fun <R> save(consumer: (id: Identifier, recipe: RECIPE) -> R): R {
        contract {
            callsInPlace(consumer, InvocationKind.EXACTLY_ONCE)
        }
        return consumer(recipeId.id.withPrefix("$prefix/"), createRecipe())
    }

    /**
     * デフォルトのIDを取得します。
     */
    protected abstract fun getPrimalId(): Identifier

    /**
     * レシピを生成します。
     */
    protected abstract fun createRecipe(): RECIPE
}
