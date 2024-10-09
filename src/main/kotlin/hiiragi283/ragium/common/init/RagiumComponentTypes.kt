package hiiragi283.ragium.common.init

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.inventory.HTSimpleInventory
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.machine.HTMachineTypeRegistry
import hiiragi283.ragium.common.RagiumContents
import net.minecraft.component.ComponentType
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

object RagiumComponentTypes {
    @JvmField
    val ELEMENT: ComponentType<RagiumContents.Element> =
        register("element", RagiumContents.Element.CODEC, RagiumContents.Element.PACKET_CODEC)

    //    Armor    //

    @JvmField
    val INVENTORY: ComponentType<HTSimpleInventory> =
        register("inventory", HTSimpleInventory.CODEC, HTSimpleInventory.PACKET_CODEC)

    //    Tool    //

    //    Machine    //

    @JvmField
    val MACHINE_TYPE: ComponentType<HTMachineType> =
        register("machine_type", HTMachineTypeRegistry.CODEC, HTMachineTypeRegistry.PACKET_CODEC)

    @JvmField
    val MACHINE_TIER: ComponentType<HTMachineTier> =
        register("machine_tier", HTMachineTier.CODEC, HTMachineTier.PACKET_CODEC)

    @JvmStatic
    private fun <T : Any> register(name: String, codec: Codec<T>, packetCodec: PacketCodec<in RegistryByteBuf, T>): ComponentType<T> =
        Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            RagiumAPI.id(name),
            ComponentType
                .builder<T>()
                .codec(codec)
                .packetCodec(packetCodec)
                .build(),
        )

    @JvmStatic
    private fun registerUnit(name: String): ComponentType<Unit> = register(name, Codec.unit(Unit), PacketCodec.unit(Unit))
}
