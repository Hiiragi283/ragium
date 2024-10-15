package hiiragi283.ragium.common.init

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.inventory.HTSimpleInventory
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.common.component.HTDynamiteComponent
import hiiragi283.ragium.common.component.HTRemoverDynamiteBehaviors
import hiiragi283.ragium.common.item.HTCrafterHammerItem
import net.minecraft.component.ComponentType
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

object RagiumComponentTypes {
    //    Armor    //

    @JvmField
    val INVENTORY: ComponentType<HTSimpleInventory> =
        register("inventory", HTSimpleInventory.COMPONENT_TYPE)

    //    Tool    //

    @JvmField
    val DYNAMITE: ComponentType<HTDynamiteComponent> =
        register("dynamite", HTDynamiteComponent.CODEC, HTDynamiteComponent.PACKET_CODEC)

    @JvmField
    val CRAFTER_HAMMER: ComponentType<HTCrafterHammerItem.Component> =
        register("crafter_hammer", HTCrafterHammerItem.Component.COMPONENT_TYPE)

    @JvmField
    val REMOVER_DYNAMITE: ComponentType<HTRemoverDynamiteBehaviors> =
        register("remover_dynamite", HTRemoverDynamiteBehaviors.CODEC, HTRemoverDynamiteBehaviors.PACKET_CODEC)

    //    Machine    //

    @JvmField
    val MACHINE_TYPE: ComponentType<HTMachineType> =
        register("machine_type", HTMachineType.COMPONENT_TYPE)

    @JvmField
    val MACHINE_TIER: ComponentType<HTMachineTier> =
        register("machine_tier", HTMachineTier.COMPONENT_TYPE)

    @JvmStatic
    private fun <T : Any> register(name: String, type: ComponentType<T>): ComponentType<T> = Registry.register(
        Registries.DATA_COMPONENT_TYPE,
        RagiumAPI.id(name),
        type,
    )

    //    Misc    //

    @JvmField
    val DAMAGE_INSTEAD_OF_DECREASE: ComponentType<Unit> =
        registerUnit("damage_instead_of_decrease")

    @JvmStatic
    private fun <T : Any> register(name: String, codec: Codec<T>, packetCodec: PacketCodec<in RegistryByteBuf, T>): ComponentType<T> =
        register(
            name,
            ComponentType
                .builder<T>()
                .codec(codec)
                .packetCodec(packetCodec)
                .build(),
        )

    @JvmStatic
    private fun registerUnit(name: String): ComponentType<Unit> = register(name, Codec.unit(Unit), PacketCodec.unit(Unit))
}
