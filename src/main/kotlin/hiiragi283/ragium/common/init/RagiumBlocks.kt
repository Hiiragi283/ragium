package hiiragi283.ragium.common.init

import hiiragi283.ragium.client.data.RagiumModels
import hiiragi283.ragium.client.util.*
import hiiragi283.ragium.common.Ragium
import hiiragi283.ragium.common.block.HTBlockWithEntity
import hiiragi283.ragium.common.block.HTCreativePowerSourceBlock
import hiiragi283.ragium.common.block.HTManualGrinderBlock
import hiiragi283.ragium.common.block.entity.HTBurningBoxBlockEntity
import hiiragi283.ragium.common.block.entity.HTWaterCollectorBlockEntity
import hiiragi283.ragium.common.recipe.HTMachineTier
import hiiragi283.ragium.common.registry.HTBlockRegister
import hiiragi283.ragium.datagen.RagiumModelProvider
import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.block.ExperienceDroppingBlock
import net.minecraft.data.client.*
import net.minecraft.registry.tag.BlockTags
import net.minecraft.state.property.IntProperty
import net.minecraft.util.math.intprovider.ConstantIntProvider

object RagiumBlocks {
    @JvmField
    val REGISTER: HTBlockRegister = HTBlockRegister(Ragium.MOD_ID)

    @JvmField
    val RAGINITE_ORE: Block =
        REGISTER.register(
            "raginite_ore",
            ExperienceDroppingBlock(
                ConstantIntProvider.create(0),
                AbstractBlock.Settings.copy(Blocks.IRON_ORE),
            ),
        ) {
            putEnglishLang("Raginite Ore")
            putJapaneseLang("ラギナイト鉱石")
            setCustomLootTable()
            registerTags(BlockTags.PICKAXE_MINEABLE)
        }

    @JvmField
    val DEEPSLATE_RAGINITE_ORE: Block =
        REGISTER.register(
            "deepslate_raginite_ore",
            ExperienceDroppingBlock(
                ConstantIntProvider.create(0),
                AbstractBlock.Settings.copy(Blocks.DEEPSLATE_IRON_ORE),
            ),
        ) {
            putEnglishLang("Deep Raginite Ore")
            putJapaneseLang("深層ラギナイト鉱石")
            setCustomLootTable()
            registerTags(BlockTags.PICKAXE_MINEABLE)
        }

    @JvmField
    val CREATIVE_SOURCE: Block =
        REGISTER.register("creative_source", HTCreativePowerSourceBlock) {
            putEnglishLang("Creative Power Source")
            putJapaneseLang("クリエイティブ用エネルギー源")
            generateState {
                it.blockStateCollector.accept(
                    VariantsBlockStateSupplier
                        .create(block, buildModelVariant(Ragium.MOD_ID, name))
                        .coordinate(
                            BlockStateVariantMap.create(HTMachineTier.PROPERTY),
                        ),
                )
            }
            setCustomBlockState()
        }

    // tier1
    @JvmField
    val RAGI_ALLOY_BLOCK: Block =
        REGISTER.registerCopy("ragi_alloy_block", Blocks.IRON_BLOCK) {
            putEnglishLang("Block of Ragi-Alloy")
            putJapaneseLang("ラギ合金ブロック")
            registerTags(BlockTags.PICKAXE_MINEABLE)
        }

    @JvmField
    val RAGI_ALLOY_HULL: Block =
        REGISTER.registerCopy("ragi_alloy_hull", Blocks.BRICKS) {
            putEnglishLang("Ragi-Alloy Hull")
            putJapaneseLang("ラギ合金筐体")
            generateState { it.registerSingleton(block, RagiumModelProvider.HULL_TEXTURE_FACTORY) }
            registerTags(BlockTags.PICKAXE_MINEABLE)
        }

    @JvmField
    val MANUAL_GRINDER: Block =
        REGISTER.register("manual_grinder", HTManualGrinderBlock) {
            putEnglishLang("Manual Grinder")
            putJapaneseLang("石臼")
            generateState {
                it.blockStateCollector.accept(
                    buildMultipartState(block) {
                        with(buildModelVariant("block/smooth_stone_slab"))
                        Properties.LEVEL_7.values.forEach { level: Int ->
                            with(
                                buildWhen(Properties.LEVEL_7, level),
                                buildStateVariant {
                                    model(
                                        Ragium.id(
                                            when (level % 2 == 0) {
                                                true -> "block/manual_grinder"
                                                else -> "block/manual_grinder_diagonal"
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
        REGISTER.register(
            "water_collector",
            HTBlockWithEntity
                .Builder<HTWaterCollectorBlockEntity>()
                .type(RagiumBlockEntityTypes.WATER_COLLECTOR)
                .ticker(HTWaterCollectorBlockEntity.TICKER)
                .build(),
        ) {
            putEnglishLang("Water Collector")
            putJapaneseLang("水収集器")
            registerTags(BlockTags.PICKAXE_MINEABLE)
        }

    @JvmField
    val BURNING_BOX: Block =
        REGISTER.register(
            "burning_box",
            HTBlockWithEntity
                .Builder<HTBurningBoxBlockEntity>()
                .type(RagiumBlockEntityTypes.BURNING_BOX)
                .ticker(HTBurningBoxBlockEntity.TICKER)
                .build(),
        ) {
            putEnglishLang("Burning Box")
            putJapaneseLang("燃焼室")
            generateState {
                it.registerSingleton(
                    block,
                    TexturedModel.makeFactory(
                        { block: Block ->
                            TextureMap()
                                .put(TextureKey.TOP, TextureMap.getId(Blocks.BRICKS))
                                .put(TextureKey.BOTTOM, TextureMap.getId(Blocks.BRICKS))
                                .put(TextureKey.FRONT, TextureMap.getSubId(block, "_front"))
                        },
                        RagiumModels.MACHINE,
                    ),
                )
            }
            registerTags(BlockTags.PICKAXE_MINEABLE)
        }

    // tier2
    @JvmField
    val RAGI_STEEL_BLOCK: Block =
        REGISTER.registerCopy("ragi_steel_block", Blocks.IRON_BLOCK) {
            putEnglishLang("Block of Ragi-Steel")
            putJapaneseLang("ラギスチールブロック")
            registerTags(BlockTags.PICKAXE_MINEABLE)
        }

    @JvmField
    val RAGI_STEEL_HULL: Block =
        REGISTER.registerCopy("ragi_steel_hull", Blocks.DEEPSLATE_TILES) {
            putEnglishLang("Ragi-Steel Hull")
            putJapaneseLang("ラギスチール筐体")
            generateState { it.registerSingleton(block, RagiumModelProvider.HULL_TEXTURE_FACTORY) }
            registerTags(BlockTags.PICKAXE_MINEABLE)
        }

    // tier3
    @JvmField
    val REFINED_RAGI_STEEL_BLOCK: Block =
        REGISTER.registerCopy("refined_ragi_steel_block", Blocks.IRON_BLOCK) {
            putEnglishLang("Block of Refined Ragi-Steel")
            putJapaneseLang("精製ラギスチールブロック")
            registerTags(BlockTags.PICKAXE_MINEABLE)
        }

    @JvmField
    val REFINED_RAGI_STEEL_HULL: Block =
        REGISTER.registerCopy("refined_ragi_steel_hull", Blocks.CHISELED_QUARTZ_BLOCK) {
            putEnglishLang("Refined Ragi-Steel Hull")
            putJapaneseLang("精製ラギスチール筐体")
            generateState { it.registerSingleton(block, RagiumModelProvider.HULL_TEXTURE_FACTORY) }
            registerTags(BlockTags.PICKAXE_MINEABLE)
        }

    // tier4
    // tier5

    init {
        RagiumBlockEntityTypes.MANUAL_GRINDER.addSupportedBlock(MANUAL_GRINDER)
        RagiumBlockEntityTypes.WATER_COLLECTOR.addSupportedBlock(WATER_COLLECTOR)
        RagiumBlockEntityTypes.BURNING_BOX.addSupportedBlock(BURNING_BOX)

        // RagiumBlocks.getFilteredInstances<Block>().forEach(::registerItem)
    }

    /*private fun <T : Block> register(name: String, block: T): T =
        Registry.register(Registries.BLOCK, Ragium.id(name), block)

    private fun register(name: String, settings: AbstractBlock.Settings = AbstractBlock.Settings.create()): Block =
        register(name, Block(settings))

    private fun registerItem(block: Block, builder: Item.Settings.() -> Unit = {}): Item =
        Registries.BLOCK.getKey(block).map {
            Registry.register(Registries.ITEM, it.value, BlockItem(block, Item.Settings().apply(builder)))
        }.orElseThrow()*/

    //    Properties    //

    object Properties {
        @JvmField
        val LEVEL_7: IntProperty = IntProperty.of("level", 0, 7)
    }
}
