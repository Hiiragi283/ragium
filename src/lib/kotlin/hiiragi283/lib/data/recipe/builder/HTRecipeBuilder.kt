@file:OptIn(ExperimentalContracts::class)

package hiiragi283.lib.data.recipe.builder

import hiiragi283.lib.data.ConditionBuilder
import hiiragi283.lib.data.ConditionalExporter
import hiiragi283.lib.recipe.HTRecipeHolder
import hiiragi283.lib.recipe.RecipeKey
import hiiragi283.lib.util.HTBuilderMarker
import hiiragi283.lib.util.Identity
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import net.minecraft.resources.Identifier
import net.minecraft.world.item.crafting.Recipe
import net.neoforged.neoforge.common.conditions.ICondition
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * Hiiragi Seriesで使用される[Recipe]のビルダークラスです。
 * @param RECIPE 生成するレシピのクラス
 * @param prefix レシピIDに使われる前置詞
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
@HTBuilderMarker
abstract class HTRecipeBuilder<out RECIPE : Recipe<*>>(private val prefix: String) {
    /**
     * デフォルトのIDを取得します。
     */
    protected abstract fun getRecipeId(): Identifier?

    /**
     * レシピを生成します。
     */
    protected abstract fun createRecipe(): RECIPE

    //    Conditions    //

    /**
     * [ICondition]を保持するインスタンス
     */
    @PublishedApi
    internal val conditions: MutableList<ICondition> = ObjectArrayList()

    inline fun condition(builderAction: ConditionBuilder.() -> Unit) {
        contract {
            callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE)
        }
        ConditionBuilder(conditions).apply(builderAction)
    }

    //    Recipe Id    //

    /**
     * レシピ[ID][Identifier]を保持するインスタンス
     */
    val recipeId: RecipeId = RecipeId()

    inner class RecipeId {
        /**
         * 保持している[ID][Identifier]
         */
        var defaultId: () -> Identifier = { getRecipeId() ?: error("Could not generate default recipe id") }
            private set

        /**
         * @since 26.1.6
         */
        infix fun modify(operator: Identity<Identifier>) {
            val defaultId1: () -> Identifier = defaultId
            defaultId = { defaultId1().let(operator) }
        }

        /**
         * 現在の[ID][Identifier]にプレフィックスを追加します。
         */
        infix fun prefix(prefix: String) {
            modify { id: Identifier -> id.withPrefix(prefix) }
        }

        /**
         * 現在の[ID][Identifier]にサフィックスを追加します。
         */
        infix fun suffix(suffix: String) {
            modify { id: Identifier -> id.withSuffix(suffix) }
        }

        /**
         * 現在の[ID][Identifier]を[newId]で置換します。
         */
        infix fun replace(newId: RecipeKey) {
            replace(newId.identifier())
        }

        /**
         * 現在の[ID][Identifier]を[newId]で置換します。
         */
        infix fun replace(newId: Identifier) {
            defaultId = { newId }
        }
    }

    //    Exporter    //

    fun build(): HTRecipeHolder<RECIPE> = build("$prefix/")

    fun buildSynthetic(): HTRecipeHolder<RECIPE> = build("/$prefix/")

    private fun build(prefix: String): HTRecipeHolder<RECIPE> = HTRecipeHolder(
        recipeId.defaultId().withPrefix(prefix).let(::RecipeKey),
        createRecipe()
    )

    /**
     * レシピを生成します。
     * @param exporter 生成したレシピの出力先
     */
    open fun save(exporter: ConditionalExporter<Recipe<*>>): RecipeKey =
        build().let { (key: RecipeKey, recipe: RECIPE) -> exporter.accept(key, recipe, conditions) }
}
