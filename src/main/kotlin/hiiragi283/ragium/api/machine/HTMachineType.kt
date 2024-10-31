package hiiragi283.ragium.api.machine

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.codecOf
import hiiragi283.ragium.api.extension.packetCodecOf
import hiiragi283.ragium.api.inventory.HTSimpleInventory
import hiiragi283.ragium.api.inventory.HTStorageBuilder
import hiiragi283.ragium.api.inventory.HTStorageIO
import hiiragi283.ragium.api.inventory.HTStorageSide
import hiiragi283.ragium.api.property.HTCombinedPropertyHolder
import hiiragi283.ragium.api.property.HTDelegatedPropertyHolder
import hiiragi283.ragium.api.property.HTPropertyHolder
import net.fabricmc.api.EnvType
import net.minecraft.component.ComponentType
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.util.StringIdentifiable

sealed class HTMachineType :
    HTMachineConvertible,
    HTDelegatedPropertyHolder {
    companion object {
        @JvmField
        val COMPONENT_TYPE: ComponentType<HTMachineType> = ComponentType
            .builder<HTMachineType>()
            .codec(HTMachineTypeRegistry.CODEC)
            .packetCodec(HTMachineTypeRegistry.PACKET_CODEC)
            .build()
    }

    //    HTMachineConvertible    //

    override fun asMachine(): HTMachineType = this

    //    HTPropertyHolder.Delegated    //

    override val delegated: HTPropertyHolder
        get() = object : HTCombinedPropertyHolder {
            override val properties: List<HTPropertyHolder> = buildList {
                add(RagiumAPI.getInstance().getMachineRegistry(EnvType.SERVER).getOrEmpty(key))
                add(RagiumAPI.getInstance().getMachineRegistry(EnvType.CLIENT).getOrEmpty(key))
            }
        }

    //    Default    //

    data object Default : HTMachineType() {
        override val key: HTMachineTypeKey
            get() = throw UnsupportedOperationException()
    }

    //    Generator    //

    @Suppress("CanSealedSubClassBeObject")
    class Generator : HTMachineType()

    //    Processor    //

    @Suppress("CanSealedSubClassBeObject")
    class Processor : HTMachineType()

    //    Size    //

    enum class Size(val invSize: Int) : StringIdentifiable {
        SIMPLE(5) {
            override fun createInventory(): HTSimpleInventory = HTStorageBuilder(5)
                .set(0, HTStorageIO.INPUT, HTStorageSide.ANY)
                .set(1, HTStorageIO.INPUT, HTStorageSide.ANY)
                .set(2, HTStorageIO.INTERNAL, HTStorageSide.NONE)
                .set(3, HTStorageIO.OUTPUT, HTStorageSide.ANY)
                .set(4, HTStorageIO.OUTPUT, HTStorageSide.ANY)
                .buildSimple()
        },
        LARGE(7) {
            override fun createInventory(): HTSimpleInventory = HTStorageBuilder(7)
                .set(0, HTStorageIO.INPUT, HTStorageSide.ANY)
                .set(1, HTStorageIO.INPUT, HTStorageSide.ANY)
                .set(2, HTStorageIO.INPUT, HTStorageSide.ANY)
                .set(3, HTStorageIO.INTERNAL, HTStorageSide.NONE)
                .set(4, HTStorageIO.OUTPUT, HTStorageSide.ANY)
                .set(5, HTStorageIO.OUTPUT, HTStorageSide.ANY)
                .set(6, HTStorageIO.OUTPUT, HTStorageSide.ANY)
                .buildSimple()
        },
        ;

        companion object {
            @JvmField
            val CODEC: Codec<Size> = codecOf(entries)

            @JvmField
            val PACKET_CODEC: PacketCodec<RegistryByteBuf, Size> = packetCodecOf(entries)
        }

        abstract fun createInventory(): HTSimpleInventory

        override fun asString(): String = name.lowercase()
    }
}
