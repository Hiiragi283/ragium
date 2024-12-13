package hiiragi283.ragium.api.material

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.toDataResult
import io.netty.buffer.ByteBuf
import net.minecraft.component.ComponentType
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.text.MutableText
import net.minecraft.text.Text

/**
 * Represents material type
 *
 * Must be registered in [HTMaterialRegistry]
 */
class HTMaterialKey private constructor(val name: String) : Comparable<HTMaterialKey> {
    companion object {
        private val instances: MutableMap<String, HTMaterialKey> = mutableMapOf()

        @JvmField
        val CODEC: Codec<HTMaterialKey> =
            Codec.STRING.xmap(Companion::of, HTMaterialKey::name).validate { key: HTMaterialKey ->
                key
                    .takeIf { it in RagiumAPI.getInstance().materialRegistry }
                    .toDataResult { "Unknown material key: $key" }
            }

        @JvmField
        val PACKET_CODEC: PacketCodec<ByteBuf, HTMaterialKey> =
            PacketCodecs.STRING.xmap(Companion::of, HTMaterialKey::name)

        @JvmField
        val COMPONENT_TYPE: ComponentType<HTMaterialKey> =
            ComponentType
                .builder<HTMaterialKey>()
                .codec(CODEC)
                .packetCodec(PACKET_CODEC)
                .build()

        /**
         * Get singleton instance or create new [hiiragi283.ragium.api.material.HTMaterialKey] instance from [name]
         */
        @JvmStatic
        fun of(name: String): HTMaterialKey = instances.computeIfAbsent(name, ::HTMaterialKey)
    }

    val translationKey: String = "material.${RagiumAPI.MOD_ID}.$name"
    val text: MutableText
        get() = Text.translatable(translationKey)

    val entry: HTMaterialRegistry.Entry by lazy { RagiumAPI.getInstance().materialRegistry.getEntry(this) }

    //    Comparable    //

    override fun compareTo(other: HTMaterialKey): Int = name.compareTo(other.name)

    override fun toString(): String = "HTMaterialKey[$name]"
}
