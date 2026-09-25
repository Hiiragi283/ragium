package hiiragi283.lib.data.recipe.builder.vanilla

import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.world.item.crafting.CraftingRecipe

/**
 * クラフトレシピ向けの[HTVanillaRecipeBuilder]の実装クラスです。
 * @param RECIPE 生成するレシピのクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
abstract class HTCraftingRecipeBuilder<out RECIPE : CraftingRecipe>(prefix: String) :
    HTVanillaRecipeBuilder<RECIPE>(prefix) {
    /**
     * レシピ本のカテゴリ
     */
    var category: RecipeCategory = RecipeCategory.MISC

    fun bookInfo(): CraftingRecipe.CraftingBookInfo = bookInfo(category)
}
