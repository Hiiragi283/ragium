package hiiragi283.ragium.api.recipe.chance

import hiiragi283.ragium.api.block.entity.HTUpgradableBlockEntity
import hiiragi283.ragium.api.item.component.HTMachineUpgrade
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.serialization.codec.BiCodec
import hiiragi283.ragium.api.stack.ImmutableItemStack
import net.minecraft.core.HolderLookup
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.RandomSource

/**
 * 確率付きの完成品を表すクラス
 * @param base 元となる完成品
 * @param chance 完成品を生成する確率
 */
data class HTItemResultWithChance(val base: HTItemResult, val chance: Float) {
    companion object {
        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, HTItemResultWithChance> = BiCodec.composite(
            HTItemResult.CODEC.toMap(),
            HTItemResultWithChance::base,
            BiCodec.floatRange(0f, 1f).optionalFieldOf("chance", 1f),
            HTItemResultWithChance::chance,
            ::HTItemResultWithChance,
        )
    }

    constructor(pair: Pair<HTItemResult, Float>) : this(pair.first, pair.second)

    constructor(base: HTItemResult) : this(base, 1f)

    val id: ResourceLocation = base.id

    fun getStackOrNull(provider: HolderLookup.Provider?): ImmutableItemStack? = this.base.getStackOrNull(provider)

    fun getStackOrNull(provider: HolderLookup.Provider?, random: RandomSource, blockEntity: HTUpgradableBlockEntity): ImmutableItemStack? =
        blockEntity
            .calculateValue(HTMachineUpgrade.Key.SUBPRODUCT_CHANCE)
            .map(
                { base.getStackOrNull(provider) },
                { chanceIn: Float ->
                    if (chance == 1f) return@map base.getStackOrNull(provider)

                    val chance1: Float = chance + chanceIn
                    val extraCount: Int = chance1.toInt()

                    var countSum: Int = base.amount * extraCount
                    if ((chance1 - extraCount) > random.nextFloat()) {
                        countSum += base.amount
                    }
                    base.copyWithAmount(countSum).getStackOrNull(provider)
                },
            )

    fun hasNoMatchingStack(): Boolean = this.base.hasNoMatchingStack()
}
