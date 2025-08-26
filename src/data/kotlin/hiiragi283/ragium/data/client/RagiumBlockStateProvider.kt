package hiiragi283.ragium.data.client

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.altModelBlock
import hiiragi283.ragium.api.extension.altTextureBlock
import hiiragi283.ragium.api.extension.blockId
import hiiragi283.ragium.api.extension.cubeColumn
import hiiragi283.ragium.api.extension.cutoutSimpleBlock
import hiiragi283.ragium.api.extension.forEach
import hiiragi283.ragium.api.extension.layeredBlock
import hiiragi283.ragium.api.extension.modelFile
import hiiragi283.ragium.api.extension.rowValues
import hiiragi283.ragium.api.extension.simpleBlock
import hiiragi283.ragium.api.extension.textureId
import hiiragi283.ragium.api.extension.translucentSimpleBlock
import hiiragi283.ragium.api.extension.vanillaId
import hiiragi283.ragium.api.registry.HTSimpleDeferredBlockHolder
import hiiragi283.ragium.api.util.RagiumConst
import hiiragi283.ragium.api.util.material.HTBlockMaterialVariant
import hiiragi283.ragium.api.util.material.HTMaterialType
import hiiragi283.ragium.api.util.material.HTMaterialVariant
import hiiragi283.ragium.common.block.HTCropBlock
import hiiragi283.ragium.integration.delight.RagiumDelightAddon
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.util.variant.HTDecorationVariant
import hiiragi283.ragium.util.variant.HTDeviceVariant
import hiiragi283.ragium.util.variant.HTMachineVariant
import net.minecraft.core.Direction
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.SlabBlock
import net.minecraft.world.level.block.StairBlock
import net.minecraft.world.level.block.WallBlock
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder
import net.neoforged.neoforge.client.model.generators.BlockStateProvider
import net.neoforged.neoforge.client.model.generators.ConfiguredModel
import net.neoforged.neoforge.common.data.ExistingFileHelper
import net.neoforged.neoforge.internal.versions.neoforge.NeoForgeVersion
import net.neoforged.neoforge.registries.DeferredHolder
import vectorwing.farmersdelight.common.block.PieBlock
import java.util.function.Supplier

class RagiumBlockStateProvider(output: PackOutput, exFileHelper: ExistingFileHelper) :
    BlockStateProvider(output, RagiumAPI.MOD_ID, exFileHelper) {
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

        for (block: HTSimpleDeferredBlockHolder in RagiumBlocks.LED_BLOCKS.values) {
            altModelBlock(block, RagiumAPI.id("block/led_block"))
        }

        RagiumBlocks.MATERIALS.rowValues(HTBlockMaterialVariant.GLASS_BLOCK).forEach(::cutoutSimpleBlock)
        RagiumBlocks.MATERIALS.rowValues(HTBlockMaterialVariant.TINTED_GLASS_BLOCK).forEach(::translucentSimpleBlock)

        // Ore
        RagiumBlocks.ORES.forEach { (variant: HTMaterialVariant.BlockTag, material: HTMaterialType, ore: HTSimpleDeferredBlockHolder) ->
            val textureId: String = RagiumAPI.id("block/${material.serializedName}").toString()
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
                        .withExistingParent(ore.id.path, RagiumAPI.id("block/layered"))
                        .texture("layer0", vanillaId(stoneTex))
                        .texture("layer1", textureId)
                        .renderType("cutout"),
                ),
            )
        }

        cubeColumn(RagiumBlocks.RESONANT_DEBRIS)
        // Log
        logBlockWithRenderType(RagiumBlocks.ASH_LOG.get(), "cutout")

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
                Blocks.NETHER_WART
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

        RagiumBlocks.FRAMES.forEach(::cutoutSimpleBlock)

        // Machine
        fun machine(variant: HTMachineVariant, top: ResourceLocation, bottom: ResourceLocation) {
            val holder: DeferredHolder<Block, *> = variant.blockHolder
            horizontalBlock(
                holder.get(),
                models()
                    .withExistingParent("block/" + holder.id.path, RagiumAPI.id("block/machine_base"))
                    .texture("top", top)
                    .texture("bottom", bottom)
                    .texture("front", holder.id.withPath { "block/${it}_front" }),
            )
        }

        val basicMachine: ResourceLocation = RagiumAPI.id("block/basic_machine_casing")
        machine(HTMachineVariant.BLOCK_BREAKER, basicMachine, vanillaId("block/stone"))
        machine(HTMachineVariant.COMPRESSOR, basicMachine, basicMachine)
        machine(HTMachineVariant.ENGRAVER, basicMachine, vanillaId("block/smooth_stone"))
        machine(HTMachineVariant.EXTRACTOR, basicMachine, basicMachine)
        machine(HTMachineVariant.PULVERIZER, basicMachine, vanillaId("block/bricks"))
        machine(HTMachineVariant.SMELTER, basicMachine, vanillaId("block/bricks"))

        val advancedMachine: ResourceLocation = RagiumAPI.id("block/advanced_machine_casing")
        machine(HTMachineVariant.ALLOY_SMELTER, advancedMachine, vanillaId("block/nether_bricks"))
        machine(HTMachineVariant.CRUSHER, advancedMachine, vanillaId("block/nether_bricks"))
        machine(HTMachineVariant.INFUSER, advancedMachine, advancedMachine)
        machine(HTMachineVariant.MELTER, advancedMachine, vanillaId("block/polished_blackstone_bricks"))
        machine(HTMachineVariant.MIXER, advancedMachine, advancedMachine)
        machine(HTMachineVariant.REFINERY, advancedMachine, vanillaId("block/polished_blackstone_bricks"))
        machine(HTMachineVariant.SOLIDIFIER, advancedMachine, advancedMachine)

        // Device
        for ((variant: HTDeviceVariant, block: DeferredHolder<Block, *>) in RagiumBlocks.DEVICES) {
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
                        ResourceLocation.fromNamespaceAndPath(NeoForgeVersion.MOD_ID, "block/milk_still"),
                        RagiumAPI.id("block/device_overlay"),
                    )
                }
                else -> simpleBlock(block.get())
            }
        }

        // Storages
        for (drum: DeferredHolder<Block, *> in RagiumBlocks.DRUMS.values) {
            val id: ResourceLocation = drum.id.withPrefix("block/")
            simpleBlock(
                drum.get(),
                models().cubeColumn(
                    id.path,
                    id.withSuffix("_side"),
                    id.withSuffix("_top"),
                ),
            )
        }

        // Delight
        pieBlock(RagiumDelightAddon.RAGI_CHERRY_PIE)
    }

    //    Extensions    //

    private fun pieBlock(block: DeferredHolder<Block, out PieBlock>) {
        val delight: String = RagiumConst.FARMERS_DELIGHT
        val blockId: ResourceLocation = block.blockId

        getVariantBuilder(block.get()).forAllStates { state: BlockState ->
            val bites: Int = state.getValue(PieBlock.BITES)
            val suffix: String = if (bites > 0) "_slice$bites" else ""
            val pieModel: BlockModelBuilder = models()
                .getBuilder(block.id.path + suffix)
                .parent(modelFile(delight, "block/pie$suffix"))
                .texture("particle", blockId.withSuffix("_top"))
                .texture("bottom", "$delight:block/pie_bottom")
                .texture("side", "$delight:block/pie_side")
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
