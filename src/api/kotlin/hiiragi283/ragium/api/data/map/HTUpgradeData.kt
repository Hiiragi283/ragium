package hiiragi283.ragium.api.data.map

import hiiragi283.ragium.api.math.fraction
import hiiragi283.ragium.api.serialization.codec.BiCodec
import hiiragi283.ragium.api.upgrade.HTUpgradeGroup
import hiiragi283.ragium.api.upgrade.HTUpgradeKey
import hiiragi283.ragium.api.upgrade.HTUpgradePropertyMap
import hiiragi283.ragium.api.util.wrapOptional
import io.netty.buffer.ByteBuf
import org.apache.commons.lang3.math.Fraction
import java.util.Optional
import kotlin.jvm.optionals.getOrNull

/**
 * @see net.minecraft.world.item.enchantment.Enchantment
 */
@ConsistentCopyVisibility
data class HTUpgradeData private constructor(val propertyMap: HTUpgradePropertyMap, val group: Optional<HTUpgradeGroup>) :
    Map<HTUpgradeKey, Fraction> by propertyMap {
        companion object {
            @JvmField
            val CODEC: BiCodec<ByteBuf, HTUpgradeData> = BiCodec.composite(
                HTUpgradePropertyMap.CODEC.fieldOf("properties").forGetter(HTUpgradeData::propertyMap),
                HTUpgradeGroup.CODEC.optionalFieldOf("group").forGetter(HTUpgradeData::group),
                ::HTUpgradeData,
            )

            @JvmStatic
            inline fun create(builderAction: Builder.() -> Unit): HTUpgradeData = Builder().apply(builderAction).build()

            @JvmStatic
            fun createSimple(key: HTUpgradeKey, group: HTUpgradeGroup? = null): HTUpgradeData = create {
                set(key, 1)
                group(group)
            }
        }

        val groupOrNull: HTUpgradeGroup? get() = this.group.getOrNull()

        //    Builder    //

        class Builder {
            private var propertyMap: MutableMap<HTUpgradeKey, Fraction> = mutableMapOf()
            private var group: HTUpgradeGroup? = null

            operator fun set(key: HTUpgradeKey, value: Fraction) {
                propertyMap[key] = value
            }

            operator fun set(key: HTUpgradeKey, value: Int) {
                set(key, fraction(value))
            }

            fun group(group: HTUpgradeGroup?): Builder = apply {
                this.group = group
            }

            fun build(): HTUpgradeData = HTUpgradeData(HTUpgradePropertyMap.create(propertyMap), group.wrapOptional())
        }
    }
