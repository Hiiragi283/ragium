package hiiragi283.ragium.setup

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.HTEntityBlock
import hiiragi283.ragium.api.block.HTFacingEntityBlock
import hiiragi283.ragium.api.block.HTHorizontalEntityBlock
import hiiragi283.ragium.api.registry.HTBlockHolderLike
import hiiragi283.ragium.api.registry.HTBlockRegister
import hiiragi283.ragium.api.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.api.registry.HTItemRegister
import hiiragi283.ragium.api.registry.HTTaggedHolder
import hiiragi283.ragium.api.util.material.HTMaterialType
import hiiragi283.ragium.api.util.material.HTMaterialVariant
import hiiragi283.ragium.common.block.HTCrimsonSoilBlock
import hiiragi283.ragium.common.block.HTDrumBlock
import hiiragi283.ragium.common.block.HTExpBerriesBushBlock
import hiiragi283.ragium.common.block.HTGlassBlock
import hiiragi283.ragium.common.block.HTMilkDrainBlock
import hiiragi283.ragium.common.block.HTSiltBlock
import hiiragi283.ragium.common.block.HTSweetBerriesCakeBlock
import hiiragi283.ragium.common.block.HTWarpedWartBlock
import hiiragi283.ragium.common.block.entity.HTBlockEntity
import hiiragi283.ragium.common.block.entity.HTDrumBlockEntity
import hiiragi283.ragium.util.material.RagiumMaterialType
import hiiragi283.ragium.util.variant.HTDecorationVariant
import net.minecraft.tags.TagKey
import net.minecraft.world.item.DyeColor
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.RotatedPillarBlock
import net.minecraft.world.level.block.SlabBlock
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.StairBlock
import net.minecraft.world.level.block.TransparentBlock
import net.minecraft.world.level.block.WallBlock
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

        ORES
        StorageBlocks.entries

        Glasses.entries
        LEDBlocks.entries

        Casings.entries
        Devices.entries

        Dynamos.entries
        Frames.entries
        Machines.entries

        Drums.entries

        Slabs.entries
        Stairs.entries
        Walls.entries

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
    private fun copyOf(block: Block, mapColor: MapColor): BlockBehaviour.Properties = copyOf(block).mapColor(mapColor)

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

    enum class Ores(override val material: HTMaterialType) : HTBlockHolderLike.Materialized {
        RAGINITE(RagiumMaterialType.RAGINITE),
        RAGI_CRYSTAL(RagiumMaterialType.RAGI_CRYSTAL),
        ;

        override val holder: DeferredBlock<*> = register(
            "${material.serializedName}_ore",
            copyOf(Blocks.DIAMOND_ORE),
        )
    }

    enum class DeepOres(override val material: HTMaterialType) : HTBlockHolderLike.Materialized {
        RAGINITE(RagiumMaterialType.RAGINITE),
        RAGI_CRYSTAL(RagiumMaterialType.RAGI_CRYSTAL),
        ;

        override val holder: DeferredBlock<*> = register(
            "deepslate_${material.serializedName}_ore",
            copyOf(Blocks.DEEPSLATE_DIAMOND_ORE),
        )
    }

    enum class NetherOres(override val material: HTMaterialType) : HTBlockHolderLike.Materialized {
        RAGINITE(RagiumMaterialType.RAGINITE),
        RAGI_CRYSTAL(RagiumMaterialType.RAGI_CRYSTAL),
        ;

        override val holder: DeferredBlock<*> = register(
            "nether_${material.serializedName}_ore",
            copyOf(Blocks.NETHER_QUARTZ_ORE),
        )
    }

    enum class EndOres(override val material: HTMaterialType) : HTBlockHolderLike.Materialized {
        RAGINITE(RagiumMaterialType.RAGINITE),
        RAGI_CRYSTAL(RagiumMaterialType.RAGI_CRYSTAL),
        ;

        override val holder: DeferredBlock<*> = register(
            "end_${material.serializedName}_ore",
            copyOf(Blocks.END_STONE),
        )
    }

    @JvmField
    val ORES: List<HTBlockHolderLike.Materialized> = buildList {
        addAll(Ores.entries)
        addAll(DeepOres.entries)
        addAll(NetherOres.entries)
        addAll(EndOres.entries)
    }

    enum class StorageBlocks(override val material: HTMaterialType, properties: BlockBehaviour.Properties) :
        HTBlockHolderLike.Materialized,
        HTTaggedHolder<Block> {
        // Gems
        RAGI_CRYSTAL(RagiumMaterialType.RAGI_CRYSTAL, copyOf(Blocks.AMETHYST_BLOCK).mapColor(MapColor.COLOR_PINK)),
        CRIMSON_CRYSTAL(RagiumMaterialType.CRIMSON_CRYSTAL, copyOf(Blocks.AMETHYST_BLOCK).mapColor(MapColor.CRIMSON_STEM)),
        WARPED_CRYSTAL(RagiumMaterialType.WARPED_CRYSTAL, copyOf(Blocks.AMETHYST_BLOCK).mapColor(MapColor.WARPED_STEM)),
        ELDRITCH_PEARL(RagiumMaterialType.ELDRITCH_PEARL, copyOf(Blocks.SHROOMLIGHT).mapColor(MapColor.COLOR_PURPLE)),

        // Ingots
        RAGI_ALLOY(RagiumMaterialType.RAGI_ALLOY, copyOf(Blocks.COPPER_BLOCK).mapColor(MapColor.COLOR_RED)),
        ADVANCED_RAGI_ALLOY(RagiumMaterialType.ADVANCED_RAGI_ALLOY, copyOf(Blocks.IRON_BLOCK).mapColor(MapColor.COLOR_ORANGE)),
        AZURE_STEEL(RagiumMaterialType.AZURE_STEEL, copyOf(Blocks.IRON_BLOCK).mapColor(MapColor.TERRACOTTA_BLUE)),
        DEEP_STEEL(RagiumMaterialType.DEEP_STEEL, copyOf(Blocks.NETHERITE_BLOCK).mapColor(MapColor.COLOR_CYAN)),

        // Others
        CHOCOLATE(RagiumMaterialType.CHOCOLATE, copyOf(Blocks.MUD).mapColor(MapColor.TERRACOTTA_BROWN)),
        MEAT(RagiumMaterialType.MEAT, copyOf(Blocks.MUD).sound(SoundType.HONEY_BLOCK)),
        COOKED_MEAT(RagiumMaterialType.COOKED_MEAT, copyOf(Blocks.PACKED_MUD).sound(SoundType.HONEY_BLOCK)),
        ;

        private val lowerName: String = name.lowercase()
        override val holder: DeferredBlock<*> = register("${lowerName}_block", properties)
        override val tagKey: TagKey<Block> = HTMaterialVariant.STORAGE_BLOCK.blockTagKey(material)
    }

    //    Buildings    //

    @JvmField
    val RAGI_STONE: DeferredBlock<Block> = register("ragi_stone", copyOf(Blocks.STONE, MapColor.COLOR_RED))

    @JvmField
    val RAGI_STONE_BRICKS: DeferredBlock<Block> = register("ragi_stone_bricks", copyOf(Blocks.STONE, MapColor.COLOR_RED))

    @JvmField
    val RAGI_STONE_SQUARE: DeferredBlock<Block> = register("ragi_stone_square", copyOf(Blocks.STONE, MapColor.COLOR_RED))

    @JvmField
    val AZURE_TILES: DeferredBlock<Block> = register("azure_tiles", copyOf(Blocks.STONE, MapColor.TERRACOTTA_BLUE))

    @JvmField
    val EMBER_STONE: DeferredBlock<Block> =
        register("ember_stone", copyOf(Blocks.AMETHYST_BLOCK, MapColor.COLOR_ORANGE))

    @JvmField
    val PLASTIC_BLOCK: DeferredBlock<Block> = register("plastic_block", copyOf(Blocks.COPPER_BLOCK, MapColor.NONE))

    @JvmField
    val BLUE_NETHER_BRICKS: DeferredBlock<Block> =
        register("blue_nether_bricks", copyOf(Blocks.NETHER_BRICKS, MapColor.COLOR_BLUE))

    @JvmField
    val SPONGE_CAKE: DeferredBlock<Block> = register("sponge_cake", copyOf(Blocks.YELLOW_WOOL))

    @JvmField
    val DECORATION_MAP: Map<HTDecorationVariant, DeferredBlock<Block>> = mapOf(
        HTDecorationVariant.RAGI_STONE to RAGI_STONE,
        HTDecorationVariant.RAGI_STONE_BRICK to RAGI_STONE_BRICKS,
        HTDecorationVariant.RAGI_STONE_SQUARE to RAGI_STONE_SQUARE,
        HTDecorationVariant.AZURE_TILE to AZURE_TILES,
        HTDecorationVariant.EMBER_STONE to EMBER_STONE,
        HTDecorationVariant.PLASTIC_BLOCK to PLASTIC_BLOCK,
        HTDecorationVariant.BLUE_NETHER_BRICK to BLUE_NETHER_BRICKS,
        HTDecorationVariant.SPONGE_CAKE to SPONGE_CAKE,
    )

    enum class Slabs(override val variant: HTDecorationVariant) : HTBlockHolderLike.Typed<HTDecorationVariant> {
        RAGI_STONE(HTDecorationVariant.RAGI_STONE),
        RAGI_STONE_BRICK(HTDecorationVariant.RAGI_STONE_BRICK),
        RAGI_STONE_SQUARE(HTDecorationVariant.RAGI_STONE_SQUARE),
        AZURE_TILE(HTDecorationVariant.AZURE_TILE),
        EMBER_STONE(HTDecorationVariant.EMBER_STONE),
        PLASTIC_BLOCK(HTDecorationVariant.PLASTIC_BLOCK),
        BLUE_NETHER_BRICK(HTDecorationVariant.BLUE_NETHER_BRICK),
        SPONGE_CAKE(HTDecorationVariant.SPONGE_CAKE),
        ;

        override val holder: DeferredBlock<SlabBlock> =
            register("${variant.serializedName}_slab", variant.properties, ::SlabBlock)
    }

    enum class Stairs(override val variant: HTDecorationVariant, base: DeferredBlock<*>) : HTBlockHolderLike.Typed<HTDecorationVariant> {
        RAGI_STONE(HTDecorationVariant.RAGI_STONE, this@RagiumBlocks.RAGI_STONE),
        RAGI_STONE_BRICK(HTDecorationVariant.RAGI_STONE_BRICK, this@RagiumBlocks.RAGI_STONE_BRICKS),
        RAGI_STONE_SQUARE(HTDecorationVariant.RAGI_STONE_SQUARE, this@RagiumBlocks.RAGI_STONE_SQUARE),
        AZURE_TILE(HTDecorationVariant.AZURE_TILE, this@RagiumBlocks.AZURE_TILES),
        EMBER_STONE(HTDecorationVariant.EMBER_STONE, this@RagiumBlocks.EMBER_STONE),
        PLASTIC_BLOCK(HTDecorationVariant.PLASTIC_BLOCK, this@RagiumBlocks.PLASTIC_BLOCK),
        BLUE_NETHER_BRICK(HTDecorationVariant.BLUE_NETHER_BRICK, this@RagiumBlocks.BLUE_NETHER_BRICKS),
        SPONGE_CAKE(HTDecorationVariant.SPONGE_CAKE, this@RagiumBlocks.SPONGE_CAKE),
        ;

        override val holder: DeferredBlock<StairBlock> = register(
            "${variant.serializedName}_stairs",
            variant.properties,
        ) { prop: BlockBehaviour.Properties -> StairBlock(base.get().defaultBlockState(), prop) }
    }

    enum class Walls(override val variant: HTDecorationVariant) : HTBlockHolderLike.Typed<HTDecorationVariant> {
        RAGI_STONE(HTDecorationVariant.RAGI_STONE),
        RAGI_STONE_BRICK(HTDecorationVariant.RAGI_STONE_BRICK),
        RAGI_STONE_SQUARE(HTDecorationVariant.RAGI_STONE_SQUARE),
        AZURE_TILE(HTDecorationVariant.AZURE_TILE),
        EMBER_STONE(HTDecorationVariant.EMBER_STONE),
        PLASTIC_BLOCK(HTDecorationVariant.PLASTIC_BLOCK),
        BLUE_NETHER_BRICK(HTDecorationVariant.BLUE_NETHER_BRICK),
        SPONGE_CAKE(HTDecorationVariant.SPONGE_CAKE),
        ;

        override val holder: DeferredBlock<WallBlock> =
            register("${variant.serializedName}_wall", variant.properties.forceSolidOn(), ::WallBlock)
    }

    enum class Glasses(
        override val material: HTMaterialType,
        isTinted: Boolean,
        canPlayerThrough: Boolean,
        blastProof: Boolean,
    ) : HTBlockHolderLike.Materialized,
        HTTaggedHolder<Block> {
        QUARTZ(RagiumMaterialType.QUARTZ, false, false, false),
        SOUL(RagiumMaterialType.SOUL, false, true, false),
        OBSIDIAN(RagiumMaterialType.OBSIDIAN, false, false, true),
        ;

        private val lowerName: String = name.lowercase()
        override val holder: DeferredBlock<*> = register("${lowerName}_glass", glass()) { prop: BlockBehaviour.Properties ->
            if (blastProof) prop.strength(5f, 1200f)
            HTGlassBlock(isTinted, canPlayerThrough, prop)
        }
        override val tagKey: TagKey<Block> = HTMaterialVariant.GLASS_BLOCK.blockTagKey(lowerName)
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
            register("${name.lowercase()}_machine_frame", properties.noOcclusion(), ::TransparentBlock)
    }

    enum class Machines(type: HTDeferredBlockEntityType<out HTBlockEntity>) : HTBlockHolderLike {
        // Basic
        CRUSHER(RagiumBlockEntityTypes.CRUSHER),
        BLOCK_BREAKER(RagiumBlockEntityTypes.BLOCK_BREAKER),
        COMPRESSOR(RagiumBlockEntityTypes.COMPRESSOR),
        ENGRAVER(RagiumBlockEntityTypes.ENGRAVER),
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

    enum class Devices(override val holder: DeferredBlock<*>) : HTBlockHolderLike {
        MILK_DRAIN(RagiumBlockEntityTypes.MILK_DRAIN, copyOf(Blocks.COBBLESTONE), ::HTMilkDrainBlock),

        // Basic
        ITEM_BUFFER(RagiumBlockEntityTypes.ITEM_BUFFER),
        SPRINKLER(RagiumBlockEntityTypes.SPRINKLER),
        WATER_COLLECTOR(RagiumBlockEntityTypes.WATER_COLLECTOR),

        // Advanced
        ENI(RagiumBlockEntityTypes.ENI),
        EXP_COLLECTOR(RagiumBlockEntityTypes.EXP_COLLECTOR),
        LAVA_COLLECTOR(RagiumBlockEntityTypes.LAVA_COLLECTOR),
        DIM_ANCHOR(RagiumBlockEntityTypes.DIM_ANCHOR),

        // Creative
        CEU(RagiumBlockEntityTypes.CEU),
        ;

        constructor(type: HTDeferredBlockEntityType<out HTBlockEntity>) : this(
            type,
            machineProperty(),
            HTEntityBlock.create(type),
        )

        constructor(
            type: HTDeferredBlockEntityType<out HTBlockEntity>,
            properties: BlockBehaviour.Properties,
            factory: (BlockBehaviour.Properties) -> HTEntityBlock<*>,
        ) : this(registerEntity(type, properties, factory))
    }

    //    Storages    //

    enum class Drums(base: Block, type: HTDeferredBlockEntityType<out HTDrumBlockEntity>) : HTBlockHolderLike {
        SMALL(Blocks.IRON_BLOCK, RagiumBlockEntityTypes.SMALL_DRUM),
        MEDIUM(Blocks.GOLD_BLOCK, RagiumBlockEntityTypes.MEDIUM_DRUM),
        LARGE(Blocks.DIAMOND_BLOCK, RagiumBlockEntityTypes.LARGE_DRUM),
        HUGE(Blocks.NETHERITE_BLOCK, RagiumBlockEntityTypes.HUGE_DRUM),
        ;

        override val holder: DeferredBlock<*> = registerEntity(type, copyOf(base), HTDrumBlock.create(type))
    }
}
