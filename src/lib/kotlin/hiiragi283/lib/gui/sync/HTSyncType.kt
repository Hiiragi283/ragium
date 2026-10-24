package hiiragi283.lib.gui.sync

/**
 * 同期の方向を管理するクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
enum class HTSyncType(val allowS2C: Boolean, val allowC2S: Boolean) {
    BOTH(true, true),
    S2C(true, false),
    C2S(false, true),
}
