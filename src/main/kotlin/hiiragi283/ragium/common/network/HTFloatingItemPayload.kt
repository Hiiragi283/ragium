package hiiragi283.ragium.common.network

import hiiragi283.ragium.api.extension.entryPacketCodec
import hiiragi283.ragium.common.init.RagiumNetworks
import net.minecraft.item.ItemStack
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.packet.CustomPayload
import net.minecraft.particle.ParticleType
import net.minecraft.registry.Registries
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.sound.SoundEvent

data class HTFloatingItemPayload(
    val stack: ItemStack,
    val particle: RegistryEntry<ParticleType<*>>,
    val soundEvent: RegistryEntry<SoundEvent>,
) : CustomPayload {
    companion object {
        @JvmField
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, HTFloatingItemPayload> = PacketCodec.tuple(
            ItemStack.OPTIONAL_PACKET_CODEC,
            HTFloatingItemPayload::stack,
            Registries.PARTICLE_TYPE.entryPacketCodec,
            HTFloatingItemPayload::particle,
            SoundEvent.ENTRY_PACKET_CODEC,
            HTFloatingItemPayload::soundEvent,
            ::HTFloatingItemPayload,
        )
    }

    override fun getId(): CustomPayload.Id<out CustomPayload> = RagiumNetworks.FLOATING_ITEM
}
