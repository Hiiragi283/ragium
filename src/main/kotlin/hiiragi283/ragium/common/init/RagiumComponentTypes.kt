package hiiragi283.ragium.common.init

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.common.component.HTDynamiteComponent
import hiiragi283.ragium.common.component.HTRemoverDynamiteBehaviors
import hiiragi283.ragium.common.item.HTCrafterHammerItem
import net.minecraft.component.ComponentType
import net.minecraft.fluid.Fluid
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.DyeColor

object RagiumComponentTypes {
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
    val MACHINE_KEY: ComponentType<HTMachineKey> =
        register("machine_key", HTMachineKey.COMPONENT_TYPE)

    @JvmField
    val MACHINE_TIER: ComponentType<HTMachineTier> =
        register("machine_tier", HTMachineTier.COMPONENT_TYPE)

    //    Material    //
    
    @JvmField
    val MATERIAL_KEY: ComponentType<HTMaterialKey> =
        register("material_key", HTMaterialKey.COMPONENT_TYPE)
    
    @JvmField
    val TAG_PREFIX: ComponentType<HTTagPrefix> = 
        register("tag_prefix", HTTagPrefix.COMPONENT_TYPE)
    
    //    Misc    //

    @JvmField
    val COLOR: ComponentType<DyeColor> =
        register("color", DyeColor.CODEC, DyeColor.PACKET_CODEC)

    @JvmField
    val DAMAGE_INSTEAD_OF_DECREASE: ComponentType<Unit> =
        registerUnit("damage_instead_of_decrease")

    @JvmField
    val FLUID: ComponentType<Fluid> =
        register("fluid", Registries.FLUID.codec, PacketCodecs.codec(Registries.FLUID.codec))

    @JvmStatic
    private fun <T : Any> register(name: String, type: ComponentType<T>): ComponentType<T> = Registry.register(
        Registries.DATA_COMPONENT_TYPE,
        RagiumAPI.id(name),
        type,
    )

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
