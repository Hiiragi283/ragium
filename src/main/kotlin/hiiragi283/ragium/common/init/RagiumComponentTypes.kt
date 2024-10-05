package hiiragi283.ragium.common.init

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.inventory.HTSimpleInventory
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.api.machine.HTMachineTypeRegistry
import hiiragi283.ragium.common.alchemy.RagiElement
import net.minecraft.component.ComponentType
import net.minecraft.entity.EntityType
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

object RagiumComponentTypes {
    @JvmField
    val ELEMENT: ComponentType<RagiElement> =
        register("element", RagiElement.CODEC, RagiElement.PACKET_CODEC)

    @JvmField
    val ENTITY_TYPE: ComponentType<EntityType<*>> =
        Registries.ENTITY_TYPE.codec.let { register("entity_type", it, PacketCodecs.codec(it)) }

    @JvmField
    val INVENTORY: ComponentType<HTSimpleInventory> =
        register("inventory", HTSimpleInventory.CODEC, HTSimpleInventory.PACKET_CODEC)

    @JvmField
    val MACHINE_TYPE: ComponentType<HTMachineType> =
        register("machine_type", HTMachineTypeRegistry.CODEC, HTMachineTypeRegistry.PACKET_CODEC)

    @JvmField
    val TIER: ComponentType<HTMachineTier> =
        register("tier", HTMachineTier.CODEC, HTMachineTier.PACKET_CODEC)

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
}
