package hiiragi283.lib.transfer.resolver

import net.minecraft.core.Direction

/**
 * 向きに応じたキャパビリティを取得するインターフェース
 *
 * 参照 : [Mekanism - ICapabilityHandlerManager](https://github.com/mekanism/Mekanism/blob/26.3/src/main/java/mekanism/common/capabilities/resolver/manager/ICapabilityHandlerManager.java)
 * @param SLOT 要素を保持するクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
interface HTSlottedCapabilityResolver<SLOT> : HTCapabilityResolver {
    /**
     * 指定された[side]から[SLOT]の一覧を返します。
     */
    fun getContainers(side: Direction?): List<SLOT>
}
