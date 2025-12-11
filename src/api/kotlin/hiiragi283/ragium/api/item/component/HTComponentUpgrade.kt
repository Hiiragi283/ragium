package hiiragi283.ragium.api.item.component

import hiiragi283.ragium.api.serialization.codec.BiCodec
import hiiragi283.ragium.api.serialization.codec.BiCodecs
import hiiragi283.ragium.api.upgrade.HTUpgradeKey
import io.netty.buffer.ByteBuf
import org.apache.commons.lang3.math.Fraction

@JvmInline
value class HTComponentUpgrade private constructor(val map: Map<HTUpgradeKey, Fraction>) : Map<HTUpgradeKey, Fraction> by map {
    companion object {
        @JvmField
        val CODEC: BiCodec<ByteBuf, HTComponentUpgrade> = BiCodecs
            .mapOf(HTUpgradeKey.CODEC, BiCodecs.NON_NEGATIVE_FRACTION)
            .xmap(::HTComponentUpgrade, HTComponentUpgrade::map)

        @JvmStatic
        val EMPTY = HTComponentUpgrade(mapOf())

        @JvmStatic
        fun create(vararg pairs: Pair<HTUpgradeKey, Fraction>): HTComponentUpgrade = create(mapOf(*pairs))

        @JvmStatic
        inline fun create(builderAction: MutableMap<HTUpgradeKey, Fraction>.() -> Unit): HTComponentUpgrade =
            create(buildMap(builderAction))

        @JvmStatic
        fun create(map: Map<HTUpgradeKey, Fraction>): HTComponentUpgrade = when {
            map.isEmpty() -> EMPTY
            else -> HTComponentUpgrade(map)
        }
    }
}
