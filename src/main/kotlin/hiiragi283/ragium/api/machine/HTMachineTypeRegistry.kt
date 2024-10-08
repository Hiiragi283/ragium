package hiiragi283.ragium.api.machine

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.RagiumAPI
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.util.Identifier

class HTMachineTypeRegistry(val registry: Map<Identifier, HTMachineType>) {
    companion object {
        //    Codecs    //

        @JvmField
        val CODEC: Codec<HTMachineType> =
            Identifier.CODEC.xmap({ RagiumAPI.getInstance().machineTypeRegistry.getOrThrow(it) }, HTMachineType::id)

        @JvmField
        val PROCESSOR_CODEC: Codec<HTMachineType> = Identifier.CODEC.xmap(
            { RagiumAPI.getInstance().machineTypeRegistry.getProcessorOrThrow(it) },
            HTMachineType::id,
        )

        @JvmField
        val PACKET_CODEC: PacketCodec<ByteBuf, HTMachineType> = Identifier.PACKET_CODEC.xmap(
            { RagiumAPI.getInstance().machineTypeRegistry.getOrThrow(it) },
            HTMachineType::id,
        )

        @JvmField
        val PROCESSOR_PACKET_CODEC: PacketCodec<ByteBuf, HTMachineType> = Identifier.PACKET_CODEC.xmap(
            { RagiumAPI.getInstance().machineTypeRegistry.getProcessorOrThrow(it) },
            HTMachineType::id,
        )
    }

    //    Registry    //

    val types: Collection<HTMachineType>
        get() = registry.values

    val generators: Collection<HTMachineType>
        get() = types.filter(HTMachineType::isGenerator)

    val processors: Collection<HTMachineType>
        get() = types.filter(HTMachineType::isProcessor)

    fun get(id: Identifier): HTMachineType? = registry[id]

    fun getOrThrow(id: Identifier): HTMachineType = checkNotNull(get(id))

    fun getProcessorOrThrow(id: Identifier): HTMachineType = checkNotNull(getOrThrow(id).takeIf(HTMachineType::isProcessor))
}
