package hiiragi283.ragium.data.tag

import hiiragi283.lib.data.tag.HTItemTagsProvider
import hiiragi283.lib.data.tag.builders
import hiiragi283.lib.registry.HTFluidContent
import hiiragi283.lib.tag.CommonTagPrefixes
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.tag.HTMachineType
import hiiragi283.ragium.api.tag.RagiumTags
import hiiragi283.ragium.common.fluid.RagiumFluids
import hiiragi283.ragium.common.item.RagiumItems
import hiiragi283.ragium.common.material.VanillaMaterialKeys
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.common.Tags

class RagiumItemTagsProvider(output: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>, contentsGetter: CompletableFuture<TagLookup<Block>>) : HTItemTagsProvider(output, lookupProvider, contentsGetter, RagiumAPI.MOD_ID) {
    override fun appendTags(registries: HolderLookup.Provider) {
        // Copy
        copy(CommonTagPrefixes.STORAGE_BLOCK, VanillaMaterialKeys.GLOWSTONE)
        copy(CommonTagPrefixes.STORAGE_BLOCK, VanillaMaterialKeys.QUARTZ)
        copy(CommonTagPrefixes.STORAGE_BLOCK, VanillaMaterialKeys.AMETHYST)

        HTMachineType.entries.forEach { copy(HTMachineType.PREFIX, it) }
        // Material
        builders(CommonTagPrefixes.GEM, VanillaMaterialKeys.ECHO).addItem(Items.ECHO_SHARD)
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
