package hiiragi283.ragium.common.network

import hiiragi283.ragium.common.init.RagiumNetworks
import net.minecraft.fluid.Fluid
import net.minecraft.item.Item
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.network.packet.CustomPayload
import net.minecraft.registry.RegistryKeys
import net.minecraft.registry.entry.RegistryEntryList

sealed class HTUpdateFilterPayload<T : Any>(val entryList: RegistryEntryList<T>) : CustomPayload {
    companion object {
        @JvmField
        val FLUID_PACKET_CODEC: PacketCodec<RegistryByteBuf, FluidFilter> =
            PacketCodecs.registryEntryList(RegistryKeys.FLUID).xmap(::FluidFilter, FluidFilter::entryList)

        @JvmField
        val ITEM_PACKET_CODEC: PacketCodec<RegistryByteBuf, ItemFilter> =
            PacketCodecs.registryEntryList(RegistryKeys.ITEM).xmap(::ItemFilter, ItemFilter::entryList)
    }

    class FluidFilter(entryList: RegistryEntryList<Fluid>) : HTUpdateFilterPayload<Fluid>(entryList) {
        override fun getId(): CustomPayload.Id<out CustomPayload> = RagiumNetworks.UPDATE_FLUID_FILTER
    }

    class ItemFilter(entryList: RegistryEntryList<Item>) : HTUpdateFilterPayload<Item>(entryList) {
        override fun getId(): CustomPayload.Id<out CustomPayload> = RagiumNetworks.UPDATE_ITEM_FILTER
    }
}
