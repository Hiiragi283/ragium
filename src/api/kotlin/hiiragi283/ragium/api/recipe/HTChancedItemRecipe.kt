package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTChancedItemResult
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeInput

/**
 * [INPUT]から確率付きの[ItemStack]を返すレシピ
 */
interface HTChancedItemRecipe<INPUT : RecipeInput> :
    HTRecipe<INPUT>,
    HTItemIngredient.CountGetter {
    /**
     * 指定された[input]から完成品を返します。
     * @param input レシピの入力
     * @return 完成品の一覧
     */
    fun getResultItems(input: INPUT): List<HTChancedItemResult>

    /**
     * 指定された[input], [registries]から完成品のプレビューを返します。
     * @param input レシピの入力
     * @param registries レジストリのアクセス
     * @return 完成品のプレビューの一覧
     */
    fun getPreviewItems(input: INPUT, registries: HolderLookup.Provider): List<ItemStack> =
        getResultItems(input).mapNotNull { it.getStackOrNull(registries) }.filterNot(ItemStack::isEmpty)
}
