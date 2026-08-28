package hiiragi283.ragium.data.model

import hiiragi283.lib.HTConstants
import hiiragi283.lib.data.model.HTModelProvider
import hiiragi283.lib.data.model.createBlock
import hiiragi283.lib.registry.HTFluidContent
import hiiragi283.lib.resource.SupplierWithId
import hiiragi283.lib.resource.modifyPath
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.tag.HTMachineType
import hiiragi283.ragium.common.block.RagiumBlocks
import hiiragi283.ragium.common.fluid.RagiumFluids
import hiiragi283.ragium.common.item.RagiumItems
import net.minecraft.client.data.models.BlockModelGenerators
import net.minecraft.client.data.models.ItemModelGenerators
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator
import net.minecraft.client.data.models.model.ModelTemplates
import net.minecraft.client.data.models.model.TextureMapping
import net.minecraft.client.data.models.model.TextureSlot
import net.minecraft.client.resources.model.sprite.Material
import net.minecraft.data.PackOutput
import net.minecraft.world.level.block.Block

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
        for ((machineType: HTMachineType, block: SupplierWithId<Block>) in RagiumBlocks.MACHINES.flatEntries) {
            val casing = Material(RagiumAPI.id(HTConstants.BLOCK, "machine", "casing", machineType.materialName))
            generators.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(
                    block.get(),
                    BlockModelGenerators.plainVariant(
                        ModelTemplates.CUBE_ORIENTABLE.createBlock(
                            block,
                            TextureMapping()
                                .put(TextureSlot.TOP, casing)
                                .put(TextureSlot.SIDE, casing)
                                .put(
                                    TextureSlot.FRONT,
                                    Material(block.getId().modifyPath { "${HTConstants.BLOCK}/mahcine/${it}_front" }),
                                ),
                            generators.modelOutput,
                        ),
                    ),
                ).with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING),
            )
        }
    }

    private fun registerItemModels(generators: ItemModelGenerators) {
        buildSet {
            addAll(RagiumItems.REGISTER.asSequence())
        }.forEach { generators.generateFlatItem(it) }
    }
}
