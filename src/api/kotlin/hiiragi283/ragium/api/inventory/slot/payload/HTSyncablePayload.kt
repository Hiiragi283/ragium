package hiiragi283.ragium.api.inventory.slot.payload

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.function.identity
import hiiragi283.ragium.api.inventory.container.HTSyncableMenu
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec

/**
 * @see mekanism.common.network.to_client.container.property.PropertyData
 */
interface HTSyncablePayload {
    companion object {
        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTSyncablePayload> =
            ByteBufCodecs
                .registry(RagiumAPI.SLOT_TYPE_KEY)
                .dispatch(HTSyncablePayload::type, identity())
    }

    fun type(): StreamCodec<RegistryFriendlyByteBuf, out HTSyncablePayload>

    fun setValue(menu: HTSyncableMenu, index: Int)
}
