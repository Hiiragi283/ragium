package hiiragi283.lib.gui.sync

import hiiragi283.ragium.api.RagiumRegistries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec

/**
 * [HTSyncableSlot]の同期に使用されるパケットを表すインターフェースです。
 *
 * 参照 : [Mekanism - PropertyData](https://github.com/mekanism/Mekanism/blob/1.21.x/src/main/java/mekanism/common/network/to_client/container/property/PropertyData.java)
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
interface HTSyncablePayload {
    companion object {
        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTSyncablePayload> = ByteBufCodecs
            .registry(RagiumRegistries.Keys.SYNCABLE_SLOT_TYPE)
            .dispatch(HTSyncablePayload::type, Type<*>::streamCodec)
    }

    /**
     * パケットへの書き込みに使用される[StreamCodec]を返します。
     */
    fun type(): Type<*>

    /**
     * 指定された[menu]と[index]から値を更新します。
     */
    fun setValue(menu: HTSyncableMenu, index: Int)

    /**
     * [StreamCodec]のラッパークラスです。
     * @author Hiiragi Tsubasa
     * @since 26.1.0
     */
    @JvmRecord
    data class Type<PAYLOAD : HTSyncablePayload>(val streamCodec: StreamCodec<RegistryFriendlyByteBuf, PAYLOAD>)
}
