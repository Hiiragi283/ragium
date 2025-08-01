package hiiragi283.ragium.setup

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.HTEntityBlock
import hiiragi283.ragium.api.block.HTFacingEntityBlock
import hiiragi283.ragium.api.block.HTHorizontalEntityBlock
import hiiragi283.ragium.api.extension.blockTagKey
import hiiragi283.ragium.api.extension.commonId
import hiiragi283.ragium.api.registry.HTBlockHolderLike
import hiiragi283.ragium.api.registry.HTBlockRegister
import hiiragi283.ragium.api.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.api.registry.HTItemRegister
import hiiragi283.ragium.api.registry.HTTaggedHolder
import hiiragi283.ragium.api.util.RagiumConst
import hiiragi283.ragium.common.block.HTCrimsonSoilBlock
import hiiragi283.ragium.common.block.HTExpBerriesBushBlock
import hiiragi283.ragium.common.block.HTMilkDrainBlock
import hiiragi283.ragium.common.block.HTSiltBlock
import hiiragi283.ragium.common.block.HTSoulGlassBlock
import hiiragi283.ragium.common.block.HTSweetBerriesCakeBlock
import hiiragi283.ragium.common.block.HTWarpedWartBlock
import hiiragi283.ragium.common.block.entity.HTBlockEntity
import hiiragi283.ragium.util.HTBuildingBlockSets
import hiiragi283.ragium.util.HTOreVariants
import net.minecraft.tags.TagKey
import net.minecraft.world.item.DyeColor
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.RotatedPillarBlock
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.TransparentBlock
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.material.MapColor
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredBlock

object RagiumBlocks {
    @JvmField
    val REGISTER = HTBlockRegister(RagiumAPI.MOD_ID)

    @JvmField
    val ITEM_REGISTER = HTItemRegister(RagiumAPI.MOD_ID)

    @JvmStatic
    fun init(eventBus: IEventBus) {
        REGISTER.addAlias(RagiumAPI.id("item_collector"), RagiumAPI.id("item_buffer"))

        RaginiteOres.entries
        RagiCrystalOres.entries
        StorageBlocks.entries

        Glasses.entries
        LEDBlocks.entries

        Casings.entries
        Devices.entries

        Dynamos.entries
        Frames.entries
        Machines.entries

        Drums.entries

        for (sets: HTBuildingBlockSets in DECORATIONS) {
            sets.init(eventBus)
        }

        REGISTER.register(eventBus)
        ITEM_REGISTER.register(eventBus)
    }

    @JvmStatic
    private fun register(name: String, properties: BlockBehaviour.Properties): DeferredBlock<Block> {
        val holder: DeferredBlock<Block> = REGISTER.registerSimpleBlock(name, properties)
        ITEM_REGISTER.registerSimpleBlockItem(holder)
        return holder
    }

    @JvmStatic
    private fun <T : Block> register(
        name: String,
        properties: BlockBehaviour.Properties,
        factory: (BlockBehaviour.Properties) -> T,
    ): DeferredBlock<T> {
        val holder: DeferredBlock<T> = REGISTER.registerBlock(name, factory, properties)
        ITEM_REGISTER.registerSimpleBlockItem(holder)
        return holder
    }

    @JvmStatic
    private fun <T : HTEntityBlock<*>> registerEntity(
        type: HTDeferredBlockEntityType<out HTBlockEntity>,
        properties: BlockBehaviour.Properties,
        factory: (BlockBehaviour.Properties) -> T,
    ): DeferredBlock<Block> = register(type.id.path, properties, factory)

    @JvmStatic
    fun machineProperty(): BlockBehaviour.Properties = BlockBehaviour.Properties
        .of()
        .mapColor(MapColor.COLOR_BLACK)
        .requiresCorrectToolForDrops()
        .sound(SoundType.COPPER)
        .strength(3.5f, 16f)

    @JvmStatic
    private fun copyOf(block: Block): BlockBehaviour.Properties = BlockBehaviour.Properties.ofFullCopy(block)

    @JvmStatic
    private fun glass(): BlockBehaviour.Properties = copyOf(Blocks.GLASS)

    //    Natural Resources    //

    @JvmField
    val SILT: DeferredBlock<Block> = register("silt", copyOf(Blocks.SAND), ::HTSiltBlock)

    @JvmField
    val CRIMSON_SOIL: DeferredBlock<Block> =
        register("crimson_soil", copyOf(Blocks.SOUL_SOIL), ::HTCrimsonSoilBlock)

    @JvmField
    val ASH_LOG: DeferredBlock<RotatedPillarBlock> = register(
        "ash_log",
        BlockBehaviour.Properties
            .of()
            .mapColor(MapColor.COLOR_GRAY)
            .strength(1f)
            .sound(SoundType.SAND),
        ::RotatedPillarBlock,
    )

    @JvmField
    val EXP_BERRY_BUSH: DeferredBlock<Block> = REGISTER.registerBlock(
        "exp_berry_bush",
        ::HTExpBerriesBushBlock,
        copyOf(Blocks.SWEET_BERRY_BUSH),
    )

    @JvmField
    val WARPED_WART: DeferredBlock<Block> = REGISTER.registerBlock(
        "warped_wart",
        ::HTWarpedWartBlock,
        copyOf(Blocks.NETHER_WART),
    )

    @JvmField
    val RESONANT_DEBRIS: DeferredBlock<Block> =
        register("resonant_debris", copyOf(Blocks.ANCIENT_DEBRIS))

    @JvmField
    val MYSTERIOUS_OBSIDIAN: DeferredBlock<Block> = register("mysterious_obsidian", copyOf(Blocks.OBSIDIAN))

    // val ELDRITCH_PORTAL: DeferredBlock<Block> = REGISTER.registerBlock("eldritch_portal", ::HTEldritchPortalBlock, copyOf(Blocks.END_GATEWAY))

    //    Materials    //

    enum class RaginiteOres(override val variant: HTOreVariants) : HTOreVariants.HolderLike {
        STONE(HTOreVariants.STONE),
        DEEP(HTOreVariants.DEEP),
        NETHER(HTOreVariants.NETHER),
        END(HTOreVariants.END),
        ;

        override val holder: DeferredBlock<*> = register(
            variant.pattern.replace("%s", RagiumConst.RAGINITE),
            variant.createProperties(),
        )
    }

    enum class RagiCrystalOres(override val variant: HTOreVariants) : HTOreVariants.HolderLike {
        STONE(HTOreVariants.STONE),
        DEEP(HTOreVariants.DEEP),
        NETHER(HTOreVariants.NETHER),
        END(HTOreVariants.END),
        ;

        override val holder: DeferredBlock<*> = register(
            variant.pattern.replace("%s", RagiumConst.RAGI_CRYSTAL),
            variant.createProperties(),
        )
    }

    enum class StorageBlocks(properties: BlockBehaviour.Properties) :
        HTBlockHolderLike,
        HTTaggedHolder<Block> {
        // Gems
        RAGI_CRYSTAL(copyOf(Blocks.AMETHYST_BLOCK).mapColor(MapColor.COLOR_PINK)),
        CRIMSON_CRYSTAL(copyOf(Blocks.AMETHYST_BLOCK).mapColor(MapColor.CRIMSON_STEM)),
        WARPED_CRYSTAL(copyOf(Blocks.AMETHYST_BLOCK).mapColor(MapColor.WARPED_STEM)),
        ELDRITCH_PEARL(copyOf(Blocks.SHROOMLIGHT).mapColor(MapColor.COLOR_PURPLE)),

        // Ingots
        RAGI_ALLOY(copyOf(Blocks.COPPER_BLOCK).mapColor(MapColor.COLOR_RED)),
        ADVANCED_RAGI_ALLOY(copyOf(Blocks.IRON_BLOCK).mapColor(MapColor.COLOR_ORANGE)),
        AZURE_STEEL(copyOf(Blocks.IRON_BLOCK).mapColor(MapColor.TERRACOTTA_BLUE)),
        DEEP_STEEL(copyOf(Blocks.NETHERITE_BLOCK).mapColor(MapColor.COLOR_CYAN)),

        // Others
        CHOCOLATE(copyOf(Blocks.MUD).mapColor(MapColor.TERRACOTTA_BROWN)),
        MEAT(copyOf(Blocks.MUD).sound(SoundType.HONEY_BLOCK)),
        COOKED_MEAT(copyOf(Blocks.PACKED_MUD).sound(SoundType.HONEY_BLOCK)),
        ;

        override val holder: DeferredBlock<*> = register("${name.lowercase()}_block", properties)
        override val tagKey: TagKey<Block> = blockTagKey(commonId(RagiumConst.STORAGE_BLOCKS, name.lowercase()))
    }

    //    Buildings    //

    @JvmStatic
    private val RAGI_STONE_PROPERTY: BlockBehaviour.Properties = copyOf(Blocks.STONE).mapColor(MapColor.COLOR_RED)

    @JvmField
    val RAGI_STONE_SETS = HTBuildingBlockSets("ragi_stone", RAGI_STONE_PROPERTY)

    @JvmField
    val RAGI_STONE_BRICKS_SETS = HTBuildingBlockSets("ragi_stone_bricks", RAGI_STONE_PROPERTY, prefix = "ragi_stone_brick")

    @JvmField
    val RAGI_STONE_SQUARE_SETS = HTBuildingBlockSets("ragi_stone_square", RAGI_STONE_PROPERTY)

    @JvmField
    val AZURE_TILE_SETS = HTBuildingBlockSets(
        "azure_tiles",
        copyOf(Blocks.STONE).mapColor(MapColor.TERRACOTTA_BLUE),
        prefix = "azure_tile",
    )

    @JvmField
    val EMBER_STONE_SETS = HTBuildingBlockSets("ember_stone", copyOf(Blocks.AMETHYST_BLOCK).mapColor(MapColor.COLOR_ORANGE))

    @JvmField
    val PLASTIC_SETS = HTBuildingBlockSets("plastic_block", copyOf(Blocks.COPPER_BLOCK).mapColor(DyeColor.WHITE))

    @JvmField
    val BLUE_NETHER_BRICK_SETS = HTBuildingBlockSets(
        "blue_nether_bricks",
        copyOf(Blocks.NETHER_BRICKS).mapColor(MapColor.COLOR_BLUE),
        prefix = "blue_nether_brick",
    )

    @JvmField
    val SPONGE_CAKE_SETS = HTBuildingBlockSets("sponge_cake", copyOf(Blocks.YELLOW_WOOL))

    @JvmField
    val DECORATIONS: List<HTBuildingBlockSets> = listOf(
        RAGI_STONE_SETS,
        RAGI_STONE_BRICKS_SETS,
        RAGI_STONE_SQUARE_SETS,
        AZURE_TILE_SETS,
        EMBER_STONE_SETS,
        PLASTIC_SETS,
        BLUE_NETHER_BRICK_SETS,
        SPONGE_CAKE_SETS,
    )

    enum class Glasses(factory: (BlockBehaviour.Properties) -> Block, blastProof: Boolean) :
        HTBlockHolderLike,
        HTTaggedHolder<Block> {
        QUARTZ(::TransparentBlock, false),
        SOUL(::HTSoulGlassBlock, false),
        OBSIDIAN(::TransparentBlock, true),
        ;

        override val holder: DeferredBlock<*> = register(
            "${name.lowercase()}_glass",
            glass().apply { if (blastProof) strength(5f, 1200f) },
            factory,
        )
        override val tagKey: TagKey<Block> = blockTagKey(commonId(RagiumConst.GLASS_BLOCKS, name.lowercase()))
    }

    enum class LEDBlocks(val color: DyeColor) : HTBlockHolderLike {
        RED(DyeColor.RED),
        GREEN(DyeColor.GREEN),
        BLUE(DyeColor.BLUE),
        CYAN(DyeColor.CYAN),
        MAGENTA(DyeColor.MAGENTA),
        YELLOW(DyeColor.YELLOW),
        WHITE(DyeColor.WHITE),
        ;

        override val holder: DeferredBlock<Block> = register(
            "${color.serializedName}_led_block",
            glass().mapColor(color).lightLevel { 15 },
        )
    }

    //    Foods    //

    @JvmField
    val SWEET_BERRIES_CAKE: DeferredBlock<Block> = register(
        "sweet_berries_cake",
        copyOf(Blocks.YELLOW_WOOL).forceSolidOn(),
        ::HTSweetBerriesCakeBlock,
    )

    //    Dynamos    //

    enum class Dynamos(type: HTDeferredBlockEntityType<out HTBlockEntity>) : HTBlockHolderLike {
        STIRLING(RagiumBlockEntityTypes.STIRLING_DYNAMO),
        ;

        override val holder: DeferredBlock<*> =
            registerEntity(type, machineProperty(), HTFacingEntityBlock.create(type))
    }

    //    Machines    //

    enum class Frames(properties: BlockBehaviour.Properties) : HTBlockHolderLike {
        BASIC(copyOf(Blocks.IRON_BLOCK)),
        ADVANCED(copyOf(Blocks.IRON_BLOCK)),
        ELITE(machineProperty()),
        ;

        override val holder: DeferredBlock<*> =
            register("${name.lowercase()}_machine_frame", properties.noCollission(), ::TransparentBlock)
    }

    enum class Machines(type: HTDeferredBlockEntityType<out HTBlockEntity>) : HTBlockHolderLike {
        // Basic
        CRUSHER(RagiumBlockEntityTypes.CRUSHER),
        BLOCK_BREAKER(RagiumBlockEntityTypes.BLOCK_BREAKER),
        EXTRACTOR(RagiumBlockEntityTypes.EXTRACTOR),
        FORMING_PRESS(RagiumBlockEntityTypes.FORMING_PRESS),

        // Advanced
        ALLOY_SMELTER(RagiumBlockEntityTypes.ALLOY_SMELTER),
        MELTER(RagiumBlockEntityTypes.MELTER),
        REFINERY(RagiumBlockEntityTypes.REFINERY),
        SOLIDIFIER(RagiumBlockEntityTypes.SOLIDIFIER),

        // Elite
        INFUSER(RagiumBlockEntityTypes.INFUSER),
        ;

        override val holder: DeferredBlock<*> =
            registerEntity(type, machineProperty(), HTHorizontalEntityBlock.create(type))
    }

    //    Devices    //

    enum class Casings(properties: BlockBehaviour.Properties) : HTBlockHolderLike {
        WOODEN(copyOf(Blocks.NOTE_BLOCK)),
        STONE(copyOf(Blocks.COBBLED_DEEPSLATE)),
        DEVICE(machineProperty()),
        ;

        override val holder: DeferredBlock<*> = register("${name.lowercase()}_casing", properties)
    }

    enum class Devices(supplier: () -> DeferredBlock<*>) : HTBlockHolderLike {
        MILK_DRAIN({ register("milk_drain", copyOf(Blocks.COBBLESTONE), ::HTMilkDrainBlock) }),

        // Basic
        ITEM_BUFFER(RagiumBlockEntityTypes.ITEM_BUFFER),
        SPRINKLER(RagiumBlockEntityTypes.SPRINKLER),
        WATER_COLLECTOR(RagiumBlockEntityTypes.WATER_COLLECTOR),

        // Advanced
        ENI(RagiumBlockEntityTypes.ENI),
        EXP_COLLECTOR(RagiumBlockEntityTypes.EXP_COLLECTOR),
        LAVA_COLLECTOR(RagiumBlockEntityTypes.LAVA_COLLECTOR),
        TELEPORT_ANCHOR({ register("teleport_anchor", machineProperty()) }),

        // Creative
        CEU(RagiumBlockEntityTypes.CEU),
        ;

        constructor(type: HTDeferredBlockEntityType<out HTBlockEntity>) : this({
            registerEntity(type, machineProperty(), HTEntityBlock.create(type))
        })

        override val holder: DeferredBlock<*> = supplier()
    }

    //    Storages    //

    enum class Drums(base: Block, type: HTDeferredBlockEntityType<out HTBlockEntity>) : HTBlockHolderLike {
        SMALL(Blocks.IRON_BLOCK, RagiumBlockEntityTypes.SMALL_DRUM),
        MEDIUM(Blocks.GOLD_BLOCK, RagiumBlockEntityTypes.MEDIUM_DRUM),
        LARGE(Blocks.DIAMOND_BLOCK, RagiumBlockEntityTypes.LARGE_DRUM),
        HUGE(Blocks.NETHERITE_BLOCK, RagiumBlockEntityTypes.HUGE_DRUM),
        ;

        override val holder: DeferredBlock<*> = registerEntity(type, copyOf(base), HTEntityBlock.create(type))
    }
}
