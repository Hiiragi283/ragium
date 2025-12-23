package hiiragi283.ragium.data.client.model

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.data.model.HTItemModelProvider
import hiiragi283.core.api.material.HTMaterialKey
import hiiragi283.core.api.material.prefix.HTMaterialPrefix
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.registry.HTSimpleFluidContent
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.resource.vanillaId
import hiiragi283.core.common.material.HCMaterialPrefixes
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.setup.RagiumFluids
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.resources.ResourceLocation
import kotlin.collections.addAll
import kotlin.collections.contains
import kotlin.collections.forEach

class RagiumItemModelProvider(context: HTDataGenContext) : HTItemModelProvider(RagiumAPI.MOD_ID, context) {
    private val wireOverlay: ResourceLocation = HiiragiCoreAPI.id(HTConst.ITEM, "wire_overlay")

    override fun registerModels() {
        existingFileHelper.trackGenerated(wireOverlay, TEXTURE)

        buildList {
            addAll(RagiumItems.REGISTER.asSequence())

            removeAll(RagiumItems.MATERIALS.values)

            remove(RagiumItems.RAGI_ALLOY_COMPOUND)
        }.forEach { item: HTIdLike -> existTexture(item, ::basicItem) }

        registerMaterials()
        registerBuckets()

        existTexture(RagiumItems.RAGI_ALLOY_COMPOUND) { texId: ResourceLocation ->
            layeredItem(
                RagiumItems.RAGI_ALLOY_COMPOUND,
                vanillaId(HTConst.ITEM, "copper_ingot"),
                texId.withPrefix("item/"),
            )
        }
        layeredItem(RagiumItems.POTION_DROP, vanillaId(HTConst.ITEM, "ghast_tear"))
    }

    private fun registerMaterials() {
        RagiumItems.MATERIALS.forEach { (prefix: HTMaterialPrefix, key: HTMaterialKey, item: HTIdLike) ->
            val textureId: ResourceLocation = RagiumAPI.id(HTConst.ITEM, prefix.asPrefixName(), key.asMaterialName())
            if (prefix == HCMaterialPrefixes.WIRE) {
                existTexture(item, textureId) { itemIn: HTIdLike, layer: ResourceLocation ->
                    layeredItem(itemIn, layer, wireOverlay)
                }
            } else {
                existTexture(item, textureId, ::layeredItem)
            }
        }
    }

    private fun registerBuckets() {
        val dripFluids: List<HTSimpleFluidContent> = buildList {
            // Organic
            add(RagiumFluids.SLIME)
            add(RagiumFluids.GELLED_EXPLOSIVE)
            add(RagiumFluids.CRUDE_BIO)
            // Oil
            add(RagiumFluids.CRUDE_OIL)
            add(RagiumFluids.LUBRICANT)
            // Misc
            add(RagiumFluids.DESTABILIZED_RAGINITE)
            add(RagiumFluids.CREOSOTE)
        }
        for (content: HTFluidContent<*, *, *> in RagiumFluids.REGISTER.entries) {
            bucketItem(content, content in dripFluids)
        }
    }
}
