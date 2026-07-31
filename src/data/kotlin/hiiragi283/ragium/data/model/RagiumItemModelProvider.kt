package hiiragi283.ragium.data.model

import hiiragi283.core.api.HTConst
import hiiragi283.core.api.data.model.HTItemModelProvider
import hiiragi283.core.api.data.model.ModelOutput
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.resource.itemId
import hiiragi283.core.api.resource.vanillaId
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.integration.mek.RagiumMekItems
import hiiragi283.ragium.setup.RagiumFluids
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.data.PackOutput
import net.minecraft.data.models.model.ModelTemplates
import net.minecraft.data.models.model.TextureMapping
import net.minecraft.resources.ResourceLocation

class RagiumItemModelProvider(output: PackOutput) : HTItemModelProvider(output, RagiumAPI.MOD_ID) {
    override fun registerModels(output: ModelOutput) {
        buildList {
            addAll(RagiumItems.REGISTER.asSequence())

            remove(RagiumItems.RAGI_ALLOY_COMPOUND)
            remove(RagiumItems.NITROGLYCERIN)
            remove(RagiumItems.NITROCELLULOSE)

            remove(RagiumItems.BLANK_DISC)
        }.forEach { item: HTIdLike -> basicItem(output, item) }
        // Materials
        RagiumItems.RAGI_ALLOY_COMPOUND.let { layeredItem(output, it, vanillaId(HTConst.ITEM, "copper_ingot"), it.itemId) }
        val explosiveOverlay: ResourceLocation = RagiumAPI.id(HTConst.ITEM, "explosive_overlay")
        layeredItem(output, RagiumItems.NITROGLYCERIN, RagiumItems.GLYCEROL_DROP.itemId, explosiveOverlay)
        layeredItem(output, RagiumItems.NITROCELLULOSE, vanillaId(HTConst.ITEM, "map"), explosiveOverlay)
        // Utilities
        RagiumItems.BLANK_DISC.itemId.let { ModelTemplates.MUSIC_DISC.create(it, TextureMapping.layer0(it), output) }
        // Buckets
        registerBuckets(output)

        // Integration
        RagiumMekItems.REGISTER.asSequence().forEach { item: HTIdLike -> basicItem(output, item) }
    }

    private fun registerBuckets(output: ModelOutput) {
        val dripFluids: List<HTFluidContent> = buildList {
            // Oil
            add(RagiumFluids.CRUDE_OIL)
            // Organic
            add(RagiumFluids.CREOSOTE)
            add(RagiumFluids.SYNTHETIC_OIL)
        }
        for (content: HTFluidContent in RagiumFluids.REGISTER.entries) {
            bucketItem(output, content, content in dripFluids)
        }
    }
}
