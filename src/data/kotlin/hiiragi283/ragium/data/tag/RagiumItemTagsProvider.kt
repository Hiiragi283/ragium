package hiiragi283.ragium.data.tag

import hiiragi283.lib.collection.forEach
import hiiragi283.lib.data.tag.HTBlockItemTagsProvider
import hiiragi283.lib.data.tag.HTItemTagsProvider
import hiiragi283.lib.item.component.HTToolType
import hiiragi283.lib.registry.HTFluidContent
import hiiragi283.lib.resource.HTSimpleValueWithKey
import hiiragi283.lib.resource.vanillaId
import hiiragi283.lib.tag.CommonTagPrefixes
import hiiragi283.lib.tag.HTCommonTags
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.material.HTItemPart
import hiiragi283.ragium.api.material.RagiumMaterial
import hiiragi283.ragium.api.tag.RagiumTags
import hiiragi283.ragium.common.block.RagiumBlocks
import hiiragi283.ragium.common.fluid.RagiumFluids
import hiiragi283.ragium.common.item.RagiumItems
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.neoforged.neoforge.common.Tags
import java.util.concurrent.CompletableFuture

class RagiumItemTagsProvider(output: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>) :
    HTItemTagsProvider(output, lookupProvider, RagiumAPI.MOD_ID) {
    override fun appendTags(registries: HolderLookup.Provider) {
        RagiumBlockItemTagsProvider { (_, item: TagKey<Item>) ->
            HTBlockItemTagsProvider.forItem(builder(item))
        }.run()
        // Material
        builder(CommonTagPrefixes.GEM, RagiumMaterial.Gem.ECHO).add(createKey(vanillaId("echo_shard")))

        RagiumItems.MATERIAL_ITEMS
            .forEach { (part: HTItemPart, material: RagiumMaterial, item: HTSimpleValueWithKey<Item>) ->
                builder(part.tagPrefix, material).add(item)
                if (part == HTItemPart.GEM || part == HTItemPart.INGOT) {
                    builder(ItemTags.BEACON_PAYMENT_ITEMS).add(item)
                }
                if (part == HTItemPart.NUGGET) {
                    builder(ItemTags.METAL_NUGGETS).add(item)
                }
            }
        // Buckets
        for (content: HTFluidContent in RagiumFluids.REGISTER.asSequence()) {
            builders(Tags.Items.BUCKETS, content.bucketTag).add(content.bucketHolder)
        }
        // Tools
        for (toolType: HTToolType in HTToolType.entries) {
            builder(toolType.toolTag).add(RagiumItems.SOOTY_IRON_TOOLS[toolType])
        }

        builder(RagiumTags.Items.SOOTY_IRON_TOOL_MATERIALS)
            .addTag(CommonTagPrefixes.INGOT.itemTagKey(RagiumMaterial.Metal.SOOTY_IRON))
        // Other
        builder(ItemTags.SLABS)
            .add(RagiumBlocks.FLUORITE_SLAB)
            .add(RagiumBlocks.CRYOLITE_SLAB)
        builder(ItemTags.STAIRS)
            .add(RagiumBlocks.FLUORITE_STAIRS)
            .add(RagiumBlocks.CRYOLITE_STAIRS)

        builder(ItemTags.PLANKS).add(RagiumItems.PARTICLE_BOARD)

        builder(Tags.Items.FEATHERS).add(RagiumItems.SYNTHETIC_FEATHER)
        builder(Tags.Items.LEATHERS).add(RagiumItems.SYNTHETIC_LEATHER)
        builder(Tags.Items.STRINGS).add(RagiumItems.SYNTHETIC_FIBER)

        builder(HTCommonTags.Items.COAL_COKE).add(RagiumItems.COAL_COKE)
        builder(HTCommonTags.Items.PAPER).add(createKey(vanillaId("paper")))
        builder(HTCommonTags.Items.PITCH_COKE).add(RagiumItems.PITCH_COKE)
        builder(HTCommonTags.Items.PLASTICS).add(RagiumItems.PLASTIC_PLATE)
        builder(HTCommonTags.Items.SILICON).add(RagiumItems.CRUDE_SILICON)
        builder(HTCommonTags.Items.STICKY_BALLS).addTag(Tags.Items.SLIME_BALLS)

        builder(RagiumTags.Items.COALS)
            .add(RagiumItems.BAMBOO_CHARCOAL)
            .add(createKey(vanillaId("charcoal")))
            .add(createKey(vanillaId("coal")))
        builder(RagiumTags.Items.COKES)
            .addTag(HTCommonTags.Items.COAL_COKE)
            .addTag(HTCommonTags.Items.PITCH_COKE)
        builder(RagiumTags.Items.SHOW_FLUID_TOOLTIPS)
            .add(RagiumBlocks.TANK)
            .add(RagiumBlocks.VOID_TANK)
            .add(RagiumBlocks.CREATIVE_TANK)
    }
}
