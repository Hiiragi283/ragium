package hiiragi283.ragium.api.recipe.chance

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.block.entity.HTUpgradableBlockEntity
import hiiragi283.ragium.api.item.component.HTMachineUpgrade
import hiiragi283.ragium.api.math.minus
import hiiragi283.ragium.api.math.times
import hiiragi283.ragium.api.math.toFraction
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.serialization.codec.BiCodec
import hiiragi283.ragium.api.serialization.codec.BiCodecs
import hiiragi283.ragium.api.stack.ImmutableItemStack
import net.minecraft.core.HolderLookup
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.RandomSource
import org.apache.commons.lang3.math.Fraction

/**
 * 確率付きの完成品を表すクラス
 * @param base 元となる完成品
 * @param chance 完成品を生成する確率
 */
data class HTItemResultWithChance(val base: HTItemResult, val chance: Fraction) {
    companion object {
        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, HTItemResultWithChance> = BiCodec.composite(
            HTItemResult.CODEC.toMap(),
            HTItemResultWithChance::base,
            BiCodecs.fractionRange(Fraction.ZERO, Fraction.ONE).optionalFieldOf(RagiumConst.CHANCE, Fraction.ONE),
            HTItemResultWithChance::chance,
            ::HTItemResultWithChance,
        )
    }

    constructor(pair: Pair<HTItemResult, Fraction>) : this(pair.first, pair.second)

    constructor(base: HTItemResult) : this(base, Fraction.ONE)

    val id: ResourceLocation = base.id

    fun getStackOrNull(provider: HolderLookup.Provider?): ImmutableItemStack? = this.base.getStackOrNull(provider)

    fun getStackOrNull(provider: HolderLookup.Provider?, random: RandomSource, blockEntity: HTUpgradableBlockEntity): ImmutableItemStack? =
        blockEntity
            .calculateValue(HTMachineUpgrade.Key.SUBPRODUCT_CHANCE)
            .map(
                { base.getStackOrNull(provider) },
                { chanceIn: Fraction ->
                    if (chance == Fraction.ONE) return@map base.getStackOrNull(provider)

                    val chance1: Fraction = chance * chanceIn
                    val extraCount: Int = chance1.properWhole

                    var countSum: Int = base.amount * extraCount
                    if ((chance1 - extraCount) > random.nextFloat().toFraction()) {
                        countSum += base.amount
                    }
                    base.copyWithAmount(countSum).getStackOrNull(provider)
                },
            )

    fun hasNoMatchingStack(): Boolean = this.base.hasNoMatchingStack()
}
