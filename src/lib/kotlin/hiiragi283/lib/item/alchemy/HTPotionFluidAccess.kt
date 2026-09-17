package hiiragi283.lib.item.alchemy

import hiiragi283.lib.registry.HTFluidContent
import hiiragi283.lib.registry.HTSimpleDeferredItem
import hiiragi283.ragium.api.RagiumAPI

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
interface HTPotionFluidAccess {
    companion object {
        @JvmField
        val INSTANCE: HTPotionFluidAccess = RagiumAPI.getService()
    }

    /**
     * Ragiumで登録される液体ポーションのインスタンス
     */
    val fluidContent: HTFluidContent

    /**
     * @since 26.1.6
     */
    val glassBottle: HTSimpleDeferredItem

    /**
     * @since 26.1.6
     */
    val splashBottle: HTSimpleDeferredItem

    /**
     * @since 26.1.6
     */
    val lingeringBottle: HTSimpleDeferredItem
}
