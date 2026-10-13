package hiiragi283.lib.gui.sync

import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec

@JvmRecord
data class HTLongSyncPayload(val value: Long) : HTSyncablePayload {
    companion object {
        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTLongSyncPayload> =
            ByteBufCodecs.VAR_LONG.map(::HTLongSyncPayload, HTLongSyncPayload::value).cast()

        @JvmField
        val TYPE: HTSyncablePayload.Type<HTLongSyncPayload> = HTSyncablePayload.Type(STREAM_CODEC)
    }

    override fun type(): HTSyncablePayload.Type<*> = TYPE

    override fun setValue(menu: HTSyncableMenu, index: Int) {
        val slot: HTSyncableSlot? = menu.getTrackedSlot(index)
        if (slot is HTLongSyncSlot) {
            slot.amountAsLong = this.value
        }
    }
}
