package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.asServerPlayer
import hiiragi283.ragium.api.machine.HTMachinePacket
import hiiragi283.ragium.common.network.HTFloatingItemPayload
import hiiragi283.ragium.common.network.HTFluidStoragePayload
import hiiragi283.ragium.common.network.HTFluidSyncPayload
import hiiragi283.ragium.common.network.HTInventoryPayload
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
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
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.BlockPos

object RagiumNetworks {
    //    S2C    //

    @JvmField
    val FLOATING_ITEM: CustomPayload.Id<HTFloatingItemPayload> =
        registerS2C("floating_item", HTFloatingItemPayload.PACKET_CODEC)

    @JvmField
    val FLUID_STORAGE: CustomPayload.Id<HTFluidStoragePayload> =
        registerS2C("fluid_storage", HTFluidStoragePayload.PACKET_CODEC)

    @JvmField
    val FLUID_SYNC: CustomPayload.Id<HTFluidSyncPayload> =
        registerS2C("fluid_sync", HTFluidSyncPayload.PACKET_CODEC)

    @JvmField
    val ITEM_SYNC: CustomPayload.Id<HTInventoryPayload> =
        registerS2C("item_sync", HTInventoryPayload.PACKET_CODEC)

    @JvmField
    val MACHINE_SYNC: CustomPayload.Id<HTMachinePacket> =
        registerS2C("machine_sync", HTMachinePacket.PACKET_CODEC)

    @JvmStatic
    private fun <T : CustomPayload> registerS2C(name: String, codec: PacketCodec<RegistryByteBuf, T>): CustomPayload.Id<T> {
        val id = CustomPayload.Id<T>(RagiumAPI.id(name))
        PayloadTypeRegistry.playS2C().register(id, codec)
        return id
    }

    @JvmStatic
    fun sendFluidSync(
        player: ServerPlayerEntity,
        index: Int,
        variant: FluidVariant,
        amount: Long,
    ) {
        ServerPlayNetworking.send(player, HTFluidSyncPayload(index, variant, amount))
    }

    @JvmStatic
    fun sendItemSync(
        player: ServerPlayerEntity,
        pos: BlockPos,
        slot: Int,
        stack: ItemStack,
    ) {
        ServerPlayNetworking.send(player, HTInventoryPayload(pos, slot, stack))
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
            ?.let { ServerPlayNetworking.send(it, HTFloatingItemPayload(stack, particleEntry, soundEntry)) }
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
