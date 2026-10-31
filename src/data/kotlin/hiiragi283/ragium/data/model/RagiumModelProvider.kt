package hiiragi283.ragium.data.model

import hiiragi283.lib.HTConstants
import hiiragi283.lib.data.model.HTModelProvider
import hiiragi283.lib.data.model.HTModelTemplates
import hiiragi283.lib.registry.HTFluidContent
import hiiragi283.lib.resource.HTSimpleValueWithKey
import hiiragi283.lib.resource.blockId
import hiiragi283.lib.resource.vanillaId
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConstants
import hiiragi283.ragium.api.machine.HTMachineType
import hiiragi283.ragium.common.block.RagiumBlockStateProperties
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
import net.minecraft.client.data.models.model.TexturedModel
import net.minecraft.client.resources.model.sprite.Material
import net.minecraft.data.PackOutput
import net.minecraft.resources.Identifier
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

class RagiumModelProvider(output: PackOutput) : HTModelProvider(output, RagiumAPI.MOD_ID) {
    override fun registerModels(blockModels: BlockModelGenerators, itemModels: ItemModelGenerators) {
        // Fluids
        val dripFluids: List<HTFluidContent> = buildList {
            addAll(RagiumFluids.DYES)
            add(RagiumFluids.HONEY)
            add(RagiumFluids.MOLTEN_BEESWAX)
            add(RagiumFluids.RESIN)
            add(RagiumFluids.OMINOUS_FLUX)
            add(RagiumFluids.MOLTEN_GLASS)
            add(RagiumFluids.MOLTEN_REDSTONE)
            add(RagiumFluids.MOLTEN_GLOWSTONE)
            add(RagiumFluids.MOLTEN_ENDER)

            add(RagiumFluids.CRUDE_OIL)
            add(RagiumFluids.MOLTEN_PLASTIC)
            add(RagiumFluids.SYNTHETIC_RESIN)
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
        sequence {
            yieldAll(RagiumBlocks.MATERIAL_BLOCKS.values)
            yield(RagiumBlocks.ECHO_BLOCK)
            yield(RagiumBlocks.FLUORITE_BLOCK)
            // yield(RagiumBlocks.CRYOLITE_BLOCK)

            yield(RagiumBlocks.CREATIVE_BATTERY) // TODO

            yield(RagiumBlocks.MACHINE_CASING)
        }.forEach { generators.createTrivialCube(it.getOrThrow()) }

        RagiumBlocks.ALL_DECORATIONS.forEach { generators.createDecoration(it) }

        // Dynamo
        RagiumBlocks.DYNAMOS.values.flatten().forEach {
            val block: Block = it.getOrThrow()
            generators.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(
                    block,
                    BlockModelGenerators.plainVariant(HTModelTemplates.Providers.DYNAMO_TEMPLATE.create(block, generators.modelOutput)),
                ).with(BlockModelGenerators.ROTATIONS_COLUMN_WITH_FACING),
            )
        }
        // Machine
        val inactiveModels: MutableMap<HTMachineType, Identifier> = hashMapOf()
        RagiumBlocks.MACHINES
            .entries
            .forEach { (machineType: HTMachineType, blockItems: List<HTSimpleValueWithKey<Block>>) ->
                val inactive: Identifier = inactiveModels.computeIfAbsent(machineType) {
                    val blockId: Identifier = RagiumAPI.id(HTConstants.BLOCK, RagiumConstants.MACHINE, machineType.materialName)
                    ModelTemplates.CUBE_ORIENTABLE.create(
                        blockId,
                        textureMapping(machineType).put(TextureSlot.FRONT, Material(blockId.withSuffix("_front"))),
                        generators.modelOutput,
                    )
                }
                for (blockItem: HTSimpleValueWithKey<Block> in blockItems) {
                    createMachineBlock(
                        generators,
                        blockItem,
                        inactive,
                        machineModel(generators, machineType, blockItem),
                    )
                }
            }

        // Decoration
        generators.createGlassBlocks(
            RagiumBlocks.QUARTZ_GLASS.getOrThrow(),
            RagiumBlocks.QUARTZ_GLASS_PANE.getOrThrow(),
        )

        for ((machineType: HTMachineType, block: HTSimpleValueWithKey<Block>) in RagiumBlocks.MACHINE_CASINGS) {
            generators.createSimple(block, ModelTemplates.CUBE_TOP, textureMapping(machineType))
        }
        // Storage
        for (blockItem: HTSimpleValueWithKey<Block> in RagiumBlocks.TANKS) {
            generators.createTrivialBlock(blockItem.getOrThrow(), HTModelTemplates.Providers.TANK_TEMPLATE)
        }
        // Misc
        generators.createTrivialBlock(RagiumBlocks.UPGRADE_STATION.getOrThrow(), TexturedModel.CUBE_TOP_BOTTOM)
    }

    private fun createMachineBlock(generators: BlockModelGenerators, block: HTSimpleValueWithKey<Block>, inactive: Identifier, active: Identifier) {
        generators.blockStateOutput.accept(
            MultiVariantGenerator.dispatch(block.getOrThrow())
                .with(
                    PropertyDispatch.initial(RagiumBlockStateProperties.IS_ACTIVE)
                        .select(false, BlockModelGenerators.plainVariant(inactive))
                        .select(true, BlockModelGenerators.plainVariant(active)),
                ).with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING),
        )
    }

    private fun machineModel(generators: BlockModelGenerators, machineType: HTMachineType, block: HTSimpleValueWithKey<Block>): Identifier {
        val blockId: Identifier = block.id().blockId
        return ModelTemplates.CUBE_ORIENTABLE.create(
            blockId,
            textureMapping(machineType).put(TextureSlot.FRONT, Material(blockId.withSuffix("_front"))),
            generators.modelOutput,
        )
    }

    private fun textureMapping(machineType: HTMachineType): TextureMapping = TextureMapping()
        .put(TextureSlot.TOP, Material(RagiumBlocks.MACHINE_CASING.id().blockId))
        .put(
            TextureSlot.SIDE,
            Material(RagiumAPI.id(HTConstants.BLOCK, RagiumConstants.MACHINE, "${machineType.materialName}_side")),
        )

    private fun registerItemModels(generators: ItemModelGenerators) {
        val handheld: Set<HTSimpleValueWithKey<Item>> = buildSet {
            add(RagiumItems.BAMBOO_CHARCOAL)
            add(RagiumItems.WITHER_DOLL)

            addAll(RagiumItems.SOOTY_IRON_TOOLS)
        }.onEach { generators.generateFlatItem(it, template = ModelTemplates.FLAT_HANDHELD_ITEM) }

        buildSet {
            addAll(RagiumItems.REGISTER.asSequence())

            remove(RagiumItems.SPLASH_BOTTLE)
            remove(RagiumItems.LINGERING_BOTTLE)

            remove(RagiumItems.MEMORY_DISC)

            removeAll(handheld)
        }.forEach { generators.generateFlatItem(it) }

        generators.generateFlatItem(
            RagiumItems.SPLASH_BOTTLE,
            layer = vanillaId(HTConstants.ITEM, "splash_potion"),
        )
        generators.generateFlatItem(
            RagiumItems.LINGERING_BOTTLE,
            layer = vanillaId(HTConstants.ITEM, "lingering_potion"),
        )
        generators.generateFlatItem(RagiumItems.MEMORY_DISC, template = ModelTemplates.MUSIC_DISC)
    }
}
