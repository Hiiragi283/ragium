package hiiragi283.ragium.setup

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.HTEntityBlock
import hiiragi283.ragium.api.block.HTFacingEntityBlock
import hiiragi283.ragium.api.block.HTHorizontalEntityBlock
import hiiragi283.ragium.api.extension.buildTable
import hiiragi283.ragium.api.registry.HTBlockHolderLike
import hiiragi283.ragium.api.registry.HTBlockRegister
import hiiragi283.ragium.api.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.api.registry.HTItemRegister
import hiiragi283.ragium.api.util.HTTable
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
import hiiragi283.ragium.util.material.HTVanillaMaterialType
import hiiragi283.ragium.util.material.RagiumMaterialType
import hiiragi283.ragium.util.variant.HTDecorationVariant
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

    @JvmField
    val ORES: HTTable<HTMaterialVariant, HTMaterialType, DeferredBlock<*>> = buildTable {
        listOf(
            HTMaterialVariant.ORE,
            HTMaterialVariant.DEEP_ORE,
            HTMaterialVariant.NETHER_ORE,
            HTMaterialVariant.END_ORE,
        ).forEach { variant: HTMaterialVariant ->
            val pattern: String = when (variant) {
                HTMaterialVariant.ORE -> "%s_ore"
                HTMaterialVariant.DEEP_ORE -> "deepslate_%s_ore"
                HTMaterialVariant.NETHER_ORE -> "nether_%s_ore"
                HTMaterialVariant.END_ORE -> "end_%s_ore"
                else -> return@forEach
            }
            val stone: Block = when (variant) {
                HTMaterialVariant.ORE -> Blocks.DIAMOND_ORE
                HTMaterialVariant.DEEP_ORE -> Blocks.DEEPSLATE_DIAMOND_ORE
                HTMaterialVariant.NETHER_ORE -> Blocks.NETHER_QUARTZ_ORE
                HTMaterialVariant.END_ORE -> Blocks.END_STONE
                else -> null
            } ?: return@forEach

            fun register(material: HTMaterialType) {
                put(
                    variant,
                    material,
                    register(pattern.replace("%s", material.serializedName), copyOf(stone)),
                )
            }
            register(RagiumMaterialType.RAGINITE)
            register(RagiumMaterialType.RAGI_CRYSTAL)
            register(RagiumMaterialType.CRIMSON_CRYSTAL)
            register(RagiumMaterialType.WARPED_CRYSTAL)
        }
    }

    @JvmField
    val MATERIALS: HTTable<HTMaterialVariant, HTMaterialType, DeferredBlock<*>> = buildTable {
        // Storage Blocks
        mapOf(
            // Gems
            RagiumMaterialType.RAGI_CRYSTAL to copyOf(Blocks.AMETHYST_BLOCK, MapColor.COLOR_PINK),
            RagiumMaterialType.CRIMSON_CRYSTAL to copyOf(Blocks.AMETHYST_BLOCK, MapColor.CRIMSON_STEM),
            RagiumMaterialType.WARPED_CRYSTAL to copyOf(Blocks.AMETHYST_BLOCK, MapColor.WARPED_STEM),
            RagiumMaterialType.ELDRITCH_PEARL to copyOf(Blocks.SHROOMLIGHT, MapColor.COLOR_PURPLE),
            // Ingots
            RagiumMaterialType.RAGI_ALLOY to copyOf(Blocks.COPPER_BLOCK, MapColor.COLOR_RED),
            RagiumMaterialType.ADVANCED_RAGI_ALLOY to copyOf(Blocks.IRON_BLOCK, MapColor.COLOR_ORANGE),
            RagiumMaterialType.AZURE_STEEL to copyOf(Blocks.IRON_BLOCK, MapColor.TERRACOTTA_BLUE),
            RagiumMaterialType.DEEP_STEEL to copyOf(Blocks.NETHERITE_BLOCK, MapColor.COLOR_CYAN),
            // Foods
            RagiumMaterialType.CHOCOLATE to copyOf(Blocks.MUD, MapColor.TERRACOTTA_BROWN),
            RagiumMaterialType.MEAT to copyOf(Blocks.MUD).sound(SoundType.HONEY_BLOCK),
            RagiumMaterialType.COOKED_MEAT to copyOf(Blocks.PACKED_MUD).sound(SoundType.HONEY_BLOCK),
        ).forEach { (material: RagiumMaterialType, properties: BlockBehaviour.Properties) ->
            put(HTMaterialVariant.STORAGE_BLOCK, material, register("${material.serializedName}_block", properties))
        }

        // Glasses
        fun glass(
            material: HTMaterialType,
            properties: BlockBehaviour.Properties,
            canPlayerThrough: Boolean,
            blastProof: Boolean,
        ) {
            put(
                HTMaterialVariant.GLASS_BLOCK,
                material,
                register(
                    "${material.serializedName}_glass",
                    properties.apply { if (blastProof) strength(5f, 1200f) },
                    HTGlassBlock.create(false, canPlayerThrough),
                ),
            )
        }

        glass(HTVanillaMaterialType.QUARTZ, glass(), canPlayerThrough = false, blastProof = false)
        glass(HTVanillaMaterialType.SOUL, glass(), canPlayerThrough = true, blastProof = false)
        glass(HTVanillaMaterialType.OBSIDIAN, glass(), canPlayerThrough = false, blastProof = true)

        // Tinted Glasses
        fun tintedGlass(
            material: HTMaterialType,
            properties: BlockBehaviour.Properties,
            canPlayerThrough: Boolean,
            blastProof: Boolean,
        ) {
            put(
                HTMaterialVariant.TINTED_GLASS_BLOCK,
                material,
                register(
                    "tinted_${material.serializedName}_glass",
                    properties.apply { if (blastProof) strength(5f, 1200f) },
                    HTGlassBlock.create(true, canPlayerThrough),
                ),
            )
        }

        tintedGlass(HTVanillaMaterialType.QUARTZ, glass(), canPlayerThrough = false, blastProof = false)
        tintedGlass(HTVanillaMaterialType.SOUL, glass(), canPlayerThrough = true, blastProof = false)
        tintedGlass(HTVanillaMaterialType.OBSIDIAN, glass(), canPlayerThrough = false, blastProof = true)
    }

    @JvmStatic
    fun getMaterial(variant: HTMaterialVariant, material: HTMaterialType): DeferredBlock<*> = MATERIALS.get(variant, material)
        ?: error("Unregistered ${variant.serializedName} block for ${material.serializedName}")

    @JvmStatic
    fun getStorageBlock(material: HTMaterialType): DeferredBlock<*> = getMaterial(HTMaterialVariant.STORAGE_BLOCK, material)

    @JvmStatic
    fun getGlass(material: HTMaterialType): DeferredBlock<*> = getMaterial(HTMaterialVariant.GLASS_BLOCK, material)

    @JvmStatic
    fun getTintedGlass(material: HTMaterialType): DeferredBlock<*> = getMaterial(HTMaterialVariant.TINTED_GLASS_BLOCK, material)

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
