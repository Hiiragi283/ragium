package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.text.HTHasText
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeManager

/**
 * レシピの一覧を取得するインターフェース
 * @param INPUT レシピの入力となるクラス
 * @param RECIPE レシピのクラス
 */
interface HTRecipeType<INPUT : RecipeInput, RECIPE : Recipe<INPUT>> : HTHasText {
    /**
     * 指定した[manager]から[RecipeHolder]の一覧を返します。
     */
    fun getAllHolders(manager: RecipeManager): Sequence<RecipeHolder<out RECIPE>>
}
