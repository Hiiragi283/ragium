package hiiragi283.ragium.data.server

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.content.HTItemContent
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumItems
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.TagsProvider
import net.minecraft.tags.ItemTags
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
        RagiumBlocks.StorageBlocks.entries.forEach { storage: RagiumBlocks.StorageBlocks ->
            getOrCreateRawBuilder(storage.tagPrefix.commonTagKey)
                .addTag(storage.prefixedTagKey.location)

            getOrCreateRawBuilder(storage.prefixedTagKey)
                .addElement(storage.id)
        }

        RagiumItems.MATERIALS.forEach { content: HTItemContent.Material ->
            getOrCreateRawBuilder(content.tagPrefix.commonTagKey)
                .addTag(content.prefixedTagKey.location)

            getOrCreateRawBuilder(content.prefixedTagKey)
                .addElement(content.id)
        }
        // Parts
        RagiumItems.Circuits.entries.forEach { circuit: RagiumItems.Circuits ->
            getOrCreateRawBuilder(circuit.machineTier.getCircuitTag())
                .addElement(circuit.id)
        }
        // Tool
        getOrCreateRawBuilder(ItemTags.DURABILITY_ENCHANTABLE).addElement(RagiumItems.FORGE_HAMMER.id)
    }
}
