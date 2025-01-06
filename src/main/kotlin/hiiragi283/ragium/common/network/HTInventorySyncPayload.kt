package hiiragi283.ragium.common.network

import hiiragi283.ragium.api.extension.asMap
import hiiragi283.ragium.common.init.RagiumNetworks
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.network.packet.CustomPayload
import net.minecraft.util.math.BlockPos

data class HTInventorySyncPayload(val pos: BlockPos, val stackMap: Map<Int, ItemStack>) : CustomPayload {
    companion object {
        @JvmField
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, HTInventorySyncPayload> = PacketCodec.tuple(
            BlockPos.PACKET_CODEC,
            HTInventorySyncPayload::pos,
            PacketCodecs.map(
                ::Int2ObjectOpenHashMap,
                PacketCodecs.INTEGER,
                ItemStack.OPTIONAL_PACKET_CODEC,
            ),
            HTInventorySyncPayload::stackMap,
            ::HTInventorySyncPayload,
        )
    }

    constructor(pos: BlockPos, inventory: Inventory) : this(pos, inventory.asMap())

    override fun getId(): CustomPayload.Id<out CustomPayload> = RagiumNetworks.INVENTORY_SYNC
}
