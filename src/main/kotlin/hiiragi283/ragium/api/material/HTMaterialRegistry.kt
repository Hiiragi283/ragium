package hiiragi283.ragium.api.material

import hiiragi283.ragium.api.content.RagiumMaterials
import hiiragi283.ragium.api.property.HTPropertyHolder
import hiiragi283.ragium.api.util.HTTable
import net.minecraft.item.Item

class HTMaterialRegistry(
    val types: Map<HTMaterialKey, RagiumMaterials.Type>,
    val items: HTTable<HTTagPrefix, HTMaterialKey, Item>,
    val properties: Map<HTMaterialKey, HTPropertyHolder>,
) {
    operator fun contains(key: HTMaterialKey): Boolean = key in types

    fun getProperty(key: HTMaterialKey): HTPropertyHolder = properties.getOrDefault(key, HTPropertyHolder.Empty)
}
