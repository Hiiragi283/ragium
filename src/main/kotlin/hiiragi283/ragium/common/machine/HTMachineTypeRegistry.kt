package hiiragi283.ragium.common.machine

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.HTMachineTypeInitializer
import hiiragi283.ragium.common.Ragium
import io.netty.buffer.ByteBuf
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.network.codec.PacketCodec
import net.minecraft.util.Identifier

object HTMachineTypeRegistry {
    //    Codecs    //

    @JvmField
    val CODEC: Codec<HTMachineType> =
        Identifier.CODEC.xmap(::getOrThrow, HTMachineType::id)

    @JvmField
    val GENERATOR_CODEC: Codec<HTMachineType.Generator> =
        Identifier.CODEC.xmap(::getGeneratorOrThrow, HTMachineType::id)

    @JvmField
    val PROCESSOR_CODEC: Codec<HTMachineType.Processor> =
        Identifier.CODEC.xmap(::getProcessorOrThrow, HTMachineType::id)

    @JvmField
    val PACKET_CODEC: PacketCodec<ByteBuf, HTMachineType> =
        Identifier.PACKET_CODEC.xmap(::getOrThrow, HTMachineType::id)

    @JvmField
    val GENERATOR_PACKET_CODEC: PacketCodec<ByteBuf, HTMachineType.Generator> =
        Identifier.PACKET_CODEC.xmap(::getGeneratorOrThrow, HTMachineType::id)

    @JvmField
    val PROCESSOR_PACKET_CODEC: PacketCodec<ByteBuf, HTMachineType.Processor> =
        Identifier.PACKET_CODEC.xmap(::getProcessorOrThrow, HTMachineType::id)

    //    Registry    //

    @JvmStatic
    val registry: Map<Identifier, HTMachineType>
        get() = registry1
    private lateinit var registry1: MutableMap<Identifier, HTMachineType>

    @JvmStatic
    val types: Collection<HTMachineType>
        get() = registry.values

    @JvmStatic
    val generators: Collection<HTMachineType.Generator>
        get() = types.filterIsInstance<HTMachineType.Generator>()

    @JvmStatic
    val processors: Collection<HTMachineType.Processor>
        get() = types.filterIsInstance<HTMachineType.Processor>()

    @JvmStatic
    fun get(id: Identifier): HTMachineType? = registry[id]

    @JvmStatic
    fun getGenerator(id: Identifier): HTMachineType.Generator? = get(id)?.asGenerator()

    @JvmStatic
    fun getProcessor(id: Identifier): HTMachineType.Processor? = get(id)?.asProcessor()

    @JvmStatic
    fun getOrThrow(id: Identifier): HTMachineType = checkNotNull(get(id))

    @JvmStatic
    fun getGeneratorOrThrow(id: Identifier): HTMachineType.Generator = checkNotNull(getOrThrow(id).asGenerator())

    @JvmStatic
    fun getProcessorOrThrow(id: Identifier): HTMachineType.Processor = checkNotNull(getOrThrow(id).asProcessor())

    //    Init    //

    @JvmStatic
    fun init() {
        registry1 = mutableMapOf()
        FabricLoader.getInstance().invokeEntrypoints(
            HTMachineTypeInitializer.KEY,
            HTMachineTypeInitializer::class.java,
        ) { initializer: HTMachineTypeInitializer ->
            initializer.registerType(::addMachine)
        }
        Ragium.log { info("HTMachineTypeRegistry initialized!") }
    }

    @JvmStatic
    private fun addMachine(convertible: HTMachineConvertible) {
        val type: HTMachineType = convertible.asMachine()
        check(type.id !in registry) { "Machine Type; ${type.id} is already registered!" }
        registry1[type.id] = type
    }
}
