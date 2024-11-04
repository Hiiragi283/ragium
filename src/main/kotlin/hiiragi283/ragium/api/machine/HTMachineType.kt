package hiiragi283.ragium.api.machine

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.extension.codecOf
import hiiragi283.ragium.api.extension.packetCodecOf
import hiiragi283.ragium.api.inventory.HTSidedInventory
import hiiragi283.ragium.api.inventory.HTStorageBuilder
import hiiragi283.ragium.api.inventory.HTStorageIO
import hiiragi283.ragium.api.inventory.HTStorageSide
import io.netty.buffer.ByteBuf
import net.minecraft.component.ComponentType
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.util.StringIdentifiable

sealed class HTMachineType : HTMachine {
    companion object {
        @JvmField
        val CODEC: Codec<HTMachineType> =
            HTMachineKey.CODEC.xmap(
                HTMachineType::Processor,
                HTMachineType::key,
            )

        @JvmField
        val PACKET_CODEC: PacketCodec<ByteBuf, HTMachineType> =
            HTMachineKey.PACKET_CODEC.xmap(
                HTMachineType::Processor,
                HTMachineType::key,
            )

        @JvmField
        val COMPONENT_TYPE: ComponentType<HTMachineType> = ComponentType
            .builder<HTMachineType>()
            .codec(CODEC)
            .packetCodec(PACKET_CODEC)
            .build()
    }

    override fun toString(): String = "MachineType[${key.id}]"

    //    Consumer    //

    class Consumer(override val key: HTMachineKey) : HTMachineType()

    //    Generator    //

    class Generator(override val key: HTMachineKey) : HTMachineType()

    //    Processor    //

    class Processor(override val key: HTMachineKey) : HTMachineType()

    //    Size    //

    enum class Size(val invSize: Int) : StringIdentifiable {
        SIMPLE(5) {
            override fun createInventory(): HTSidedInventory = HTStorageBuilder(5)
                .set(0, HTStorageIO.INPUT, HTStorageSide.ANY)
                .set(1, HTStorageIO.INPUT, HTStorageSide.ANY)
                .set(2, HTStorageIO.INTERNAL, HTStorageSide.NONE)
                .set(3, HTStorageIO.OUTPUT, HTStorageSide.ANY)
                .set(4, HTStorageIO.OUTPUT, HTStorageSide.ANY)
                .buildSided()
        },
        LARGE(7) {
            override fun createInventory(): HTSidedInventory = HTStorageBuilder(7)
                .set(0, HTStorageIO.INPUT, HTStorageSide.ANY)
                .set(1, HTStorageIO.INPUT, HTStorageSide.ANY)
                .set(2, HTStorageIO.INPUT, HTStorageSide.ANY)
                .set(3, HTStorageIO.INTERNAL, HTStorageSide.NONE)
                .set(4, HTStorageIO.OUTPUT, HTStorageSide.ANY)
                .set(5, HTStorageIO.OUTPUT, HTStorageSide.ANY)
                .set(6, HTStorageIO.OUTPUT, HTStorageSide.ANY)
                .buildSided()
        },
        ;

        companion object {
            @JvmField
            val CODEC: Codec<Size> = codecOf(entries)

            @JvmField
            val PACKET_CODEC: PacketCodec<RegistryByteBuf, Size> = packetCodecOf(entries)
        }

        abstract fun createInventory(): HTSidedInventory

        override fun asString(): String = name.lowercase()
    }
}
