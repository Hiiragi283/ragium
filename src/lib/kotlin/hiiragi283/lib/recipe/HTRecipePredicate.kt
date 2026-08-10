package hiiragi283.lib.recipe

import net.minecraft.world.item.crafting.RecipeInput

/**
 * レシピの判定部分を切り出したインターフェースです。
 * @param INPUT レシピの入力となるクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
fun interface HTRecipePredicate<INPUT : RecipeInput> {
    /**
     * 指定された[input]が，このレシピの条件を満たすか判定します。
     */
    fun matches(input: INPUT): Boolean
}
