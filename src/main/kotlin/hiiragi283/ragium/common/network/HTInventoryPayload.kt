package hiiragi283.ragium.common.network

import hiiragi283.ragium.common.init.RagiumNetworks
import net.minecraft.item.ItemStack
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.network.packet.CustomPayload
import net.minecraft.util.math.BlockPos

sealed class HTInventoryPayload(val pos: BlockPos, val slot: Int) : CustomPayload {
    companion object {
        @JvmStatic
        fun createPacket(pos: BlockPos, slot: Int, stack: ItemStack): HTInventoryPayload = when {
            stack.isEmpty -> Remover(pos, slot)
            else -> Setter(pos, slot, stack)
        }
    }

    operator fun component1(): BlockPos = pos

    operator fun component2(): Int = slot

    class Setter(pos: BlockPos, slot: Int, val stack: ItemStack) : HTInventoryPayload(pos, slot) {
        companion object {
            @JvmField
            val PACKET_CODEC: PacketCodec<RegistryByteBuf, Setter> = PacketCodec.tuple(
                BlockPos.PACKET_CODEC,
                Setter::pos,
                PacketCodecs.INTEGER,
                Setter::slot,
                ItemStack.PACKET_CODEC,
                Setter::stack,
                ::Setter,
            )
        }

        override fun getId(): CustomPayload.Id<out CustomPayload> = RagiumNetworks.SET_STACK

        operator fun component3(): ItemStack = stack
    }

    class Remover(pos: BlockPos, slot: Int) : HTInventoryPayload(pos, slot) {
        companion object {
            @JvmField
            val PACKET_CODEC: PacketCodec<RegistryByteBuf, Remover> = PacketCodec.tuple(
                BlockPos.PACKET_CODEC,
                Remover::pos,
                PacketCodecs.INTEGER,
                Remover::slot,
                ::Remover,
            )
        }

        override fun getId(): CustomPayload.Id<out CustomPayload> = RagiumNetworks.REMOVE_STACK
    }
}
