package hiiragi283.ragium.api.recipe.result

import hiiragi283.ragium.api.data.recipe.HTResultHelper
import hiiragi283.ragium.api.serialization.codec.BiCodec
import hiiragi283.ragium.api.stack.ImmutableItemStack
import net.minecraft.core.HolderLookup
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.util.RandomSource

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

    fun getStackOrNull(provider: HolderLookup.Provider?, random: RandomSource): ImmutableItemStack? = when {
        chance > random.nextFloat() -> base.getStackOrNull(provider)
        else -> null
    }

    override fun copyWithCount(count: Int): HTChancedItemResult = HTChancedItemResult(base.copyWithCount(count), chance)
}
