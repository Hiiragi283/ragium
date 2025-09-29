package hiiragi283.ragium.api.storage.resolver

import net.minecraft.core.Direction

/**
 * 向きに応じたCapabilityを取得するインターフェース
 * @param CONTAINER 要素を保持するクラス
 * @see [mekanism.common.capabilities.resolver.manager.ICapabilityHandlerManager]
 */
interface HTCapabilityManager<CONTAINER : Any> : HTCapabilityResolver<Direction> {
    /**
     * このCapabilityが運用可能か判定します。
     */
    fun canHandle(): Boolean

    /**
     * 指定された[side]から[CONTAINER]の一覧を返します。
     */
    fun getContainers(side: Direction?): List<CONTAINER>
}
