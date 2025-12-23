package hiiragi283.ragium.api.upgrade

import hiiragi283.core.api.serialization.codec.BiCodec
import hiiragi283.core.api.text.HTTranslation
import hiiragi283.ragium.api.RagiumAPI
import io.netty.buffer.ByteBuf
import net.minecraft.Util

@ConsistentCopyVisibility
data class HTUpgradeKey private constructor(val name: String) : HTTranslation {
    companion object {
        @JvmStatic
        private val instances: MutableMap<String, HTUpgradeKey> = mutableMapOf()

        @JvmField
        val CODEC: BiCodec<ByteBuf, HTUpgradeKey> = BiCodec.STRING.xmap(HTUpgradeKey::get, HTUpgradeKey::name)

        @JvmStatic
        fun get(name: String): HTUpgradeKey = instances.computeIfAbsent(name, ::HTUpgradeKey)

        @JvmStatic
        fun getAll(): Collection<HTUpgradeKey> = instances.values
    }

    override val translationKey: String = Util.makeDescriptionId("gui", RagiumAPI.id("machine.upgrade.$name"))
}
