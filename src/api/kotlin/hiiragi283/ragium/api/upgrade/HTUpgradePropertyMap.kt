package hiiragi283.ragium.api.upgrade

import hiiragi283.ragium.api.serialization.codec.BiCodec
import hiiragi283.ragium.api.serialization.codec.BiCodecs
import io.netty.buffer.ByteBuf
import org.apache.commons.lang3.math.Fraction

@JvmInline
value class HTUpgradePropertyMap private constructor(val map: Map<HTUpgradeKey, Fraction>) : Map<HTUpgradeKey, Fraction> by map {
    companion object {
        @JvmField
        val CODEC: BiCodec<ByteBuf, HTUpgradePropertyMap> = BiCodecs
            .mapOf(HTUpgradeKey.CODEC, BiCodecs.NON_NEGATIVE_FRACTION)
            .xmap(::create, HTUpgradePropertyMap::map)

        @JvmStatic
        val EMPTY = HTUpgradePropertyMap(mapOf())

        @JvmStatic
        fun create(vararg pairs: Pair<HTUpgradeKey, Fraction>): HTUpgradePropertyMap = create(mapOf(*pairs))

        @JvmStatic
        inline fun create(builderAction: MutableMap<HTUpgradeKey, Fraction>.() -> Unit): HTUpgradePropertyMap =
            create(buildMap(builderAction))

        @JvmStatic
        fun create(map: Map<HTUpgradeKey, Fraction>): HTUpgradePropertyMap =
            createInternal(map.filter { (_, value: Fraction) -> value > Fraction.ZERO })

        @JvmStatic
        private fun createInternal(map: Map<HTUpgradeKey, Fraction>): HTUpgradePropertyMap = when {
            map.isEmpty() -> EMPTY
            else -> HTUpgradePropertyMap(map)
        }
    }
}
