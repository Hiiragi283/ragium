package hiiragi283.lib.gui.sync

import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec

@JvmRecord
data class HTFloatSyncPayload(val value: Float) : HTSyncablePayload {
    companion object {
        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTFloatSyncPayload> =
            ByteBufCodecs.FLOAT.map(::HTFloatSyncPayload, HTFloatSyncPayload::value).cast()

        @JvmField
        val TYPE: HTSyncablePayload.Type<HTFloatSyncPayload> = HTSyncablePayload.Type(STREAM_CODEC)
    }

    override fun type(): HTSyncablePayload.Type<*> = TYPE

    override fun setValue(menu: HTSyncableMenu, index: Int) {
        val slot: HTSyncableSlot? = menu.getTrackedSlot(index)
        if (slot is HTFloatSyncSlot) {
            slot.amountAsFloat = this.value
        }
    }
}
