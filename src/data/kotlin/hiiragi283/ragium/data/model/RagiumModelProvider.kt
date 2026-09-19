package hiiragi283.ragium.data.model

import hiiragi283.lib.HTConstants
import hiiragi283.lib.data.model.HTModelProvider
import hiiragi283.lib.data.model.HTModelTemplates
import hiiragi283.lib.data.model.createBlock
import hiiragi283.lib.registry.HTFluidContent
import hiiragi283.lib.registry.HTSimpleDeferredBlockAndItem
import hiiragi283.lib.registry.HTSimpleDeferredItem
import hiiragi283.lib.resource.HTValueWithId
import hiiragi283.lib.resource.blockId
import hiiragi283.lib.resource.vanillaId
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.tag.HTMachineType
import hiiragi283.ragium.common.block.HTMachineBlock
import hiiragi283.ragium.common.block.RagiumBlocks
import hiiragi283.ragium.common.fluid.RagiumFluids
import hiiragi283.ragium.common.item.RagiumItems
import net.minecraft.client.data.models.BlockModelGenerators
import net.minecraft.client.data.models.ItemModelGenerators
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
            addAll(RagiumFluids.DYES)
            add(RagiumFluids.HONEY)
            add(RagiumFluids.OMINOUS_FLUX)
            add(RagiumFluids.MOLTEN_GLASS)
            add(RagiumFluids.MOLTEN_REDSTONE)
            add(RagiumFluids.MOLTEN_GLOWSTONE)
            add(RagiumFluids.MOLTEN_ENDER)

            add(RagiumFluids.WOOD_TAR)
            add(RagiumFluids.COAL_TAR)
            add(RagiumFluids.CRUDE_OIL)
            add(RagiumFluids.SULFURIC_ACID)

            add(RagiumFluids.ORE_SLURRY)
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
        sequence {
            yieldAll(RagiumBlocks.MATERIAL_BLOCKS.values)

            yield(RagiumBlocks.CREATIVE_BATTERY) // TODO

            yield(RagiumBlocks.MACHINE_CASING)
        }.forEach { generators.createTrivialCube(it.getOrThrow()) }

        // Machine
        val inactiveModels: Map<HTMachineType, Identifier> = HTMachineType.entries
            .associateWith { machineType: HTMachineType ->
                val blockId: Identifier = RagiumAPI.id(HTConstants.BLOCK, "machine", machineType.materialName)
                ModelTemplates.CUBE_ORIENTABLE.create(
                    blockId,
                    textureMapping(machineType).put(TextureSlot.FRONT, Material(blockId.withSuffix("_front"))),
                    generators.modelOutput
                )
            }
        RagiumBlocks.MACHINES.flatEntries
            .forEach { (machineType: HTMachineType, blockItem: HTSimpleDeferredBlockAndItem) ->
                generators.blockStateOutput.accept(
                    MultiVariantGenerator.dispatch(blockItem.getOrThrow())
                        .with(
                            PropertyDispatch.initial(HTMachineBlock.IS_ACTIVE)
                                .select(false, BlockModelGenerators.plainVariant(inactiveModels[machineType]!!))
                                .select(
                                    true,
                                    BlockModelGenerators.plainVariant(machineModel(generators, machineType, blockItem))
                                )
                        ).with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING)
                )
            }
        // Decoration
        for ((machineType: HTMachineType, block: HTSimpleDeferredBlockAndItem) in RagiumBlocks.MACHINE_CASINGS) {
            generators.createSimple(
                block.getOrThrow(),
                ModelTemplates.CUBE_TOP.createBlock(
                    block,
                    textureMapping(machineType),
                    generators.modelOutput
                )
            )
        }
        // Storage
        generators.createTrivialBlock(RagiumBlocks.TANK.getOrThrow(), HTModelTemplates.Providers.TANK_TEMPLATE)
        generators.createTrivialBlock(RagiumBlocks.VOID_TANK.getOrThrow(), HTModelTemplates.Providers.TANK_TEMPLATE)
        generators.createTrivialBlock(RagiumBlocks.CREATIVE_TANK.getOrThrow(), HTModelTemplates.Providers.TANK_TEMPLATE)
    }

    private fun machineModel(
        generators: BlockModelGenerators,
        machineType: HTMachineType,
        block: HTValueWithId<Block>
    ): Identifier {
        val blockId: Identifier = block.idOrThrow.blockId
        return ModelTemplates.CUBE_ORIENTABLE.create(
            blockId,
            textureMapping(machineType).put(TextureSlot.FRONT, Material(blockId.withSuffix("_front"))),
            generators.modelOutput
        )
    }

    private fun textureMapping(machineType: HTMachineType): TextureMapping = TextureMapping()
        .put(TextureSlot.TOP, Material(RagiumBlocks.MACHINE_CASING.idOrThrow.blockId))
        .put(
            TextureSlot.SIDE,
            Material(RagiumAPI.id(HTConstants.BLOCK, "machine", "${machineType.materialName}_side"))
        )

    private fun registerItemModels(generators: ItemModelGenerators) {
        val handheld: Set<HTSimpleDeferredItem> = buildSet {
            add(RagiumItems.BAMBOO_CHARCOAL)
            add(RagiumItems.WITHER_DOLL)

            addAll(RagiumItems.SOOTY_IRON_TOOLS)
        }.onEach { generators.generateFlatItem(it, template = ModelTemplates.FLAT_HANDHELD_ITEM) }

        buildSet {
            addAll(RagiumItems.REGISTER.asSequence())

            remove(RagiumItems.SPLASH_BOTTLE)
            remove(RagiumItems.LINGERING_BOTTLE)

            remove(RagiumItems.MEMORY_DISC)

            removeAll(RagiumItems.MACHINE_PARTS.values)
            removeAll(handheld)
        }.forEach { generators.generateFlatItem(it) }

        generators.generateFlatItem(
            RagiumItems.SPLASH_BOTTLE,
            layer = vanillaId(HTConstants.ITEM, "splash_potion")
        )
        generators.generateFlatItem(
            RagiumItems.LINGERING_BOTTLE,
            layer = vanillaId(HTConstants.ITEM, "lingering_potion")
        )
        generators.generateFlatItem(RagiumItems.MEMORY_DISC, template = ModelTemplates.MUSIC_DISC)

        for ((machineType: HTMachineType, parts: HTSimpleDeferredItem) in RagiumItems.MACHINE_PARTS) {
            generators.generateFlatItem(
                parts,
                layer = RagiumAPI.id(HTConstants.ITEM, "parts", machineType.materialName)
            )
        }
    }
}
