package hiiragi283.ragium.common.data

import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.data.HTDynamicResourceProvider
import hiiragi283.core.api.data.map.HTDataMapGenTask
import hiiragi283.core.api.material.HTMaterialManager
import hiiragi283.core.api.material.property.getDefaultPart
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.map.RagiumDataMapTypes
import hiiragi283.ragium.api.material.property.RagiumMaterialPropertyKeys
import net.mehvahdjukaar.moonlight.api.resources.pack.ResourceGenTask
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import java.util.function.Consumer

data object RagiumServerResourceProvider : HTDynamicResourceProvider.Server(RagiumAPI.MOD_ID) {
    override fun regenerateDynamicAssets(executor: Consumer<ResourceGenTask>) {
        val materialManager: HTMaterialManager = HiiragiCoreAccess.INSTANCE.materialManager
        // Data Map
        executor.accept(object : HTDataMapGenTask<Int, Item>(RagiumDataMapTypes.DUPLICATION_COST) {
            override fun gather() {
                for (entry: HTMaterialManager.Entry in materialManager) {
                    // 素材のプロパティから材料を取得
                    val inputTag: TagKey<Item> = entry.getDefaultPart(entry) ?: continue
                    // 必要なマター量を取得
                    val matterValue: Int = entry[RagiumMaterialPropertyKeys.MATTER_VALUE] ?: continue
                    // データを登録
                    add(inputTag, matterValue)
                }
            }
        })
    }
}
