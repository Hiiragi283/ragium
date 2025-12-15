package hiiragi283.ragium.api.recipe

import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.level.Level

/**
 * 指定したインプットに一致する最初のレシピを取得する関数型インターフェース
 * @param INPUT レシピの入力となるクラス
 * @param RECIPE レシピのクラス
 */
fun interface HTRecipeFinder<INPUT : RecipeInput, RECIPE : Any> {
    /**
     * 指定した引数から最初に一致するレシピを返します。
     * @param input レシピの入力
     * @param level この処理を行っている[Level]
     * @param lastRecipe 最後に一致したレシピのキャッシュ
     * @return 一致するレシピがない場合は`null`
     */
    fun getRecipeFor(input: INPUT, level: Level, lastRecipe: Pair<ResourceLocation, RECIPE>?): Pair<ResourceLocation, RECIPE>?

    fun interface Vanilla<INPUT : RecipeInput, RECIPE : Recipe<INPUT>> : HTRecipeFinder<INPUT, RECIPE> {
        override fun getRecipeFor(
            input: INPUT,
            level: Level,
            lastRecipe: Pair<ResourceLocation, RECIPE>?,
        ): Pair<ResourceLocation, RECIPE>? = getVanillaRecipeFor(input, level, lastRecipe?.toHolder())?.toPair()

        fun getVanillaRecipeFor(input: INPUT, level: Level, lastRecipe: RecipeHolder<RECIPE>?): RecipeHolder<RECIPE>?
    }
}
