package hiiragi283.ragium.common.inventory.slot.payload

import hiiragi283.ragium.api.inventory.container.HTSyncableMenu
import hiiragi283.ragium.api.inventory.slot.HTSyncableSlot
import hiiragi283.ragium.api.inventory.slot.payload.HTSyncablePayload
import hiiragi283.ragium.common.inventory.slot.HTIntSyncSlot
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec

@JvmRecord
data class HTIntSyncPayload(val value: Int) : HTSyncablePayload {
    companion object {
        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTIntSyncPayload> = ByteBufCodecs.VAR_INT
            .map(::HTIntSyncPayload, HTIntSyncPayload::value)
            .cast()
    }

    override fun type(): StreamCodec<RegistryFriendlyByteBuf, HTIntSyncPayload> = STREAM_CODEC

    override fun setValue(menu: HTSyncableMenu, index: Int) {
        val slot: HTSyncableSlot? = menu.getTrackedSlot(index)
        if (slot is HTIntSyncSlot) {
            slot.setAmountAsInt(this.value)
        }
    }
}
