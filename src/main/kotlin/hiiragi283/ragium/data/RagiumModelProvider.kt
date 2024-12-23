package hiiragi283.ragium.data

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.HTMachineBlock
import hiiragi283.ragium.api.content.HTBlockContent
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachinePropertyKeys
import hiiragi283.ragium.api.machine.HTMachineRegistry
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.common.init.RagiumBlockProperties
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.init.RagiumItemsNew
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

        registerSimple(RagiumBlocks.AUTO_ILLUMINATOR)
        registerSimple(RagiumBlocks.ITEM_DISPLAY)
        registerSimple(RagiumBlocks.MUTATED_SOIL)
        registerSimple(RagiumBlocks.NETWORK_INTERFACE)
        registerSimple(RagiumBlocks.OPEN_CRATE)
        registerSimple(RagiumBlocks.POROUS_NETHERRACK)
        registerSimple(RagiumBlocks.SPONGE_CAKE)
        registerSimple(RagiumBlocks.TELEPORT_ANCHOR)
        registerSimple(RagiumBlocks.TRASH_BOX)

        buildList {
            addAll(RagiumBlocks.StorageBlocks.entries)
            addAll(RagiumBlocks.Grates.entries)
            addAll(RagiumBlocks.Casings.entries)
        }.map(HTBlockContent::get).forEach(::registerSimple)
        // layered
        RagiumBlocks.Ores.entries.forEach { ore: RagiumBlocks.Ores ->
            registerLayered(
                ore.get(),
                TextureMap.getId(ore.baseStone),
                RagiumAPI.id("block/${ore.material.name}"),
            )
        }
        RagiumBlocks.Crates.entries.forEach { crate: RagiumBlocks.Crates ->
            registerLayered(
                crate.get(),
                crate.tier
                    .getCoil()
                    .id
                    .withPath { "block/${it}_side" },
                RagiumAPI.id("block/crate_overlay"),
            )
        }
        // static
        registerStaticModel(RagiumBlocks.MANUAL_FORGE)
        registerStaticModel(RagiumBlocks.MANUAL_MIXER)
        generator.excludeFromSimpleItemModelGeneration(RagiumBlocks.ROPE.get())
        registerStaticModel(RagiumBlocks.ROPE)
        registerStaticModel(RagiumBlocks.SWEET_BERRIES_CAKE)
        // factory

        registerFactory(RagiumBlocks.BACKPACK_INTERFACE, RagiumModels.ALL_TINTED, TextureMap::all)
        /*registerFactory(RagiumBlocks.ENCHANTMENT_BOOKSHELF, Models.CUBE_COLUMN) {
            HTTextureMapBuilder.create {
                put(TextureKey.END, Identifier.of("block/chiseled_bookshelf_top"))
                put(TextureKey.SIDE, RagiumBlocks.ENCHANTMENT_BOOKSHELF)
            }
        }*/

        RagiumBlocks.Drums.entries.forEach { registerFactory(it.get(), TexturedModel.CUBE_COLUMN) }
        RagiumBlocks.Hulls.entries.forEach { hull: RagiumBlocks.Hulls ->
            val tier: HTMachineTier = hull.tier
            registerFactory(hull.get(), RagiumModels.HULL) { block: Block ->
                HTTextureMapBuilder.create {
                    put(TextureKey.INSIDE, tier.getCasing())
                    put(TextureKey.TOP, tier.getStorageBlock().id.withPrefixedPath("block/"))
                    put(TextureKey.SIDE, block)
                }
            }
        }
        RagiumBlocks.CrossPipes.entries.forEach { cross: RagiumBlocks.CrossPipes ->
            registerFactory(cross.get(), RagiumModels.CROSS_PIPE, TextureMap::all)
        }
        // supplier

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
        RagiumBlocks.Exporters.entries.forEach { exporter: RagiumBlocks.Exporters ->
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
        // pipes
        RagiumBlocks.Pipes.entries.forEach { pipe: RagiumBlocks.Pipes ->
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
        RagiumBlocks.PipeStations.entries.forEach { pipe: RagiumBlocks.PipeStations ->
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
        RagiumBlocks.FilteringPipes.entries.forEach { filtering: RagiumBlocks.FilteringPipes ->
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
        RagiumBlocks.Coils.entries.forEach { coil: RagiumBlocks.Coils ->
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

        // creative
        registerLayered(
            RagiumBlocks.Creatives.CRATE.get(),
            RagiumAPI.id("block/creative_coil_side"),
            RagiumAPI.id("block/crate_overlay"),
        )
        registerFactory(RagiumBlocks.Creatives.DRUM.get(), TexturedModel.CUBE_COLUMN)
        registerDirectional(
            RagiumBlocks.Creatives.EXPORTER.get(),
            stateVariantOf(
                RagiumModels.EXPORTER.upload(
                    RagiumBlocks.Creatives.EXPORTER.get(),
                    HTTextureMapBuilder.create {
                        put(TextureKey.TOP, RagiumAPI.id("block/creative_coil_top"))
                        put(TextureKey.SIDE, RagiumAPI.id("block/creative_coil_side"))
                    },
                    generator.modelCollector,
                ),
            ),
        )
        registerSimple(RagiumBlocks.Creatives.SOURCE)

        // stone
        RagiumBlocks.Stones.entries.forEach(::registerSimple)
        // slab
        RagiumBlocks.Slabs.entries.forEach(::registerSlab)
        // stair
        RagiumBlocks.Stairs.entries.forEach(::registerStair)
        // white line
        listOf(RagiumBlocks.WhiteLines.SIMPLE, RagiumBlocks.WhiteLines.T_SHAPED).forEach { content: HTBlockContent ->
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
        generator.excludeFromSimpleItemModelGeneration(RagiumBlocks.WhiteLines.CROSS.get())
        registerFactory(RagiumBlocks.WhiteLines.CROSS, RagiumModels.SURFACE) {
            HTTextureMapBuilder.of(TextureKey.TOP, RagiumBlocks.WhiteLines.CROSS)
        }
        // glass
        RagiumBlocks.Glasses.entries.forEach(::registerSimple)
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
            addAll(RagiumItemsNew.Dusts.entries)
            addAll(RagiumItemsNew.Gears.entries)
            addAll(RagiumItemsNew.Gems.entries)
            addAll(RagiumItemsNew.Ingots.entries)
            addAll(RagiumItemsNew.Plates.entries)
            addAll(RagiumItemsNew.RawMaterials.entries)
            addAll(RagiumItemsNew.CircuitBoards.entries)
            addAll(RagiumItemsNew.Circuits.entries)
            addAll(RagiumItemsNew.PressMolds.entries)

            addAll(RagiumItemsNew.SteelArmors.entries)
            addAll(RagiumItemsNew.DeepSteelArmors.entries)
            addAll(RagiumItemsNew.StellaSuits.entries)

            addAll(RagiumItemsNew.SteelTools.entries)
            addAll(RagiumItemsNew.DeepSteelTools.entries)
            addAll(RagiumItemsNew.Dynamites.entries)

            add(RagiumItemsNew.BACKPACK)
            add(RagiumItemsNew.FLUID_FILTER)
            add(RagiumItemsNew.FORGE_HAMMER)
            add(RagiumItemsNew.GUIDE_BOOK)
            add(RagiumItemsNew.ITEM_FILTER)
            add(RagiumItemsNew.RAGI_WRENCH)
            add(RagiumItemsNew.RAGIUM_SABER)
            add(RagiumItemsNew.STELLA_SABER)
            add(RagiumItemsNew.TRADER_CATALOG)

            addAll(RagiumItemsNew.FOODS)
            addAll(RagiumItems.INGREDIENTS)
            addAll(RagiumItemsNew.MISC)

            add(RagiumBlocks.ROPE)

            remove(RagiumItemsNew.CHOCOLATE_APPLE)
            remove(RagiumItems.RAGI_ALLOY_COMPOUND)
        }.map(ItemConvertible::asItem).forEach(::register)

        registerLayered(
            RagiumItems.RAGI_ALLOY_COMPOUND.asItem(),
            TextureMap.getId(Items.COPPER_INGOT),
            TextureMap.getId(RagiumItems.RAGI_ALLOY_COMPOUND.asItem()),
        )
        registerLayered(
            RagiumItemsNew.CHOCOLATE_APPLE.get(),
            TextureMap.getId(Items.APPLE),
            TextureMap.getId(RagiumItemsNew.CHOCOLATE_APPLE.get()),
        )
        register(RagiumItemsNew.FILLED_FLUID_CUBE.get(), RagiumModels.FILLED_FLUID_CUBE)

        RagiumBlocks.WhiteLines.entries.forEach { content: HTBlockContent ->
            val block: Block = content.get()
            Models.GENERATED.upload(
                TextureMap.getId(block.asItem()),
                TextureMap.layer0(block),
                generator.writer,
            )
        }
    }
}
