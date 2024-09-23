package hiiragi283.ragium.common.init

import hiiragi283.ragium.common.Ragium
import hiiragi283.ragium.common.alchemy.RagiElement
import hiiragi283.ragium.common.block.*
import hiiragi283.ragium.common.data.HTLangType
import hiiragi283.ragium.common.machine.HTMachineType
import hiiragi283.ragium.common.registry.HTBlockRegister
import hiiragi283.ragium.common.util.*
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.data.client.*
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.tag.BlockTags
import net.minecraft.state.property.IntProperty
import net.minecraft.util.Identifier

object RagiumBlocks {
    @JvmField
    val REGISTER: HTBlockRegister = HTBlockRegister(Ragium.MOD_ID)
        .apply(::initMachines)
        .apply(::initElements)
        .apply(::initStorageBlocks)
        .apply(::initHulls)

    //    Ores    //

    @JvmField
    val RAGINITE_ORE: Block =
        REGISTER.registerCopy("raginite_ore", Blocks.IRON_ORE) {
            putEnglish("Raginite Ore")
            putEnglishTips("Found in Overworld between y=112 to -16")
            putJapanese("ラギナイト鉱石")
            putJapaneseTips("オーバーワールドのy=112から-16の範囲で見つかる")
            generateState {
                it.registerSingleton(
                    block,
                    RagiumModels.createLayered(Identifier.of("block/stone"), prefixedId),
                )
            }
            setCustomLootTable()
            registerTags(BlockTags.PICKAXE_MINEABLE)
        }

    @JvmField
    val DEEPSLATE_RAGINITE_ORE: Block =
        REGISTER.registerCopy("deepslate_raginite_ore", Blocks.DEEPSLATE_IRON_ORE) {
            putEnglish("Deep Raginite Ore")
            putEnglishTips("Found in Overworld between 112 to -16")
            putJapanese("深層ラギナイト鉱石")
            putJapaneseTips("オーバーワールドのy=112から-16の範囲で見つかる")
            generateState {
                it.registerSingleton(
                    block,
                    RagiumModels.createLayered(Identifier.of("block/deepslate"), Ragium.id("block/raginite_ore")),
                )
            }

            setCustomLootTable()
            registerTags(BlockTags.PICKAXE_MINEABLE)
        }

    //    Machines    //

    @JvmField
    val CREATIVE_SOURCE: Block =
        REGISTER.registerWithBE(
            "creative_source",
            RagiumBlockEntityTypes.CREATIVE_SOURCE,
            blockSettings(Blocks.COMMAND_BLOCK),
        ) {
            putEnglish("Creative Power Source")
            // putEnglishTips("(L-Shift +) Right-Click to change power level")
            putJapanese("クリエイティブ用エネルギー源")
            // putJapaneseTips("右クリック（＋左シフト）で出力を変更")
            generateSimpleState(Identifier.of("block/respawn_anchor_top_off"))
        }

    @JvmField
    val MANUAL_GRINDER: Block =
        REGISTER.register("manual_grinder", HTManualGrinderBlock) {
            putEnglish("Manual Grinder")
            putEnglishTips("Right-Click to rotate, input to side, output from down")
            putJapanese("石臼")
            putJapaneseTips("右クリックで回転，側面から搬入，底面から搬出")
            generateState {
                it.blockStateCollector.accept(
                    buildMultipartState(block) {
                        Properties.LEVEL_7.values.forEach { level: Int ->
                            with(
                                buildWhen(Properties.LEVEL_7, level),
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
            registerTags(BlockTags.PICKAXE_MINEABLE)
        }

    @JvmField
    val WATER_COLLECTOR: Block =
        REGISTER.registerWithBE(
            "water_collector",
            RagiumBlockEntityTypes.WATER_COLLECTOR,
            blockSettings(Blocks.BRICKS),
        ) {
            putEnglish("Water Collector")
            putEnglishTips("Adjacent to two or more Water Source")
            putJapanese("水収集器")
            putJapaneseTips("二か所以上の水源と隣接させる")
            registerTags(BlockTags.PICKAXE_MINEABLE)
        }

    @JvmField
    val BURNING_BOX: Block =
        REGISTER.registerHorizontalWithBE(
            "burning_box",
            RagiumBlockEntityTypes.BURNING_BOX,
            blockSettings(Blocks.BRICKS),
        ) {
            putEnglish("Burning Box")
            putEnglishTips("Provides heat to top by burning fuel")
            putJapanese("燃焼室")
            putJapaneseTips("燃料を燃やして上面に熱を供給する")
            generateState {
                it.registerNorthDefaultHorizontalRotated(
                    block,
                    RagiumModels.createMachine(
                        Blocks.BRICKS,
                        Blocks.BRICKS,
                        TextureMap.getSubId(block, "_front"),
                    ),
                )
            }
            registerTags(BlockTags.PICKAXE_MINEABLE)
        }

    @JvmField
    val WATER_GENERATOR: Block =
        REGISTER.registerHorizontalWithBE(
            "water_generator",
            RagiumBlockEntityTypes.WATER_GENERATOR,
        ) {
            putEnglish("Water Generator")
            putEnglishTips("Power when adjacent to two or more Flowing Water")
            putJapanese("水力発動機")
            putJapaneseTips("二か所以上の水流と隣接すると動力を供給")
        }

    @JvmField
    val WIND_GENERATOR: Block =
        REGISTER.registerHorizontalWithBE(
            "wind_generator",
            RagiumBlockEntityTypes.WIND_GENERATOR,
        ) {
            putEnglish("Wind Generator")
            putEnglishTips("Power at y=128 or more")
            putJapanese("風力発動機")
            putJapaneseTips("y=128以上の時に動力を供給")
        }

    @JvmField
    val SHAFT: Block = REGISTER.register("shaft", HTShaftBlock) {
        putEnglish("Shaft")
        putEnglishTips("Transmits rotation in the opposite direction of the input")
        putJapanese("シャフト")
        putJapaneseTips("入力面と反対の向きに回転を伝える")
        generateState {
            it.registerAxisRotated(block, prefixedId)
        }
    }

    @JvmField
    val GEAR_BOX: Block = REGISTER.register("gear_box", HTGearBoxBlock) {
        putEnglish("Gear Box")
        putEnglishTips("Transmits rotation in the facing direction")
        putJapanese("ギアボックス")
        putJapaneseTips("出力面の向きに回転を伝える")
        generateState {
            it.blockStateCollector.accept(
                VariantsBlockStateSupplier
                    .create(
                        block,
                        buildModelVariant(prefixedId),
                    ).coordinate(BlockStateModelGenerator.createNorthDefaultRotationStates()),
            )
        }
    }

    @JvmField
    val BLAZING_BOX: Block =
        REGISTER.registerHorizontalWithBE(
            "blazing_box",
            RagiumBlockEntityTypes.BLAZING_BOX,
            blockSettings(Blocks.POLISHED_BLACKSTONE_BRICKS),
        ) {
            putEnglish("Blazing Box")
            putEnglishTips("Provides more heat to top by burning fuel")
            putJapanese("豪炎室")
            putJapaneseTips("燃料を燃やして上面に更なる熱を供給する")
            generateState {
                it.registerNorthDefaultHorizontalRotated(
                    block,
                    RagiumModels.createMachine(
                        Blocks.POLISHED_BLACKSTONE_BRICKS,
                        Blocks.POLISHED_BLACKSTONE_BRICKS,
                        TextureMap.getSubId(BURNING_BOX, "_front"),
                    ),
                )
            }
            registerTags(BlockTags.PICKAXE_MINEABLE)
        }

    @JvmField
    val ALCHEMICAL_INFUSER: Block =
        REGISTER.register("alchemical_infuser", HTAlchemicalInfuserBlock) {
            putEnglish("Alchemical Infuser")
            putEnglishTips("Gotcha!")
            putJapanese("錬金注入機")
            putJapaneseTips("ガッチャ！")
            generateStateWithoutModel()
        }

    @JvmField
    val ITEM_DISPLAY: Block =
        REGISTER.register("item_display", HTItemDisplayBlock) {
            putEnglish("Item Display")
            putEnglishTips("")
            putJapanese("アイテムティスプレイ")
            putJapaneseTips("")
            generateState {
                it.registerSingleton(
                    block,
                    RagiumModels.createDisplay(
                        Identifier.of("block/oak_log_top"),
                        Identifier.of("block/oak_log"),
                    ),
                )
            }
        }

    @JvmField
    val INFESTING: Block = REGISTER.register("infesting", HTInfectingBlock) {
        generateState {
            it.blockStateCollector.accept(
                VariantsBlockStateSupplier
                    .create(block)
                    .coordinate(
                        BlockStateVariantMap
                            .create(net.minecraft.state.property.Properties.ENABLED)
                            .register(false, buildModelVariant(TextureMap.getId(Blocks.GRAY_CONCRETE_POWDER)))
                            .register(true, buildModelVariant(TextureMap.getId(Blocks.LIGHT_GRAY_CONCRETE_POWDER))),
                    ),
            )
        }
    }

    init {
        RagiumBlockEntityTypes.ALCHEMICAL_INFUSER.addSupportedBlock(ALCHEMICAL_INFUSER)
        RagiumBlockEntityTypes.ITEM_DISPLAY.addSupportedBlock(ITEM_DISPLAY)
    }

    //    Machines    //

    @JvmStatic
    private fun initMachines(register: HTBlockRegister) {
        HTMachineType.getEntries().forEach { type: HTMachineType ->
            val id: Identifier = type.id
            val block: Block = type.block
            // Machine Block
            register.register(id.path, block) {
                generateState {
                    it.registerNorthDefaultHorizontalRotated(
                        type.block,
                        RagiumModels.createMachine(
                            type.tier.casingTex,
                            type.tier.baseTex,
                            TextureMap.getSubId(type.block, "_front"),
                        ),
                    )
                }
            }
            // BlockEntityType
            Registry.register(Registries.BLOCK_ENTITY_TYPE, id, type.blockEntityType)
            type.blockEntityType.addSupportedBlock(block)
            // RecipeType
            Registry.register(Registries.RECIPE_TYPE, id, type)
        }
    }

    //    Elements    //

    @JvmStatic
    private fun initElements(register: HTBlockRegister) {
        RagiElement.entries.forEach { element: RagiElement ->
            // Budding Block
            register.register("budding_${element.asString()}", element.buddingBlock) {
                putEnglish("Budding ${element.getTranslatedName(HTLangType.EN_US)}")
                putJapanese("芽生えた${element.getTranslatedName(HTLangType.JA_JP)}")
            }
            // Cluster Block
            register.register("${element.asString()}_cluster", element.clusterBlock) {
                putEnglish("${element.getTranslatedName(HTLangType.EN_US)} Cluster")
                putJapanese("${element.getTranslatedName(HTLangType.JA_JP)}の塊")
                generateState {
                    it.blockStateCollector.accept(
                        VariantsBlockStateSupplier
                            .create(
                                block,
                                buildModelVariant(
                                    Models.CROSS.upload(
                                        block,
                                        TextureMap.cross(block),
                                        it.modelCollector,
                                    ),
                                ),
                            ).coordinate(it.createUpDefaultFacingVariantMap()),
                    )
                }
                setCustomLootTable()
            }
        }
    }

    //    Storage Blocks    //

    enum class StorageBlocks(val material: RagiumMaterials) :
        ItemConvertible,
        HTTranslationFormatter {
        RAGI_ALLOY(RagiumMaterials.RAGI_ALLOY),
        RAGI_STEEL(RagiumMaterials.RAGI_STEEL),
        REFINED_RAGI_STEEL(RagiumMaterials.REFINED_RAGI_STEEL),
        ;

        val block = Block(blockSettings(Blocks.IRON_BLOCK))

        override fun asItem(): Item = block.asItem()

        override val enPattern: String = "Block of %s"
        override val jaPattern: String = "%sブロック"
    }

    @JvmStatic
    private fun initStorageBlocks(register: HTBlockRegister) {
        StorageBlocks.entries.forEach { block: StorageBlocks ->
            register.register("${block.name.lowercase()}_block", block.block) {
                registerTags(BlockTags.PICKAXE_MINEABLE)
            }
        }
    }

    //    Hulls    //

    enum class Hulls(val material: RagiumMaterials) :
        ItemConvertible,
        HTTranslationFormatter {
        RAGI_ALLOY(RagiumMaterials.RAGI_ALLOY),
        RAGI_STEEL(RagiumMaterials.RAGI_STEEL),
        REFINED_RAGI_STEEL(RagiumMaterials.REFINED_RAGI_STEEL),
        ;

        val block = Block(blockSettings(material.tier.base))

        override fun asItem(): Item = block.asItem()

        override val enPattern: String = "%s Hull"
        override val jaPattern: String = "%s筐体"
    }

    @JvmStatic
    private fun initHulls(register: HTBlockRegister) {
        Hulls.entries.forEach { hull: Hulls ->
            register.register("${hull.name.lowercase()}_hull", hull.block) {
                generateState { it.registerSingleton(block, RagiumModels.HULL_TEXTURE_FACTORY) }
                registerTags(BlockTags.PICKAXE_MINEABLE)
            }
        }
    }

    //    Properties    //

    object Properties {
        @JvmField
        val LEVEL_7: IntProperty = IntProperty.of("level", 0, 7)
    }
}
