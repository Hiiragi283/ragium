package hiiragi283.ragium.data

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.client.model.HTMachineModel
import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.init.RagiumBlockProperties
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumFluids
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.data.client.*
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.item.Items
import net.minecraft.state.property.Properties
import net.minecraft.util.Identifier

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

        fun accept(supplier: BlockStateSupplier) {
            generator.blockStateCollector.accept(supplier)
        }

        register(RagiumBlocks.OBLIVION_CLUSTER, generator::registerAmethyst)

        registerSimple(RagiumBlocks.CREATIVE_SOURCE)
        register(RagiumBlocks.MANUAL_GRINDER) {
            accept(
                buildMultipartState(it) {
                    RagiumBlockProperties.LEVEL_7.values.forEach { level: Int ->
                        with(
                            buildWhen(RagiumBlockProperties.LEVEL_7, level),
                            buildStateVariant {
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
        register(RagiumBlocks.ALCHEMICAL_INFUSER) {
            accept(
                VariantsBlockStateSupplier.create(
                    it,
                    buildModelVariant(TextureMap.getId(it)),
                ),
            )
        }
        register(RagiumBlocks.ITEM_DISPLAY) {
            generator.registerSingleton(
                it,
                RagiumModels.createDisplay(
                    Identifier.of("block/oak_log_top"),
                    Identifier.of("block/oak_log"),
                ),
            )
        }
        register(RagiumBlocks.DATA_DRIVE) {
            accept(VariantsBlockStateSupplier.create(it, buildModelVariant(TextureMap.getId(it))))
        }
        registerSimple(RagiumBlocks.NETWORK_INTERFACE)
        registerSimple(RagiumBlocks.BASIC_CASING, Identifier.of("block/blast_furnace_top"))
        registerSimple(RagiumBlocks.ADVANCED_CASING)
        register(RagiumBlocks.POROUS_NETHERRACK) {
            generator.registerSingleton(
                it,
                RagiumModels.createLayered(Identifier.of("block/netherrack"), Identifier.of("block/destroy_stage_5")),
            )
        }

        registerSimple(RagiumBlocks.SPONGE_CAKE)
        register(RagiumBlocks.META_MACHINE) { block: Block ->
            accept(
                VariantsBlockStateSupplier
                    .create(
                        block,
                        buildModelVariant(HTMachineModel.MODEL_ID),
                    ).coordinate(BlockStateModelGenerator.createNorthDefaultHorizontalRotationStates()),
            )
            RagiumModels.DYNAMIC_MACHINE.upload(
                ModelIds.getItemModelId(block.asItem()),
                TextureMap(),
                generator.modelCollector,
            )
        }
        accept(
            VariantsBlockStateSupplier
                .create(RagiumBlocks.SWEET_BERRIES_CAKE)
                .coordinate(
                    BlockStateVariantMap
                        .create(Properties.BITES)
                        .register(0, buildModelVariant(ModelIds.getBlockModelId(RagiumBlocks.SWEET_BERRIES_CAKE)))
                        .register(1, buildModelVariant(ModelIds.getBlockSubModelId(RagiumBlocks.SWEET_BERRIES_CAKE, "_slice1")))
                        .register(2, buildModelVariant(ModelIds.getBlockSubModelId(RagiumBlocks.SWEET_BERRIES_CAKE, "_slice2")))
                        .register(3, buildModelVariant(ModelIds.getBlockSubModelId(RagiumBlocks.SWEET_BERRIES_CAKE, "_slice3")))
                        .register(4, buildModelVariant(ModelIds.getBlockSubModelId(RagiumBlocks.SWEET_BERRIES_CAKE, "_slice4")))
                        .register(5, buildModelVariant(ModelIds.getBlockSubModelId(RagiumBlocks.SWEET_BERRIES_CAKE, "_slice5")))
                        .register(6, buildModelVariant(ModelIds.getBlockSubModelId(RagiumBlocks.SWEET_BERRIES_CAKE, "_slice6"))),
                ),
        )

        register(RagiumBlocks.INFESTING) {
            accept(
                VariantsBlockStateSupplier
                    .create(it)
                    .coordinate(
                        BlockStateVariantMap
                            .create(net.minecraft.state.property.Properties.ENABLED)
                            .register(false, buildModelVariant(TextureMap.getId(Blocks.GRAY_CONCRETE_POWDER)))
                            .register(true, buildModelVariant(TextureMap.getId(Blocks.LIGHT_GRAY_CONCRETE_POWDER))),
                    ),
            )
        }
        // ores
        RagiumContents.Ores.entries
            .forEach {
                register(it.value) { block: Block ->
                    generator.registerSingleton(
                        block,
                        RagiumModels.createLayered(Identifier.of("block/stone"), TextureMap.getId(block)),
                    )
                }
            }
        RagiumContents.DeepOres.entries
            .forEach { deepOre: RagiumContents.DeepOres ->
                register(deepOre.value) { block: Block ->
                    generator.registerSingleton(
                        block,
                        RagiumModels.createLayered(
                            Identifier.of("block/deepslate"),
                            deepOre.material.getOre()?.let { TextureMap.getId(it.value) } ?: TextureMap.getId(block),
                        ),
                    )
                }
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
        // motors
        RagiumContents.Motors.entries.forEach { motor: RagiumContents.Motors ->
            register(motor.value) {
                generator.registerAxisRotated(
                    it,
                    TexturedModel.makeFactory({
                        textureMap {
                            put(TextureKey.SIDE, RagiumAPI.id("block/motor"))
                            put(
                                TextureKey.END,
                                motor.tier
                                    .getCoil()
                                    .id
                                    .withPath { path: String -> "block/${path}_top" },
                            )
                        }
                    }, Models.CUBE_COLUMN),
                )
            }
        }
        // elements
        RagiumContents.Element.entries.forEach { element: RagiumContents.Element ->
            // budding block
            registerSimple(element.buddingBlock)
            // cluster block
            register(element.clusterBlock, generator::registerAmethyst)
        }
        // fluids
        accept(VariantsBlockStateSupplier.create(RagiumFluids.PETROLEUM.block))
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

        buildList {
            add(RagiumContents.Accessories.DIVING_GOGGLES)
            add(RagiumContents.Accessories.NIGHT_VISION_GOGGLES)
            add(RagiumContents.Accessories.PISTON_BOOTS)
            add(RagiumContents.Accessories.PARACHUTE)
        }.map(RagiumContents.Accessories::asItem).forEach(::register)

        register(RagiumBlocks.OBLIVION_CLUSTER.asItem(), Models.GENERATED, TextureMap.layer0(RagiumBlocks.OBLIVION_CLUSTER))

        // contents
        buildList {
            addAll(RagiumContents.Dusts.entries)
            addAll(RagiumContents.Ingots.entries)
            addAll(RagiumContents.Plates.entries)
            addAll(RagiumContents.RawMaterials.entries)
            addAll(RagiumContents.Tools.entries)
            addAll(RagiumContents.Armors.entries)
            addAll(RagiumContents.Circuits.entries)
            addAll(RagiumContents.Foods.entries)
            addAll(RagiumContents.Misc.entries)

            remove(RagiumContents.Foods.CHOCOLATE_APPLE)
            remove(RagiumContents.Misc.EMPTY_FLUID_CUBE)
            remove(RagiumContents.Misc.OBLIVION_CUBE_SPAWN_EGG)
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
        // elements
        RagiumContents.Element.entries.forEach { element: RagiumContents.Element ->
            register(element.clusterBlock.asItem(), Models.GENERATED, TextureMap.layer0(element.clusterBlock))
            register(element.dustItem)
            register(element.pendantItem)
            register(element.ringItem)
        }
        // fluids
        RagiumContents.Fluids.entries
            .map(RagiumContents.Fluids::asItem)
            .forEach { register(it, RagiumModels.FILLED_FLUID_CUBE) }

        register(RagiumFluids.PETROLEUM.bucketItem)
    }
}
