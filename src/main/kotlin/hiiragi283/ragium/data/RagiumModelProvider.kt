package hiiragi283.ragium.data

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.machine.HTMachineBlockRegistry
import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.alchemy.RagiElement
import hiiragi283.ragium.common.block.HTMachineBlock
import hiiragi283.ragium.common.init.RagiumBlockProperties
import hiiragi283.ragium.common.init.RagiumModels
import hiiragi283.ragium.common.util.*
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.data.client.*
import net.minecraft.item.Item
import net.minecraft.item.Items
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

        register(RagiumContents.RAGINITE_ORE) {
            generator.registerSingleton(
                it,
                RagiumModels.createLayered(Identifier.of("block/stone"), TextureMap.getId(it)),
            )
        }
        register(RagiumContents.DEEPSLATE_RAGINITE_ORE) {
            generator.registerSingleton(
                it,
                RagiumModels.createLayered(Identifier.of("block/deepslate"), RagiumAPI.id("block/raginite_ore")),
            )
        }
        register(RagiumContents.OBLIVION_CLUSTER, generator::registerAmethyst)

        registerSimple(RagiumContents.CREATIVE_SOURCE, Identifier.of("block/respawn_anchor_top_off"))
        register(RagiumContents.MANUAL_GRINDER) {
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
        register(RagiumContents.SHAFT) { generator.registerAxisRotated(it, TextureMap.getId(it)) }
        register(RagiumContents.ALCHEMICAL_INFUSER) {
            accept(
                VariantsBlockStateSupplier.create(
                    it,
                    buildModelVariant(TextureMap.getId(it)),
                ),
            )
        }
        register(RagiumContents.ITEM_DISPLAY) {
            generator.registerSingleton(
                it,
                RagiumModels.createDisplay(
                    Identifier.of("block/oak_log_top"),
                    Identifier.of("block/oak_log"),
                ),
            )
        }
        register(RagiumContents.DATA_DRIVE) {
            accept(VariantsBlockStateSupplier.create(it, buildModelVariant(TextureMap.getId(it))))
        }
        registerSimple(RagiumContents.NETWORK_INTERFACE)
        registerSimple(RagiumContents.BASIC_CASING, Identifier.of("block/smithing_table_top"))
        registerSimple(RagiumContents.ADVANCED_CASING)

        register(RagiumContents.INFESTING) {
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
        // storage blocks
        RagiumContents.StorageBlocks.entries
            .map(RagiumContents.StorageBlocks::block)
            .forEach(::registerSimple)
        // hulls
        RagiumContents.Hulls.entries.forEach { hull: RagiumContents.Hulls ->
            register(hull.block) {
                generator.registerSingleton(
                    it,
                    RagiumModels.HULL_TEXTURE_FACTORY,
                )
            }
        }
        // coils
        RagiumContents.Coils.entries.forEach { coil: RagiumContents.Coils ->
            register(coil.block) {
                generator.registerAxisRotated(
                    it,
                    TexturedModel.CUBE_COLUMN.upload(
                        it,
                        generator.modelCollector,
                    ),
                )
            }
        }
        // machines
        HTMachineBlockRegistry.forEachBlock { machineBlock: HTMachineBlock ->
            register(machineBlock) { block: Block ->
                accept(
                    VariantsBlockStateSupplier
                        .create(
                            block,
                            buildModelVariant(RagiumModels.MACHINE_MODEL_ID),
                        ).coordinate(BlockStateModelGenerator.createNorthDefaultHorizontalRotationStates()),
                )
                RagiumModels.DYNAMIC_MACHINE.upload(
                    ModelIds.getItemModelId(block.asItem()),
                    TextureMap(),
                    generator.modelCollector,
                )
            }
        }
        // elements
        RagiElement.entries.forEach { element: RagiElement ->
            // budding block
            registerSimple(element.buddingBlock)
            // cluster block
            register(element.clusterBlock, generator::registerAmethyst)
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

        register(RagiumContents.STEEL_SWORD)
        register(RagiumContents.STEEL_SHOVEL)
        register(RagiumContents.STEEL_PICKAXE)
        register(RagiumContents.STEEL_AXE)
        register(RagiumContents.STEEL_HOE)
        register(RagiumContents.FORGE_HAMMER)
        register(RagiumContents.BACKPACK)
        register(RagiumContents.LARGE_BACKPACK)
        register(RagiumContents.ENDER_BACKPACK)

        register(RagiumContents.STEEL_HELMET)
        register(RagiumContents.STEEL_CHESTPLATE)
        register(RagiumContents.STEEL_LEGGINGS)
        register(RagiumContents.STEEL_BOOTS)

        register(RagiumContents.RAW_RAGINITE)
        register(
            RagiumContents.RAGI_ALLOY_COMPOUND,
            Models.GENERATED_TWO_LAYERS,
            textureMap {
                put(TextureKey.LAYER0, TextureMap.getId(Items.COPPER_INGOT))
                put(TextureKey.LAYER1, TextureMap.getId(RagiumContents.RAGI_ALLOY_COMPOUND))
            },
        )
        register(RagiumContents.SOAP_INGOT)
        register(RagiumContents.BASALT_FIBER)

        register(RagiumContents.RAGI_CRYSTAL)
        register(RagiumContents.OBLIVION_CRYSTAL)
        register(RagiumContents.OBLIVION_CLUSTER.asItem(), Models.GENERATED, TextureMap.layer0(RagiumContents.OBLIVION_CLUSTER))

        register(RagiumContents.BEE_WAX)
        register(RagiumContents.FLOUR)
        register(RagiumContents.DOUGH)
        register(RagiumContents.PULP)
        // circuits
        RagiumContents.Circuit.entries
            .map(RagiumContents.Circuit::asItem)
            .forEach(::register)
        // contents
        buildList {
            addAll(RagiumContents.Dusts.entries)
            addAll(RagiumContents.Ingots.entries)
            addAll(RagiumContents.Plates.entries)
        }.map(HTItemContent::asItem).forEach(::register)
        // elements
        RagiElement.entries.forEach { element: RagiElement ->
            register(element.clusterBlock.asItem(), Models.GENERATED, TextureMap.layer0(element.clusterBlock))
            register(element.dustItem)
        }
        // fluids
        RagiumContents.Fluids.entries
            .map(RagiumContents.Fluids::asItem)
            .forEach { register(it, RagiumModels.FILLED_FLUID_CUBE) }
    }
}
