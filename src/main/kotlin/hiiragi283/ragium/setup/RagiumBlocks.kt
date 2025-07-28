package hiiragi283.ragium.setup

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.registry.HTBlockRegister
import hiiragi283.ragium.api.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.api.registry.HTItemRegister
import hiiragi283.ragium.api.util.RagiumConst
import hiiragi283.ragium.common.block.HTCrimsonSoilBlock
import hiiragi283.ragium.common.block.HTEntityBlock
import hiiragi283.ragium.common.block.HTExpBerriesBushBlock
import hiiragi283.ragium.common.block.HTHorizontalEntityBlock
import hiiragi283.ragium.common.block.HTMilkDrainBlock
import hiiragi283.ragium.common.block.HTSiltBlock
import hiiragi283.ragium.common.block.HTSoulGlassBlock
import hiiragi283.ragium.common.block.HTSweetBerriesCakeBlock
import hiiragi283.ragium.common.block.entity.HTBlockEntity
import hiiragi283.ragium.util.HTBuildingBlockSets
import hiiragi283.ragium.util.HTOreSets
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
    val MYSTERIOUS_OBSIDIAN: DeferredBlock<Block> = register("mysterious_obsidian", copyOf(Blocks.OBSIDIAN))

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
    val RAGINITE_ORES = HTOreSets(RagiumConst.RAGINITE)

    @JvmField
    val RAGI_CRYSTAL_ORES = HTOreSets(RagiumConst.RAGI_CRYSTAL)

    @JvmField
    val RESONANT_DEBRIS: DeferredBlock<Block> =
        register("resonant_debris", copyOf(Blocks.ANCIENT_DEBRIS))

    //    Materials    //

    // Gems
    @JvmField
    val RAGI_CRYSTAL_BLOCK: DeferredBlock<Block> =
        register("${RagiumConst.RAGI_CRYSTAL}_block", copyOf(Blocks.AMETHYST_BLOCK).mapColor(MapColor.COLOR_PINK))

    @JvmField
    val CRIMSON_CRYSTAL_BLOCK: DeferredBlock<Block> =
        register("${RagiumConst.CRIMSON_CRYSTAL}_block", copyOf(Blocks.AMETHYST_BLOCK).mapColor(MapColor.CRIMSON_STEM))

    @JvmField
    val WARPED_CRYSTAL_BLOCK: DeferredBlock<Block> =
        register("${RagiumConst.WARPED_CRYSTAL}_block", copyOf(Blocks.AMETHYST_BLOCK).mapColor(MapColor.WARPED_STEM))

    @JvmField
    val ELDRITCH_PEARL_BLOCK: DeferredBlock<Block> = register("${RagiumConst.ELDRITCH_PEARL}_block", copyOf(Blocks.SHROOMLIGHT).mapColor(MapColor.COLOR_PURPLE))

    // Ingots
    @JvmField
    val RAGI_ALLOY_BLOCK: DeferredBlock<Block> =
        register("${RagiumConst.RAGI_ALLOY}_block", copyOf(Blocks.COPPER_BLOCK).mapColor(MapColor.COLOR_RED))

    @JvmField
    val ADVANCED_RAGI_ALLOY_BLOCK: DeferredBlock<Block> =
        register("${RagiumConst.ADVANCED_RAGI_ALLOY}_block", copyOf(Blocks.IRON_BLOCK).mapColor(MapColor.COLOR_ORANGE))

    @JvmField
    val AZURE_STEEL_BLOCK: DeferredBlock<Block> =
        register("${RagiumConst.AZURE_STEEL}_block", copyOf(Blocks.IRON_BLOCK).mapColor(MapColor.TERRACOTTA_BLUE))

    @JvmField
    val DEEP_STEEL_BLOCK: DeferredBlock<Block> =
        register("${RagiumConst.DEEP_STEEL}_block", copyOf(Blocks.NETHERITE_BLOCK).mapColor(MapColor.COLOR_CYAN))

    // Others
    @JvmField
    val CHOCOLATE_BLOCK: DeferredBlock<Block> =
        register("${RagiumConst.CHOCOLATE}_block", copyOf(Blocks.OAK_PLANKS).mapColor(MapColor.TERRACOTTA_BROWN))

    @JvmField
    val MEAT_BLOCK: DeferredBlock<Block> =
        register("${RagiumConst.MEAT}_block", copyOf(Blocks.MUD).sound(SoundType.HONEY_BLOCK))

    @JvmField
    val COOKED_MEAT_BLOCK: DeferredBlock<Block> =
        register("${RagiumConst.COOKED_MEAT}_block", copyOf(Blocks.PACKED_MUD).sound(SoundType.HONEY_BLOCK))

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

    @JvmField
    val QUARTZ_GLASS: DeferredBlock<Block> =
        register("quartz_glass", glass(), ::TransparentBlock)

    @JvmField
    val OBSIDIAN_GLASS: DeferredBlock<Block> =
        register("obsidian_glass", glass().strength(5f, 1200f), ::TransparentBlock)

    @JvmField
    val SOUL_GLASS: DeferredBlock<Block> =
        register("soul_glass", glass(), ::HTSoulGlassBlock)

    @JvmField
    val GLASSES: List<DeferredBlock<Block>> = listOf(
        QUARTZ_GLASS,
        OBSIDIAN_GLASS,
        SOUL_GLASS,
    )

    @JvmField
    val LED_BLOCKS: Map<DyeColor, DeferredBlock<Block>> = listOf(
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
    fun getLedBlock(color: DyeColor): DeferredBlock<Block> = LED_BLOCKS[color] ?: error("Unregistered color: ${color.serializedName}")

    //    Foods    //

    @JvmField
    val SWEET_BERRIES_CAKE: DeferredBlock<Block> = register(
        "sweet_berries_cake",
        copyOf(Blocks.YELLOW_WOOL).forceSolidOn(),
        ::HTSweetBerriesCakeBlock,
    )

    //    Casings    //

    @JvmField
    val WOODEN_CASING: DeferredBlock<Block> = register("wooden_casing", copyOf(Blocks.NOTE_BLOCK))

    @JvmField
    val STONE_CASING: DeferredBlock<Block> = register("stone_casing", copyOf(Blocks.COBBLED_DEEPSLATE))

    @JvmField
    val BASIC_MACHINE_FRAME: DeferredBlock<Block> = register(
        "basic_machine_frame",
        copyOf(Blocks.IRON_BLOCK).noCollission(),
        ::TransparentBlock,
    )

    @JvmField
    val ADVANCED_MACHINE_FRAME: DeferredBlock<Block> = register(
        "advanced_machine_frame",
        copyOf(Blocks.IRON_BLOCK).noCollission(),
        ::TransparentBlock,
    )

    @JvmField
    val ELITE_MACHINE_FRAME: DeferredBlock<Block> = register(
        "elite_machine_frame",
        machineProperty().noCollission(),
        ::TransparentBlock,
    )

    @JvmField
    val DEVICE_CASING: DeferredBlock<Block> = register(
        "device_casing",
        machineProperty(),
    )

    @JvmField
    val CASINGS: List<DeferredBlock<Block>> = listOf(
        WOODEN_CASING,
        STONE_CASING,
        BASIC_MACHINE_FRAME,
        ADVANCED_MACHINE_FRAME,
        ELITE_MACHINE_FRAME,
        DEVICE_CASING,
    )

    //    Machines    //

    @JvmStatic
    private fun <T : HTEntityBlock<*>> registerEntity(
        type: HTDeferredBlockEntityType<out HTBlockEntity>,
        properties: BlockBehaviour.Properties,
        factory: (BlockBehaviour.Properties) -> T,
    ): DeferredBlock<Block> = register(type.id.path, properties, factory)

    @JvmStatic
    private fun registerMachine(type: HTDeferredBlockEntityType<out HTBlockEntity>): DeferredBlock<Block> =
        registerEntity(type, machineProperty(), HTHorizontalEntityBlock.create(type))

    // Basic
    @JvmField
    val CRUSHER: DeferredBlock<Block> = registerMachine(RagiumBlockEntityTypes.CRUSHER)

    @JvmField
    val BLOCK_BREAKER: DeferredBlock<Block> = registerMachine(RagiumBlockEntityTypes.BLOCK_BREAKER)

    @JvmField
    val EXTRACTOR: DeferredBlock<Block> = registerMachine(RagiumBlockEntityTypes.EXTRACTOR)

    @JvmField
    val FORMING_PRESS: DeferredBlock<Block> = registerMachine(RagiumBlockEntityTypes.FORMING_PRESS)

    // Advanced
    @JvmField
    val ALLOY_SMELTER: DeferredBlock<Block> = registerMachine(RagiumBlockEntityTypes.ALLOY_SMELTER)

    @JvmField
    val MELTER: DeferredBlock<Block> = registerMachine(RagiumBlockEntityTypes.MELTER)

    @JvmField
    val REFINERY: DeferredBlock<Block> = registerMachine(RagiumBlockEntityTypes.REFINERY)

    @JvmField
    val SOLIDIFIER: DeferredBlock<Block> = registerMachine(RagiumBlockEntityTypes.SOLIDIFIER)

    // Elite
    @JvmField
    val INFUSER: DeferredBlock<Block> = registerMachine(RagiumBlockEntityTypes.INFUSER)

    @JvmField
    val MACHINES: List<DeferredBlock<Block>> = listOf(
        // Basic
        CRUSHER,
        BLOCK_BREAKER,
        EXTRACTOR,
        FORMING_PRESS,
        // Advanced
        ALLOY_SMELTER,
        MELTER,
        REFINERY,
        SOLIDIFIER,
        // Elite
        INFUSER,
    )

    //    Devices    //

    @JvmField
    val MILK_DRAIN: DeferredBlock<Block> = register("milk_drain", copyOf(Blocks.COBBLESTONE), ::HTMilkDrainBlock)

    // Basic
    @JvmStatic
    private fun registerDevice(type: HTDeferredBlockEntityType<out HTBlockEntity>): DeferredBlock<Block> =
        registerEntity(type, machineProperty(), HTEntityBlock.create(type))

    @JvmField
    val ITEM_BUFFER: DeferredBlock<Block> = registerDevice(RagiumBlockEntityTypes.ITEM_BUFFER)

    @JvmField
    val SPRINKLER: DeferredBlock<Block> = registerDevice(RagiumBlockEntityTypes.SPRINKLER)

    @JvmField
    val WATER_COLLECTOR: DeferredBlock<Block> = registerDevice(RagiumBlockEntityTypes.WATER_COLLECTOR)

    // Advanced
    @JvmField
    val ENI: DeferredBlock<Block> = registerDevice(RagiumBlockEntityTypes.ENI)

    @JvmField
    val EXP_COLLECTOR: DeferredBlock<Block> = registerDevice(RagiumBlockEntityTypes.EXP_COLLECTOR)

    @JvmField
    val LAVA_COLLECTOR: DeferredBlock<Block> = registerDevice(RagiumBlockEntityTypes.LAVA_COLLECTOR)

    @JvmField
    val TELEPORT_ANCHOR: DeferredBlock<Block> = register("teleport_anchor", machineProperty())

    // Creative
    @JvmField
    val CEU: DeferredBlock<Block> = registerDevice(RagiumBlockEntityTypes.CEU)

    @JvmField
    val DEVICES: List<DeferredBlock<Block>> = listOf(
        MILK_DRAIN,
        // 色でソート
        ITEM_BUFFER,
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

    @JvmStatic
    private fun registerTank(base: Block, type: HTDeferredBlockEntityType<out HTBlockEntity>): DeferredBlock<Block> =
        registerEntity(type, copyOf(base), HTEntityBlock.create(type))

    @JvmField
    val SMALL_DRUM: DeferredBlock<Block> = registerTank(Blocks.IRON_BLOCK, RagiumBlockEntityTypes.SMALL_DRUM)

    @JvmField
    val MEDIUM_DRUM: DeferredBlock<Block> = registerTank(Blocks.GOLD_BLOCK, RagiumBlockEntityTypes.MEDIUM_DRUM)

    @JvmField
    val LARGE_DRUM: DeferredBlock<Block> = registerTank(Blocks.DIAMOND_BLOCK, RagiumBlockEntityTypes.LARGE_DRUM)

    @JvmField
    val HUGE_DRUM: DeferredBlock<Block> = registerTank(Blocks.NETHERITE_BLOCK, RagiumBlockEntityTypes.HUGE_DRUM)

    @JvmField
    val DRUMS: List<DeferredBlock<Block>> = listOf(SMALL_DRUM, MEDIUM_DRUM, LARGE_DRUM, HUGE_DRUM)
}
