package hiiragi283.ragium.common.init

import hiiragi283.ragium.common.Ragium
import hiiragi283.ragium.common.block.HTAlchemicalInfuserBlock
import hiiragi283.ragium.common.block.HTGearBoxBlock
import hiiragi283.ragium.common.block.HTManualGrinderBlock
import hiiragi283.ragium.common.block.HTShaftBlock
import hiiragi283.ragium.common.machine.HTMachineTier
import hiiragi283.ragium.common.machine.HTMachineType
import hiiragi283.ragium.common.recipe.HTMachineRecipe
import hiiragi283.ragium.common.registry.HTBlockRegister
import hiiragi283.ragium.common.util.*
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.data.client.BlockStateModelGenerator
import net.minecraft.data.client.TextureMap
import net.minecraft.data.client.VariantSettings
import net.minecraft.data.client.VariantsBlockStateSupplier
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.tag.BlockTags
import net.minecraft.state.property.IntProperty
import net.minecraft.util.Identifier

object RagiumBlocks {
    @JvmField
    val REGISTER: HTBlockRegister = HTBlockRegister(Ragium.MOD_ID).apply(::registerMachines)

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

    //    Blocks    //

    @JvmField
    val RAGI_ALLOY_BLOCK: Block =
        REGISTER.registerCopy("ragi_alloy_block", Blocks.IRON_BLOCK) {
            putEnglish("Block of Ragi-Alloy")
            putEnglishTips("Just a compressed block.")
            putJapanese("ラギ合金ブロック")
            putJapaneseTips("ただの圧縮ブロック。")
            registerTags(BlockTags.PICKAXE_MINEABLE)
        }

    @JvmField
    val RAGI_STEEL_BLOCK: Block =
        REGISTER.registerCopy("ragi_steel_block", Blocks.IRON_BLOCK) {
            putEnglish("Block of Ragi-Steel")
            putEnglishTips("Just a compressed block?")
            putJapanese("ラギスチールブロック")
            putJapaneseTips("ただの圧縮ブロック？")
            registerTags(BlockTags.PICKAXE_MINEABLE)
        }

    @JvmField
    val REFINED_RAGI_STEEL_BLOCK: Block =
        REGISTER.registerCopy("refined_ragi_steel_block", Blocks.IRON_BLOCK) {
            putEnglish("Block of Refined Ragi-Steel")
            putEnglishTips("Just a compressed block!")
            putJapanese("精製ラギスチールブロック")
            putJapaneseTips("ただの圧縮ブロック！")
            registerTags(BlockTags.PICKAXE_MINEABLE)
        }

    //    Hulls    //

    @JvmField
    val RAGI_ALLOY_HULL: Block =
        REGISTER.registerCopy("ragi_alloy_hull", HTMachineTier.HEAT.base) {
            putEnglish("Ragi-Alloy Hull")
            putEnglishTips("Tier 1 - Machine Hull")
            putJapanese("ラギ合金筐体")
            putJapaneseTips("Tier 1 - マシン筐体")
            generateState { it.registerSingleton(block, RagiumModels.HULL_TEXTURE_FACTORY) }
            registerTags(BlockTags.PICKAXE_MINEABLE)
        }

    @JvmField
    val RAGI_STEEL_HULL: Block =
        REGISTER.registerCopy("ragi_steel_hull", HTMachineTier.KINETIC.base) {
            putEnglish("Ragi-Steel Hull")
            putEnglishTips("Tier 2 - Machine Hull")
            putJapanese("ラギスチール筐体")
            putJapaneseTips("Tier 2 - マシン筐体")
            generateState { it.registerSingleton(block, RagiumModels.HULL_TEXTURE_FACTORY) }
            registerTags(BlockTags.PICKAXE_MINEABLE)
        }

    @JvmField
    val REFINED_RAGI_STEEL_HULL: Block =
        REGISTER.registerCopy("refined_ragi_steel_hull", HTMachineTier.ELECTRIC.base) {
            putEnglish("Refined Ragi-Steel Hull")
            putEnglishTips("Tier 3 - Machine Hull")
            putJapanese("精製ラギスチール筐体")
            putJapaneseTips("Tier 3 - マシン筐体")
            generateState { it.registerSingleton(block, RagiumModels.HULL_TEXTURE_FACTORY) }
            registerTags(BlockTags.PICKAXE_MINEABLE)
        }

    //    Machines    //

    @JvmField
    val CREATIVE_SOURCE: Block =
        REGISTER.registerCopy("creative_source", Blocks.COMMAND_BLOCK) {
            putEnglish("Creative Power Source")
            // putEnglishTips("(L-Shift +) Right-Click to change power level")
            putJapanese("クリエイティブ用エネルギー源")
            // putJapaneseTips("右クリック（＋左シフト）で出力を変更")
            /*generateState {
                it.blockStateCollector.accept(
                    VariantsBlockStateSupplier
                        .create(block, buildModelVariant(Ragium.MOD_ID, name))
                        .coordinate(
                            BlockStateVariantMap.create(HTMachineTier.PROPERTY),
                        ),
                )
            }*/
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
        REGISTER.registerWithBE("item_display", RagiumBlockEntityTypes.ITEM_DISPLAY) {
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

    init {
        RagiumBlockEntityTypes.ALCHEMICAL_INFUSER.addSupportedBlock(ALCHEMICAL_INFUSER)
        RagiumBlockEntityTypes.ITEM_DISPLAY.addSupportedBlock(ITEM_DISPLAY)
    }

    @JvmStatic
    private fun registerMachines(register: HTBlockRegister) {
        // Recipe Serializer
        Registry.register(Registries.RECIPE_SERIALIZER, Ragium.id("generic"), HTMachineRecipe.Serializer)
        // for each type
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

    //    Properties    //

    object Properties {
        @JvmField
        val LEVEL_7: IntProperty = IntProperty.of("level", 0, 7)
    }
}
