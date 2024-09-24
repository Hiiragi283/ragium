package hiiragi283.ragium.common.init

import com.mojang.serialization.Codec
import hiiragi283.ragium.common.Ragium
import hiiragi283.ragium.common.alchemy.RagiElement
import hiiragi283.ragium.common.inventory.HTSimpleInventory
import hiiragi283.ragium.common.machine.HTMachineTier
import net.minecraft.component.ComponentType
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

object RagiumComponentTypes {
    @JvmField
    val ELEMENT: ComponentType<RagiElement> =
        register("element", RagiElement.CODEC, RagiElement.PACKET_CODEC)

    @JvmField
    val INVENTORY: ComponentType<HTSimpleInventory> =
        register("inventory", HTSimpleInventory.CODEC, HTSimpleInventory.PACKET_CODEC)

    @JvmField
    val TIER: ComponentType<HTMachineTier> =
        register("tier", HTMachineTier.CODEC, HTMachineTier.PACKET_CODEC)

    @JvmStatic
    private fun <T : Any> register(name: String, codec: Codec<T>, packetCodec: PacketCodec<in RegistryByteBuf, T>): ComponentType<T> =
        Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Ragium.id(name),
            ComponentType
                .builder<T>()
                .codec(codec)
                .packetCodec(packetCodec)
                .build(),
        )
}
