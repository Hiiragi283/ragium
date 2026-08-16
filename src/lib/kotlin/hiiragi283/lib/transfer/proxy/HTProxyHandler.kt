package hiiragi283.lib.transfer.proxy

import hiiragi283.lib.transfer.holder.HTCapabilityHolder
import net.minecraft.core.Direction

/**
 * 任意のCapabilityのラッパーとなる抽象クラス
 *
 * 参照 : [Mekanism - ProxyHandler](https://github.com/mekanism/Mekanism/blob/26.1/src/main/java/mekanism/common/capabilities/proxy/ProxyHandler.java)
 * @param side 現在の向き
 * @param holder 搬入出の制御
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
abstract class HTProxyHandler(protected val side: Direction?, protected val holder: HTCapabilityHolder?) {
    /**
     * このCapabilityが読み取り専用かどうか判定します。
     */
    protected val readOnly: Boolean = side == null

    protected val readOnlyInsert: Boolean
        get() = readOnly || holder != null && !holder.canInsert(side)

    protected val readOnlyExtract: Boolean
        get() = readOnly || holder != null && !holder.canExtract(side)
}
