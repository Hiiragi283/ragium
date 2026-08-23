package hiiragi283.ragium.data.model

import hiiragi283.lib.data.model.HTModelProvider
import hiiragi283.lib.registry.HTFluidContent
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.block.RagiumBlocks
import hiiragi283.ragium.fluid.RagiumFluids
import hiiragi283.ragium.item.RagiumItems
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
            add(RagiumFluids.OMINOUS_FLUX)
            add(RagiumFluids.MOLTEN_GLASS)
            add(RagiumFluids.MOLTEN_REDSTONE)
            add(RagiumFluids.MOLTEN_GLOWSTONE)
            add(RagiumFluids.MOLTEN_ENDER)
            add(RagiumFluids.MOLTEN_BLAZE)

            add(RagiumFluids.CREOSOTE)
            add(RagiumFluids.CRUDE_OIL)
            add(RagiumFluids.SULFURIC_ACID)
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

    private fun registerBlockModels(generators: BlockModelGenerators) {
        RagiumBlocks.MATERIAL_BLOCKS.values.forEach { generators.createTrivialCube(it.get()) }

        // Machine
        RagiumBlocks.MACHINES.values.forEach { generators.createNonTemplateModelBlock(it.get()) }
    }

    private fun registerItemModels(generators: ItemModelGenerators) {
        buildSet {
            addAll(RagiumItems.REGISTER.asSequence())
        }.forEach { generators.generateFlatItem(it) }
    }
}
