package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTChancedItemResult
import hiiragi283.ragium.api.stack.ImmutableItemStack
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.crafting.RecipeInput

/**
 * [INPUT]から確率付きの[ImmutableItemStack]を返すレシピ
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
    fun getPreviewItems(input: INPUT, registries: HolderLookup.Provider): List<ImmutableItemStack> =
        getResultItems(input).mapNotNull { it.getStackOrNull(registries) }
}
