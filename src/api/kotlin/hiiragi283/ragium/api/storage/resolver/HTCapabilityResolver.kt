package hiiragi283.ragium.api.storage.resolver

import net.neoforged.neoforge.capabilities.BlockCapability

/**
 * Capabilityの取得と無効化を制御するインターフェース
 * @param CONTEXT コンテキストのクラス
 * @see [mekanism.common.capabilities.resolver.ICapabilityResolver]
 */
interface HTCapabilityResolver<CONTEXT : Any> {
    /**
     * 指定された引数からCapabilityを取得します。
     * @param T Capabilityのクラス
     * @param capability Capabilityの種類
     * @param context コンテキスト
     * @return 見つからない場合は`null`
     */
    fun <T : Any> resolve(capability: BlockCapability<T, CONTEXT?>, context: CONTEXT?): T?

    /**
     * 指定された[context]におけるCapabilityを無効化します。
     * @param capability Capabilityの種類
     * @param context コンテキスト
     */
    fun invalidate(capability: BlockCapability<*, CONTEXT>, context: CONTEXT?)

    /**
     * すべてのCapabilityを無効化します。
     */
    fun invalidateAll()
}
