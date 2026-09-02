package hiiragi283.ragium.data.model

import hiiragi283.lib.HTConstants
import hiiragi283.lib.data.model.HTModelProvider
import hiiragi283.lib.data.model.createBlock
import hiiragi283.lib.registry.HTFluidContent
import hiiragi283.lib.resource.HTIdOrValue
import hiiragi283.lib.resource.blockId
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.tag.HTMachineType
import hiiragi283.ragium.common.block.HTMachineBlock
import hiiragi283.ragium.common.block.RagiumBlocks
import hiiragi283.ragium.common.fluid.RagiumFluids
import hiiragi283.ragium.common.item.RagiumItems
import net.minecraft.client.data.models.BlockModelGenerators
import net.minecraft.client.data.models.ItemModelGenerators
import net.minecraft.client.data.models.MultiVariant
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator
import net.minecraft.client.data.models.blockstates.PropertyDispatch
import net.minecraft.client.data.models.model.ModelTemplates
import net.minecraft.client.data.models.model.TextureMapping
import net.minecraft.client.data.models.model.TextureSlot
import net.minecraft.client.resources.model.sprite.Material
import net.minecraft.data.PackOutput
import net.minecraft.resources.Identifier
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
        RagiumBlocks.MATERIAL_BLOCKS.values.forEach { generators.createTrivialCube(it.getOrThrow()) }

        // Machine
        for ((machineType: HTMachineType, block: HTIdOrValue<Block>) in RagiumBlocks.MACHINES.flatEntries) {
            val inactiveModel: MultiVariant = BlockModelGenerators.plainVariant(
                machineModel(generators, machineType, block, false)
            )
            val activeModel: MultiVariant = BlockModelGenerators.plainVariant(
                machineModel(generators, machineType, block, true)
            )
            generators.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(block.getOrThrow())
                    .with(
                        PropertyDispatch.initial(HTMachineBlock.IS_ACTIVE)
                            .select(false, inactiveModel)
                            .select(true, activeModel)
                    ).with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING)
            )
        }
    }

    private fun machineModel(
        generators: BlockModelGenerators,
        machineType: HTMachineType,
        block: HTIdOrValue<Block>,
        isActive: Boolean
    ): Identifier {
        val blockId: Identifier = block.idOrThrow.blockId
        val mapping: TextureMapping = TextureMapping()
            .put(TextureSlot.TOP, Material(RagiumAPI.id(HTConstants.BLOCK, "machine_casing")))
            .put(
                TextureSlot.SIDE,
                Material(RagiumAPI.id(HTConstants.BLOCK, "machine_casing", machineType.materialName))
            )
        return when (isActive) {
            true -> ModelTemplates.CUBE_ORIENTABLE.create(
                blockId.withSuffix("_active"),
                mapping.put(TextureSlot.FRONT, Material(blockId.withSuffix("_front_active"))),
                generators.modelOutput
            )

            false -> ModelTemplates.CUBE_ORIENTABLE.createBlock(
                block,
                mapping.put(TextureSlot.FRONT, Material(blockId.withSuffix("_front"))),
                generators.modelOutput
            )
        }
    }

    private fun registerItemModels(generators: ItemModelGenerators) {
        buildSet {
            addAll(RagiumItems.REGISTER.asSequence())

            remove(RagiumItems.BAMBOO_CHARCOAL)
            remove(RagiumItems.WITHER_DOLL)
            remove(RagiumItems.MEMORY_DISC)
        }.forEach { generators.generateFlatItem(it) }

        generators.generateFlatItem(RagiumItems.BAMBOO_CHARCOAL, template = ModelTemplates.FLAT_HANDHELD_ITEM)
        generators.generateFlatItem(RagiumItems.WITHER_DOLL, template = ModelTemplates.FLAT_HANDHELD_ITEM)

        generators.generateFlatItem(RagiumItems.MEMORY_DISC, template = ModelTemplates.MUSIC_DISC)
    }
}
