package hiiragi283.ragium.api.upgrade

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.serialization.codec.BiCodec
import hiiragi283.ragium.api.text.HTTranslation
import io.netty.buffer.ByteBuf
import net.minecraft.Util

@ConsistentCopyVisibility
data class HTUpgradeGroup private constructor(val name: String) : HTTranslation {
    companion object {
        @JvmStatic
        private val instances: MutableMap<String, HTUpgradeGroup> = mutableMapOf()

        @JvmField
        val CODEC: BiCodec<ByteBuf, HTUpgradeGroup> = BiCodec.STRING.xmap(HTUpgradeGroup::get, HTUpgradeGroup::name)

        @JvmStatic
        fun get(name: String): HTUpgradeGroup = instances.computeIfAbsent(name, ::HTUpgradeGroup)

        @JvmStatic
        fun getAll(): Collection<HTUpgradeGroup> = instances.values
    }

    override val translationKey: String = Util.makeDescriptionId("gui", RagiumAPI.id("machine.upgrade.group.$name"))
}
