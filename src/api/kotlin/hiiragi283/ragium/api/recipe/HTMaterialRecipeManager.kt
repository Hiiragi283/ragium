package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.RagiumPlatform
import hiiragi283.ragium.api.data.map.HTMaterialRecipe
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level

/**
 * [HTMaterialRecipe]に基づいたレシピ向けのマネージャ
 * @see RagiumPlatform.getMaterialRecipeManager
 */
interface HTMaterialRecipeManager {
    /**
     * 指定した[recipeType]に対応するすべてのレシピを返します。
     * @param RECIPE レシピのクラス
     * @return [RecipeHolder]の[List]
     */
    fun <RECIPE : Recipe<*>> getAllRecipes(recipeType: RecipeType<RECIPE>): List<RecipeHolder<RECIPE>>

    /**
     * 指定した引数から，最初に一致するレシピを返します。
     * @param INPUT レシピの入力となるクラス
     * @param RECIPE レシピのクラス
     * @param input レシピの入力
     * @param level この処理を行っている[Level]
     * @param lastRecipe 最後に一致したレシピのキャッシュ
     * @return 一致するレシピがない場合は`null`
     */
    fun <INPUT : RecipeInput, RECIPE : Recipe<INPUT>> getRecipeFor(
        recipeType: RecipeType<RECIPE>,
        input: INPUT,
        level: Level,
        lastRecipe: RecipeHolder<RECIPE>?,
    ): RecipeHolder<RECIPE>?
}
