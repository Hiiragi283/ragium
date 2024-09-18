package hiiragi283.ragium.common.init

import com.mojang.serialization.Codec
import hiiragi283.ragium.common.Ragium
import hiiragi283.ragium.common.component.HTTooltipsComponent
import hiiragi283.ragium.common.machine.HTMachineTier
import net.minecraft.component.ComponentType
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry

object RagiumComponentTypes {
    @JvmField
    val DISABLE_CYCLE_POWER: ComponentType<Unit> =
        register("disable_cycle_power", Codec.unit(Unit), PacketCodec.unit(Unit))

    @JvmField
    val TIER: ComponentType<HTMachineTier> =
        register("tier", HTMachineTier.CODEC, HTMachineTier.PACKET_CODEC)

    @JvmField
    val TOOLTIPS: ComponentType<HTTooltipsComponent> =
        register("tooltips", HTTooltipsComponent.CODEC, HTTooltipsComponent.PACKET_CODEC)

    @JvmField
    val DISABLE_TOOLTIPS: ComponentType<Unit> =
        register("disable_tooltips", Codec.unit(Unit), PacketCodec.unit(Unit))

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
