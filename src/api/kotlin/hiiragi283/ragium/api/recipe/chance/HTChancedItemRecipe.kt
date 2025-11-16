package hiiragi283.ragium.api.recipe.chance

import hiiragi283.ragium.api.recipe.HTRecipe
import hiiragi283.ragium.api.stack.ImmutableItemStack
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.crafting.RecipeInput

/**
 * 複数の確率付きの完成品を生産するレシピ
 */
interface HTChancedItemRecipe<INPUT : RecipeInput> : HTRecipe<INPUT> {
    /**
     * 指定された[input]から完成品を返します。
     * @param input レシピの入力
     * @return 完成品の一覧
     */
    fun getResultItems(input: INPUT): List<HTItemResultWithChance>

    /**
     * 指定された[input], [registries]から完成品のプレビューを返します。
     * @param input レシピの入力
     * @param registries レジストリのアクセス
     * @return 完成品のプレビューの一覧
     */
    fun getPreviewItems(input: INPUT, registries: HolderLookup.Provider): List<ImmutableItemStack> =
        getResultItems(input).mapNotNull { it.getStackOrNull(registries) }
}
