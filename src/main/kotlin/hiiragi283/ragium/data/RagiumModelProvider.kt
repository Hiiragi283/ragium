package hiiragi283.ragium.data

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.content.HTContent
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.util.HTCrossDirection
import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.init.RagiumBlockProperties
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumItems
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.minecraft.block.Block
import net.minecraft.block.ConnectingBlock
import net.minecraft.data.client.*
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.item.Items
import net.minecraft.state.property.Properties
import net.minecraft.util.Identifier
import net.minecraft.util.math.Direction

class RagiumModelProvider(output: FabricDataOutput) : FabricModelProvider(output) {
    //   BlockState    //

    override fun generateBlockStateModels(generator: BlockStateModelGenerator) {
        fun register(block: Block, action: (Block) -> Unit) {
            action(block)
        }

        // supplier-based
        fun registerSupplier(block: Block, supplier: BlockStateSupplier) {
            generator.blockStateCollector.accept(supplier)
        }

        fun registerDirectional(block: Block, model: BlockStateVariant = stateVariantOf(block)) {
            registerSupplier(
                block,
                VariantsBlockStateSupplier
                    .create(block, model)
                    .coordinate(BlockStateModelGenerator.createNorthDefaultRotationStates()),
            )
        }

        fun registerHorizontal(block: Block, modelId: Identifier = TextureMap.getId(block)) {
            registerSupplier(
                block,
                VariantsBlockStateSupplier
                    .create(block, stateVariantOf(modelId))
                    .coordinate(BlockStateModelGenerator.createNorthDefaultHorizontalRotationStates()),
            )
        }

        // model-based
        fun registerFactory(block: Block, factory: TexturedModel.Factory) {
            generator.registerSingleton(block, factory)
        }

        fun registerFactory(block: Block, model: Model, getter: (Block) -> TextureMap) {
            registerFactory(block, factory = TexturedModel.makeFactory(getter, model))
        }

        fun registerSimple(block: Block, id: Identifier = TextureMap.getId(block)) {
            generator.registerSingleton(block) { TexturedModel.getCubeAll(id) }
        }

        fun registerLayered(block: Block, layer0: Identifier, layer1: Identifier) {
            registerFactory(block, RagiumModels.LAYERED) {
                HTTextureMapBuilder.create {
                    put(TextureKey.LAYER0, layer0)
                    put(TextureKey.LAYER1, layer1)
                }
            }
        }

        fun registerSlab(slabBlock: Block, fullBlock: Block) {
            val textureMap: TextureMap = HTTextureMapBuilder.create {
                put(TextureKey.SIDE, fullBlock)
                put(TextureKey.TOP, fullBlock)
                put(TextureKey.BOTTOM, fullBlock)
            }
            val slabBottom: Identifier = Models.SLAB.upload(slabBlock, textureMap, generator.modelCollector)
            val slabTop: Identifier = Models.SLAB_TOP.upload(slabBlock, textureMap, generator.modelCollector)
            registerSupplier(
                slabBlock,
                BlockStateModelGenerator.createSlabBlockState(
                    slabBlock,
                    slabBottom,
                    slabTop,
                    TextureMap.getId(fullBlock),
                ),
            )
            generator.registerParentedItemModel(slabBlock, slabBottom)
        }

        fun registerStair(stairBlock: Block, fullBlock: Block) {
            val textureMap: TextureMap = HTTextureMapBuilder.create {
                put(TextureKey.SIDE, fullBlock)
                put(TextureKey.TOP, fullBlock)
                put(TextureKey.BOTTOM, fullBlock)
            }
            val innerId: Identifier = Models.INNER_STAIRS.upload(stairBlock, textureMap, generator.modelCollector)
            val stairId: Identifier = Models.STAIRS.upload(stairBlock, textureMap, generator.modelCollector)
            val outerId: Identifier = Models.OUTER_STAIRS.upload(stairBlock, textureMap, generator.modelCollector)
            registerSupplier(
                stairBlock,
                BlockStateModelGenerator.createStairsBlockState(
                    stairBlock,
                    innerId,
                    stairId,
                    outerId,
                ),
            )
            generator.registerParentedItemModel(stairBlock, stairId)
        }

        fun registerStaticModel(block: Block, modelId: Identifier = TextureMap.getId(block)) {
            registerSupplier(block, VariantsBlockStateSupplier.create(block, stateVariantOf(modelId)))
        }

        // simple
        // asphalt
        registerSimple(RagiumBlocks.ASPHALT)
        registerSlab(RagiumBlocks.ASPHALT_SLAB, RagiumBlocks.ASPHALT)
        registerStair(RagiumBlocks.ASPHALT_STAIRS, RagiumBlocks.ASPHALT)

        registerSimple(RagiumBlocks.POLISHED_ASPHALT)
        registerSlab(RagiumBlocks.POLISHED_ASPHALT_SLAB, RagiumBlocks.POLISHED_ASPHALT)
        registerStair(RagiumBlocks.POLISHED_ASPHALT_STAIRS, RagiumBlocks.POLISHED_ASPHALT)
        // gypsum
        registerSimple(RagiumBlocks.GYPSUM)
        registerSlab(RagiumBlocks.GYPSUM_SLAB, RagiumBlocks.GYPSUM)
        registerStair(RagiumBlocks.GYPSUM_STAIRS, RagiumBlocks.GYPSUM)

        registerSimple(RagiumBlocks.POLISHED_GYPSUM)
        registerSlab(RagiumBlocks.POLISHED_GYPSUM_SLAB, RagiumBlocks.POLISHED_GYPSUM)
        registerStair(RagiumBlocks.POLISHED_GYPSUM_STAIRS, RagiumBlocks.POLISHED_GYPSUM)
        // slate
        registerSimple(RagiumBlocks.SLATE)
        registerSlab(RagiumBlocks.SLATE_SLAB, RagiumBlocks.SLATE)
        registerStair(RagiumBlocks.SLATE_STAIRS, RagiumBlocks.SLATE)

        registerSimple(RagiumBlocks.POLISHED_SLATE)
        registerSlab(RagiumBlocks.POLISHED_SLATE_SLAB, RagiumBlocks.POLISHED_SLATE)
        registerStair(RagiumBlocks.POLISHED_SLATE_STAIRS, RagiumBlocks.POLISHED_SLATE)
        // glass
        registerSimple(RagiumBlocks.STEEL_GLASS)
        registerSimple(RagiumBlocks.RAGIUM_GLASS)

        registerSimple(RagiumBlocks.AUTO_ILLUMINATOR)
        registerSimple(RagiumBlocks.CREATIVE_SOURCE)
        registerSimple(RagiumBlocks.ITEM_DISPLAY)
        registerSimple(RagiumBlocks.MUTATED_SOIL)
        registerSimple(RagiumBlocks.NETWORK_INTERFACE)
        registerSimple(RagiumBlocks.OPEN_CRATE)
        registerSimple(RagiumBlocks.POROUS_NETHERRACK)
        registerSimple(RagiumBlocks.SPONGE_CAKE)
        registerSimple(RagiumBlocks.TELEPORT_ANCHOR)
        registerSimple(RagiumBlocks.TRASH_BOX)

        buildList {
            addAll(RagiumContents.StorageBlocks.entries)
            addAll(RagiumContents.Grates.entries)
        }.map { it.value }.forEach(::registerSimple)

        registerSimple(RagiumContents.Casings.PRIMITIVE.value, Identifier.of("block/blast_furnace_top"))
        registerSimple(RagiumContents.Casings.BASIC.value)
        registerSimple(RagiumContents.Casings.ADVANCED.value)
        // layered
        RagiumContents.Ores.entries.forEach { ore: RagiumContents.Ores ->
            registerLayered(
                ore.value,
                TextureMap.getId(ore.baseStone),
                RagiumAPI.id("block/ore/${ore.material.asString()}"),
            )
        }
        // static
        registerStaticModel(RagiumBlocks.MANUAL_FORGE)
        registerStaticModel(RagiumBlocks.MANUAL_MIXER)
        generator.excludeFromSimpleItemModelGeneration(RagiumBlocks.ROPE)
        registerStaticModel(RagiumBlocks.ROPE)
        registerStaticModel(RagiumBlocks.SWEET_BERRIES_CAKE)
        // factory
        generator.excludeFromSimpleItemModelGeneration(RagiumBlocks.CROSS_WHITE_LINE)
        registerFactory(RagiumBlocks.CROSS_WHITE_LINE, RagiumModels.SURFACE) {
            HTTextureMapBuilder.of(TextureKey.TOP, RagiumBlocks.CROSS_WHITE_LINE)
        }
        registerFactory(RagiumBlocks.BACKPACK_INTERFACE, RagiumModels.ALL_TINTED) {
            TextureMap.all(RagiumBlocks.BACKPACK_INTERFACE)
        }
        registerFactory(RagiumBlocks.ENCHANTMENT_BOOKSHELF, Models.CUBE_COLUMN) {
            HTTextureMapBuilder.create {
                put(TextureKey.END, Identifier.of("block/chiseled_bookshelf_top"))
                put(TextureKey.SIDE, RagiumBlocks.ENCHANTMENT_BOOKSHELF)
            }
        }
        registerFactory(RagiumBlocks.CREATIVE_DRUM, TexturedModel.CUBE_COLUMN)

        RagiumContents.Drums.entries.forEach { registerFactory(it.value, TexturedModel.CUBE_COLUMN) }
        RagiumContents.Hulls.entries.forEach { hull: RagiumContents.Hulls ->
            val tier: HTMachineTier = hull.tier
            registerFactory(hull.value, RagiumModels.HULL) { block: Block ->
                HTTextureMapBuilder.create {
                    put(
                        TextureKey.INSIDE,
                        when (tier) {
                            HTMachineTier.PRIMITIVE -> Identifier.of("block/blast_furnace_top")
                            HTMachineTier.BASIC -> RagiumAPI.id("block/basic_casing")
                            HTMachineTier.ADVANCED -> RagiumAPI.id("block/advanced_casing")
                        },
                    )
                    put(TextureKey.TOP, tier.getStorageBlock().id.withPrefixedPath("block/"))
                    put(TextureKey.SIDE, block)
                }
            }
        }
        // supplier
        listOf(RagiumBlocks.WHITE_LINE, RagiumBlocks.T_WHITE_LINE).forEach { block: Block ->
            generator.excludeFromSimpleItemModelGeneration(block)
            registerSupplier(
                block,
                VariantsBlockStateSupplier
                    .create(
                        block,
                        stateVariantOf(
                            RagiumModels.SURFACE.upload(
                                block,
                                HTTextureMapBuilder.of(TextureKey.TOP, block),
                                generator.modelCollector,
                            ),
                        ),
                    ).coordinate(BlockStateModelGenerator.createNorthDefaultHorizontalRotationStates()),
            )
        }

        registerSupplier(
            RagiumBlocks.MANUAL_GRINDER,
            buildMultipartState(RagiumBlocks.MANUAL_GRINDER) {
                RagiumBlockProperties.LEVEL_7.values.forEach { level: Int ->
                    with(
                        buildWhen(RagiumBlockProperties.LEVEL_7, level),
                        stateVariantOf {
                            model(
                                RagiumAPI.id(
                                    when (level % 2 == 0) {
                                        true -> "block/manual_grinder"
                                        false -> "block/manual_grinder_diagonal"
                                    },
                                ),
                            )
                            rotY(
                                when (level / 2) {
                                    0 -> VariantSettings.Rotation.R0
                                    1 -> VariantSettings.Rotation.R90
                                    2 -> VariantSettings.Rotation.R180
                                    3 -> VariantSettings.Rotation.R270
                                    else -> throw IllegalStateException()
                                },
                            )
                        },
                    )
                }
            },
        )
        registerHorizontal(
            RagiumBlocks.LARGE_PROCESSOR,
            TexturedModel.CUBE_ALL.upload(RagiumBlocks.LARGE_PROCESSOR, generator.modelCollector),
        )
        RagiumContents.Exporters.entries.forEach { exporter: RagiumContents.Exporters ->
            val block: Block = exporter.value
            val coil: Block = exporter.tier.getCoil().value
            registerDirectional(
                block,
                stateVariantOf(
                    RagiumModels.EXPORTER.upload(
                        block,
                        HTTextureMapBuilder.create {
                            put(TextureKey.TOP, coil, "_top")
                            put(TextureKey.SIDE, coil, "_side")
                        },
                        generator.modelCollector,
                    ),
                ),
            )
        }
        registerDirectional(
            RagiumBlocks.CREATIVE_EXPORTER,
            stateVariantOf(
                RagiumModels.EXPORTER.upload(
                    RagiumBlocks.CREATIVE_EXPORTER,
                    HTTextureMapBuilder.create {
                        put(TextureKey.TOP, RagiumAPI.id("block/creative_coil_top"))
                        put(TextureKey.SIDE, RagiumAPI.id("block/creative_coil_side"))
                    },
                    generator.modelCollector,
                ),
            ),
        )

        // pipes
        RagiumContents.Pipes.entries.forEach { pipe: RagiumContents.Pipes ->
            val block: Block = pipe.value
            // blockstate
            registerSupplier(
                block,
                buildMultipartState(block) {
                    with(stateVariantOf(block))
                    apply {
                        Direction.entries.forEach { direction: Direction ->
                            // pipe connection
                            this.with(
                                When.create().set(ConnectingBlock.FACING_PROPERTIES[direction], true),
                                stateVariantOf(TextureMap.getSubId(block, "_side")).rot(direction),
                            )
                            // pipe facing
                            this.with(
                                When.create().set(Properties.FACING, direction),
                                stateVariantOf(RagiumAPI.id("block/pipe_overlay")).rot(direction),
                            )
                        }
                    }
                },
            )
            // model
            RagiumModels.PIPE.upload(
                block,
                TextureMap.all(block),
                generator.modelCollector,
            )
            RagiumModels.PIPE_SIDE.upload(
                TextureMap.getSubId(block, "_side"),
                TextureMap.all(block),
                generator.modelCollector,
            )
        }
        RagiumContents.CrossPipes.entries.forEach { crossPipe: RagiumContents.CrossPipes ->
            val block: Block = crossPipe.value
            // blockstate
            registerSupplier(
                block,
                buildMultipartState(block) {
                    with(stateVariantOf(block))
                    HTCrossDirection.entries.forEach { direction: HTCrossDirection ->
                        // pipe facing
                        this.with(
                            When.create().set(RagiumBlockProperties.CROSS_DIRECTION, direction),
                            stateVariantOf(RagiumAPI.id("block/cross_pipe_overlay"))
                                .rot(direction.second)
                                .apply {
                                    if (direction.first == Direction.DOWN) {
                                        rotX(VariantSettings.Rotation.R180)
                                    }
                                },
                        )
                    }
                },
            )
            // model
            RagiumModels.CROSS_PIPE.upload(
                block,
                TextureMap.all(block),
                generator.modelCollector,
            )
        }
        buildList {
            addAll(RagiumContents.PipeStations.entries)
            addAll(RagiumContents.FilteringPipe.entries)
        }.forEach { pipe: HTContent<Block> ->
            val block: Block = pipe.value
            // blockstate
            registerSupplier(
                block,
                buildMultipartState(block) {
                    with(stateVariantOf(block))
                    Direction.entries.forEach { direction: Direction ->
                        // pipe facing
                        this.with(
                            When.create().set(Properties.FACING, direction),
                            stateVariantOf(RagiumAPI.id("block/pipe_overlay")).rot(direction),
                        )
                    }
                },
            )
            // model
            RagiumModels.CROSS_PIPE.upload(
                block,
                TextureMap.all(block),
                generator.modelCollector,
            )
        }
        // custom
        register(RagiumBlocks.SHAFT) { generator.registerAxisRotated(it, TextureMap.getId(it)) }
        RagiumContents.Coils.entries.forEach { coil: RagiumContents.Coils ->
            register(coil.value) {
                generator.registerAxisRotated(
                    it,
                    TexturedModel.CUBE_COLUMN.upload(
                        it,
                        generator.modelCollector,
                    ),
                )
            }
        }
    }

    //    Model    //

    override fun generateItemModels(generator: ItemModelGenerator) {
        fun register(item: Item, model: Model = Models.GENERATED) {
            generator.register(item, model)
        }

        fun register(item: Item, model: Model, textureMap: TextureMap = TextureMap()) {
            model.upload(
                TextureMap.getId(item),
                textureMap,
                generator.writer,
            )
        }

        fun registerLayered(item: Item, layer0: Identifier, layer1: Identifier) {
            register(
                item,
                Models.GENERATED_TWO_LAYERS,
                HTTextureMapBuilder.create {
                    put(TextureKey.LAYER0, layer0)
                    put(TextureKey.LAYER1, layer1)
                },
            )
        }
        // contents
        buildList {
            addAll(RagiumContents.Dusts.entries)
            addAll(RagiumContents.Gems.entries)
            addAll(RagiumContents.Ingots.entries)
            addAll(RagiumContents.Plates.entries)
            addAll(RagiumContents.RawMaterials.entries)
            addAll(RagiumItems.TOOLS)
            addAll(RagiumItems.ARMORS)
            addAll(RagiumContents.CircuitBoards.entries)
            addAll(RagiumContents.Circuits.entries)
            addAll(RagiumItems.FOODS)
            addAll(RagiumItems.INGREDIENTS)
            addAll(RagiumItems.MISC)

            add(RagiumBlocks.ROPE)

            remove(RagiumItems.GIGANT_HAMMER)
            remove(RagiumItems.CHOCOLATE_APPLE)
            remove(RagiumItems.EMPTY_FLUID_CUBE)
            remove(RagiumItems.FILLED_FLUID_CUBE)
            remove(RagiumItems.RAGI_ALLOY_COMPOUND)
        }.map(ItemConvertible::asItem).forEach(::register)

        registerLayered(
            RagiumItems.RAGI_ALLOY_COMPOUND.asItem(),
            TextureMap.getId(Items.COPPER_INGOT),
            TextureMap.getId(RagiumItems.RAGI_ALLOY_COMPOUND.asItem()),
        )
        registerLayered(
            RagiumItems.CHOCOLATE_APPLE.asItem(),
            TextureMap.getId(Items.APPLE),
            TextureMap.getId(RagiumItems.CHOCOLATE_APPLE.asItem()),
        )
        register(RagiumItems.FILLED_FLUID_CUBE.asItem(), RagiumModels.FILLED_FLUID_CUBE)

        listOf(
            RagiumBlocks.WHITE_LINE,
            RagiumBlocks.T_WHITE_LINE,
            RagiumBlocks.CROSS_WHITE_LINE,
        ).forEach { block: Block ->
            Models.GENERATED.upload(
                TextureMap.getId(block.asItem()),
                TextureMap.layer0(block),
                generator.writer,
            )
        }
    }
}
