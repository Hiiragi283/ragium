package hiiragi283.ragium.data.client

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTDataGenContext
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.registry.HTHolderLike
import hiiragi283.ragium.api.registry.blockId
import hiiragi283.ragium.api.registry.impl.HTBasicDeferredBlock
import hiiragi283.ragium.api.registry.impl.HTDeferredBlock
import hiiragi283.ragium.api.registry.impl.HTDescriptionDeferredBlock
import hiiragi283.ragium.api.registry.impl.HTSimpleDeferredBlock
import hiiragi283.ragium.api.registry.toHolderLike
import hiiragi283.ragium.api.registry.vanillaId
import hiiragi283.ragium.common.HTDecorationType
import hiiragi283.ragium.common.block.HTCropBlock
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import hiiragi283.ragium.common.variant.HTGlassVariant
import hiiragi283.ragium.common.variant.HTOreVariant
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumFluidContents
import net.minecraft.core.Direction
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.level.block.IronBarsBlock
import net.minecraft.world.level.block.SlabBlock
import net.minecraft.world.level.block.StairBlock
import net.minecraft.world.level.block.WallBlock
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.client.model.generators.BlockStateProvider
import net.neoforged.neoforge.client.model.generators.ConfiguredModel
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder
import net.neoforged.neoforge.client.model.generators.ModelBuilder
import net.neoforged.neoforge.client.model.generators.ModelFile
import net.neoforged.neoforge.client.model.generators.ModelProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper

class RagiumBlockStateProvider(context: HTDataGenContext) : BlockStateProvider(context.output, RagiumAPI.MOD_ID, context.fileHelper) {
    private val fileHelper: ExistingFileHelper = context.fileHelper

    override fun registerStatesAndModels() {
        // Simple Blocks
        buildSet {
            // Resource
            add(RagiumBlocks.SILT)

            add(RagiumBlocks.SOOTY_COBBLESTONE)
            add(RagiumBlocks.SMOOTH_BLACKSTONE)
            add(RagiumBlocks.CRIMSON_SOIL)

            addAll(RagiumBlocks.getMaterialMap(CommonMaterialPrefixes.STORAGE_BLOCK).values)
            // Device
            add(RagiumBlocks.DEVICE_CASING)

            add(RagiumBlocks.ITEM_COLLECTOR)

            add(RagiumBlocks.DIM_ANCHOR)
            add(RagiumBlocks.ENI)

            add(RagiumBlocks.TELEPAD)

            add(RagiumBlocks.CEU)

            // Decoration
            addAll(RagiumBlocks.DECORATION_MAP.values)
        }.forEach(::simpleBlockAndItem)

        altTextureBlock(RagiumBlocks.BUDDING_QUARTZ, vanillaId("block", "chiseled_quartz_block_top"))
        layeredBlock(RagiumBlocks.MYSTERIOUS_OBSIDIAN, vanillaId("block", "obsidian"), RagiumBlocks.MYSTERIOUS_OBSIDIAN.blockId)

        // Decoration
        for (type: HTDecorationType in HTDecorationType.entries) {
            val textureId: ResourceLocation = type.base.blockId
            val slab: HTBasicDeferredBlock<SlabBlock> = type.slab
            val stair: HTBasicDeferredBlock<StairBlock> = type.stairs
            val wall: HTBasicDeferredBlock<WallBlock> = type.wall

            slabBlock(slab, textureId)
            stairsBlock(stair, textureId)
            wallBlock(wall, textureId)
        }

        for (block: HTSimpleDeferredBlock in RagiumBlocks.LED_BLOCKS.values) {
            altModelBlock(block, RagiumAPI.id("block", "led_block"))
            itemModels().withExistingParent(block.getPath(), RagiumAPI.id("block", "led_block"))
        }

        for ((_, bars: HTBasicDeferredBlock<IronBarsBlock>) in RagiumBlocks.METAL_BARS) {
            val texture: ResourceLocation = bars.blockId
            paneBlockWithRenderType(bars.get(), texture, texture, "cutout")
            itemModels()
                .getBuilder(bars)
                .parent(ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", texture)
        }
        RagiumBlocks.GLASSES.forEach { (variant: HTGlassVariant, _, block: HTDescriptionDeferredBlock<*>) ->
            when (variant) {
                HTGlassVariant.DEFAULT -> cutoutSimpleBlock(block)
                HTGlassVariant.TINTED -> translucentSimpleBlock(block)
            }
        }
        RagiumBlocks.COILS.values.forEach(::cubeColumn)

        // Ore
        RagiumBlocks.ORES.forEach { (variant: HTOreVariant, key: HTMaterialKey, ore: HTSimpleDeferredBlock) ->
            layeredBlock(
                ore,
                variant.baseStone.toHolderLike().getIdWithPrefix("block/"),
                RagiumAPI.id("block", key.name),
            )
        }

        cubeColumn(RagiumBlocks.RESONANT_DEBRIS)
        cutoutSimpleBlock(RagiumBlocks.IMITATION_SPAWNER)

        crossBlock(RagiumBlocks.QUARTZ_CLUSTER)

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
        itemModels().basicItem(RagiumBlocks.EXP_BERRIES.id)

        getVariantBuilder(RagiumBlocks.WARPED_WART.get())
            .forAllStates { state: BlockState ->
                val age: Int = when (state.getValue(HTCropBlock.AGE)) {
                    0 -> 0
                    1 -> 1
                    2 -> 1
                    else -> 2
                }
                val id: ResourceLocation = RagiumBlocks.WARPED_WART.getIdWithSuffix("_stage$age")
                ConfiguredModel
                    .builder()
                    .modelFile(
                        models()
                            .withExistingParent(id.path, "crop")
                            .texture("crop", id.withPrefix("block/"))
                            .renderType("cutout"),
                    ).build()
            }
        itemModels().basicItem(RagiumBlocks.WARPED_WART.id)

        // Food
        altModelBlock(RagiumBlocks.SWEET_BERRIES_CAKE)

        // Machine
        fun machine(
            block: HTDeferredBlock<*, *>,
            top: ResourceLocation,
            bottom: ResourceLocation,
            front: ResourceLocation = block.blockId.withSuffix("_front"),
        ) {
            horizontalBlock(
                block,
                models()
                    .withExistingParent(block.blockId.path, RagiumAPI.id("block", "machine_base"))
                    .texture("top", top)
                    .texture("bottom", bottom)
                    .texture("front", front),
            )
        }

        val basicCasing: ResourceLocation = RagiumAPI.id("block", "basic_machine_casing")
        val basicFrame: ResourceLocation = RagiumAPI.id("block", "basic_machine_frame")
        val bricks: ResourceLocation = vanillaId("block", "bricks")

        val advancedCasing: ResourceLocation = RagiumAPI.id("block", "advanced_machine_casing")
        val advancedFrame: ResourceLocation = RagiumAPI.id("block", "advanced_machine_frame")
        val blackstone: ResourceLocation = vanillaId("block", "polished_blackstone_bricks")

        val eliteMachine: ResourceLocation = RagiumAPI.id("block", "elite_machine_casing")
        val eliteFrame: ResourceLocation = RagiumAPI.id("block", "elite_machine_frame")
        val deepslateTiles: ResourceLocation = vanillaId("block", "deepslate_tiles")

        val ultimateMachine: ResourceLocation = RagiumAPI.id("block", "ultimate_machine_casing")
        val endStoneBricks: ResourceLocation = vanillaId("block", "end_stone_bricks")

        // Generator
        builtIn(RagiumBlocks.THERMAL_GENERATOR, basicCasing)
        builtIn(RagiumBlocks.CULINARY_GENERATOR, advancedCasing)
        builtIn(RagiumBlocks.MAGMATIC_GENERATOR, advancedCasing)
        builtIn(RagiumBlocks.COMBUSTION_GENERATOR, eliteMachine)
        altModelBlock(RagiumBlocks.SOLAR_PANEL_UNIT)
        builtIn(RagiumBlocks.ENCHANTMENT_GENERATOR, ultimateMachine)

        // Processor
        val smelterFront: ResourceLocation = RagiumAPI.id("block", "smelter_front")
        // Basic
        machine(RagiumBlocks.ALLOY_SMELTER, basicCasing, bricks, smelterFront)
        machine(RagiumBlocks.BLOCK_BREAKER, basicCasing, bricks)
        machine(RagiumBlocks.COMPRESSOR, basicCasing, bricks)
        machine(RagiumBlocks.CUTTING_MACHINE, basicCasing, bricks)
        machine(RagiumBlocks.ELECTRIC_FURNACE, basicCasing, vanillaId("block", "furnace_side"), smelterFront)
        machine(RagiumBlocks.EXTRACTOR, basicCasing, bricks)
        machine(RagiumBlocks.PULVERIZER, basicCasing, bricks)
        // Advanced
        machine(RagiumBlocks.CRUSHER, advancedCasing, blackstone, RagiumAPI.id("block", "pulverizer_front"))
        machine(RagiumBlocks.MELTER, advancedFrame, blackstone)
        machine(RagiumBlocks.MIXER, advancedFrame, blackstone)
        altModelBlock(RagiumBlocks.REFINERY, factory = ::horizontalBlock)
        // Elite
        machine(RagiumBlocks.ADVANCED_MIXER, eliteFrame, deepslateTiles, RagiumAPI.id("block", "mixer_front"))
        machine(RagiumBlocks.BREWERY, eliteMachine, deepslateTiles)
        machine(RagiumBlocks.MULTI_SMELTER, eliteMachine, deepslateTiles, smelterFront)
        machine(RagiumBlocks.PLANTER, eliteMachine, deepslateTiles)
        // Ultimate
        machine(RagiumBlocks.ENCHANTER, ultimateMachine, endStoneBricks)
        machine(RagiumBlocks.MOB_CRUSHER, ultimateMachine, endStoneBricks)
        machine(RagiumBlocks.SIMULATOR, ultimateMachine, endStoneBricks)

        // Device
        layeredBlock(
            RagiumBlocks.FLUID_COLLECTOR,
            vanillaId("block", "water_still"),
            RagiumAPI.id("block", "device_overlay"),
        )

        // Storages
        cutoutSimpleBlock(RagiumBlocks.CRATE, basicFrame)
        altModelBlock(RagiumBlocks.TANK)

        // Fluids
        for (content: HTFluidContent<*, *, *, *, *> in RagiumFluidContents.REGISTER.contents) {
            simpleBlock(
                content.block.get(),
                models()
                    .getBuilder(content.blockId)
                    .texture("particle", vanillaId("block", "water_still")),
            )
        }
    }

    //    Extensions    //

    private fun <BUILDER : ModelBuilder<BUILDER>, PROVIDER : ModelProvider<BUILDER>> PROVIDER.getBuilder(id: ResourceLocation): BUILDER =
        this.getBuilder(id.toString())

    private fun <BUILDER : ModelBuilder<BUILDER>, PROVIDER : ModelProvider<BUILDER>> PROVIDER.getBuilder(holder: HTHolderLike): BUILDER =
        this.getBuilder(holder.getId())

    private fun Direction.getRotationY(): Int = ((this.toYRot() + 180) % 360).toInt()

    // Block
    private fun simpleBlockAndItem(block: HTDeferredBlock<*, *>, model: ModelFile = cubeAll(block.get())) {
        simpleBlockWithItem(block.get(), model)
    }

    private fun layeredBlock(block: HTDeferredBlock<*, *>, layer0: ResourceLocation, layer1: ResourceLocation) {
        simpleBlockAndItem(
            block,
            models()
                .withExistingParent(block.getPath(), RagiumAPI.id("block", "layered"))
                .texture("layer0", layer0)
                .texture("layer1", layer1)
                .renderType("cutout"),
        )
    }

    private fun horizontalBlock(block: HTDeferredBlock<*, *>, model: ModelFile) {
        horizontalBlock(block.get(), model)
        itemModels().simpleBlockItem(block.get())
    }

    private fun cubeColumn(
        block: HTDeferredBlock<*, *>,
        side: ResourceLocation = block.blockId.withSuffix("_side"),
        end: ResourceLocation = block.blockId.withSuffix("_top"),
    ) {
        simpleBlockAndItem(block, models().cubeColumn(block.blockId.path, side, end))
    }

    private fun altModelBlock(
        block: HTDeferredBlock<*, *>,
        id: ResourceLocation = block.blockId,
        factory: (HTDeferredBlock<*, *>, ModelFile) -> Unit = ::simpleBlockAndItem,
    ) {
        factory(block, ModelFile.ExistingModelFile(id, fileHelper))
    }

    private fun altTextureBlock(block: HTDeferredBlock<*, *>, all: ResourceLocation) {
        simpleBlockAndItem(block, models().cubeAll(block.getPath(), all))
    }

    private fun cutoutSimpleBlock(block: HTDeferredBlock<*, *>, texId: ResourceLocation = block.blockId) {
        simpleBlockAndItem(block, models().cubeAll(block.getPath(), texId).renderType("cutout"))
    }

    private fun translucentSimpleBlock(block: HTDeferredBlock<*, *>, texId: ResourceLocation = block.blockId) {
        simpleBlockAndItem(block, models().cubeAll(block.getPath(), texId).renderType("translucent"))
    }

    private fun slabBlock(block: HTDeferredBlock<SlabBlock, *>, texture: ResourceLocation) {
        slabBlock(block.get(), texture, texture)
        itemModels().simpleBlockItem(block.id)
    }

    private fun stairsBlock(block: HTDeferredBlock<StairBlock, *>, texture: ResourceLocation) {
        stairsBlock(block.get(), texture)
        itemModels().simpleBlockItem(block.id)
    }

    private fun wallBlock(block: HTDeferredBlock<WallBlock, *>, texture: ResourceLocation) {
        wallBlock(block.get(), texture)
        itemModels().wallInventory(block.getPath(), texture)
    }

    private fun crossBlock(block: HTDeferredBlock<*, *>) {
        val blockId: ResourceLocation = block.blockId
        // Block
        directionalBlock(
            block.get(),
            models()
                .withExistingParent(block.getPath(), "cross")
                .texture("cross", blockId)
                .renderType("cutout"),
        )
        // Item
        itemModels()
            .getBuilder(block)
            .parent(ModelFile.UncheckedModelFile("item/generated"))
            .texture("layer0", blockId)
    }

    private fun builtIn(block: HTDeferredBlock<*, *>, particle: ResourceLocation) {
        // Block
        simpleBlock(block.get(), models().getBuilder(block).texture("particle", particle))
        // Item
        itemModels()
            .getBuilder(block)
            .parent(ModelFile.UncheckedModelFile(vanillaId("builtin", "entity")))
            .blockTransforms()
    }

    /*private fun pieBlock(block: HTDeferredBlock<out PieBlock, *>) {
        val blockId: ResourceLocation = block.blockId

        getVariantBuilder(block.get()).forAllStates { state: BlockState ->
            val bites: Int = state.getValue(PieBlock.BITES)
            val suffix: String = if (bites > 0) "_slice$bites" else ""

            val pieModel: BlockModelBuilder = models()
                .withExistingParent(block.getPath() + suffix, RagiumConst.FARMERS_DELIGHT.toId("block", "pie$suffix"))
                .texture("particle", blockId.withSuffix("_top"))
                .texture("bottom", RagiumConst.FARMERS_DELIGHT.toId("block", "pie_bottom"))
                .texture("side", RagiumConst.FARMERS_DELIGHT.toId("block", "pie_side"))
                .texture("top", blockId.withSuffix("_top"))
                .texture("inner", blockId.withSuffix("_inner"))

            ConfiguredModel
                .builder()
                .modelFile(pieModel)
                .rotationY(state.getValue(PieBlock.FACING).getRotationY())
                .build()
        }
        itemModels().basicItem(block.id)
    }*/

    // Item
    private fun ItemModelBuilder.blockTransforms(): ItemModelBuilder = this.transforms {
        transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND) {
            rotation(75f, 45f, 0f).translation(0f, 2.5f, 0f).scale(0.375f)
        }
        transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND) {
            rotation(75f, 45f, 0f).translation(0f, 2.5f, 0f).scale(0.375f)
        }
        transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND) { rotation(0f, 45f, 0f).scale(0.4f) }
        transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND) { rotation(0f, 225f, 0f).scale(0.4f) }
        transform(ItemDisplayContext.GROUND) { translation(0f, 3f, 0f).scale(0.25f) }
        transform(ItemDisplayContext.GUI) { rotation(30f, 225f, 0f).scale(0.625f) }
        transform(ItemDisplayContext.FIXED) { scale(0.5f) }
    }

    private fun ItemModelBuilder.transforms(builderAction: ModelBuilder<*>.TransformsBuilder.() -> Unit): ItemModelBuilder =
        this.transforms().apply(builderAction).end()

    private fun ModelBuilder<*>.TransformsBuilder.transform(
        context: ItemDisplayContext,
        builderAction: ModelBuilder<*>.TransformsBuilder.TransformVecBuilder.() -> Unit,
    ): ModelBuilder<*>.TransformsBuilder = this.transform(context).apply(builderAction).end()

    /*private fun <T : Any> ConfiguredModel.Builder<T>.rotationY(state: BlockState): ConfiguredModel.Builder<T> =
        rotationY(state.getValue(HTBlockStateProperties.HORIZONTAL).getRotationY())

    private fun cauldronBlock(block: DeferredBlock<*>) {
        if (block == RagiumBlocks.HONEY_CAULDRON) return
        getVariantBuilder(block.get())
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
    }*/
}
