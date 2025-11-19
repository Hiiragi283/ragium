package hiiragi283.ragium.data.client

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.HTDataGenContext
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.registry.HTHolderLike
import hiiragi283.ragium.api.registry.impl.HTDeferredItem
import hiiragi283.ragium.api.registry.itemId
import hiiragi283.ragium.api.registry.toId
import hiiragi283.ragium.api.registry.vanillaId
import hiiragi283.ragium.common.variant.HTChargeVariant
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumIntegrationItems
import hiiragi283.ragium.setup.RagiumItems
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder
import net.neoforged.neoforge.client.model.generators.ItemModelProvider
import net.neoforged.neoforge.client.model.generators.loaders.DynamicFluidContainerModelBuilder

class RagiumItemModelProvider(context: HTDataGenContext) : ItemModelProvider(context.output, RagiumAPI.MOD_ID, context.fileHelper) {
    override fun registerModels() {
        registerItems()
    }

    private fun registerItems() {
        val tools: Collection<HTDeferredItem<*>> = RagiumItems.TOOLS.values
        val tools1: Collection<HTDeferredItem<*>> = RagiumIntegrationItems.TOOLS.values

        buildSet {
            // Ragium
            addAll(RagiumItems.REGISTER.entries)

            remove(RagiumItems.RAGI_ALLOY_COMPOUND)
            remove(RagiumItems.POTION_DROP)

            removeAll(HTChargeVariant.entries.map(HTChargeVariant::getItem))
            remove(RagiumItems.MEDIUM_DRUM_UPGRADE)
            remove(RagiumItems.LARGE_DRUM_UPGRADE)
            remove(RagiumItems.HUGE_DRUM_UPGRADE)
            removeAll(tools)

            // Integration
            addAll(RagiumIntegrationItems.REGISTER.entries)

            removeAll(tools1)
        }.asSequence()
            .map(HTHolderLike::getId)
            .forEach(::basicItem)

        withExistingParent(RagiumItems.RAGI_ALLOY_COMPOUND.getPath(), vanillaId("item", "generated"))
            .texture("layer0", "minecraft:item/copper_ingot")
            .texture("layer1", RagiumItems.RAGI_ALLOY_COMPOUND.itemId)

        withExistingParent(RagiumItems.POTION_DROP.getPath(), vanillaId("item", "generated"))
            .texture("layer0", "minecraft:item/ghast_tear")

        for (content: HTFluidContent<*, *, *> in RagiumFluidContents.REGISTER.contents) {
            withExistingParent(content.getIdWithSuffix("_bucket").path, RagiumConst.NEOFORGE.toId("item", "bucket"))
                .customLoader(DynamicFluidContainerModelBuilder<ItemModelBuilder>::begin)
                .fluid(content.get())
        }

        // Tools
        buildList {
            addAll(tools)
            addAll(HTChargeVariant.entries)

            addAll(tools1)
        }.asSequence()
            .map(HTHolderLike::getId)
            .forEach(::handheldItem)
    }
}
