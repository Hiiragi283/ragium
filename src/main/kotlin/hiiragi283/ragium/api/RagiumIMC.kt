package hiiragi283.ragium.api

import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import net.minecraft.world.level.ItemLike

object RagiumIMC {
    const val BIND_ITEM = "bind_item"

    @JvmStatic
    fun sendMaterialItemIMC(prefix: HTTagPrefix, key: HTMaterialKey, item: ItemLike) {
    }

    @JvmStatic
    fun sendMaterialItemIMC(prefix: HTTagPrefix, key: HTMaterialKey, vararg items: ItemLike) {
    }
}
