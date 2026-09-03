package hiiragi283.lib.gui.sync

/**
 * 同期のフラグを管理するクラスです。
 *
 * 参照 : [Mekanism - ISyncableData.DirtyType](https://github.com/mekanism/Mekanism/blob/1.21.x/src/main/java/mekanism/common/inventory/container/sync/ISyncableData.java)
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
enum class HTChangeType {
    PARTIAL,
    FULL
}
