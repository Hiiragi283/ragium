package hiiragi283.ragium.api.machine

import com.google.common.collect.BiMap
import com.mojang.serialization.Codec
import hiiragi283.ragium.api.RagiumAPI
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.util.Identifier

class HTMachineTypeRegistry(val registry: BiMap<HTMachineTypeKey, HTMachineType>) {
    companion object {
        //    Codecs    //

        @JvmField
        val CODEC: Codec<HTMachineType> =
            HTMachineTypeKey.VALIDATED_CODEC.xmap(
                { RagiumAPI.getInstance().machineTypeRegistry.getOrThrow(it) },
                { RagiumAPI.getInstance().machineTypeRegistry.getKeyOrThrow(it) },
            )

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

    val consumers: Collection<HTMachineType.Consumer>
        get() = types.mapNotNull { it.asMachine() as? HTMachineType.Consumer }

    val generators: Collection<HTMachineType.Generator>
        get() = types.mapNotNull { it.asMachine() as? HTMachineType.Generator }

    val processors: Collection<HTMachineType.Processor>
        get() = types.mapNotNull { it.asMachine() as? HTMachineType.Processor }

    fun get(key: HTMachineTypeKey): HTMachineType? = registry[key]

    fun get(id: Identifier): HTMachineType? = get(HTMachineTypeKey.of(id))

    fun getOrThrow(key: HTMachineTypeKey): HTMachineType = checkNotNull(get(key))

    fun getOrThrow(id: Identifier): HTMachineType = checkNotNull(get(id))

    fun getKey(type: HTMachineType): HTMachineTypeKey? = registry.inverse()[type]

    fun getKeyOrThrow(type: HTMachineType): HTMachineTypeKey = checkNotNull(getKey(type))
}
