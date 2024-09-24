package hiiragi283.ragium.common.network

import hiiragi283.ragium.common.init.RagiumNetworks
import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.packet.CustomPayload

class HTFloatingItemPayload(val stack: ItemStack) : CustomPayload {
    companion object {
        @JvmField
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, HTFloatingItemPayload> =
            ItemStack.PACKET_CODEC.xmap(::HTFloatingItemPayload, HTFloatingItemPayload::stack)
    }

    constructor(item: ItemConvertible, count: Int = 1) : this(ItemStack(item, count))

    override fun getId(): CustomPayload.Id<out CustomPayload> = RagiumNetworks.FLOATING_ITEM
}
