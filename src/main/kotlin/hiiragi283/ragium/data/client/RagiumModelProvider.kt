package hiiragi283.ragium.data.client

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.content.HTBlockContent
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumItems
import net.minecraft.data.PackOutput
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.client.model.generators.ItemModelProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper

class RagiumModelProvider(output: PackOutput, existingFileHelper: ExistingFileHelper) :
    ItemModelProvider(output, RagiumAPI.MOD_ID, existingFileHelper) {
    override fun registerModels() {
        registerBlocks()
        registerItems()
    }

    private fun registerBlocks() {
        buildList {
            addAll(RagiumBlocks.StorageBlocks.entries)

            addAll(RagiumBlocks.Grates.entries)
            addAll(RagiumBlocks.Casings.entries)
            addAll(RagiumBlocks.Hulls.entries)
            addAll(RagiumBlocks.Coils.entries)

            addAll(RagiumBlocks.Drums.entries)
        }.map(HTBlockContent::id)
            .forEach(::simpleBlockItem)
    }

    private fun registerItems() {
        buildList {
            addAll(RagiumItems.MATERIALS)

            addAll(RagiumItems.Circuits.entries)

            addAll(RagiumItems.FOODS)

            addAll(RagiumItems.Circuits.entries)
            addAll(RagiumItems.PressMolds.entries)
            addAll(RagiumItems.Catalysts.entries)
            addAll(RagiumItems.INGREDIENTS)
            addAll(RagiumItems.Radioactives.entries)
        }.map(ItemLike::asItem)
            .forEach(::basicItem)
    }
}
