package hiiragi283.ragium.data

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.HTMachineBlock
import hiiragi283.ragium.api.content.HTBlockContent
import hiiragi283.ragium.api.content.HTItemContent
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachinePropertyKeys
import hiiragi283.ragium.api.machine.HTMachineRegistry
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.common.init.RagiumBlockProperties
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumItems
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.minecraft.block.Block
import net.minecraft.block.ConnectingBlock
import net.minecraft.data.client.*
import net.minecraft.item.Items
import net.minecraft.state.property.Properties
import net.minecraft.util.Identifier
import net.minecraft.util.math.Direction

class RagiumModelProvider(output: FabricDataOutput) : FabricModelProvider(output) {
    //   BlockState    //

    private lateinit var blockGenerator: BlockStateModelGenerator
    private val modelCollector: ModelCollector
        get() = blockGenerator.modelCollector

    fun register(content: HTBlockContent, action: (Block) -> Unit) {
        action(content.get())
    }

    // supplier-based
    fun registerSupplier(content: HTBlockContent, supplier: BlockStateSupplier) {
        blockGenerator.blockStateCollector.accept(supplier)
    }

    fun registerDirectional(content: HTBlockContent, model: BlockStateVariant = stateVariantOf(content)) {
        registerSupplier(
            content,
            VariantsBlockStateSupplier
                .create(content.get(), model)
                .coordinate(BlockStateModelGenerator.createNorthDefaultRotationStates()),
        )
    }

    fun registerHorizontal(content: HTBlockContent, modelId: Identifier = content.id.withPrefixedPath("block/")) {
        registerSupplier(
            content,
            VariantsBlockStateSupplier
                .create(content.get(), stateVariantOf(modelId))
                .coordinate(BlockStateModelGenerator.createNorthDefaultHorizontalRotationStates()),
        )
    }

    // model-based
    fun registerFactory(content: HTBlockContent, factory: TexturedModel.Factory) {
        blockGenerator.registerSingleton(content.get(), factory)
    }

    fun registerFactory(content: HTBlockContent, model: Model, getter: (Block) -> TextureMap) {
        blockGenerator.registerSingleton(content.get(), TexturedModel.makeFactory(getter, model))
    }

    fun registerSimple(content: HTBlockContent, id: Identifier = content.id.withPrefixedPath("block/")) {
        blockGenerator.registerSingleton(content.get()) { TexturedModel.getCubeAll(id) }
    }

    fun registerLayered(content: HTBlockContent, layer0: Identifier, layer1: Identifier) {
        registerFactory(content, RagiumModels.LAYERED) {
            HTTextureMapBuilder.create {
                put(TextureKey.LAYER0, layer0)
                put(TextureKey.LAYER1, layer1)
            }
        }
    }

    fun registerSlab(slabBlock: HTBlockContent, fullBlock: HTBlockContent) {
        val textureMap: TextureMap = HTTextureMapBuilder.create {
            put(TextureKey.SIDE, fullBlock)
            put(TextureKey.TOP, fullBlock)
            put(TextureKey.BOTTOM, fullBlock)
        }
        val slabBottom: Identifier = Models.SLAB.upload(slabBlock, textureMap, modelCollector)
        val slabTop: Identifier = Models.SLAB_TOP.upload(slabBlock, textureMap, modelCollector)
        registerSupplier(
            slabBlock,
            BlockStateModelGenerator.createSlabBlockState(
                slabBlock.get(),
                slabBottom,
                slabTop,
                fullBlock.id.withPrefixedPath("block/"),
            ),
        )
        blockGenerator.registerParentedItemModel(slabBlock.get(), slabBottom)
    }

    fun registerSlab(slabs: RagiumBlocks.Slabs) {
        registerSlab(slabs, slabs.baseStone)
    }

    fun registerStair(stairBlock: HTBlockContent, fullBlock: HTBlockContent) {
        val textureMap: TextureMap = HTTextureMapBuilder.create {
            put(TextureKey.SIDE, fullBlock)
            put(TextureKey.TOP, fullBlock)
            put(TextureKey.BOTTOM, fullBlock)
        }
        val innerId: Identifier =
            Models.INNER_STAIRS.upload(stairBlock, textureMap, modelCollector)
        val stairId: Identifier = Models.STAIRS.upload(stairBlock, textureMap, modelCollector)
        val outerId: Identifier =
            Models.OUTER_STAIRS.upload(stairBlock, textureMap, modelCollector)
        registerSupplier(
            stairBlock,
            BlockStateModelGenerator.createStairsBlockState(
                stairBlock.get(),
                innerId,
                stairId,
                outerId,
            ),
        )
        blockGenerator.registerParentedItemModel(stairBlock.get(), stairId)
    }

    fun registerStair(stair: RagiumBlocks.Stairs) {
        registerStair(stair, stair.baseStone)
    }

    fun registerStaticModel(content: HTBlockContent, modelId: Identifier = content.id.withPrefixedPath("block/")) {
        registerSupplier(content, VariantsBlockStateSupplier.create(content.get(), stateVariantOf(modelId)))
    }

    override fun generateBlockStateModels(generator: BlockStateModelGenerator) {
        blockGenerator = generator
        // creative
        registerLayered(
            RagiumBlocks.Creatives.CRATE,
            RagiumAPI.id("block/creative_coil_side"),
            RagiumAPI.id("block/crate_overlay"),
        )
        registerFactory(RagiumBlocks.Creatives.DRUM, TexturedModel.CUBE_COLUMN)
        registerDirectional(
            RagiumBlocks.Creatives.EXPORTER,
            stateVariantOf(
                RagiumModels.EXPORTER.upload(
                    RagiumBlocks.Creatives.EXPORTER,
                    HTTextureMapBuilder.create {
                        put(TextureKey.TOP, RagiumAPI.id("block/creative_coil_top"))
                        put(TextureKey.SIDE, RagiumAPI.id("block/creative_coil_side"))
                    },
                    modelCollector,
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
        // glass
        RagiumBlocks.Glasses.entries.forEach(::registerSimple)
        // white line
        listOf(RagiumBlocks.WhiteLines.SIMPLE, RagiumBlocks.WhiteLines.T_SHAPED).forEach { content: HTBlockContent ->
            val block: Block = content.get()
            blockGenerator.excludeFromSimpleItemModelGeneration(block)
            registerSupplier(
                content,
                VariantsBlockStateSupplier
                    .create(
                        block,
                        stateVariantOf(
                            RagiumModels.SURFACE.upload(
                                content,
                                HTTextureMapBuilder.of(TextureKey.TOP, block),
                                modelCollector,
                            ),
                        ),
                    ).coordinate(BlockStateModelGenerator.createNorthDefaultHorizontalRotationStates()),
            )
        }
        blockGenerator.excludeFromSimpleItemModelGeneration(RagiumBlocks.WhiteLines.CROSS.get())
        registerFactory(RagiumBlocks.WhiteLines.CROSS, RagiumModels.SURFACE) {
            HTTextureMapBuilder.of(TextureKey.TOP, RagiumBlocks.WhiteLines.CROSS)
        }
        // ore
        RagiumBlocks.Ores.entries.forEach { ore: RagiumBlocks.Ores ->
            registerLayered(
                ore,
                TextureMap.getId(ore.baseStone),
                RagiumAPI.id("block/${ore.material.name}"),
            )
        }
        // hull
        RagiumBlocks.Hulls.entries.forEach { hull: RagiumBlocks.Hulls ->
            val tier: HTMachineTier = hull.tier
            registerFactory(hull, RagiumModels.HULL) { block: Block ->
                HTTextureMapBuilder.create {
                    put(TextureKey.INSIDE, tier.getCasing())
                    put(TextureKey.TOP, tier.getStorageBlock().id.withPrefixedPath("block/"))
                    put(TextureKey.SIDE, block)
                }
            }
        }
        // coil
        RagiumBlocks.Coils.entries.forEach { coil: RagiumBlocks.Coils ->
            register(coil) {
                blockGenerator.registerAxisRotated(
                    it,
                    TexturedModel.CUBE_COLUMN.upload(
                        it,
                        modelCollector,
                    ),
                )
            }
        }
        // crate
        RagiumBlocks.Crates.entries.forEach { crate: RagiumBlocks.Crates ->
            registerLayered(
                crate,
                crate.tier
                    .getCoil()
                    .id
                    .withPath { "block/${it}_side" },
                RagiumAPI.id("block/crate_overlay"),
            )
        }
        // drum
        RagiumBlocks.Drums.entries.forEach { registerFactory(it, TexturedModel.CUBE_COLUMN) }
        // exporter
        RagiumBlocks.Exporters.entries.forEach { exporter: RagiumBlocks.Exporters ->
            val block: Block = exporter.get()
            val coil: Block = exporter.tier.getCoil().get()
            registerDirectional(
                exporter,
                stateVariantOf(
                    RagiumModels.EXPORTER.upload(
                        block,
                        HTTextureMapBuilder.create {
                            put(TextureKey.TOP, coil, "_top")
                            put(TextureKey.SIDE, coil, "_side")
                        },
                        modelCollector,
                    ),
                ),
            )
        }
        // pipe
        RagiumBlocks.Pipes.entries.forEach { pipe: RagiumBlocks.Pipes ->
            val block: Block = pipe.get()
            // blockstate
            registerSupplier(
                pipe,
                buildMultipartState(pipe) {
                    with(stateVariantOf(pipe))
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
                pipe,
                TextureMap.all(block),
                modelCollector,
            )
            RagiumModels.PIPE_SIDE.upload(
                TextureMap.getSubId(block, "_side"),
                TextureMap.all(block),
                modelCollector,
            )
        }
        // cross pipe
        RagiumBlocks.CrossPipes.entries.forEach { cross: RagiumBlocks.CrossPipes ->
            registerFactory(cross, RagiumModels.CROSS_PIPE, TextureMap::all)
        }
        // pipe station
        RagiumBlocks.PipeStations.entries.forEach { pipe: RagiumBlocks.PipeStations ->
            // blockstate
            registerSupplier(
                pipe,
                buildMultipartState(pipe) {
                    with(stateVariantOf(pipe))
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
                pipe,
                TextureMap.all(pipe.get()),
                modelCollector,
            )
        }
        // filtering pipe
        RagiumBlocks.FilteringPipes.entries.forEach { filtering: RagiumBlocks.FilteringPipes ->
            // blockstate
            val block: Block = filtering.get()
            registerSupplier(
                filtering,
                VariantsBlockStateSupplier
                    .create(block, stateVariantOf(filtering))
                    .coordinate(BlockStateModelGenerator.createNorthDefaultRotationStates()),
            )
            // model
            RagiumModels.FILTERING_PIPE.upload(
                block,
                HTTextureMapBuilder.of(TextureKey.BACK, filtering),
                modelCollector,
            )
        }
        // other
        buildList {
            addAll(RagiumBlocks.StorageBlocks.entries)
            addAll(RagiumBlocks.Grates.entries)
            addAll(RagiumBlocks.Casings.entries)

            add(RagiumBlocks.AUTO_ILLUMINATOR)
            add(RagiumBlocks.ITEM_DISPLAY)
            add(RagiumBlocks.MUTATED_SOIL)
            add(RagiumBlocks.NETWORK_INTERFACE)
            add(RagiumBlocks.OPEN_CRATE)
            add(RagiumBlocks.POROUS_NETHERRACK)
            add(RagiumBlocks.SPONGE_CAKE)
            add(RagiumBlocks.TELEPORT_ANCHOR)
            add(RagiumBlocks.TRASH_BOX)
        }.forEach(::registerSimple)

        // blockGenerator.excludeFromSimpleItemModelGeneration(RagiumBlocks.ROPE.get())
        buildList {
            add(RagiumBlocks.MANUAL_FORGE)
            add(RagiumBlocks.MANUAL_MIXER)
            // add(RagiumBlocks.ROPE)
            add(RagiumBlocks.SWEET_BERRIES_CAKE)
        }.forEach(::registerStaticModel)

        registerFactory(RagiumBlocks.BACKPACK_INTERFACE, RagiumModels.ALL_TINTED, TextureMap::all)

        registerHorizontal(
            RagiumBlocks.EXTENDED_PROCESSOR,
            TexturedModel.CUBE_ALL.upload(RagiumBlocks.EXTENDED_PROCESSOR.get(), modelCollector),
        )

        registerSupplier(
            RagiumBlocks.MANUAL_GRINDER,
            buildMultipartState(RagiumBlocks.MANUAL_GRINDER.get()) {
                RagiumBlockProperties.LEVEL_7.values.forEach { level: Int ->
                    with(
                        buildWhen(RagiumBlockProperties.LEVEL_7, level),
                        stateVariantOf(
                            RagiumAPI.id(
                                when (level % 2 == 0) {
                                    true -> "block/manual_grinder"
                                    false -> "block/manual_grinder_diagonal"
                                },
                            ),
                        ).rotY(
                            when (level / 2) {
                                0 -> VariantSettings.Rotation.R0
                                1 -> VariantSettings.Rotation.R90
                                2 -> VariantSettings.Rotation.R180
                                3 -> VariantSettings.Rotation.R270
                                else -> throw IllegalStateException()
                            },
                        ),
                    )
                }
            },
        )

        register(RagiumBlocks.SHAFT) { blockGenerator.registerAxisRotated(it, TextureMap.getId(it)) }

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
            blockGenerator.excludeFromSimpleItemModelGeneration(block)
            blockGenerator.blockStateCollector.accept(
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
                    modelCollector,
                )
        }
    }

    //    Model    //

    private lateinit var itemGenerator: ItemModelGenerator

    fun register(content: HTItemContent, model: Model = Models.GENERATED) {
        itemGenerator.register(content.get(), model)
    }

    fun register(content: HTItemContent, model: Model, textureMap: TextureMap = TextureMap()) {
        model.upload(
            content.id.withPrefixedPath("item/"),
            textureMap,
            itemGenerator.writer,
        )
    }

    fun registerLayered(content: HTItemContent, layer0: Identifier, layer1: Identifier) {
        register(
            content,
            Models.GENERATED_TWO_LAYERS,
            HTTextureMapBuilder.create {
                put(TextureKey.LAYER0, layer0)
                put(TextureKey.LAYER1, layer1)
            },
        )
    }

    override fun generateItemModels(generator: ItemModelGenerator) {
        itemGenerator = generator
        // contents
        buildList {
            addAll(RagiumItems.Dusts.entries)
            addAll(RagiumItems.Gears.entries)
            addAll(RagiumItems.Gems.entries)
            addAll(RagiumItems.Ingots.entries)
            addAll(RagiumItems.Plates.entries)
            addAll(RagiumItems.RawMaterials.entries)
            addAll(RagiumItems.CircuitBoards.entries)
            addAll(RagiumItems.Circuits.entries)
            addAll(RagiumItems.PressMolds.entries)

            addAll(RagiumItems.SteelArmors.entries)
            addAll(RagiumItems.DeepSteelArmors.entries)
            addAll(RagiumItems.StellaSuits.entries)

            addAll(RagiumItems.SteelTools.entries)
            addAll(RagiumItems.DeepSteelTools.entries)
            addAll(RagiumItems.Dynamites.entries)

            add(RagiumItems.BACKPACK)
            add(RagiumItems.FLUID_FILTER)
            add(RagiumItems.FORGE_HAMMER)
            add(RagiumItems.GUIDE_BOOK)
            add(RagiumItems.ITEM_FILTER)
            add(RagiumItems.RAGI_WRENCH)
            add(RagiumItems.RAGIUM_SABER)
            add(RagiumItems.STELLA_SABER)
            add(RagiumItems.TRADER_CATALOG)

            addAll(RagiumItems.FOODS)
            addAll(RagiumItems.Ingredients.entries)
            addAll(RagiumItems.MISC)

            remove(RagiumItems.CHOCOLATE_APPLE)
            remove(RagiumItems.Ingredients.RAGI_ALLOY_COMPOUND)
        }.forEach(::register)

        // generator.register(RagiumBlocks.ROPE.get().asItem(), Models.GENERATED)

        registerLayered(
            RagiumItems.Ingredients.RAGI_ALLOY_COMPOUND,
            TextureMap.getId(Items.COPPER_INGOT),
            TextureMap.getId(RagiumItems.Ingredients.RAGI_ALLOY_COMPOUND.asItem()),
        )
        registerLayered(
            RagiumItems.CHOCOLATE_APPLE,
            TextureMap.getId(Items.APPLE),
            RagiumItems.CHOCOLATE_APPLE.id.withPrefixedPath("item/"),
        )
        register(RagiumItems.FILLED_FLUID_CUBE, RagiumModels.FILLED_FLUID_CUBE)

        RagiumBlocks.WhiteLines.entries.forEach { content: HTBlockContent ->
            Models.GENERATED.uploadAsItem(
                content.get(),
                HTTextureMapBuilder.of(TextureKey.LAYER0, content),
                generator.writer,
            )
        }
    }
}
