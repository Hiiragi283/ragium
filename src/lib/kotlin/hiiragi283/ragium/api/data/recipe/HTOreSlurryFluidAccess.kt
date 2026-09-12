package hiiragi283.ragium.api.data.recipe

import hiiragi283.lib.registry.HTFluidContent
import hiiragi283.ragium.api.RagiumAPI

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.5
 */
interface HTOreSlurryFluidAccess {
    companion object {
        @JvmField
        val INSTANCE: HTOreSlurryFluidAccess = RagiumAPI.getService()
    }

    /**
     * Ragiumで登録される鉱石泥のインスタンス
     */
    val fluidContent: HTFluidContent
}
