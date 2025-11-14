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
import hiiragi283.ragium.common.integration.RagiumCreateAddon
import hiiragi283.ragium.common.integration.RagiumDelightAddon
import hiiragi283.ragium.common.integration.RagiumKaleidoCookeryAddon
import hiiragi283.ragium.common.integration.RagiumMekanismAddon
import hiiragi283.ragium.setup.RagiumFluidContents
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

        buildSet {
            // Ragium
            addAll(RagiumItems.REGISTER.entries)

            remove(RagiumItems.RAGI_ALLOY_COMPOUND)

            remove(RagiumItems.BLAST_CHARGE)
            remove(RagiumItems.MEDIUM_DRUM_UPGRADE)
            remove(RagiumItems.LARGE_DRUM_UPGRADE)
            remove(RagiumItems.HUGE_DRUM_UPGRADE)
            removeAll(tools)

            // Create
            addAll(RagiumCreateAddon.ITEM_REGISTER.entries)
            // Food
            addAll(RagiumDelightAddon.ITEM_REGISTER.entries)
            addAll(RagiumKaleidoCookeryAddon.ITEM_REGISTER.entries)

            removeAll(RagiumDelightAddon.KNIFE_MAP.values)
            removeAll(RagiumKaleidoCookeryAddon.KNIFE_MAP.values)
            // Mekanism
            addAll(RagiumMekanismAddon.ITEM_REGISTER.entries)
        }.asSequence()
            .map(HTHolderLike::getId)
            .forEach(::basicItem)

        withExistingParent(RagiumItems.RAGI_ALLOY_COMPOUND.getPath(), vanillaId("item", "generated"))
            .texture("layer0", "minecraft:item/copper_ingot")
            .texture("layer1", RagiumItems.RAGI_ALLOY_COMPOUND.itemId)

        for (content: HTFluidContent<*, *, *> in RagiumFluidContents.REGISTER.contents) {
            withExistingParent(content.getIdWithSuffix("_bucket").path, RagiumConst.NEOFORGE.toId("item", "bucket"))
                .customLoader(DynamicFluidContainerModelBuilder<ItemModelBuilder>::begin)
                .fluid(content.get())
        }

        // Tools
        buildList {
            addAll(tools)
            add(RagiumItems.BLAST_CHARGE)

            addAll(RagiumDelightAddon.KNIFE_MAP.values)
            addAll(RagiumKaleidoCookeryAddon.KNIFE_MAP.values)
        }.asSequence()
            .map(HTHolderLike::getId)
            .forEach(::handheldItem)
    }
}
