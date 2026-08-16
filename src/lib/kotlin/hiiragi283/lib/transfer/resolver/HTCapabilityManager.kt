package hiiragi283.lib.transfer.resolver

import net.minecraft.core.Direction

/**
 * 向きに応じたキャパビリティを取得するインターフェース
 *
 * 参照 : [Mekanism - ICapabilityHandlerManager](https://github.com/mekanism/Mekanism/blob/26.1/src/main/java/mekanism/common/capabilities/resolver/manager/ICapabilityHandlerManager.java)
 *       [Mekanism - ICapabilityResolver](https://github.com/mekanism/Mekanism/blob/26.1/src/main/java/mekanism/common/capabilities/resolver/ICapabilityResolver.java)
 * @param SLOT 要素を保持するクラス
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
interface HTCapabilityManager<SLOT> {
    /**
     * 指定された引数からキャパビリティを取得します。
     * @param T キャパビリティのクラス
     * @param side アクセスする面
     * @return 見つからない場合は`null`
     */
    fun <T : Any> resolve(side: Direction?): T?

    /**
     * 指定された[side]から[SLOT]の一覧を返します。
     */
    fun getContainers(side: Direction?): List<SLOT>
}
