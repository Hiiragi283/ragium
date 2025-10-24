package hiiragi283.ragium.common.storage.proxy

import hiiragi283.ragium.api.storage.holder.HTCapabilityHolder
import net.minecraft.core.Direction

/**
 * 任意のCapabilityのラッパーとなる抽象クラス
 * @param side 現在の向き
 * @param holder 搬入出の制御
 * @see mekanism.common.capabilities.proxy.ProxyHandler
 */
abstract class HTProxyHandler(protected val side: Direction?, protected val holder: HTCapabilityHolder?) {
    /**
     * このCapabilityが読み取り専用かどうか判定します。
     */
    protected val readOnly: Boolean = side == null

    protected val readOnlyInsert: Boolean
        get() = readOnly || holder?.canInsert(side) != true

    protected val readOnlyExtract: Boolean
        get() = readOnly || holder?.canExtract(side) != true
}
