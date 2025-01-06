package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.asServerPlayer
import hiiragi283.ragium.common.network.HTCratePreviewPayload
import hiiragi283.ragium.common.network.HTFloatingItemPayload
import hiiragi283.ragium.common.network.HTFluidSyncPayload
import hiiragi283.ragium.common.network.HTInventorySyncPayload
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.packet.CustomPayload
import net.minecraft.particle.ParticleType
import net.minecraft.particle.ParticleTypes
import net.minecraft.particle.SimpleParticleType
import net.minecraft.registry.Registries
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents

object RagiumNetworks {
    //    S2C    //

    @JvmField
    val CRATE_PREVIEW: CustomPayload.Id<HTCratePreviewPayload> =
        registerS2C("crate_preview", HTCratePreviewPayload.PACKET_CODEC)

    @JvmField
    val FLOATING_ITEM: CustomPayload.Id<HTFloatingItemPayload> =
        registerS2C("floating_item", HTFloatingItemPayload.PACKET_CODEC)

    @JvmField
    val FLUID_SYNC: CustomPayload.Id<HTFluidSyncPayload> =
        registerS2C("fluid_sync", HTFluidSyncPayload.PACKET_CODEC)

    @JvmField
    val INVENTORY_SYNC: CustomPayload.Id<HTInventorySyncPayload> =
        registerS2C("inventory_sync", HTInventorySyncPayload.PACKET_CODEC)

    @JvmStatic
    private fun <T : CustomPayload> registerS2C(name: String, codec: PacketCodec<RegistryByteBuf, T>): CustomPayload.Id<T> {
        val id = CustomPayload.Id<T>(RagiumAPI.id(name))
        PayloadTypeRegistry.playS2C().register(id, codec)
        return id
    }

    @JvmStatic
    fun sendFloatingItem(
        player: PlayerEntity,
        stack: ItemStack,
        particle: SimpleParticleType = ParticleTypes.TOTEM_OF_UNDYING,
        soundEvent: SoundEvent = SoundEvents.ITEM_TOTEM_USE,
    ) {
        val particleEntry: RegistryEntry<ParticleType<*>> = Registries.PARTICLE_TYPE.getEntry(particle)
        val soundEntry: RegistryEntry<SoundEvent> = Registries.SOUND_EVENT.getEntry(soundEvent)
        player
            .asServerPlayer()
            ?.let { ServerPlayNetworking.send(it, HTFloatingItemPayload(stack.copy(), particleEntry, soundEntry)) }
    }

    @JvmStatic
    fun sendFloatingItem(
        player: PlayerEntity,
        item: ItemConvertible,
        count: Int = 1,
        particle: SimpleParticleType = ParticleTypes.TOTEM_OF_UNDYING,
        soundEvent: SoundEvent = SoundEvents.ITEM_TOTEM_USE,
    ) {
        sendFloatingItem(player, ItemStack(item, count), particle, soundEvent)
    }

    //    C2S    //

    @JvmStatic
    private fun <T : CustomPayload> registerC2S(name: String, codec: PacketCodec<RegistryByteBuf, T>): CustomPayload.Id<T> {
        val id = CustomPayload.Id<T>(RagiumAPI.id(name))
        PayloadTypeRegistry.playC2S().register(id, codec)
        return id
    }
}
