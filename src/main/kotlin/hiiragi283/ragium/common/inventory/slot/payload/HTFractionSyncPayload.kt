package hiiragi283.ragium.common.inventory.slot.payload

import hiiragi283.ragium.api.inventory.container.HTSyncableMenu
import hiiragi283.ragium.api.inventory.slot.HTSyncableSlot
import hiiragi283.ragium.api.inventory.slot.payload.HTSyncablePayload
import hiiragi283.ragium.api.serialization.codec.BiCodecs
import hiiragi283.ragium.common.inventory.slot.HTFractionSyncSlot
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import org.apache.commons.lang3.math.Fraction

@JvmRecord
data class HTFractionSyncPayload(val value: Fraction) : HTSyncablePayload {
    companion object {
        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTFractionSyncPayload> = BiCodecs.FRACTION
            .streamCodec
            .map(::HTFractionSyncPayload, HTFractionSyncPayload::value)
            .cast()
    }

    override fun type(): StreamCodec<RegistryFriendlyByteBuf, HTFractionSyncPayload> = STREAM_CODEC

    override fun setValue(menu: HTSyncableMenu, index: Int) {
        val slot: HTSyncableSlot? = menu.getTrackedSlot(index)
        if (slot is HTFractionSyncSlot) {
            slot.setAmount(this.value)
        }
    }
}
