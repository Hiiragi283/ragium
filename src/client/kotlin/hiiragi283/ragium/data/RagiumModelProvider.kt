package hiiragi283.ragium.data

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.client.model.HTMachineModel
import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.init.RagiumBlockProperties
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.item.HTCrafterHammerItem
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.minecraft.block.Block
import net.minecraft.block.Blocks
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

        fun register(block: Block, factory: TexturedModel.Factory) {
            register(block) { generator.registerSingleton(it, factory) }
        }

        fun registerSimple(block: Block, id: Identifier = TextureMap.getId(block)) {
            register(block) { generator.registerSingleton(it) { TexturedModel.getCubeAll(id) } }
        }

        fun registerLayered(block: Block, layer0: Identifier, layer1: Identifier) {
            register(block) { generator.registerSingleton(it, RagiumModels.createLayered(layer0, layer1)) }
        }

        fun accept(supplier: BlockStateSupplier) {
            generator.blockStateCollector.accept(supplier)
        }

        registerSimple(RagiumBlocks.CREATIVE_SOURCE)
        register(RagiumBlocks.MANUAL_GRINDER) {
            accept(
                buildMultipartState(it) {
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
        }
        register(RagiumBlocks.SHAFT) { generator.registerAxisRotated(it, TextureMap.getId(it)) }
        register(RagiumBlocks.ITEM_DISPLAY) {
            generator.registerSingleton(
                it,
                RagiumModels.createDisplay(
                    Identifier.of("block/oak_log_top"),
                    Identifier.of("block/oak_log"),
                ),
            )
        }
        registerSimple(RagiumBlocks.NETWORK_INTERFACE)
        registerSimple(RagiumBlocks.BASIC_CASING, Identifier.of("block/blast_furnace_top"))
        registerSimple(RagiumBlocks.ADVANCED_CASING)
        registerLayered(
            RagiumBlocks.POROUS_NETHERRACK,
            Identifier.of("block/netherrack"),
            Identifier.of("block/destroy_stage_5"),
        )

        registerSimple(RagiumBlocks.SPONGE_CAKE)
        listOf(
            RagiumBlocks.META_CONSUMER,
            RagiumBlocks.META_GENERATOR,
            RagiumBlocks.META_PROCESSOR,
        ).forEach { metaMachine: Block ->
            register(metaMachine) { block: Block ->
                accept(
                    VariantsBlockStateSupplier
                        .create(
                            block,
                            stateVariantOf(HTMachineModel.MODEL_ID),
                        ).coordinate(BlockStateModelGenerator.createNorthDefaultHorizontalRotationStates()),
                )
                RagiumModels.DYNAMIC_MACHINE.upload(
                    ModelIds.getItemModelId(block.asItem()),
                    TextureMap(),
                    generator.modelCollector,
                )
            }
        }
        accept(
            VariantsBlockStateSupplier
                .create(RagiumBlocks.SWEET_BERRIES_CAKE)
                .coordinate(
                    BlockStateVariantMap
                        .create(Properties.BITES)
                        .register { bite: Int ->
                            when (bite) {
                                0 -> ""
                                else -> "_slice$bite"
                            }.let {
                                stateVariantOf(
                                    ModelIds.getBlockSubModelId(
                                        RagiumBlocks.SWEET_BERRIES_CAKE,
                                        it,
                                    ),
                                )
                            }
                        },
                ),
        )
        register(RagiumBlocks.BACKPACK_INTERFACE) {
            accept(
                MultipartBlockStateSupplier
                    .create(it)
                    .with(
                        stateVariantOf(
                            RagiumModels
                                .createAllTinted(Identifier.of("block/white_wool"))
                                .upload(it, generator.modelCollector),
                        ),
                    ),
            )
        }
        register(RagiumBlocks.MANUAL_FORGE) { accept(VariantsBlockStateSupplier.create(it, stateVariantOf(it))) }

        register(RagiumBlocks.FIREBOX) {
            generator.excludeFromSimpleItemModelGeneration(it)
            accept(
                VariantsBlockStateSupplier
                    .create(it)
                    .coordinate(
                        BlockStateVariantMap
                            .create(Properties.HORIZONTAL_FACING, RagiumBlockProperties.ACTIVE)
                            .register { direction: Direction, isActive: Boolean ->
                                when (isActive) {
                                    true -> stateVariantOf(ModelIds.getBlockSubModelId(it, "_active"))
                                    false -> stateVariantOf(ModelIds.getBlockModelId(it))
                                }.rot(direction)
                            },
                    ),
            )
        }
        // exporters
        RagiumContents.Exporters.entries.forEach { exporter: RagiumContents.Exporters ->
            val coil: Block = exporter.tier.getCoil().value
            val modelId: Identifier = RagiumModels.EXPORTER.upload(
                exporter.value,
                textureMap {
                    put(TextureKey.TOP, ModelIds.getBlockSubModelId(coil, "_top"))
                    put(TextureKey.SIDE, ModelIds.getBlockSubModelId(coil, "_side"))
                },
                generator.modelCollector,
            )
            accept(
                VariantsBlockStateSupplier
                    .create(exporter.value, stateVariantOf(modelId))
                    .coordinate(BlockStateModelGenerator.createNorthDefaultRotationStates()),
            )
        }
        // pipes
        RagiumContents.Pipes.entries.forEach { pipe: RagiumContents.Pipes ->
            val block: Block = pipe.value
            // blockstate
            register(block) {
                accept(
                    MultipartBlockStateSupplier
                        .create(it)
                        .with(stateVariantOf(it))
                        .apply {
                            Direction.entries.forEach { direction: Direction ->
                                // pipe connection
                                this.with(
                                    When.create().set(ConnectingBlock.FACING_PROPERTIES[direction], true),
                                    stateVariantOf(ModelIds.getBlockSubModelId(it, "_side")).rot(direction),
                                )
                                // pipe facing
                                this.with(
                                    When.create().set(Properties.FACING, direction),
                                    stateVariantOf(RagiumAPI.id("block/pipe_overlay")).rot(direction),
                                )
                            }
                        },
                )
            }
            // model
            RagiumModels.PIPE.upload(
                block,
                TextureMap.all(block),
                generator.modelCollector,
            )
            RagiumModels.PIPE_SIDE.upload(
                ModelIds.getBlockSubModelId(block, "_side"),
                TextureMap.all(block),
                generator.modelCollector,
            )
        }

        register(RagiumBlocks.INFESTING) {
            accept(
                VariantsBlockStateSupplier
                    .create(it)
                    .coordinate(
                        BlockStateVariantMap
                            .create(Properties.ENABLED)
                            .register(
                                false,
                                stateVariantOf(TextureMap.getId(Blocks.GRAY_CONCRETE_POWDER)),
                            ).register(
                                true,
                                stateVariantOf(TextureMap.getId(Blocks.LIGHT_GRAY_CONCRETE_POWDER)),
                            ),
                    ),
            )
        }
        // ores
        RagiumContents.Ores.entries.forEach { ore: RagiumContents.Ores ->
            registerLayered(
                ore.value,
                TextureMap.getId(ore.baseStone),
                RagiumAPI.id("block/ore/${ore.material.asString()}"),
            )
        }
        // storage blocks
        RagiumContents.StorageBlocks.entries
            .map(RagiumContents.StorageBlocks::value)
            .forEach(::registerSimple)
        // hulls
        RagiumContents.Hulls.entries.forEach { hull: RagiumContents.Hulls ->
            register(hull.value) {
                generator.registerSingleton(
                    it,
                    RagiumModels.HULL_TEXTURE_FACTORY,
                )
            }
        }
        // coils
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
                textureMap {
                    put(TextureKey.LAYER0, layer0)
                    put(TextureKey.LAYER1, layer1)
                },
            )
        }

        // register(RagiumBlocks.OBLIVION_CLUSTER.asItem(), Models.GENERATED, TextureMap.layer0(RagiumBlocks.OBLIVION_CLUSTER))

        // contents
        buildList {
            addAll(RagiumContents.Dusts.entries)
            addAll(RagiumContents.Gems.entries)
            addAll(RagiumContents.Ingots.entries)
            addAll(RagiumContents.Plates.entries)
            addAll(RagiumContents.RawMaterials.entries)
            addAll(RagiumContents.Tools.entries)
            addAll(HTCrafterHammerItem.Behavior.entries)
            addAll(RagiumContents.Armors.entries)
            addAll(RagiumContents.CircuitBoards.entries)
            addAll(RagiumContents.Circuits.entries)
            addAll(RagiumContents.Foods.entries)
            addAll(RagiumContents.Misc.entries)

            remove(RagiumContents.Foods.CHOCOLATE_APPLE)
            remove(RagiumContents.Misc.EMPTY_FLUID_CUBE)
            remove(RagiumContents.Misc.FILLED_FLUID_CUBE)
            // remove(RagiumContents.Misc.OBLIVION_CUBE_SPAWN_EGG)
            remove(RagiumContents.Misc.RAGI_ALLOY_COMPOUND)
            remove(RagiumContents.Misc.SOLAR_PANEL)
        }.map(ItemConvertible::asItem).forEach(::register)

        registerLayered(
            RagiumContents.Misc.RAGI_ALLOY_COMPOUND.asItem(),
            TextureMap.getId(Items.COPPER_INGOT),
            TextureMap.getId(RagiumContents.Misc.RAGI_ALLOY_COMPOUND.asItem()),
        )
        register(
            RagiumContents.Misc.SOLAR_PANEL.asItem(),
            Models.GENERATED,
            TextureMap.layer0(RagiumAPI.id("block/solar_front")),
        )
        registerLayered(
            RagiumContents.Foods.CHOCOLATE_APPLE.asItem(),
            TextureMap.getId(Items.APPLE),
            TextureMap.getId(RagiumContents.Foods.CHOCOLATE_APPLE.asItem()),
        )
        register(RagiumContents.Misc.FILLED_FLUID_CUBE.asItem(), RagiumModels.FILLED_FLUID_CUBE)
        // elements
        /*RagiumContents.Element.entries.forEach { element: RagiumContents.Element ->
            register(element.clusterBlock.asItem(), Models.GENERATED, TextureMap.layer0(element.clusterBlock))
            register(element.dustItem)
            register(element.pendantItem)
            register(element.ringItem)
        }*/
    }
}
