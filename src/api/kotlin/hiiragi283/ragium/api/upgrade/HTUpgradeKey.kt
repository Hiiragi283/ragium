package hiiragi283.ragium.api.upgrade

import hiiragi283.core.api.serialization.codec.BiCodec
import hiiragi283.core.api.serialization.codec.VanillaBiCodecs
import hiiragi283.core.api.text.HTTranslation
import io.netty.buffer.ByteBuf
import net.minecraft.Util
import net.minecraft.resources.ResourceLocation

@ConsistentCopyVisibility
data class HTUpgradeKey private constructor(val id: ResourceLocation) :
    HTTranslation,
    Comparable<HTUpgradeKey> {
        companion object {
            @JvmStatic
            private val instances: MutableMap<ResourceLocation, HTUpgradeKey> = mutableMapOf()

            @JvmField
            val CODEC: BiCodec<ByteBuf, HTUpgradeKey> = VanillaBiCodecs.ID.xmap(HTUpgradeKey::get, HTUpgradeKey::id)

            @JvmStatic
            fun get(id: ResourceLocation): HTUpgradeKey = instances.computeIfAbsent(id, ::HTUpgradeKey)

            @JvmStatic
            fun getAll(): Collection<HTUpgradeKey> = instances.values
        }

        override val translationKey: String = Util.makeDescriptionId("gui", id.withPrefix("machine.upgrade."))

        override fun compareTo(other: HTUpgradeKey): Int = this.id.compareNamespaced(other.id)
    }
