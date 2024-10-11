package hiiragi283.ragium.api.machine

import com.google.common.collect.BiMap
import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.RagiumAPI
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.util.Identifier

class HTMachineTypeRegistry(val registry: BiMap<HTMachineTypeKey, HTMachineType>) {
    companion object {
        //    Codecs    //

        @JvmField
        val CODEC: Codec<HTMachineType> =
            HTMachineTypeKey.CODEC.xmap(
                { RagiumAPI.getInstance().machineTypeRegistry.getOrThrow(it) },
                { RagiumAPI.getInstance().machineTypeRegistry.getKeyOrThrow(it) },
            )

        @JvmField
        val PROCESSOR_CODEC: Codec<HTMachineType> = CODEC.validate {
            when (it.isProcessor()) {
                true -> DataResult.success(it)
                false -> DataResult.error { "Given machine type; ${it.id} is not a processor!" }
            }
        }

        @JvmField
        val PACKET_CODEC: PacketCodec<ByteBuf, HTMachineType> =
            HTMachineTypeKey.PACKET_CODEC.xmap(
                { RagiumAPI.getInstance().machineTypeRegistry.getOrThrow(it) },
                { RagiumAPI.getInstance().machineTypeRegistry.getKeyOrThrow(it) },
            )
    }

    //    Registry    //

    val types: Collection<HTMachineType>
        get() = registry.values

    val generators: Collection<HTMachineType>
        get() = types.filter(HTMachineType::isGenerator)

    val processors: Collection<HTMachineType>
        get() = types.filter(HTMachineType::isProcessor)

    fun get(key: HTMachineTypeKey): HTMachineType? = registry[key]

    fun get(id: Identifier): HTMachineType? = get(HTMachineTypeKey.of(id))

    fun getOrThrow(key: HTMachineTypeKey): HTMachineType = checkNotNull(get(key))

    fun getOrThrow(id: Identifier): HTMachineType = checkNotNull(get(id))

    fun getProcessorOrThrow(key: HTMachineTypeKey): HTMachineType = checkNotNull(getOrThrow(key).takeIf(HTMachineType::isProcessor))

    fun getProcessorOrThrow(id: Identifier): HTMachineType = checkNotNull(getOrThrow(id).takeIf(HTMachineType::isProcessor))

    fun getKey(type: HTMachineType): HTMachineTypeKey? = registry.inverse()[type]

    fun getKeyOrThrow(type: HTMachineType): HTMachineTypeKey = checkNotNull(getKey(type))
}
