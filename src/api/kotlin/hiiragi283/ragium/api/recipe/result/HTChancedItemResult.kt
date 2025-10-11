package hiiragi283.ragium.api.recipe.result

import hiiragi283.ragium.api.data.recipe.HTResultHelper
import hiiragi283.ragium.api.serialization.codec.BiCodec
import net.minecraft.network.RegistryFriendlyByteBuf

/**
 * 確率付きの完成品を表すクラス
 * @param base 元となる完成品
 * @param chance 完成品を生成する確率
 */
data class HTChancedItemResult(val base: HTItemResult, val chance: Float) : HTItemResult by base {
    companion object {
        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, HTChancedItemResult> = BiCodec.composite(
            HTResultHelper.INSTANCE.itemCodec().toMap(),
            HTChancedItemResult::base,
            BiCodec.floatRange(0f, 1f).optionalFieldOf("chance", 1f),
            HTChancedItemResult::chance,
            ::HTChancedItemResult,
        )
    }

    constructor(base: HTItemResult) : this(base, 1f)

    override fun copyWithCount(count: Int): HTChancedItemResult = HTChancedItemResult(base.copyWithCount(count), chance)
}
