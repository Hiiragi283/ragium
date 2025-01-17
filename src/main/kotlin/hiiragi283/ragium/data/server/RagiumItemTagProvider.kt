package hiiragi283.ragium.data.server

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.content.HTItemContent
import hiiragi283.ragium.api.extension.itemTagKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.data.addElement
import hiiragi283.ragium.data.addTag
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.TagsProvider
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagBuilder
import net.minecraft.world.item.Item
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

class RagiumItemTagProvider(
    output: PackOutput,
    provider: CompletableFuture<HolderLookup.Provider>,
    existingFileHelper: ExistingFileHelper,
) : TagsProvider<Item>(output, Registries.ITEM, provider, RagiumAPI.MOD_ID, existingFileHelper) {
    override fun addTags(provider: HolderLookup.Provider) {
        // Materials
        RagiumBlocks.Ores.entries.forEach { ore: RagiumBlocks.Ores ->
            getOrCreateRawBuilder(ore.tagPrefix.commonTagKey)
                .addTag(ore.prefixedTagKey)

            getOrCreateRawBuilder(ore.prefixedTagKey)
                .addElement(ore)
        }

        RagiumBlocks.StorageBlocks.entries.forEach { storage: RagiumBlocks.StorageBlocks ->
            getOrCreateRawBuilder(storage.tagPrefix.commonTagKey)
                .addTag(storage.prefixedTagKey)

            getOrCreateRawBuilder(storage.prefixedTagKey)
                .addElement(storage)
        }

        RagiumItems.MATERIALS.forEach { content: HTItemContent.Material ->
            getOrCreateRawBuilder(content.tagPrefix.commonTagKey)
                .addTag(content.prefixedTagKey)

            getOrCreateRawBuilder(content.prefixedTagKey)
                .addElement(content)
        }
        // Parts
        RagiumItems.Circuits.entries.forEach { circuit: RagiumItems.Circuits ->
            getOrCreateRawBuilder(circuit.machineTier.getCircuitTag())
                .addElement(circuit)
        }

        val plates: TagBuilder = getOrCreateRawBuilder(HTTagPrefix.PLATE.commonTagKey)
        RagiumItems.Plastics.entries.forEach { plastic: RagiumItems.Plastics ->
            plates.addElement(plastic)
        }
        // Tool
        getOrCreateRawBuilder(ItemTags.DURABILITY_ENCHANTABLE).addElement(RagiumItems.FORGE_HAMMER)

        getOrCreateRawBuilder(
            itemTagKey(ResourceLocation.fromNamespaceAndPath("modern_industrialization", "forge_hammer_tools")),
        ).addElement(RagiumItems.FORGE_HAMMER)
    }
}
