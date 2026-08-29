package hiiragi283.lib.data.recipe

import hiiragi283.lib.recipe.RecipeKey
import net.minecraft.advancements.Advancement
import net.minecraft.advancements.AdvancementHolder
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.world.item.crafting.Recipe
import net.neoforged.neoforge.common.conditions.ICondition

/**
 * レシピの出力先を表すインターフェースです。
 *
 * 参照 : [Minecraft - RecipeOutput][RecipeOutput]
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun interface HTRecipeExporter {
    /**
     * 受け取ったレシピを処理します。
     * @param id 受け取ったレシピのID
     * @param recipe 受け取ったレシピの値
     * @param conditions レシピを読み込む条件の一覧
     */
    fun accept(id: RecipeKey, recipe: Recipe<*>, conditions: List<ICondition>)

    /**
     * 受け取ったレシピを処理します。
     * @param id 受け取ったレシピのID
     * @param recipe 受け取ったレシピの値
     */
    fun accept(id: RecipeKey, recipe: Recipe<*>) {
        accept(id, recipe, listOf())
    }

    /**
     * [RecipeOutput]に変換します。
     */
    fun asOutput(): RecipeOutput = object : RecipeOutput {
        override fun accept(id: RecipeKey, recipe: Recipe<*>, advancement: AdvancementHolder?, vararg conditions: ICondition) {
            this@HTRecipeExporter.accept(id, recipe, conditions.toList())
        }

        override fun advancement(): Advancement.Builder = Advancement.Builder.recipeAdvancement()

        override fun includeRootAdvancement(): Unit = Unit
    }
}
