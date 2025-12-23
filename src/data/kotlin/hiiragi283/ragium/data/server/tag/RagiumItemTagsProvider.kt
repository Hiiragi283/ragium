package hiiragi283.ragium.data.server.tag

import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.data.tag.HTTagsProvider
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.prefix.HTMaterialPrefix
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.common.material.HCMaterialPrefixes
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.core.registries.Registries
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Item

class RagiumItemTagsProvider(context: HTDataGenContext) : HTTagsProvider<Item>(RagiumAPI.MOD_ID, Registries.ITEM, context) {
    override fun addTagsInternal(factory: BuilderFactory<Item>) {
        material(factory)
    }

    //    Material    //

    private fun material(factory: BuilderFactory<Item>) {
        RagiumItems.MATERIALS.forEach { (prefix: HTMaterialPrefix, key: HTMaterialKey, item: HTIdLike) ->
            addMaterial(factory, prefix, key).add(item)
            if (prefix == HCMaterialPrefixes.GEM || prefix == HCMaterialPrefixes.INGOT) {
                factory.apply(ItemTags.BEACON_PAYMENT_ITEMS).addTag(prefix, key)
            }
        }
    }
}
