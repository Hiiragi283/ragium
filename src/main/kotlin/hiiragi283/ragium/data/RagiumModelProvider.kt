package hiiragi283.ragium.data

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.HTMachineBlock
import hiiragi283.ragium.api.content.HTBlockContent
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachinePropertyKeys
import hiiragi283.ragium.api.machine.HTMachineRegistry
import hiiragi283.ragium.api.machine.HTMachineTier
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

        fun register(content: HTBlockContent, action: (Block) -> Unit) {
            action(content.get())
        }

        // supplier-based
        fun registerSupplier(block: Block, supplier: BlockStateSupplier) {
            generator.blockStateCollector.accept(supplier)
        }

        fun registerSupplier(content: HTBlockContent, supplier: BlockStateSupplier) {
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

        fun registerFactory(content: HTBlockContent, model: Model, getter: (Block) -> TextureMap) {
            registerFactory(content.get(), factory = TexturedModel.makeFactory(getter, model))
        }

        fun registerSimple(block: Block, id: Identifier = TextureMap.getId(block)) {
            generator.registerSingleton(block) { TexturedModel.getCubeAll(id) }
        }

        fun registerSimple(content: HTBlockContent, id: Identifier = content.id.withPrefixedPath("block/")) {
            generator.registerSingleton(content.get()) { TexturedModel.getCubeAll(id) }
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

        fun registerSlab(slabs: RagiumBlocks.Slabs) {
            registerSlab(slabs.get(), slabs.baseStone.get())
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

        fun registerStair(stair: RagiumBlocks.Stairs) {
            registerStair(stair.get(), stair.baseStone.get())
        }

        fun registerStaticModel(content: HTBlockContent, modelId: Identifier = content.id.withPrefixedPath("block/")) {
            registerSupplier(content, VariantsBlockStateSupplier.create(content.get(), stateVariantOf(modelId)))
        }

        // simple
        RagiumBlocks.Stones.entries.forEach(::registerSimple)
        RagiumBlocks.Slabs.entries.forEach(::registerSlab)
        RagiumBlocks.Stairs.entries.forEach(::registerStair)
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
        }.map(HTBlockContent::get).forEach(::registerSimple)

        registerSimple(RagiumContents.Casings.PRIMITIVE.get(), Identifier.of("block/blast_furnace_top"))
        registerSimple(RagiumContents.Casings.BASIC.get())
        registerSimple(RagiumContents.Casings.ADVANCED.get())
        // layered
        RagiumContents.Ores.entries.forEach { ore: RagiumContents.Ores ->
            registerLayered(
                ore.get(),
                TextureMap.getId(ore.baseStone),
                RagiumAPI.id("block/${ore.material.name}"),
            )
        }
        RagiumContents.Crates.entries.forEach { crate: RagiumContents.Crates ->
            registerLayered(
                crate.get(),
                crate.tier
                    .getCoil()
                    .id
                    .withPath { "block/${it}_side" },
                RagiumAPI.id("block/crate_overlay"),
            )
        }
        registerLayered(
            RagiumBlocks.CREATIVE_CRATE.get(),
            RagiumAPI.id("block/creative_coil_side"),
            RagiumAPI.id("block/crate_overlay"),
        )
        // static
        registerStaticModel(RagiumBlocks.MANUAL_FORGE)
        registerStaticModel(RagiumBlocks.MANUAL_MIXER)
        generator.excludeFromSimpleItemModelGeneration(RagiumBlocks.ROPE.get())
        registerStaticModel(RagiumBlocks.ROPE)
        registerStaticModel(RagiumBlocks.SWEET_BERRIES_CAKE)
        // factory
        generator.excludeFromSimpleItemModelGeneration(RagiumBlocks.CROSS_WHITE_LINE.get())
        registerFactory(RagiumBlocks.CROSS_WHITE_LINE, RagiumModels.SURFACE) {
            HTTextureMapBuilder.of(TextureKey.TOP, RagiumBlocks.CROSS_WHITE_LINE)
        }
        registerFactory(RagiumBlocks.BACKPACK_INTERFACE, RagiumModels.ALL_TINTED, TextureMap::all)
        registerFactory(RagiumBlocks.ENCHANTMENT_BOOKSHELF, Models.CUBE_COLUMN) {
            HTTextureMapBuilder.create {
                put(TextureKey.END, Identifier.of("block/chiseled_bookshelf_top"))
                put(TextureKey.SIDE, RagiumBlocks.ENCHANTMENT_BOOKSHELF)
            }
        }
        registerFactory(RagiumBlocks.CREATIVE_DRUM.get(), TexturedModel.CUBE_COLUMN)

        RagiumContents.Drums.entries.forEach { registerFactory(it.get(), TexturedModel.CUBE_COLUMN) }
        RagiumContents.Hulls.entries.forEach { hull: RagiumContents.Hulls ->
            val tier: HTMachineTier = hull.tier
            registerFactory(hull.get(), RagiumModels.HULL) { block: Block ->
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
        RagiumContents.CrossPipes.entries.forEach { cross: RagiumContents.CrossPipes ->
            registerFactory(cross.get(), RagiumModels.CROSS_PIPE, TextureMap::all)
        }
        // supplier
        listOf(RagiumBlocks.WHITE_LINE, RagiumBlocks.T_WHITE_LINE).forEach { content: HTBlockContent ->
            val block: Block = content.get()
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
            buildMultipartState(RagiumBlocks.MANUAL_GRINDER.get()) {
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
            RagiumBlocks.EXTENDED_PROCESSOR.get(),
            TexturedModel.CUBE_ALL.upload(RagiumBlocks.EXTENDED_PROCESSOR.get(), generator.modelCollector),
        )
        RagiumContents.Exporters.entries.forEach { exporter: RagiumContents.Exporters ->
            val block: Block = exporter.get()
            val coil: Block = exporter.tier.getCoil().get()
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
            RagiumBlocks.CREATIVE_EXPORTER.get(),
            stateVariantOf(
                RagiumModels.EXPORTER.upload(
                    RagiumBlocks.CREATIVE_EXPORTER.get(),
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
            val block: Block = pipe.get()
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
        RagiumContents.PipeStations.entries.forEach { pipe: RagiumContents.PipeStations ->
            val block: Block = pipe.get()
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
        RagiumContents.FilteringPipe.entries.forEach { filtering: RagiumContents.FilteringPipe ->
            // blockstate
            val block: Block = filtering.get()
            registerSupplier(
                block,
                VariantsBlockStateSupplier
                    .create(block, stateVariantOf(block))
                    .coordinate(BlockStateModelGenerator.createNorthDefaultRotationStates()),
            )
            // model
            RagiumModels.FILTERING_PIPE.upload(
                block,
                HTTextureMapBuilder.of(TextureKey.BACK, block),
                generator.modelCollector,
            )
        }
        // machine
        RagiumAPI.getInstance().machineRegistry.entryMap.forEach { (_: HTMachineKey, entry: HTMachineRegistry.Entry) ->
            val block: HTMachineBlock = entry.block
            val coordinateMap: BlockStateVariantMap = BlockStateVariantMap
                .create(
                    Properties.HORIZONTAL_FACING,
                    RagiumBlockProperties.ACTIVE,
                    HTMachineTier.PROPERTY,
                ).register { front: Direction, isActive: Boolean, _: HTMachineTier ->
                    val modelId: Identifier = when (isActive) {
                        true -> HTMachinePropertyKeys.ACTIVE_MODEL_ID
                        false -> HTMachinePropertyKeys.MODEL_ID
                    }.let(entry::getOrDefault)
                    stateVariantOf(modelId).rot(front)
                }
            // block state
            generator.excludeFromSimpleItemModelGeneration(block)
            registerSupplier(
                block,
                VariantsBlockStateSupplier
                    .create(block)
                    .coordinate(coordinateMap),
            )
            // model
            RagiumModels
                .model(entry.getOrDefault(HTMachinePropertyKeys.MODEL_ID))
                .uploadAsItem(
                    block,
                    TextureMap(),
                    generator.modelCollector,
                )
        }
        // custom
        register(RagiumBlocks.SHAFT) { generator.registerAxisRotated(it, TextureMap.getId(it)) }
        RagiumContents.Coils.entries.forEach { coil: RagiumContents.Coils ->
            register(coil.get()) {
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
            addAll(RagiumContents.Gears.entries)
            addAll(RagiumContents.Gems.entries)
            addAll(RagiumContents.Ingots.entries)
            addAll(RagiumContents.Plates.entries)
            addAll(RagiumContents.RawMaterials.entries)
            addAll(RagiumContents.CircuitBoards.entries)
            addAll(RagiumContents.Circuits.entries)
            addAll(RagiumContents.PressMolds.entries)

            addAll(RagiumItems.TOOLS)
            addAll(RagiumItems.ARMORS)
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
        ).forEach { content: HTBlockContent ->
            val block: Block = content.get()
            Models.GENERATED.upload(
                TextureMap.getId(block.asItem()),
                TextureMap.layer0(block),
                generator.writer,
            )
        }
    }
}
