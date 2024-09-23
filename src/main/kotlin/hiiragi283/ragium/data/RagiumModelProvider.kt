package hiiragi283.ragium.data

import hiiragi283.ragium.common.Ragium
import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.alchemy.RagiElement
import hiiragi283.ragium.common.init.RagiumBlockProperties
import hiiragi283.ragium.common.init.RagiumItems
import hiiragi283.ragium.common.init.RagiumModels
import hiiragi283.ragium.common.machine.HTMachineTier
import hiiragi283.ragium.common.machine.HTMachineType
import hiiragi283.ragium.common.util.*
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.data.client.*
import net.minecraft.util.Identifier

class RagiumModelProvider(output: FabricDataOutput) : FabricModelProvider(output) {
    //   BlockState    //

    override fun generateBlockStateModels(generator: BlockStateModelGenerator) {
        fun register(block: Block, action: (Block) -> Unit) {
            action(block)
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
                RagiumModels.createLayered(Identifier.of("block/deepslate"), Ragium.id("block/raginite_ore")),
            )
        }
        registerSimple(RagiumContents.CREATIVE_SOURCE, Identifier.of("block/respawn_anchor_top_off"))
        register(RagiumContents.MANUAL_GRINDER) {
            accept(
                buildMultipartState(it) {
                    RagiumBlockProperties.LEVEL_7.values.forEach { level: Int ->
                        with(
                            buildWhen(RagiumBlockProperties.LEVEL_7, level),
                            buildStateVariant {
                                model(
                                    Ragium.id(
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
        register(RagiumContents.BURNING_BOX) {
            generator.registerNorthDefaultHorizontalRotated(
                it,
                RagiumModels.createMachine(
                    Blocks.BRICKS,
                    Blocks.BRICKS,
                    TextureMap.getSubId(it, "_front"),
                ),
            )
        }
        registerSimple(RagiumContents.WATER_GENERATOR)
        registerSimple(RagiumContents.WIND_GENERATOR)
        register(RagiumContents.SHAFT) { generator.registerAxisRotated(it, TextureMap.getId(it)) }
        register(RagiumContents.GEAR_BOX) {
            generator.blockStateCollector.accept(
                VariantsBlockStateSupplier
                    .create(
                        it,
                        buildModelVariant(TextureMap.getId(it)),
                    ).coordinate(BlockStateModelGenerator.createNorthDefaultRotationStates()),
            )
        }
        register(RagiumContents.BLAZING_BOX) {
            generator.registerNorthDefaultHorizontalRotated(
                it,
                RagiumModels.createMachine(
                    Blocks.POLISHED_BLACKSTONE_BRICKS,
                    Blocks.POLISHED_BLACKSTONE_BRICKS,
                    TextureMap.getSubId(RagiumContents.BURNING_BOX, "_front"),
                ),
            )
        }
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
        // machines
        HTMachineType.getEntries().forEach { type: HTMachineType ->
            val block: Block = type.block
            val tier: HTMachineTier = type.tier
            register(block) {
                generator.registerNorthDefaultHorizontalRotated(
                    block,
                    RagiumModels.createMachine(
                        tier.casingTex,
                        tier.baseTex,
                        TextureMap.getSubId(block, "_front"),
                    ),
                )
            }
        }
        // elements
        RagiElement.entries.forEach { element: RagiElement ->
            registerSimple(element.buddingBlock)
            register(element.clusterBlock) {
                accept(
                    VariantsBlockStateSupplier
                        .create(
                            it,
                            buildModelVariant(
                                Models.CROSS.upload(
                                    it,
                                    TextureMap.cross(it),
                                    generator.modelCollector,
                                ),
                            ),
                        ).coordinate(generator.createUpDefaultFacingVariantMap()),
                )
            }
        }
    }

    //    Model    //

    override fun generateItemModels(generator: ItemModelGenerator) {
        RagiumItems.REGISTER.generateModel(generator)
    }
}
