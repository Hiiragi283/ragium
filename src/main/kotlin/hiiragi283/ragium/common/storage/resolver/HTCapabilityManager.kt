package hiiragi283.ragium.common.storage.resolver

import net.minecraft.core.Direction
import net.neoforged.neoforge.capabilities.BlockCapability

/**
 * 向きに応じたCapabilityを取得するインターフェース
 * @param CONTAINER 要素を保持するクラス
 * @see mekanism.common.capabilities.resolver.ICapabilityResolver
 * @see mekanism.common.capabilities.resolver.manager.ICapabilityHandlerManager
 */
interface HTCapabilityManager<CONTAINER : Any> {
    /**
     * 指定された引数からCapabilityを取得します。
     * @param T Capabilityのクラス
     * @param capability Capabilityの種類
     * @param side アクセスする面
     * @return 見つからない場合は`null`
     */
    fun <T : Any> resolve(capability: BlockCapability<T, Direction?>, side: Direction?): T?

    /**
     * このCapabilityが運用可能か判定します。
     */
    fun canHandle(): Boolean

    /**
     * 指定された[side]から[CONTAINER]の一覧を返します。
     */
    fun getContainers(side: Direction?): List<CONTAINER>
}
