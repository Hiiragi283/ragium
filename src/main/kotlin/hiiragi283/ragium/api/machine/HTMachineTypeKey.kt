package hiiragi283.ragium.api.machine

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.RagiumAPI
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.util.Identifier

class HTMachineTypeKey private constructor(val id: Identifier) : HTMachineConvertible {
    companion object {
        private val instances: MutableMap<Identifier, HTMachineTypeKey> = mutableMapOf()

        @JvmField
        val CODEC: Codec<HTMachineTypeKey> = Identifier.CODEC.xmap(Companion::of, HTMachineTypeKey::id)

        @JvmField
        val PACKET_CODEC: PacketCodec<ByteBuf, HTMachineTypeKey> =
            Identifier.PACKET_CODEC.xmap(Companion::of, HTMachineTypeKey::id)

        @JvmField
        val DEFAULT: HTMachineTypeKey = of(RagiumAPI.id("default"))

        @JvmStatic
        fun of(id: Identifier): HTMachineTypeKey = instances.computeIfAbsent(id, ::HTMachineTypeKey)
    }

    private lateinit var cache: HTMachineType

    override fun asMachine(): HTMachineType {
        if (!::cache.isInitialized) {
            cache = RagiumAPI.getInstance().machineTypeRegistry.getOrThrow(id)
        }
        return cache
    }
}
