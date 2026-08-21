package hiiragi283.lib.gui.sync

/**
 * [HTSyncableSlot]を保持するGUIを表すインターフェースです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
interface HTSyncableMenu {
    /**
     * 指定した[index]から[HTSyncableSlot]を取得します。
     * @return 取得できない場合は`null`
     */
    fun getTrackedSlot(index: Int): HTSyncableSlot?
}
