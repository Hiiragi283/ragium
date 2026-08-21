package hiiragi283.lib.gui.sync

import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack

@JvmRecord
data class HTItemSyncPayload(val value: ItemStack) : HTSyncablePayload {
    companion object {
        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTItemSyncPayload> = ItemStack.OPTIONAL_STREAM_CODEC.map(::HTItemSyncPayload, HTItemSyncPayload::value)

        @JvmField
        val TYPE: HTSyncablePayload.Type<HTItemSyncPayload> = HTSyncablePayload.Type(STREAM_CODEC)
    }

    override fun type(): HTSyncablePayload.Type<*> = TYPE

    @Suppress("UNCHECKED_CAST")
    override fun setValue(menu: HTSyncableMenu, index: Int) {
        val slot: HTSyncableSlot? = menu.getTrackedSlot(index)
        if (slot is HTItemSyncSlot) {
            slot.asItemStack = this.value
        }
    }
}
