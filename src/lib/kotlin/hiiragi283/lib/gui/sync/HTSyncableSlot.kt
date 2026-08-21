package hiiragi283.lib.gui.sync

import net.minecraft.core.RegistryAccess

/**
 * 任意の値をサーバーからクライアントへ同期可能なオブジェクトを表すインターフェースです。
 *
 * 参照 : [Mekanism - ISyncableData](https://github.com/mekanism/Mekanism/blob/1.21.x/src/main/java/mekanism/common/inventory/container/sync/ISyncableData.java)
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
interface HTSyncableSlot {
    /**
     * 現在の同期のフラグを取得します。
     * @return 同期を行わない場合は`null`
     */
    fun getChange(): HTChangeType?

    /**
     * 指定した[access]と[changeType]から[HTSyncablePayload]を作成します。
     * @return 同期を行わない場合は`null`
     */
    fun createPayload(access: RegistryAccess, changeType: HTChangeType): HTSyncablePayload?
}
