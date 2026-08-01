package hiiragi283.ragium.data.tag

import hiiragi283.core.api.data.tag.HTTagBuilder
import hiiragi283.core.api.data.tag.HTTagDependType
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.registry.toLike
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HiiragiCoreTags
import hiiragi283.core.support.data.tag.HTItemTagsProvider
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.tag.BlockItemTagKey
import hiiragi283.ragium.api.tag.RagiumTags
import hiiragi283.ragium.common.integration.mek.RagiumMekItems
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumFluids
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture
import mekanism.common.tags.MekanismTags
import net.minecraft.tags.TagKey

class RagiumItemTagsProvider(
    fileHelper: ExistingFileHelper,
    output: PackOutput,
    lookupProvider: CompletableFuture<HolderLookup.Provider>,
    blockTags: CompletableFuture<TagLookup<Block>>,
) : HTItemTagsProvider(fileHelper, output, lookupProvider, RagiumAPI.MOD_ID, blockTags) {
    override fun createEmptyTags(registries: HolderLookup.Provider, consumer: (TagKey<Item>) -> Unit) {
        RagiumTags.Items.EXPLOSIVES.prepare(consumer)
    }

    override fun appendTags(registries: HolderLookup.Provider) {
        // Copy
        RagiumTags.BlockItems.allTags.forEach(::copy)
        // Buckets
        for (content: HTFluidContent in RagiumFluids.REGISTER.asSequence()) {
            tags(Tags.Items.BUCKETS, content.bucketTag).add(content.bucketHolder)
        }
        // Explosives
        RagiumTags.Items.EXPLOSIVES.apply(::builder)
        builder(RagiumTags.Items.EXPLOSIVES.basic)
            .add(RagiumItems.DYNAMITE)
            .add(Items.FIREWORK_ROCKET.toLike())
        builder(RagiumTags.Items.EXPLOSIVES.advanced)
            .add(Items.TNT.toLike())
        builder(RagiumTags.Items.EXPLOSIVES.elite)
            .add(RagiumBlocks.INDUSTRIAL_TNT)
            .add(Items.END_CRYSTAL.toLike())
        // Foods
        builder(Tags.Items.FOODS_EDIBLE_WHEN_PLACED)
            .add(RagiumBlocks.MEAT_BLOCK)
            .add(RagiumBlocks.COOKED_MEAT_BLOCK)

        tags(Tags.Items.FOODS, RagiumTags.Items.FOODS_CAN).add(RagiumItems.CANNED_COOKED_MEAT)

        builder(Tags.Items.FOODS_RAW_MEAT).add(RagiumItems.MEAT_INGOT)
        builder(Tags.Items.FOODS_COOKED_MEAT).add(RagiumItems.COOKED_MEAT_INGOT)

        tags(CommonTagPrefixes.DUST, RagiumMaterialKeys.MEAT).add(RagiumItems.MINCED_MEAT)
        tags(CommonTagPrefixes.INGOT, RagiumMaterialKeys.MEAT).add(RagiumItems.MEAT_INGOT)
        tags(CommonTagPrefixes.INGOT, RagiumMaterialKeys.COOKED_MEAT).add(RagiumItems.COOKED_MEAT_INGOT)
        // Others
        builder(HiiragiCoreTags.Items.SILICON)
            .add(RagiumItems.CRUDE_SILICON)

        // Integration
        tags(MekanismTags.Items.ENRICHED, RagiumTags.Items.ENRICHED_RAGINITE).add(RagiumMekItems.ENRICHED_RAGINITE)
    }

    private fun copy(tagKey: BlockItemTagKey) {
        copy(tagKey.block, tagKey.item)
    }

    fun HTTagBuilder<Item>.addTag(tagKey: BlockItemTagKey, type: HTTagDependType = HTTagDependType.REQUIRED): HTTagBuilder<Item> = this.addTag(tagKey.item, type)
}
