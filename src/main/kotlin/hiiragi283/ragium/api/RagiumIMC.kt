package hiiragi283.ragium.api

import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.property.HTPropertyHolderBuilder
import net.minecraft.world.level.ItemLike
import net.neoforged.fml.InterModComms
import java.util.function.Consumer

object RagiumIMC {
    const val REGISTER_MATERIAL = "register_material"
    const val SETUP_MATERIAL = "setup_material"
    const val BIND_ITEM = "bind_item"

    @JvmStatic
    fun sendMaterialIMC(key: HTMaterialKey, type: HTMaterialType) {
        InterModComms.sendTo(RagiumAPI.MOD_ID, REGISTER_MATERIAL) { NewMaterial(key, type) }
    }

    @JvmStatic
    fun sendPropertyIMC(key: HTMaterialKey, action: Consumer<HTPropertyHolderBuilder>) {
        InterModComms.sendTo(RagiumAPI.MOD_ID, SETUP_MATERIAL) { MaterialProperty(key, action) }
    }

    @JvmStatic
    fun sendMaterialItemIMC(prefix: HTTagPrefix, key: HTMaterialKey, item: ItemLike) {
        InterModComms.sendTo(RagiumAPI.MOD_ID, BIND_ITEM) { MaterialItems(prefix, key, item) }
    }

    @JvmStatic
    fun sendMaterialItemIMC(prefix: HTTagPrefix, key: HTMaterialKey, items: Iterable<ItemLike>) {
        items.forEach { sendMaterialItemIMC(prefix, key, it) }
    }

    data class NewMaterial(val key: HTMaterialKey, val type: HTMaterialType)

    data class MaterialProperty(val key: HTMaterialKey, val action: Consumer<HTPropertyHolderBuilder>)

    data class MaterialItems(val prefix: HTTagPrefix, val key: HTMaterialKey, val item: ItemLike)
}
