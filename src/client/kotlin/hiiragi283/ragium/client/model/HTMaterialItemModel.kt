package hiiragi283.ragium.client.model

import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialPropertyKeys
import hiiragi283.ragium.api.material.HTMaterialRegistry
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.model.HTColoredItemModel
import net.minecraft.client.texture.MissingSprite
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier

object HTMaterialItemModel : HTColoredItemModel() {
    override fun getSpriteId(stack: ItemStack): Identifier {
        val entry: HTMaterialRegistry.Entry = stack
            .get(HTMaterialKey.COMPONENT_TYPE)
            ?.getEntryOrNull()
            ?: return MissingSprite.getMissingSpriteId()
        val prefix: HTTagPrefix = stack.get(HTTagPrefix.COMPONENT_TYPE) ?: return MissingSprite.getMissingSpriteId()
        return entry.getOrDefault(HTMaterialPropertyKeys.MODEL_ID).apply(prefix)
    }

    override fun getColor(stack: ItemStack): Int = stack
        .get(HTMaterialKey.COMPONENT_TYPE)
        ?.getEntryOrNull()
        ?.get(HTMaterialPropertyKeys.MODEL_COLOR)
        ?.rgb
        ?: -1
}
