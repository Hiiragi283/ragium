package hiiragi283.ragium.data.client

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.HTDataGenContext
import hiiragi283.ragium.api.registry.HTBasicFluidContent
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.registry.HTHolderLike
import hiiragi283.ragium.api.registry.impl.HTDeferredItem
import hiiragi283.ragium.api.registry.impl.HTSimpleDeferredItem
import hiiragi283.ragium.api.registry.itemId
import hiiragi283.ragium.api.registry.toId
import hiiragi283.ragium.api.registry.vanillaId
import hiiragi283.ragium.api.tier.HTBaseTier
import hiiragi283.ragium.common.HTChargeType
import hiiragi283.ragium.common.variant.HTUpgradeVariant
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumIntegrationItems
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.resources.ResourceLocation
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

            remove(RagiumItems.POTION_DROP)

            remove(RagiumItems.RAGI_ALLOY_COMPOUND)
            remove(RagiumItems.RAGI_CHERRY_JUICE)

            removeAll(HTChargeType.entries.map(HTChargeType::getItem))
            removeAll(tools)

            removeAll(RagiumItems.MACHINE_UPGRADES.values)
            // Integration
            addAll(RagiumIntegrationItems.REGISTER.entries)

            removeAll(tools1)
        }.asSequence()
            .map(HTHolderLike::getId)
            .forEach(::basicItem)

        mapOf(RagiumItems.POTION_DROP to "item/ghast_tear").forEach { (item: HTHolderLike, path: String) ->
            withExistingParent(item.getPath(), vanillaId("item", "generated"))
                .texture("layer0", vanillaId(path))
        }

        mapOf(
            RagiumItems.RAGI_ALLOY_COMPOUND to "item/copper_ingot",
            RagiumItems.RAGI_CHERRY_JUICE to "item/potion",
        ).forEach { (item: HTSimpleDeferredItem, path: String) ->
            withExistingParent(item.getPath(), vanillaId("item", "generated"))
                .texture("layer0", vanillaId(path))
                .texture("layer1", item.itemId)
        }

        val dripFluids: List<HTBasicFluidContent> = listOf(
            // Vanilla
            RagiumFluidContents.HONEY,
            RagiumFluidContents.MUSHROOM_STEW,
            // Organic
            RagiumFluidContents.CREAM,
            RagiumFluidContents.CHOCOLATE,
            RagiumFluidContents.SLIME,
            RagiumFluidContents.ORGANIC_MUTAGEN,
            // Oil
            RagiumFluidContents.CRUDE_OIL,
            RagiumFluidContents.NAPHTHA,
            RagiumFluidContents.LUBRICANT,
            // Molten
            RagiumFluidContents.DESTABILIZED_RAGINITE,
            RagiumFluidContents.CRIMSON_BLOOD,
            RagiumFluidContents.DEW_OF_THE_WARP,
            RagiumFluidContents.ELDRITCH_FLUX,
        )
        for (content: HTFluidContent<*, *, *, *, *> in RagiumFluidContents.REGISTER.contents) {
            val parent: ResourceLocation = when (content) {
                in dripFluids -> "bucket_drip"
                else -> "bucket"
            }.let { RagiumConst.NEOFORGE.toId("item", it) }

            withExistingParent(content.bucket.getPath(), parent)
                .customLoader(DynamicFluidContainerModelBuilder<ItemModelBuilder>::begin)
                .fluid(content.getFluid())
                .flipGas(content.getType().isLighterThanAir)
        }

        // Tools
        buildList {
            addAll(tools)
            addAll(HTChargeType.entries)

            addAll(tools1)
        }.asSequence()
            .map(HTHolderLike::getId)
            .forEach(::handheldItem)

        // Upgrades
        RagiumItems.MACHINE_UPGRADES.forEach { (variant: HTUpgradeVariant, tier: HTBaseTier, item: HTHolderLike) ->
            withExistingParent(item.getPath(), vanillaId("item", "generated"))
                .texture("layer0", RagiumAPI.id("item", "${tier.serializedName}_upgrade_base"))
                .texture("layer1", RagiumAPI.id("item", variant.variantName()))
        }
    }
}
