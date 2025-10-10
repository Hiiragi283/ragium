package hiiragi283.ragium.api.recipe

import hiiragi283.ragium.api.data.recipe.HTResultHelper
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.serialization.codec.BiCodec
import net.minecraft.core.HolderLookup
import net.minecraft.network.RegistryFriendlyByteBuf
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
    fun getResultItems(input: INPUT): List<ChancedResult>

    /**
     * 指定された[input], [registries]から完成品のプレビューを返します。
     * @param input レシピの入力
     * @param registries レジストリのアクセス
     * @return 完成品のプレビューの一覧
     */
    fun getPreviewItems(input: INPUT, registries: HolderLookup.Provider): List<ItemStack> =
        getResultItems(input).mapNotNull { it.getStackOrNull(registries) }.filterNot(ItemStack::isEmpty)

    /**
     * 確率付きの完成品を表すクラス
     * @param base 元となる完成品
     * @param chance 完成品を生成する確率
     */
    data class ChancedResult(val base: HTItemResult, val chance: Float) : HTItemResult by base {
        companion object {
            @JvmField
            val CODEC: BiCodec<RegistryFriendlyByteBuf, ChancedResult> = BiCodec.composite(
                HTResultHelper.INSTANCE.itemCodec().toMap(),
                ChancedResult::base,
                BiCodec.floatRange(0f, 1f).optionalFieldOf("chance", 1f),
                ChancedResult::chance,
                ::ChancedResult,
            )
        }

        constructor(base: HTItemResult) : this(base, 1f)

        override fun copyWithCount(count: Int): ChancedResult = ChancedResult(base.copyWithCount(count), chance)
    }
}
