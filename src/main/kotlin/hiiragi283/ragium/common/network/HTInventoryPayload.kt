package hiiragi283.ragium.common.network

import hiiragi283.ragium.common.init.RagiumNetworks
import net.minecraft.item.ItemStack
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.network.packet.CustomPayload
import net.minecraft.util.math.BlockPos

data class HTInventoryPayload(val pos: BlockPos, val slot: Int, val stack: ItemStack) : CustomPayload {
    companion object {
        @JvmField
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, HTInventoryPayload> = PacketCodec.tuple(
            BlockPos.PACKET_CODEC,
            HTInventoryPayload::pos,
            PacketCodecs.VAR_INT,
            HTInventoryPayload::slot,
            ItemStack.OPTIONAL_PACKET_CODEC,
            HTInventoryPayload::stack,
            ::HTInventoryPayload,
        )
    }

    override fun getId(): CustomPayload.Id<out CustomPayload> = RagiumNetworks.ITEM_SYNC
}
