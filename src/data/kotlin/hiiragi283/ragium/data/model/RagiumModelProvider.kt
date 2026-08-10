package hiiragi283.ragium.data.model

import hiiragi283.lib.data.model.HTModelProvider
import hiiragi283.lib.registry.HTFluidContent
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.init.RagiumFluids
import net.minecraft.client.data.models.BlockModelGenerators
import net.minecraft.client.data.models.ItemModelGenerators
import net.minecraft.data.PackOutput

class RagiumModelProvider(output: PackOutput) : HTModelProvider(output, RagiumAPI.MOD_ID) {
    override fun registerModels(blockModels: BlockModelGenerators, itemModels: ItemModelGenerators) {
        // Fluids
        val dripFluids: List<HTFluidContent> = buildList {
            // Vanilla
            addAll(RagiumFluids.DYES)

            add(RagiumFluids.HONEY)
        }
        for (content: HTFluidContent in RagiumFluids.REGISTER.asSequence()) {
            // Item
            itemModels.generateBucketItem(content, content in dripFluids)
            // Block
            if (content is HTFluidContent.Flowing) {
                content.blockHolder?.let { blockModels.createFluid(it) }
            }
        }

        // Block
        registerBlockModels(blockModels)
        // Item
        registerItemModels(itemModels)
    }

    private fun registerBlockModels(generators: BlockModelGenerators) = Unit

    private fun registerItemModels(generators: ItemModelGenerators) = Unit
}
