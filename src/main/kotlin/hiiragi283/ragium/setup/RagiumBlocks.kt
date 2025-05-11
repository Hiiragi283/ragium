package hiiragi283.ragium.setup

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.HTEntityBlock
import hiiragi283.ragium.api.block.HTHorizontalEntityBlock
import hiiragi283.ragium.api.block.entity.HTBlockEntity
import hiiragi283.ragium.api.registry.HTBlockRegister
import hiiragi283.ragium.api.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.api.registry.HTItemRegister
import hiiragi283.ragium.common.block.*
import hiiragi283.ragium.util.HTBuildingBlockSets
import hiiragi283.ragium.util.HTOreSets
import net.minecraft.util.ColorRGBA
import net.minecraft.world.item.DyeColor
import net.minecraft.world.level.block.*
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
    val RAGINITE_ORES = HTOreSets("raginite")

    @JvmField
    val RAGI_CRYSTAL_ORES = HTOreSets("ragi_crystal")

    @JvmField
    val SILT: DeferredBlock<ColoredFallingBlock> = register(
        "silt",
        copyOf(Blocks.SAND),
    ) { prop: BlockBehaviour.Properties -> ColoredFallingBlock(ColorRGBA(0xccccff), prop) }

    @JvmField
    val MYSTERIOUS_OBSIDIAN: DeferredBlock<Block> = register("mysterious_obsidian", copyOf(Blocks.OBSIDIAN))

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

    //    Materials    //

    // Gems
    @JvmField
    val RAGI_CRYSTAL_BLOCK: DeferredBlock<Block> =
        register("ragi_crystal_block", crystal().mapColor(MapColor.COLOR_PINK))

    @JvmField
    val CRIMSON_CRYSTAL_BLOCK: DeferredBlock<Block> =
        register("crimson_crystal_block", crystal().mapColor(MapColor.CRIMSON_STEM))

    @JvmField
    val WARPED_CRYSTAL_BLOCK: DeferredBlock<Block> =
        register("warped_crystal_block", crystal().mapColor(MapColor.WARPED_STEM))

    // Ingots
    @JvmField
    val RAGI_ALLOY_BLOCK: DeferredBlock<Block> =
        register("ragi_alloy_block", lightMetal().mapColor(MapColor.COLOR_RED))

    @JvmField
    val ADVANCED_RAGI_ALLOY_BLOCK: DeferredBlock<Block> =
        register("advanced_ragi_alloy_block", heavyMetal().mapColor(MapColor.COLOR_ORANGE))

    @JvmField
    val AZURE_STEEL_BLOCK: DeferredBlock<Block> =
        register("azure_steel_block", heavyMetal().mapColor(MapColor.TERRACOTTA_BLUE))

    @JvmField
    val DEEP_STEEL_BLOCK: DeferredBlock<Block> =
        register("deep_steel_block", heavyMetal().mapColor(MapColor.COLOR_CYAN))

    // Others
    @JvmField
    val CHEESE_BLOCK: DeferredBlock<Block> =
        register("cheese_block", wooden().mapColor(MapColor.TERRACOTTA_YELLOW))

    @JvmField
    val CHOCOLATE_BLOCK: DeferredBlock<Block> =
        register("chocolate_block", wooden().mapColor(MapColor.TERRACOTTA_BROWN))

    @JvmField
    val STORAGE_BLOCKS: List<DeferredBlock<Block>> = listOf(
        // Gems
        RAGI_CRYSTAL_BLOCK,
        CRIMSON_CRYSTAL_BLOCK,
        WARPED_CRYSTAL_BLOCK,
        // Ingots
        RAGI_ALLOY_BLOCK,
        ADVANCED_RAGI_ALLOY_BLOCK,
        AZURE_STEEL_BLOCK,
        DEEP_STEEL_BLOCK,
        // Others
        CHEESE_BLOCK,
        CHOCOLATE_BLOCK,
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
        copyOf(Blocks.MUD),
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
    val MACHINE_CASING: DeferredBlock<Block> = register(
        "machine_casing",
        lightMetal(),
    )

    @JvmField
    val ADVANCED_MACHINE_CASING: DeferredBlock<Block> = register(
        "advanced_machine_casing",
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
        MACHINE_CASING,
        ADVANCED_MACHINE_CASING,
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
    val EXTRACTOR: DeferredBlock<*> = registerMachine(lightMetal(), RagiumBlockEntityTypes.EXTRACTOR)

    // Advanced
    @JvmField
    val ADVANCED_CRUSHER: DeferredBlock<*> = registerMachine(heavyMetal(), RagiumBlockEntityTypes.ADVANCED_CRUSHER)

    @JvmField
    val ADVANCED_EXTRACTOR: DeferredBlock<*> = registerMachine(heavyMetal(), RagiumBlockEntityTypes.ADVANCED_EXTRACTOR)

    @JvmField
    val INFUSER: DeferredBlock<*> = registerMachine(heavyMetal(), RagiumBlockEntityTypes.INFUSER)

    @JvmField
    val REFINERY: DeferredBlock<*> = registerMachine(heavyMetal(), RagiumBlockEntityTypes.REFINERY)

    @JvmField
    val MACHINES: List<DeferredBlock<*>> = listOf(
        // Basic
        CRUSHER,
        EXTRACTOR,
        // Advanced
        ADVANCED_CRUSHER,
        ADVANCED_EXTRACTOR,
        INFUSER,
        REFINERY,
    )

    //    Devices    //

    @JvmStatic
    private fun registerDevice(
        properties: BlockBehaviour.Properties,
        type: HTDeferredBlockEntityType<out HTBlockEntity>,
    ): DeferredBlock<*> = register(type.id.path, properties, HTEntityBlock.create(type))

    @JvmField
    val MILK_DRAIN: DeferredBlock<HTMilkDrainBlock> =
        register("milk_drain", stone(), ::HTMilkDrainBlock)

    @JvmField
    val SOUL_SPIKE: DeferredBlock<HTSoulSpikeBlock> = register("soul_spike", heavyMetal(), ::HTSoulSpikeBlock)

    // Basic
    @JvmField
    val ITEM_COLLECTOR: DeferredBlock<*> = registerDevice(lightMetal(), RagiumBlockEntityTypes.ITEM_COLLECTOR)

    @JvmField
    val SPRINKLER: DeferredBlock<*> = registerDevice(lightMetal(), RagiumBlockEntityTypes.SPRINKLER)

    @JvmField
    val WATER_COLLECTOR: DeferredBlock<*> = registerDevice(lightMetal(), RagiumBlockEntityTypes.WATER_COLLECTOR)

    // Advanced
    @JvmField
    val ENI: DeferredBlock<*> = registerDevice(heavyMetal(), RagiumBlockEntityTypes.ENI)

    @JvmField
    val EXP_COLLECTOR: DeferredBlock<*> = registerDevice(heavyMetal(), RagiumBlockEntityTypes.EXP_COLLECTOR)

    @JvmField
    val LAVA_COLLECTOR: DeferredBlock<*> = registerDevice(heavyMetal(), RagiumBlockEntityTypes.LAVA_COLLECTOR)

    @JvmField
    val TELEPORT_ANCHOR: DeferredBlock<Block> = register("teleport_anchor", heavyMetal())

    @JvmField
    val DEVICES: List<DeferredBlock<*>> = listOf(
        MILK_DRAIN,
        SOUL_SPIKE,
        // 色でソート
        ITEM_COLLECTOR,
        LAVA_COLLECTOR,
        EXP_COLLECTOR,
        TELEPORT_ANCHOR,
        SPRINKLER,
        WATER_COLLECTOR,
        // 無彩色
        ENI,
    )
}
