package hiiragi283.ragium.setup

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.registry.HTBlockRegister
import hiiragi283.ragium.api.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.api.registry.HTItemRegister
import hiiragi283.ragium.api.util.RagiumConstantValues
import hiiragi283.ragium.common.block.HTCrimsonSoilBlock
import hiiragi283.ragium.common.block.HTEntityBlock
import hiiragi283.ragium.common.block.HTExpBerriesBushBlock
import hiiragi283.ragium.common.block.HTHorizontalEntityBlock
import hiiragi283.ragium.common.block.HTMeatBlock
import hiiragi283.ragium.common.block.HTMilkDrainBlock
import hiiragi283.ragium.common.block.HTSoulGlassBlock
import hiiragi283.ragium.common.block.HTSpongeCakeBlock
import hiiragi283.ragium.common.block.HTSpongeCakeSlabBlock
import hiiragi283.ragium.common.block.HTSweetBerriesCakeBlock
import hiiragi283.ragium.common.block.entity.HTBlockEntity
import hiiragi283.ragium.common.util.HTBuildingBlockSets
import hiiragi283.ragium.common.util.HTOreSets
import net.minecraft.util.ColorRGBA
import net.minecraft.world.item.DyeColor
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.ColoredFallingBlock
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
        REGISTER.register(eventBus)
        ITEM_REGISTER.register(eventBus)

        RAGINITE_ORES.init(eventBus)
        RAGI_CRYSTAL_ORES.init(eventBus)

        for (sets: HTBuildingBlockSets in DECORATIONS) {
            sets.init(eventBus)
        }
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
    private fun wooden(): BlockBehaviour.Properties = BlockBehaviour.Properties
        .of()
        .mapColor(MapColor.WOOD)
        .requiresCorrectToolForDrops()
        .strength(0.8f)
        .sound(SoundType.WOOD)
        .ignitedByLava()

    @JvmStatic
    private fun stone(): BlockBehaviour.Properties = BlockBehaviour.Properties
        .of()
        .requiresCorrectToolForDrops()
        .strength(2f)
        .sound(SoundType.DEEPSLATE_BRICKS)

    @JvmStatic
    private fun lightMetal(): BlockBehaviour.Properties = BlockBehaviour.Properties
        .of()
        .requiresCorrectToolForDrops()
        .strength(3f)
        .sound(SoundType.COPPER)

    @JvmStatic
    private fun heavyMetal(): BlockBehaviour.Properties = BlockBehaviour.Properties
        .of()
        .requiresCorrectToolForDrops()
        .strength(6f)
        .sound(SoundType.METAL)

    @JvmStatic
    private fun crystal(): BlockBehaviour.Properties = BlockBehaviour.Properties
        .of()
        .requiresCorrectToolForDrops()
        .strength(2f)
        .sound(SoundType.AMETHYST)

    @JvmStatic
    private fun copyOf(block: Block): BlockBehaviour.Properties = BlockBehaviour.Properties.ofFullCopy(block)

    @JvmStatic
    private fun glass(): BlockBehaviour.Properties = copyOf(Blocks.GLASS)

    @JvmStatic
    private fun soft(): BlockBehaviour.Properties = BlockBehaviour.Properties
        .of()
        .strength(0.5f)
        .sound(SoundType.WOOL)

    //    Natural Resources    //

    @JvmField
    val SILT: DeferredBlock<ColoredFallingBlock> = register(
        "silt",
        copyOf(Blocks.SAND),
    ) { prop: BlockBehaviour.Properties -> ColoredFallingBlock(ColorRGBA(0xccccff), prop) }

    @JvmField
    val MYSTERIOUS_OBSIDIAN: DeferredBlock<Block> = register("mysterious_obsidian", copyOf(Blocks.OBSIDIAN))

    @JvmField
    val CRIMSON_SOIL: DeferredBlock<HTCrimsonSoilBlock> =
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
    val EXP_BERRY_BUSH: DeferredBlock<HTExpBerriesBushBlock> = REGISTER.registerBlock(
        "exp_berry_bush",
        ::HTExpBerriesBushBlock,
        copyOf(Blocks.SWEET_BERRY_BUSH),
    )

    @JvmField
    val RAGINITE_ORES = HTOreSets(RagiumConstantValues.RAGINITE)

    @JvmField
    val RAGI_CRYSTAL_ORES = HTOreSets(RagiumConstantValues.RAGI_CRYSTAL)

    @JvmField
    val RESONANT_DEBRIS: DeferredBlock<Block> =
        register("resonant_debris", copyOf(Blocks.ANCIENT_DEBRIS))

    //    Materials    //

    // Gems
    @JvmField
    val RAGI_CRYSTAL_BLOCK: DeferredBlock<Block> =
        register("${RagiumConstantValues.RAGI_CRYSTAL}_block", crystal().mapColor(MapColor.COLOR_PINK))

    @JvmField
    val CRIMSON_CRYSTAL_BLOCK: DeferredBlock<Block> =
        register("${RagiumConstantValues.CRIMSON_CRYSTAL}_block", crystal().mapColor(MapColor.CRIMSON_STEM))

    @JvmField
    val WARPED_CRYSTAL_BLOCK: DeferredBlock<Block> =
        register("${RagiumConstantValues.WARPED_CRYSTAL}_block", crystal().mapColor(MapColor.WARPED_STEM))

    @JvmField
    val ELDRITCH_PEARL_BLOCK: DeferredBlock<Block> = register(
        "${RagiumConstantValues.ELDRITCH_PEARL}_block",
        wooden().sound(SoundType.SHROOMLIGHT).mapColor(MapColor.COLOR_PURPLE),
    )

    // Ingots
    @JvmField
    val RAGI_ALLOY_BLOCK: DeferredBlock<Block> =
        register("${RagiumConstantValues.RAGI_ALLOY}_block", lightMetal().mapColor(MapColor.COLOR_RED))

    @JvmField
    val ADVANCED_RAGI_ALLOY_BLOCK: DeferredBlock<Block> =
        register("${RagiumConstantValues.ADVANCED_RAGI_ALLOY}_block", heavyMetal().mapColor(MapColor.COLOR_ORANGE))

    @JvmField
    val AZURE_STEEL_BLOCK: DeferredBlock<Block> =
        register("${RagiumConstantValues.AZURE_STEEL}_block", heavyMetal().mapColor(MapColor.TERRACOTTA_BLUE))

    @JvmField
    val DEEP_STEEL_BLOCK: DeferredBlock<Block> =
        register("${RagiumConstantValues.DEEP_STEEL}_block", heavyMetal().mapColor(MapColor.COLOR_CYAN))

    // Others
    @JvmField
    val CHOCOLATE_BLOCK: DeferredBlock<Block> =
        register("${RagiumConstantValues.CHOCOLATE}_block", wooden().mapColor(MapColor.TERRACOTTA_BROWN))

    @JvmField
    val MEAT_BLOCK: DeferredBlock<Block> =
        register("${RagiumConstantValues.MEAT}_block", copyOf(Blocks.MUD).sound(SoundType.HONEY_BLOCK))

    @JvmField
    val COOKED_MEAT_BLOCK: DeferredBlock<Block> =
        register("${RagiumConstantValues.COOKED_MEAT}_block", copyOf(Blocks.PACKED_MUD).sound(SoundType.HONEY_BLOCK))

    @JvmField
    val STORAGE_BLOCKS: List<DeferredBlock<Block>> = listOf(
        // Gems
        RAGI_CRYSTAL_BLOCK,
        CRIMSON_CRYSTAL_BLOCK,
        WARPED_CRYSTAL_BLOCK,
        ELDRITCH_PEARL_BLOCK,
        // Ingots
        RAGI_ALLOY_BLOCK,
        ADVANCED_RAGI_ALLOY_BLOCK,
        AZURE_STEEL_BLOCK,
        DEEP_STEEL_BLOCK,
        // Others
        CHOCOLATE_BLOCK,
        MEAT_BLOCK,
        COOKED_MEAT_BLOCK,
    )

    //    Buildings    //

    @JvmField
    val RAGI_STONE_SETS = HTBuildingBlockSets("ragi_stone", stone().mapColor(MapColor.COLOR_RED))

    @JvmField
    val RAGI_STONE_BRICKS_SETS = HTBuildingBlockSets(
        "ragi_stone_bricks",
        stone().mapColor(MapColor.COLOR_RED),
        prefix = "ragi_stone_brick",
    )

    @JvmField
    val RAGI_STONE_SQUARE_SETS = HTBuildingBlockSets("ragi_stone_square", stone().mapColor(MapColor.COLOR_RED))

    @JvmField
    val AZURE_TILE_SETS = HTBuildingBlockSets(
        "azure_tiles",
        stone().mapColor(MapColor.TERRACOTTA_BLUE),
        prefix = "azure_tile",
    )

    @JvmField
    val EMBER_STONE_SETS = HTBuildingBlockSets(
        "ember_stone",
        crystal().mapColor(MapColor.COLOR_ORANGE),
    )

    @JvmField
    val PLASTIC_SETS = HTBuildingBlockSets(
        "plastic_block",
        stone().sound(SoundType.COPPER),
    )

    @JvmField
    val BLUE_NETHER_BRICK_SETS = HTBuildingBlockSets(
        "blue_nether_bricks",
        stone().mapColor(MapColor.COLOR_BLUE).sound(SoundType.NETHER_BRICKS),
        prefix = "blue_nether_brick",
    )

    @JvmField
    val DECORATIONS: List<HTBuildingBlockSets> = listOf(
        RAGI_STONE_SETS,
        RAGI_STONE_BRICKS_SETS,
        RAGI_STONE_SQUARE_SETS,
        AZURE_TILE_SETS,
        EMBER_STONE_SETS,
        PLASTIC_SETS,
        BLUE_NETHER_BRICK_SETS,
    )

    @JvmField
    val QUARTZ_GLASS: DeferredBlock<TransparentBlock> =
        register("quartz_glass", glass(), ::TransparentBlock)

    @JvmField
    val OBSIDIAN_GLASS: DeferredBlock<TransparentBlock> =
        register("obsidian_glass", glass().strength(5f, 1200f), ::TransparentBlock)

    @JvmField
    val SOUL_GLASS: DeferredBlock<HTSoulGlassBlock> =
        register("soul_glass", glass(), ::HTSoulGlassBlock)

    @JvmField
    val GLASSES: List<DeferredBlock<*>> = listOf(
        QUARTZ_GLASS,
        OBSIDIAN_GLASS,
        SOUL_GLASS,
    )

    @JvmField
    val LED_BLOCKS: Map<DyeColor, DeferredBlock<*>> = listOf(
        DyeColor.RED,
        DyeColor.GREEN,
        DyeColor.BLUE,
        DyeColor.CYAN,
        DyeColor.MAGENTA,
        DyeColor.YELLOW,
        DyeColor.WHITE,
    ).associateWith { color: DyeColor ->
        register(
            "${color.serializedName}_led_block",
            glass().mapColor(color).lightLevel { 15 },
        )
    }

    @JvmStatic
    fun getLedBlock(color: DyeColor): DeferredBlock<*> = LED_BLOCKS[color] ?: error("Unregistered color: ${color.serializedName}")

    //    Foods    //

    @JvmField
    val COOKED_MEAT_ON_THE_BONE: DeferredBlock<HTMeatBlock> = register(
        "cooked_meat_on_the_bone",
        copyOf(Blocks.PACKED_MUD),
        ::HTMeatBlock,
    )

    @JvmField
    val SPONGE_CAKE: DeferredBlock<HTSpongeCakeBlock> = register(
        "sponge_cake",
        soft().mapColor(MapColor.COLOR_YELLOW),
        ::HTSpongeCakeBlock,
    )

    @JvmField
    val SPONGE_CAKE_SLAB: DeferredBlock<HTSpongeCakeSlabBlock> = register(
        "sponge_cake_slab",
        soft().mapColor(MapColor.COLOR_YELLOW),
        ::HTSpongeCakeSlabBlock,
    )

    @JvmField
    val SWEET_BERRIES_CAKE: DeferredBlock<HTSweetBerriesCakeBlock> = register(
        "sweet_berries_cake",
        soft().forceSolidOn(),
        ::HTSweetBerriesCakeBlock,
    )

    //    Casings    //

    @JvmField
    val WOODEN_CASING: DeferredBlock<Block> = register("wooden_casing", wooden())

    @JvmField
    val STONE_CASING: DeferredBlock<Block> = register("stone_casing", stone())

    @JvmField
    val BASIC_MACHINE_FRAME: DeferredBlock<Block> = register(
        "basic_machine_frame",
        lightMetal(),
    )

    @JvmField
    val ADVANCED_MACHINE_FRAME: DeferredBlock<Block> = register(
        "advanced_machine_frame",
        heavyMetal(),
    )

    @JvmField
    val DEVICE_CASING: DeferredBlock<Block> = register(
        "device_casing",
        heavyMetal(),
    )

    @JvmField
    val CASINGS: List<DeferredBlock<*>> = listOf(
        WOODEN_CASING,
        STONE_CASING,
        BASIC_MACHINE_FRAME,
        ADVANCED_MACHINE_FRAME,
        DEVICE_CASING,
    )

    //    Machines    //

    @JvmStatic
    private fun registerMachine(
        properties: BlockBehaviour.Properties,
        type: HTDeferredBlockEntityType<out HTBlockEntity>,
    ): DeferredBlock<*> = register(type.id.path, properties, HTHorizontalEntityBlock.create(type))

    // Basic
    @JvmField
    val CRUSHER: DeferredBlock<*> = registerMachine(lightMetal(), RagiumBlockEntityTypes.CRUSHER)

    @JvmField
    val BLOCK_BREAKER: DeferredBlock<*> = registerMachine(lightMetal(), RagiumBlockEntityTypes.BLOCK_BREAKER)

    @JvmField
    val EXTRACTOR: DeferredBlock<*> = registerMachine(lightMetal(), RagiumBlockEntityTypes.EXTRACTOR)

    // Advanced
    @JvmField
    val ALLOY_SMELTER: DeferredBlock<*> = registerMachine(lightMetal(), RagiumBlockEntityTypes.ALLOY_SMELTER)

    @JvmField
    val FORMING_PRESS: DeferredBlock<*> = registerMachine(heavyMetal(), RagiumBlockEntityTypes.FORMING_PRESS)

    @JvmField
    val MELTER: DeferredBlock<*> = registerMachine(heavyMetal(), RagiumBlockEntityTypes.MELTER)

    @JvmField
    val REFINERY: DeferredBlock<*> = registerMachine(heavyMetal(), RagiumBlockEntityTypes.REFINERY)

    @JvmField
    val SOLIDIFIER: DeferredBlock<*> = registerMachine(heavyMetal(), RagiumBlockEntityTypes.SOLIDIFIER)

    @JvmField
    val MACHINES: List<DeferredBlock<*>> = listOf(
        // Basic
        CRUSHER,
        BLOCK_BREAKER,
        EXTRACTOR,
        // Advanced
        ALLOY_SMELTER,
        FORMING_PRESS,
        MELTER,
        REFINERY,
        SOLIDIFIER,
    )

    //    Devices    //

    @JvmField
    val MILK_DRAIN: DeferredBlock<*> = register("milk_drain", stone(), ::HTMilkDrainBlock)

    // Basic
    @JvmStatic
    private fun registerEntityBlock(
        properties: BlockBehaviour.Properties,
        type: HTDeferredBlockEntityType<out HTBlockEntity>,
    ): DeferredBlock<*> = register(type.id.path, properties, HTEntityBlock.create(type))

    @JvmField
    val ITEM_COLLECTOR: DeferredBlock<*> = registerEntityBlock(lightMetal(), RagiumBlockEntityTypes.ITEM_COLLECTOR)

    @JvmField
    val SPRINKLER: DeferredBlock<*> = registerEntityBlock(lightMetal(), RagiumBlockEntityTypes.SPRINKLER)

    @JvmField
    val WATER_COLLECTOR: DeferredBlock<*> = registerEntityBlock(lightMetal(), RagiumBlockEntityTypes.WATER_COLLECTOR)

    // Advanced
    @JvmField
    val ENI: DeferredBlock<*> = registerEntityBlock(heavyMetal(), RagiumBlockEntityTypes.ENI)

    @JvmField
    val EXP_COLLECTOR: DeferredBlock<*> = registerEntityBlock(heavyMetal(), RagiumBlockEntityTypes.EXP_COLLECTOR)

    @JvmField
    val LAVA_COLLECTOR: DeferredBlock<*> = registerEntityBlock(heavyMetal(), RagiumBlockEntityTypes.LAVA_COLLECTOR)

    @JvmField
    val TELEPORT_ANCHOR: DeferredBlock<Block> = register("teleport_anchor", heavyMetal())

    // Creative
    @JvmField
    val CEU: DeferredBlock<*> = registerEntityBlock(heavyMetal(), RagiumBlockEntityTypes.CEU)

    @JvmField
    val DEVICES: List<DeferredBlock<*>> = listOf(
        MILK_DRAIN,
        // 色でソート
        ITEM_COLLECTOR,
        LAVA_COLLECTOR,
        EXP_COLLECTOR,
        TELEPORT_ANCHOR,
        SPRINKLER,
        WATER_COLLECTOR,
        CEU,
        // 無彩色
        ENI,
    )

    //    Storages    //

    @JvmField
    val SMALL_DRUM: DeferredBlock<*> = registerEntityBlock(lightMetal(), RagiumBlockEntityTypes.SMALL_DRUM)

    @JvmField
    val MEDIUM_DRUM: DeferredBlock<*> = registerEntityBlock(lightMetal(), RagiumBlockEntityTypes.MEDIUM_DRUM)

    @JvmField
    val LARGE_DRUM: DeferredBlock<*> = registerEntityBlock(lightMetal(), RagiumBlockEntityTypes.LARGE_DRUM)

    @JvmField
    val HUGE_DRUM: DeferredBlock<*> = registerEntityBlock(lightMetal(), RagiumBlockEntityTypes.HUGE_DRUM)

    @JvmField
    val DRUMS: List<DeferredBlock<*>> = listOf(SMALL_DRUM, MEDIUM_DRUM, LARGE_DRUM, HUGE_DRUM)
}
