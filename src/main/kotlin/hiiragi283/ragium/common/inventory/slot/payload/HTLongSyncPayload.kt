package hiiragi283.ragium.common.inventory.slot.payload

import hiiragi283.ragium.api.inventory.container.HTSyncableMenu
import hiiragi283.ragium.api.inventory.slot.HTSyncableSlot
import hiiragi283.ragium.api.inventory.slot.payload.HTSyncablePayload
import hiiragi283.ragium.common.inventory.slot.HTLongSyncSlot
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec

@JvmRecord
data class HTLongSyncPayload(val value: Long) : HTSyncablePayload {
    companion object {
        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTLongSyncPayload> = ByteBufCodecs.VAR_LONG
            .map(::HTLongSyncPayload, HTLongSyncPayload::value)
            .cast()
    }

    override fun type(): StreamCodec<RegistryFriendlyByteBuf, HTLongSyncPayload> = STREAM_CODEC

    override fun setValue(menu: HTSyncableMenu, index: Int) {
        val slot: HTSyncableSlot? = menu.getTrackedSlot(index)
        if (slot is HTLongSyncSlot) {
            slot.setAmount(this.value)
        }
    }
}
