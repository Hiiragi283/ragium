package hiiragi283.ragium.data.tag

import hiiragi283.lib.collection.forEach
import hiiragi283.lib.data.tag.HTItemTagsProvider
import hiiragi283.lib.registry.HTFluidContent
import hiiragi283.lib.resource.HTKeyLike
import hiiragi283.lib.tag.CommonTagPrefixes
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.tag.HTBlockPart
import hiiragi283.ragium.api.tag.HTItemPart
import hiiragi283.ragium.api.tag.HTMachineType
import hiiragi283.ragium.api.tag.HTMaterial
import hiiragi283.ragium.api.tag.RagiumTags
import hiiragi283.ragium.block.RagiumBlocks
import hiiragi283.ragium.fluid.RagiumFluids
import hiiragi283.ragium.item.RagiumItems
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.common.Tags

class RagiumItemTagsProvider(output: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>, contentsGetter: CompletableFuture<TagLookup<Block>>) : HTItemTagsProvider(output, lookupProvider, contentsGetter, RagiumAPI.MOD_ID) {
    override fun appendTags(registries: HolderLookup.Provider) {
        // Copy
        copy(CommonTagPrefixes.STORAGE_BLOCK, HTMaterial.Mineral.GLOWSTONE)
        copy(CommonTagPrefixes.STORAGE_BLOCK, HTMaterial.Gem.QUARTZ)
        copy(CommonTagPrefixes.STORAGE_BLOCK, HTMaterial.Gem.AMETHYST)

        RagiumBlocks.MATERIAL_BLOCKS.forEach { (part: HTBlockPart, material: HTMaterial, _) -> copy(part.tagPrefix, material) }

        HTMachineType.entries.forEach { copy(HTMachineType.PREFIX, it) }
        // Material
        tags(CommonTagPrefixes.GEM, HTMaterial.Gem.ECHO).addItem(Items.ECHO_SHARD)

        RagiumItems.MATERIAL_ITEMS.forEach { (part: HTItemPart, material: HTMaterial, item: HTKeyLike<Item>) ->
            tags(part.tagPrefix, material).add(item)
            if (part == HTItemPart.NUGGET) {
                builder(ItemTags.METAL_NUGGETS).add(item)
            }
        }
        // Buckets
        for (content: HTFluidContent in RagiumFluids.REGISTER.asSequence()) {
            tags(Tags.Items.BUCKETS, content.bucketTag).add(content.bucketHolder)
        }

        // Other
        builder(ItemTags.PLANKS).add(RagiumItems.PARTICLE_BOARD)

        builder(Tags.Items.FEATHERS).add(RagiumItems.SYNTHETIC_FEATHER)
        builder(Tags.Items.LEATHERS).add(RagiumItems.SYNTHETIC_LEATHER)
        builder(Tags.Items.STRINGS).add(RagiumItems.SYNTHETIC_FIBER)

        builder(RagiumTags.Items.PAPER).addItem(Items.PAPER)
        builder(RagiumTags.Items.STICKY_BALLS).addTag(Tags.Items.SLIME_BALLS)
    }
}
