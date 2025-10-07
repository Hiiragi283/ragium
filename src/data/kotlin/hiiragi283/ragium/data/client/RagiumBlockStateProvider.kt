package hiiragi283.ragium.data.client

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.HTDataGenContext
import hiiragi283.ragium.api.extension.blockId
import hiiragi283.ragium.api.extension.forEach
import hiiragi283.ragium.api.extension.rowValues
import hiiragi283.ragium.api.extension.toId
import hiiragi283.ragium.api.extension.vanillaId
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.material.HTMaterialVariant
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.registry.impl.HTDeferredBlock
import hiiragi283.ragium.api.registry.impl.HTSimpleDeferredBlock
import hiiragi283.ragium.api.variant.HTVariantKey
import hiiragi283.ragium.common.block.HTCropBlock
import hiiragi283.ragium.common.material.HTBlockMaterialVariant
import hiiragi283.ragium.common.variant.HTDecorationVariant
import hiiragi283.ragium.common.variant.HTDeviceVariant
import hiiragi283.ragium.common.variant.HTGeneratorVariant
import hiiragi283.ragium.common.variant.HTMachineVariant
import hiiragi283.ragium.integration.delight.RagiumDelightAddon
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumFluidContents
import net.minecraft.core.Direction
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.SlabBlock
import net.minecraft.world.level.block.StairBlock
import net.minecraft.world.level.block.WallBlock
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder
import net.neoforged.neoforge.client.model.generators.BlockStateProvider
import net.neoforged.neoforge.client.model.generators.ConfiguredModel
import net.neoforged.neoforge.client.model.generators.ModelFile
import net.neoforged.neoforge.common.data.ExistingFileHelper
import vectorwing.farmersdelight.common.block.PieBlock
import java.util.function.Supplier

class RagiumBlockStateProvider(context: HTDataGenContext) : BlockStateProvider(context.output, RagiumAPI.MOD_ID, context.fileHelper) {
    private val fileHelper: ExistingFileHelper = context.fileHelper

    override fun registerStatesAndModels() {
        // Simple Blocks
        buildSet {
            add(RagiumBlocks.CRIMSON_SOIL)
            add(RagiumBlocks.SILT)

            addAll(RagiumBlocks.DECORATION_MAP.values)
            addAll(RagiumBlocks.MATERIALS.rowValues(HTBlockMaterialVariant.STORAGE_BLOCK))

            add(RagiumBlocks.DEVICE_CASING)
        }.forEach(::simpleBlock)

        layeredBlock(
            RagiumBlocks.MYSTERIOUS_OBSIDIAN,
            vanillaId("block/obsidian"),
            RagiumAPI.id("block/mysterious_obsidian"),
        )

        // Decoration
        for (variant: HTDecorationVariant in HTDecorationVariant.entries) {
            val textureId: ResourceLocation = variant.textureId
            val slab: Supplier<SlabBlock> = variant.slab
            val stair: Supplier<StairBlock> = variant.stairs
            val wall: Supplier<WallBlock> = variant.wall

            slabBlock(slab.get(), textureId, textureId)
            stairsBlock(stair.get(), textureId)
            wallBlock(wall.get(), textureId)
        }

        for (block: HTSimpleDeferredBlock in RagiumBlocks.LED_BLOCKS.values) {
            altModelBlock(block, RagiumAPI.id("block/led_block"))
        }

        RagiumBlocks.MATERIALS.rowValues(HTBlockMaterialVariant.GLASS_BLOCK).forEach(::cutoutSimpleBlock)
        RagiumBlocks.MATERIALS.rowValues(HTBlockMaterialVariant.TINTED_GLASS_BLOCK).forEach(::translucentSimpleBlock)

        RagiumBlocks.COILS.values.forEach(::cubeColumn)

        // Ore
        RagiumBlocks.ORES.forEach { (variant: HTMaterialVariant.BlockTag, material: HTMaterialType, ore: HTSimpleDeferredBlock) ->
            val textureId: String = RagiumAPI.id("block/${material.materialName()}").toString()
            val stoneTex: String = when (variant) {
                HTBlockMaterialVariant.ORE -> "block/stone"
                HTBlockMaterialVariant.DEEP_ORE -> "block/deepslate"
                HTBlockMaterialVariant.NETHER_ORE -> "block/netherrack"
                HTBlockMaterialVariant.END_ORE -> "block/end_stone"
                else -> null
            } ?: return@forEach
            simpleBlock(
                ore.get(),
                ConfiguredModel(
                    models()
                        .withExistingParent(ore.getPath(), RagiumAPI.id("block/layered"))
                        .texture("layer0", vanillaId(stoneTex))
                        .texture("layer1", textureId)
                        .renderType("cutout"),
                ),
            )
        }

        cubeColumn(RagiumBlocks.RESONANT_DEBRIS)

        // Crop
        getVariantBuilder(RagiumBlocks.EXP_BERRIES.get())
            .forAllStates { state: BlockState ->
                val age: Int = state.getValue(HTCropBlock.AGE)
                val id: ResourceLocation = RagiumAPI.id("exp_berry_bush_stage$age")
                ConfiguredModel
                    .builder()
                    .modelFile(
                        models()
                            .withExistingParent(id.path, "cross")
                            .texture("cross", id.withPrefix("block/"))
                            .renderType("cutout"),
                    ).build()
            }

        getVariantBuilder(RagiumBlocks.WARPED_WART.get())
            .forAllStates { state: BlockState ->
                val age: Int = when (state.getValue(HTCropBlock.AGE)) {
                    0 -> 0
                    1 -> 1
                    2 -> 1
                    else -> 2
                }
                val id: ResourceLocation = RagiumBlocks.WARPED_WART.id.withSuffix("_stage$age")
                ConfiguredModel
                    .builder()
                    .modelFile(
                        models()
                            .withExistingParent(id.path, "crop")
                            .texture("crop", id.withPrefix("block/"))
                            .renderType("cutout"),
                    ).build()
            }

        // Food
        altModelBlock(RagiumBlocks.SWEET_BERRIES_CAKE)

        // Machine Frame
        altTextureBlock(RagiumBlocks.WOODEN_CASING, vanillaId("block/note_block"))

        cubeColumn(RagiumBlocks.STONE_CASING, vanillaId("block/furnace_side"), vanillaId("block/furnace_top"))
        cubeColumn(
            RagiumBlocks.REINFORCED_STONE_CASING,
            vanillaId("block/blast_furnace_side"),
            vanillaId("block/blast_furnace_top"),
        )

        // Machine
        fun machine(
            variant: HTVariantKey.WithBE<*>,
            top: ResourceLocation,
            bottom: ResourceLocation,
            front: ResourceLocation,
        ) {
            val holder: HTDeferredBlock<*, *> = variant.blockHolder
            horizontalBlock(
                holder.get(),
                models()
                    .withExistingParent("block/" + holder.getPath(), RagiumAPI.id("block/machine_base"))
                    .texture("top", top)
                    .texture("bottom", bottom)
                    .texture("front", front),
            )
        }

        fun machine(variant: HTVariantKey.WithBE<*>, top: ResourceLocation, bottom: ResourceLocation) {
            machine(variant, top, bottom, variant.blockHolder.id.withPath { "block/${it}_front" })
        }

        val basicMachine: ResourceLocation = RagiumAPI.id("block/basic_machine_casing")
        val bricks: ResourceLocation = vanillaId("block/bricks")

        val advancedMachine: ResourceLocation = RagiumAPI.id("block/advanced_machine_casing")
        val blackstone: ResourceLocation = vanillaId("block/polished_blackstone_bricks")

        val eliteMachine: ResourceLocation = RagiumAPI.id("block/elite_machine_casing")
        val deepslateTiles: ResourceLocation = vanillaId("block/deepslate_tiles")

        // Generator
        machine(HTGeneratorVariant.THERMAL, basicMachine, bricks)

        // Processor
        val smelterFront: ResourceLocation = RagiumAPI.id("block/smelter_front")
        // Basic
        machine(HTMachineVariant.ALLOY_SMELTER, basicMachine, bricks, smelterFront)
        machine(HTMachineVariant.BLOCK_BREAKER, basicMachine, bricks)
        machine(HTMachineVariant.COMPRESSOR, basicMachine, bricks)
        machine(HTMachineVariant.CUTTING_MACHINE, basicMachine, bricks)
        machine(HTMachineVariant.EXTRACTOR, basicMachine, bricks)
        machine(HTMachineVariant.PULVERIZER, basicMachine, bricks)
        // Advanced
        machine(HTMachineVariant.CRUSHER, advancedMachine, blackstone, RagiumAPI.id("block/pulverizer_front"))
        machine(HTMachineVariant.MELTER, advancedMachine, blackstone)
        altModelBlock(HTMachineVariant.REFINERY.blockHolder, factory = ::horizontalBlock)
        machine(HTMachineVariant.WASHER, advancedMachine, blackstone)
        // Elite
        machine(HTMachineVariant.BREWERY, eliteMachine, deepslateTiles)
        machine(HTMachineVariant.MULTI_SMELTER, eliteMachine, deepslateTiles, smelterFront)
        machine(HTMachineVariant.PLANTER, eliteMachine, deepslateTiles)
        machine(HTMachineVariant.SIMULATOR, eliteMachine, deepslateTiles)

        // Device
        for ((variant: HTDeviceVariant, block: HTDeferredBlock<*, *>) in RagiumBlocks.DEVICES) {
            when (variant) {
                HTDeviceVariant.WATER_COLLECTOR -> {
                    layeredBlock(
                        block,
                        vanillaId("block/water_still"),
                        RagiumAPI.id("block/device_overlay"),
                    )
                }
                HTDeviceVariant.LAVA_COLLECTOR -> {
                    layeredBlock(
                        block,
                        vanillaId("block/lava_still"),
                        RagiumAPI.id("block/device_overlay"),
                    )
                }
                HTDeviceVariant.MILK_COLLECTOR -> {
                    layeredBlock(
                        block,
                        RagiumConst.NEOFORGE.toId("block/milk_still"),
                        RagiumAPI.id("block/device_overlay"),
                    )
                }
                else -> simpleBlock(block.get())
            }
        }

        // Storages
        for (drum: HTDeferredBlock<*, *> in RagiumBlocks.DRUMS.values) {
            val id: ResourceLocation = drum.blockId
            simpleBlock(
                drum.get(),
                models().cubeColumn(
                    id.path,
                    id.withSuffix("_side"),
                    id.withSuffix("_top"),
                ),
            )
        }

        // Fluids
        for (content: HTFluidContent<*, *, *> in RagiumFluidContents.REGISTER.contents) {
            getVariantBuilder(content.getBlock())
                .partialState()
                .addModels(
                    models()
                        .getBuilder("block/${content.getPath()}")
                        .texture("particle", vanillaId("block/water_still"))
                        .let(::ConfiguredModel),
                )
        }

        // Delight
        pieBlock(RagiumDelightAddon.RAGI_CHERRY_PIE)
    }

    //    Extensions    //

    private fun pieBlock(block: HTDeferredBlock<out PieBlock, *>) {
        val blockId: ResourceLocation = block.blockId

        getVariantBuilder(block.get()).forAllStates { state: BlockState ->
            val bites: Int = state.getValue(PieBlock.BITES)
            val suffix: String = if (bites > 0) "_slice$bites" else ""
            val pieModel: BlockModelBuilder = models()
                .getBuilder(block.getPath() + suffix)
                .parent(ModelFile.ExistingModelFile(RagiumConst.FARMERS_DELIGHT.toId("block/pie$suffix"), fileHelper))
                .texture("particle", blockId.withSuffix("_top"))
                .texture("bottom", RagiumConst.FARMERS_DELIGHT.toId("block/pie_bottom"))
                .texture("side", RagiumConst.FARMERS_DELIGHT.toId("block/pie_side"))
                .texture("top", blockId.withSuffix("_top"))
                .texture("inner", blockId.withSuffix("_inner"))

            ConfiguredModel
                .builder()
                .modelFile(pieModel)
                .rotationY(state.getValue(PieBlock.FACING).getRotationY())
                .build()
        }
    }

    private fun Direction.getRotationY(): Int = ((this.toYRot() + 180) % 360).toInt()

    private fun simpleBlock(holder: HTDeferredBlock<*, *>) {
        simpleBlock(holder.get())
    }

    private fun layeredBlock(holder: HTDeferredBlock<*, *>, layer0: ResourceLocation, layer1: ResourceLocation) {
        simpleBlock(
            holder.get(),
            ConfiguredModel(
                models()
                    .withExistingParent(holder.getPath(), RagiumAPI.id("block/layered"))
                    .texture("layer0", layer0)
                    .texture("layer1", layer1)
                    .renderType("cutout"),
            ),
        )
    }

    private fun cubeColumn(
        holder: HTDeferredBlock<*, *>,
        side: ResourceLocation = holder.blockId.withSuffix("_side"),
        end: ResourceLocation = holder.blockId.withSuffix("_top"),
    ) {
        simpleBlock(holder.get(), models().cubeColumn(holder.blockId.path, side, end))
    }

    private fun altModelBlock(
        holder: HTDeferredBlock<*, *>,
        id: ResourceLocation = holder.blockId,
        factory: (Block, ModelFile) -> Unit = ::simpleBlock,
    ) {
        factory(holder.get(), ModelFile.ExistingModelFile(id, fileHelper))
    }

    private fun altTextureBlock(holder: HTDeferredBlock<*, *>, all: ResourceLocation) {
        simpleBlock(
            holder.get(),
            ConfiguredModel(models().cubeAll(holder.getPath(), all)),
        )
    }

    private fun cutoutSimpleBlock(holder: HTDeferredBlock<*, *>, texId: ResourceLocation = holder.blockId) {
        simpleBlock(
            holder.get(),
            ConfiguredModel(models().cubeAll(holder.getPath(), texId).renderType("cutout")),
        )
    }

    private fun translucentSimpleBlock(holder: HTDeferredBlock<*, *>, texId: ResourceLocation = holder.blockId) {
        simpleBlock(
            holder.get(),
            ConfiguredModel(models().cubeAll(holder.getPath(), texId).renderType("translucent")),
        )
    }

    /*private fun <T : Any> ConfiguredModel.Builder<T>.rotationY(state: BlockState): ConfiguredModel.Builder<T> =
        rotationY(state.getValue(HTBlockStateProperties.HORIZONTAL).getRotationY())

    private fun cauldronBlock(holder: DeferredBlock<*>) {
        if (holder == RagiumBlocks.HONEY_CAULDRON) return
        getVariantBuilder(holder.get())
            .forAllStates { state: BlockState ->
                val level: Int = state.getValue(LayeredCauldronBlock.LEVEL)
                val suffix: String = when (level) {
                    3 -> "_full"
                    else -> "_level$level"
                }
                ConfiguredModel
                    .builder()
                    .modelFile(modelFile(vanillaId("block/water_cauldron").withSuffix(suffix)))
                    .build()
            }
    }

    private fun feastBlock(holder: DeferredBlock<out HTFeastBlock>) {
        getVariantBuilder(holder.get()).forAllStates { state: BlockState ->
            val block: HTFeastBlock = holder.get()
            val property: IntegerProperty = block.getServingsProperty()
            val servings: Int = state.getValue(property)
            val suffix: String =
                if (servings == 0) {
                    if (block.hasLeftovers) {
                        "_leftover"
                    } else {
                        "_stage${property.possibleValues.size - 2}"
                    }
                } else {
                    "_stage${block.getMaxServings() - servings}"
                }
            ConfiguredModel
                .builder()
                .modelFile(modelFile(holder.blockId.withSuffix(suffix)))
                .rotationY(state)
                .build()
        }
    }*/
}
