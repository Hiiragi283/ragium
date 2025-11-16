package hiiragi283.ragium.api.inventory.slot.payload

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.inventory.container.HTSyncableMenu
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import java.util.function.Function

/**
 * @see mekanism.common.network.to_client.container.property.PropertyData
 */
interface HTSyncablePayload {
    companion object {
        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTSyncablePayload> =
            ByteBufCodecs
                .registry(RagiumAPI.SLOT_TYPE_KEY)
                .dispatch(HTSyncablePayload::type, Function.identity())
    }

    fun type(): StreamCodec<RegistryFriendlyByteBuf, out HTSyncablePayload>

    fun setValue(menu: HTSyncableMenu, index: Int)
}
