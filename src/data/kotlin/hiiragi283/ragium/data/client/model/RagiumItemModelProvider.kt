package hiiragi283.ragium.data.client.model

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.HTDataGenContext
import hiiragi283.core.api.data.model.HTItemModelProvider
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.resource.itemId
import hiiragi283.core.api.resource.toId
import hiiragi283.core.common.registry.register.HTSimpleFluidContent
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.item.HTFoodCanType
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

            remove(RagiumItems.RAGI_ALLOY_COMPOUND)

            removeAll(RagiumItems.FOOD_CANS.values)

            removeAll(RagiumItems.MOLDS.values)
            remove(RagiumItems.BLANK_DISC)
            remove(RagiumItems.POTION_DROP)

            removeAll(RagiumItems.UPGRADES.values)
        }.forEach { item: HTIdLike -> existTexture(item, ::basicItem) }
        // Materials
        registerMaterials()
        existTexture(RagiumItems.RAGI_ALLOY_COMPOUND) { item ->
            layeredItem(item, HTConst.MINECRAFT.toId(HTConst.ITEM, "copper_ingot"), item.itemId)
        }
        // Foods
        for ((canType: HTFoodCanType, item: HTIdLike) in RagiumItems.FOOD_CANS) {
            existTexture(item, RagiumAPI.id(HTConst.ITEM, "food_can", canType.serializedName), ::layeredItem)
        }
        // Utilities
        existTexture(RagiumItems.BLANK_DISC) { item: HTIdLike ->
            withExistingParent(item.path, HTConst.MINECRAFT.toId(HTConst.ITEM, "template_music_disc"))
                .texture("layer0", item.itemId)
        }
        layeredItem(RagiumItems.POTION_DROP, HTConst.MINECRAFT.toId(HTConst.ITEM, "ghast_tear"))

        for ((moldType: HTMoldType, item: HTIdLike) in RagiumItems.MOLDS) {
            existTexture(item, RagiumAPI.id(HTConst.ITEM, "mold", moldType.serializedName), ::layeredItem)
        }
        // Upgrades
        registerUpgrades()
        // Buckets
        registerBuckets()
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
            add(RagiumFluids.CRUDE_BIO)
            // Oil
            add(RagiumFluids.CRUDE_OIL)
            add(RagiumFluids.LUBRICANT)
            // Misc
            add(RagiumFluids.MOLTEN_RAGINITE)
            add(RagiumFluids.CREOSOTE)
        }
        for (content: HTFluidContent<*, *, *> in RagiumFluids.REGISTER.entries) {
            bucketItem(content, content in dripFluids)
        }
    }
}
