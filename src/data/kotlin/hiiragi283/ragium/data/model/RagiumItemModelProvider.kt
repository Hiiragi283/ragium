package hiiragi283.ragium.data.model

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.HiiragiCoreAPI
import hiiragi283.core.api.data.model.HTItemModelProvider
import hiiragi283.core.api.data.model.withExistingParent
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.resource.itemId
import hiiragi283.core.api.resource.toId
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.item.HTFoodCanType
import hiiragi283.ragium.setup.RagiumFluids
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.common.data.ExistingFileHelper
import kotlin.collections.iterator

class RagiumItemModelProvider(fileHelper: ExistingFileHelper, output: PackOutput) : HTItemModelProvider(fileHelper, output, RagiumAPI.MOD_ID) {
    private val wireOverlay: ResourceLocation = HiiragiCoreAPI.id(HTConst.ITEM, "wire_overlay")

    override fun registerModels() {
        existingFileHelper.trackGenerated(wireOverlay, TEXTURE)

        trackItem(RagiumItems.CRYO_CHARGE)

        trackItem(RagiumItems.CRUDE_SILICON)
        trackItem(RagiumItems.SMOKELESS_POWDER)

        buildList {
            addAll(RagiumItems.REGISTER.asSequence())

            remove(RagiumItems.RAGI_ALLOY_COMPOUND)
            remove(RagiumItems.NITROGLYCERIN)
            remove(RagiumItems.NITROCELLULOSE)

            removeAll(RagiumItems.FOOD_CANS.values)

            remove(RagiumItems.BLANK_DISC)
        }.forEach { item: HTIdLike -> existTexture(item, ::basicItem) }
        // Materials
        existTexture(RagiumItems.RAGI_ALLOY_COMPOUND) { item: HTIdLike ->
            layeredItem(item, HTConst.MINECRAFT.toId(HTConst.ITEM, "copper_ingot"), item.itemId)
        }
        val explosiveOverlay: ResourceLocation = RagiumAPI.id(HTConst.ITEM, "explosive_overlay")
        existTexture(RagiumItems.GLYCEROL_DROP) { item: HTIdLike ->
            layeredItem(RagiumItems.NITROGLYCERIN, item.itemId, explosiveOverlay)
        }
        existTexture(RagiumItems.NITROCELLULOSE, HTConst.MINECRAFT.toId(HTConst.ITEM, "map")) { item: HTIdLike, id: ResourceLocation ->
            layeredItem(item, id, explosiveOverlay)
        }
        // Foods
        for ((canType: HTFoodCanType, item: HTIdLike) in RagiumItems.FOOD_CANS) {
            existTexture(item, RagiumAPI.id(HTConst.ITEM, "food_can", canType.serializedName), ::layeredItem)
        }
        // Utilities
        existTexture(RagiumItems.BLANK_DISC) { item: HTIdLike ->
            withExistingParent(item, HTConst.MINECRAFT.toId(HTConst.ITEM, "template_music_disc"))
                .texture("layer0", item.itemId)
        }
        // Buckets
        registerBuckets()
    }

    private fun registerBuckets() {
        val dripFluids: List<HTFluidContent> = buildList {
            // Oil
            add(RagiumFluids.CRUDE_OIL)
            // Organic
            add(RagiumFluids.CREOSOTE)
            add(RagiumFluids.SYNTHETIC_OIL)
        }
        for (content: HTFluidContent in RagiumFluids.REGISTER.entries) {
            bucketItem(content, content in dripFluids)
        }
    }
}
