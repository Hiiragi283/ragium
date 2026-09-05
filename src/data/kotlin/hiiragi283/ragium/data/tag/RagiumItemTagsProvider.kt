package hiiragi283.ragium.data.tag

import hiiragi283.lib.collection.forEach
import hiiragi283.lib.data.tag.HTBlockItemTagsProvider
import hiiragi283.lib.data.tag.HTItemTagsProvider
import hiiragi283.lib.registry.HTFluidContent
import hiiragi283.lib.resource.HTSimpleKeyOrValue
import hiiragi283.lib.tag.CommonTagPrefixes
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.material.HTItemPart
import hiiragi283.ragium.api.material.RagiumMaterial
import hiiragi283.ragium.api.tag.RagiumTags
import hiiragi283.ragium.common.fluid.RagiumFluids
import hiiragi283.ragium.common.item.RagiumItems
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags
import java.util.concurrent.CompletableFuture

class RagiumItemTagsProvider(output: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>) :
    HTItemTagsProvider(output, lookupProvider, RagiumAPI.MOD_ID) {
    override fun appendTags(registries: HolderLookup.Provider) {
        RagiumBlockItemTagsProvider { (_, item: TagKey<Item>) ->
            HTBlockItemTagsProvider.forItem(builder(item))
        }.run()
        // Material
        builder(CommonTagPrefixes.GEM, RagiumMaterial.Gem.ECHO).addItem(Items.ECHO_SHARD)

        RagiumItems.MATERIAL_ITEMS.forEach { (part: HTItemPart, material, item: HTSimpleKeyOrValue<Item>) ->
            builder(part.tagPrefix, material).add(item)
            if (part == HTItemPart.NUGGET) {
                builder(ItemTags.METAL_NUGGETS).add(item)
            }
        }
        // Buckets
        for (content: HTFluidContent in RagiumFluids.REGISTER.asSequence()) {
            builders(Tags.Items.BUCKETS, content.bucketTag).add(content.bucketHolder)
        }

        // Other
        builder(ItemTags.PLANKS).add(RagiumItems.PARTICLE_BOARD)

        builder(Tags.Items.FEATHERS).add(RagiumItems.SYNTHETIC_FEATHER)
        builder(Tags.Items.LEATHERS).add(RagiumItems.SYNTHETIC_LEATHER)
        builder(Tags.Items.STRINGS).add(RagiumItems.SYNTHETIC_FIBER)

        builder(RagiumTags.Items.PAPER).addItem(Items.PAPER)
        builder(RagiumTags.Items.PLASTICS).add(RagiumItems.PLASTIC_PLATE)
        builder(RagiumTags.Items.STICKY_BALLS).addTag(Tags.Items.SLIME_BALLS)
        RagiumItems.SHAPE_PATTERNS.forEach(builder(RagiumTags.Items.SHAPE_PATTERNS)::add)
    }
}
