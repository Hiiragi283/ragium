package hiiragi283.ragium.api.recipe

import net.minecraft.world.item.crafting.SingleRecipeInput

/**
 * 単一の材料を受け付けるレシピの拡張インターフェース
 */
interface HTSingleInputRecipe : HTRecipe<SingleRecipeInput> {
    /**
     * 指定された[input]から材料の個数を返します。
     * @param input レシピの入力
     * @return 必要な材料の個数
     */
    fun getIngredientCount(input: SingleRecipeInput): Int
}
