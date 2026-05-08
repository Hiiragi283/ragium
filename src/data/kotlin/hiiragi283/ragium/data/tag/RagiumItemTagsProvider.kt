package hiiragi283.ragium.data.tag

import hiiragi283.core.api.HiiragiCoreAccess
import hiiragi283.core.api.data.tag.HTItemTagsProvider
import hiiragi283.core.api.data.tag.HTTagBuilder
import hiiragi283.core.api.data.tag.HTTagsProvider
import hiiragi283.core.api.material.HTMaterialContents
import hiiragi283.core.api.material.getOrThrow
import hiiragi283.core.api.material.part.CommonParts
import hiiragi283.core.api.material.part.HTPart
import hiiragi283.core.api.registry.HTFluidContent
import hiiragi283.core.api.tag.HiiragiCoreTags
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.tag.RagiumTags
import hiiragi283.ragium.common.integration.mek.RagiumMekItems
import hiiragi283.ragium.common.item.HTFoodCanType
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

class RagiumItemTagsProvider(
    fileHelper: ExistingFileHelper,
    output: PackOutput,
    lookupProvider: CompletableFuture<HolderLookup.Provider>,
    blockTags: CompletableFuture<TagLookup<Block>>,
) : HTItemTagsProvider(fileHelper, output, lookupProvider, RagiumAPI.MOD_ID, blockTags) {
    override fun addTagsInternal(factory: HTTagsProvider.BuilderFactory<Item>) {
        val items: HTMaterialContents<HTPart, HTMaterialContents.ItemEntry> = HiiragiCoreAccess.INSTANCE.registeredContents.items
        // Copy
        copy(RagiumTags.Blocks.DEVICES, RagiumTags.Items.DEVICES)
        copy(RagiumTags.Blocks.GENERATORS, RagiumTags.Items.GENERATORS)
        copy(RagiumTags.Blocks.MACHINES, RagiumTags.Items.MACHINES)

        copy(RagiumTags.Blocks.STORAGES, RagiumTags.Items.STORAGES)
        copy(RagiumTags.Blocks.STORAGES_CREATIVE, RagiumTags.Items.STORAGES_CREATIVE)
        // Buckets
        for (content: HTFluidContent in RagiumFluids.REGISTER.asSequence()) {
            factory.addTags(Tags.Items.BUCKETS, content.bucketTag).add(content.getBucket())
        }
        // Explosives
        RagiumTags.Items.EXPLOSIVES.apply(factory)
        factory
            .apply(RagiumTags.Items.EXPLOSIVES.basic)
            .add(RagiumItems.DYNAMITE)
            .addItem(Items.FIREWORK_ROCKET)
        factory
            .apply(RagiumTags.Items.EXPLOSIVES.advanced)
            .addItem(Items.TNT)
        factory
            .apply(RagiumTags.Items.EXPLOSIVES.elite)
            .add(RagiumBlocks.INDUSTRIAL_TNT)
            .addItem(Items.END_CRYSTAL)
        // Foods
        factory
            .apply(Tags.Items.FOODS_EDIBLE_WHEN_PLACED)
            .add(RagiumBlocks.MEAT_BLOCK)
            .add(RagiumBlocks.COOKED_MEAT_BLOCK)

        val foodsCan: HTTagBuilder<Item> = factory.addTags(Tags.Items.FOODS, RagiumTags.Items.FOODS_CAN)
        HTFoodCanType.entries.forEach(foodsCan::add)

        factory
            .apply(Tags.Items.FOODS_RAW_MEAT)
            .add(items.getOrThrow(CommonParts.INGOT, RagiumMaterialKeys.MEAT))
        factory
            .apply(Tags.Items.FOODS_COOKED_MEAT)
            .add(items.getOrThrow(CommonParts.INGOT, RagiumMaterialKeys.COOKED_MEAT))
        // Others
        factory
            .apply(HiiragiCoreTags.Items.SILICON)
            .add(RagiumItems.CRUDE_SILICON)

        // Integration
        factory
            .addTags(MekanismTags.Items.ENRICHED, RagiumTags.Items.ENRICHED_RAGINITE)
            .add(RagiumMekItems.ENRICHED_RAGINITE)
    }

    private fun copy(blockTags: RagiumTags.TieredTags<Block>, itemTags: RagiumTags.TieredTags<Item>) {
        copy(blockTags.base, itemTags.base)
        copy(blockTags.basic, itemTags.basic)
        copy(blockTags.advanced, itemTags.advanced)
        copy(blockTags.elite, itemTags.elite)
        copy(blockTags.ultimate, itemTags.ultimate)
    }
}
