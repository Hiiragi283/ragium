package hiiragi283.ragium.common.inventory.slot.payload

import hiiragi283.ragium.api.inventory.container.HTSyncableMenu
import hiiragi283.ragium.api.inventory.slot.HTSyncableSlot
import hiiragi283.ragium.api.inventory.slot.payload.HTSyncablePayload
import hiiragi283.ragium.api.item.component.HTTeleportPos
import hiiragi283.ragium.api.util.wrapOptional
import hiiragi283.ragium.common.inventory.slot.HTTeleportPosSyncSlot
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import java.util.Optional
import kotlin.jvm.optionals.getOrNull

@JvmRecord
data class HTTeleportPosSyncPayload(val value: Optional<HTTeleportPos>) : HTSyncablePayload {
    companion object {
        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTTeleportPosSyncPayload> =
            HTTeleportPos.CODEC
                .toOptional()
                .streamCodec
                .map(::HTTeleportPosSyncPayload, HTTeleportPosSyncPayload::value)
                .cast()
    }
    constructor(pos: HTTeleportPos?) : this(pos.wrapOptional())

    override fun type(): StreamCodec<RegistryFriendlyByteBuf, HTTeleportPosSyncPayload> = STREAM_CODEC

    override fun setValue(menu: HTSyncableMenu, index: Int) {
        val slot: HTSyncableSlot? = menu.getTrackedSlot(index)
        if (slot is HTTeleportPosSyncSlot) {
            slot.setHTTeleportPos(this.value.getOrNull())
        }
    }
}
