package hiiragi283.lib.item.alchemy

import hiiragi283.lib.registry.HTFluidContent
import hiiragi283.ragium.api.RagiumAPI
import net.minecraft.core.component.DataComponentType

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
     * Ragiumで登録される[HTBottleType]向けの[DataComponentType]のインスタンス
     */
    val bottleType: DataComponentType<HTBottleType>
}
