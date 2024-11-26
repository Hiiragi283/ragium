package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.machine.HTMachinePacket
import hiiragi283.ragium.common.network.HTFloatingItemPayload
import hiiragi283.ragium.common.network.HTFluidStoragePayload
import hiiragi283.ragium.common.network.HTFluidSyncPayload
import hiiragi283.ragium.common.network.HTInventoryPayload
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.packet.CustomPayload
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.BlockPos

object RagiumNetworks {
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
    private fun <T : CustomPayload> registerC2S(name: String, codec: PacketCodec<RegistryByteBuf, T>): CustomPayload.Id<T> {
        val id = CustomPayload.Id<T>(RagiumAPI.id(name))
        PayloadTypeRegistry.playC2S().register(id, codec)
        return id
    }

    //    Utils    //

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
    fun sendFloatingItem(player: ServerPlayerEntity, stack: ItemStack) {
        ServerPlayNetworking.send(player, HTFloatingItemPayload(stack))
    }

    @JvmStatic
    fun sendFloatingItem(player: ServerPlayerEntity, item: ItemConvertible, count: Int = 1) {
        ServerPlayNetworking.send(player, HTFloatingItemPayload(item, count))
    }
}
