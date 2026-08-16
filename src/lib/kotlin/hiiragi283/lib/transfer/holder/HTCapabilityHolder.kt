package hiiragi283.lib.transfer.holder

import net.minecraft.core.Direction

/**
 * 搬出入を制御するインターフェースです。
 *
 * 参照 : [Mekanism - IHolder](https://github.com/mekanism/Mekanism/blob/26.1/src/main/java/mekanism/common/capabilities/holder/IHolder.java)
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
interface HTCapabilityHolder {
    /**
     * 指定された[面][side]に搬入できるか判定します。
     */
    fun canInsert(side: Direction?): Boolean

    /**
     * 指定された[面][side]から搬出できるか判定します。
     */
    fun canExtract(side: Direction?): Boolean
}
