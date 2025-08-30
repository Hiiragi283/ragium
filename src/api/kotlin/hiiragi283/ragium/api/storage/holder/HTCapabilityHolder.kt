package hiiragi283.ragium.api.storage.holder

import net.minecraft.core.Direction

/**
 * 指定された向きにおいて搬出入を制御するインターフェース
 * @see [mekanism.common.capabilities.holder.IHolder]
 */
interface HTCapabilityHolder {
    /**
     * 指定された[side]から搬入可能か判定します。
     */
    fun canInsert(side: Direction?): Boolean

    /**
     * 指定された[side]から搬出可能か判定します。
     */
    fun canExtract(side: Direction?): Boolean
}
