package hiiragi283.ragium.data

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.common.RagiumContents
import hiiragi283.ragium.common.init.RagiumBlockProperties
import hiiragi283.ragium.common.init.RagiumBlocks
import hiiragi283.ragium.common.init.RagiumItems
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
                textureMap {
                    put(TextureKey.LAYER0, layer0)
                    put(TextureKey.LAYER1, layer1)
                }
            }
        }

        fun registerSlab(slabBlock: Block, fullBlock: Block) {
            val textureMap: TextureMap = textureMap {
                put(TextureKey.SIDE, TextureMap.getId(fullBlock))
                put(TextureKey.TOP, TextureMap.getId(fullBlock))
                put(TextureKey.BOTTOM, TextureMap.getId(fullBlock))
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
            val textureMap: TextureMap = textureMap {
                put(TextureKey.SIDE, TextureMap.getId(fullBlock))
                put(TextureKey.TOP, TextureMap.getId(fullBlock))
                put(TextureKey.BOTTOM, TextureMap.getId(fullBlock))
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
        registerSimple(RagiumBlocks.ASPHALT)
        registerSlab(RagiumBlocks.ASPHALT_SLAB, RagiumBlocks.ASPHALT)
        registerStair(RagiumBlocks.ASPHALT_STAIRS, RagiumBlocks.ASPHALT)

        registerSimple(RagiumBlocks.LINED_ASPHALT)
        registerSlab(RagiumBlocks.LINED_ASPHALT_SLAB, RagiumBlocks.LINED_ASPHALT)
        registerStair(RagiumBlocks.LINED_ASPHALT_STAIRS, RagiumBlocks.LINED_ASPHALT)

        registerSimple(RagiumBlocks.GYPSUM)
        registerSlab(RagiumBlocks.GYPSUM_SLAB, RagiumBlocks.GYPSUM)
        registerStair(RagiumBlocks.GYPSUM_STAIRS, RagiumBlocks.GYPSUM)

        registerSimple(RagiumBlocks.AUTO_ILLUMINATOR)
        registerSimple(RagiumBlocks.CREATIVE_SOURCE)
        registerSimple(RagiumBlocks.MUTATED_SOIL, Identifier.of("block/dirt"))
        registerSimple(RagiumBlocks.NETWORK_INTERFACE)
        registerSimple(RagiumBlocks.OPEN_CRATE)
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
        registerLayered(
            RagiumBlocks.POROUS_NETHERRACK,
            Identifier.of("block/netherrack"),
            Identifier.of("block/destroy_stage_5"),
        )

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
        registerStaticModel(RagiumBlocks.SWEET_BERRIES_CAKE)
        // factory
        registerFactory(RagiumBlocks.BACKPACK_INTERFACE, RagiumModels.ALL_TINTED) {
            TextureMap.all(Blocks.WHITE_WOOL)
        }
        registerFactory(RagiumBlocks.ITEM_DISPLAY, RagiumModels.DISPLAY) {
            textureMap {
                put(TextureKey.TOP, Identifier.of("block/oak_log_top"))
                put(TextureKey.SIDE, Identifier.of("block/oak_log"))
            }
        }
        registerFactory(RagiumBlocks.ENCHANTMENT_BOOKSHELF, Models.CUBE_COLUMN) {
            textureMap {
                put(TextureKey.END, Identifier.of("block/chiseled_bookshelf_top"))
                put(TextureKey.SIDE, TextureMap.getId(RagiumBlocks.ENCHANTMENT_BOOKSHELF))
            }
        }

        RagiumContents.Drums.entries.forEach { registerFactory(it.value, TexturedModel.CUBE_COLUMN) }
        RagiumContents.Hulls.entries.forEach { hull: RagiumContents.Hulls ->
            val tier: HTMachineTier = hull.tier
            registerFactory(hull.value, RagiumModels.HULL) { block: Block ->
                textureMap {
                    put(
                        TextureKey.INSIDE,
                        when (tier) {
                            HTMachineTier.PRIMITIVE -> Identifier.of("block/blast_furnace_top")
                            HTMachineTier.BASIC -> RagiumAPI.id("block/basic_casing")
                            HTMachineTier.ADVANCED -> RagiumAPI.id("block/advanced_casing")
                        },
                    )
                    put(TextureKey.TOP, tier.getStorageBlock().id.withPrefixedPath("block/"))
                    put(TextureKey.SIDE, TextureMap.getId(block))
                }
            }
        }
        // supplier
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
            val modelId: Identifier = RagiumModels.EXPORTER.upload(
                block,
                textureMap {
                    put(TextureKey.TOP, TextureMap.getSubId(coil, "_top"))
                    put(TextureKey.SIDE, TextureMap.getSubId(coil, "_side"))
                },
                generator.modelCollector,
            )
            registerDirectional(block, stateVariantOf(modelId))
        }
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
                textureMap {
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
            remove(RagiumItems.BUJIN)
            remove(RagiumItems.GIGANT_HAMMER)
            addAll(RagiumItems.ARMORS)
            addAll(RagiumContents.CircuitBoards.entries)
            addAll(RagiumContents.Circuits.entries)
            addAll(RagiumItems.FOODS)
            addAll(RagiumItems.INGREDIENTS)
            addAll(RagiumItems.MISC)

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
    }
}
