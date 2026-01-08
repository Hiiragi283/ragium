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
import hiiragi283.ragium.common.item.HTMoldType
import hiiragi283.ragium.common.upgrade.RagiumUpgradeType
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

            remove(RagiumItems.BLANK_DISC)
            remove(RagiumItems.POTION_DROP)

            removeAll(RagiumItems.MOLDS.values)
            removeAll(RagiumItems.UPGRADES.values)
        }.forEach { item: HTIdLike -> existTexture(item, ::basicItem) }

        registerMaterials()
        existTexture(RagiumItems.RAGI_ALLOY_COMPOUND) { itemId: ResourceLocation ->
            layeredItem(
                RagiumItems.RAGI_ALLOY_COMPOUND,
                vanillaId(HTConst.ITEM, "copper_ingot"),
                itemId.withPrefix("item/"),
            )
        }

        existTexture(RagiumItems.BLANK_DISC) { itemId: ResourceLocation ->
            withExistingParent(itemId.path, vanillaId(HTConst.ITEM, "template_music_disc"))
                .texture("layer0", itemId.withPrefix("item/"))
        }
        layeredItem(RagiumItems.POTION_DROP, vanillaId(HTConst.ITEM, "ghast_tear"))

        for ((moldType: HTMoldType, item: HTIdLike) in RagiumItems.MOLDS) {
            existTexture(item, RagiumAPI.id(HTConst.ITEM, "mold", moldType.serializedName), ::layeredItem)
        }
        registerUpgrades()

        registerBuckets()
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

    private fun registerUpgrades() {
        for ((type: RagiumUpgradeType, item: HTIdLike) in RagiumItems.UPGRADES) {
            val base: ResourceLocation = when (type.group) {
                RagiumUpgradeType.Group.CREATIVE -> {
                    existTexture(type::getId, ::basicItem)
                    continue
                }
                else -> RagiumAPI.id("item", "upgrade", "${type.group.serializedName}_base")
            }
            val textureId: ResourceLocation = RagiumAPI.id("item", "upgrade", type.serializedName)
            existTexture(item, textureId) { itemIn: HTIdLike, texId: ResourceLocation ->
                layeredItem(itemIn, base, texId)
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
            addAll(RagiumFluids.MATERIALS.values)
            add(RagiumFluids.CREOSOTE)
        }
        for (content: HTFluidContent<*, *, *> in RagiumFluids.REGISTER.entries) {
            bucketItem(content, content in dripFluids)
        }
    }
}
