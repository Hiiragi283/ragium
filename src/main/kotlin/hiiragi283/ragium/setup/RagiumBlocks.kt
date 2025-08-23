package hiiragi283.ragium.setup

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.HTEntityBlock
import hiiragi283.ragium.api.block.HTHorizontalEntityBlock
import hiiragi283.ragium.api.extension.buildTable
import hiiragi283.ragium.api.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.api.registry.HTDeferredBlockHolder
import hiiragi283.ragium.api.registry.HTDeferredBlockRegister
import hiiragi283.ragium.api.registry.HTSimpleDeferredBlockHolder
import hiiragi283.ragium.api.registry.HTVariantKey
import hiiragi283.ragium.api.util.HTTable
import hiiragi283.ragium.api.util.material.HTMaterialType
import hiiragi283.ragium.api.util.material.HTMaterialVariant
import hiiragi283.ragium.common.block.HTCrimsonSoilBlock
import hiiragi283.ragium.common.block.HTDrumBlock
import hiiragi283.ragium.common.block.HTExpBerriesBushBlock
import hiiragi283.ragium.common.block.HTGlassBlock
import hiiragi283.ragium.common.block.HTSiltBlock
import hiiragi283.ragium.common.block.HTSweetBerriesCakeBlock
import hiiragi283.ragium.common.block.HTTintedGlassBlock
import hiiragi283.ragium.common.block.HTWarpedWartBlock
import hiiragi283.ragium.common.block.entity.HTDrumBlockEntity
import hiiragi283.ragium.common.item.HTWarpedWartItem
import hiiragi283.ragium.util.material.HTVanillaMaterialType
import hiiragi283.ragium.util.material.RagiumMaterialType
import hiiragi283.ragium.util.variant.HTColorVariant
import hiiragi283.ragium.util.variant.HTDecorationVariant
import hiiragi283.ragium.util.variant.HTDeviceVariant
import hiiragi283.ragium.util.variant.HTDrumVariant
import hiiragi283.ragium.util.variant.HTGeneratorVariant
import hiiragi283.ragium.util.variant.HTMachineVariant
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemNameBlockItem
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
import kotlin.enums.enumEntries

object RagiumBlocks {
    @JvmField
    val REGISTER = HTDeferredBlockRegister(RagiumAPI.MOD_ID)

    @JvmStatic
    fun init(eventBus: IEventBus) {
        REGISTER.register(eventBus)
    }

    @JvmStatic
    private fun <B : HTEntityBlock> registerEntity(
        type: HTDeferredBlockEntityType<*>,
        properties: BlockBehaviour.Properties,
        factory: (HTDeferredBlockEntityType<*>, BlockBehaviour.Properties) -> B,
    ): HTDeferredBlockHolder<B, BlockItem> =
        REGISTER.registerSimple(type.id.path, properties, { prop: BlockBehaviour.Properties -> factory(type, prop) })

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
    val SILT: HTDeferredBlockHolder<HTSiltBlock, BlockItem> =
        REGISTER.registerSimple("silt", copyOf(Blocks.SAND), ::HTSiltBlock)

    @JvmField
    val CRIMSON_SOIL: HTDeferredBlockHolder<HTCrimsonSoilBlock, BlockItem> =
        REGISTER.registerSimple("crimson_soil", copyOf(Blocks.SOUL_SOIL), ::HTCrimsonSoilBlock)

    @JvmField
    val ASH_LOG: HTDeferredBlockHolder<RotatedPillarBlock, BlockItem> = REGISTER.registerSimple(
        "ash_log",
        BlockBehaviour.Properties
            .of()
            .mapColor(MapColor.COLOR_GRAY)
            .strength(1f)
            .sound(SoundType.SAND),
        ::RotatedPillarBlock,
    )

    @JvmField
    val EXP_BERRIES: HTDeferredBlockHolder<HTExpBerriesBushBlock, ItemNameBlockItem> =
        REGISTER.register("exp_berries", copyOf(Blocks.SWEET_BERRY_BUSH), ::HTExpBerriesBushBlock, ::ItemNameBlockItem)

    @JvmField
    val WARPED_WART: HTDeferredBlockHolder<HTWarpedWartBlock, HTWarpedWartItem> = REGISTER.register(
        "warped_wart",
        copyOf(Blocks.NETHER_WART),
        ::HTWarpedWartBlock,
        ::HTWarpedWartItem,
        Item.Properties().food(RagiumFoods.WARPED_WART),
    )

    @JvmField
    val RESONANT_DEBRIS: HTSimpleDeferredBlockHolder =
        REGISTER.registerSimple("resonant_debris", copyOf(Blocks.ANCIENT_DEBRIS))

    @JvmField
    val MYSTERIOUS_OBSIDIAN: HTSimpleDeferredBlockHolder =
        REGISTER.registerSimple("mysterious_obsidian", copyOf(Blocks.OBSIDIAN))

    // val ELDRITCH_PORTAL: DeferredBlock<Block> = REGISTER.registerBlock("eldritch_portal", ::HTEldritchPortalBlock, copyOf(Blocks.END_GATEWAY))

    //    Materials    //

    @JvmField
    val ORES: HTTable<HTMaterialVariant, HTMaterialType, HTSimpleDeferredBlockHolder> = buildTable {
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
                    REGISTER.registerSimple(pattern.replace("%s", material.serializedName), copyOf(stone)),
                )
            }
            register(RagiumMaterialType.RAGINITE)
            register(RagiumMaterialType.RAGI_CRYSTAL)
            register(RagiumMaterialType.CRIMSON_CRYSTAL)
            register(RagiumMaterialType.WARPED_CRYSTAL)
        }
    }

    @JvmField
    val MATERIALS: HTTable<HTMaterialVariant, HTMaterialType, HTSimpleDeferredBlockHolder> = buildTable {
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
            RagiumMaterialType.IRIDESCENTIUM to copyOf(Blocks.IRON_BLOCK),
            // Foods
            RagiumMaterialType.CHOCOLATE to copyOf(Blocks.MUD, MapColor.TERRACOTTA_BROWN),
            RagiumMaterialType.MEAT to copyOf(Blocks.MUD).sound(SoundType.HONEY_BLOCK),
            RagiumMaterialType.COOKED_MEAT to copyOf(Blocks.PACKED_MUD).sound(SoundType.HONEY_BLOCK),
            // Misc
            RagiumMaterialType.PLASTIC to copyOf(Blocks.TUFF, MapColor.NONE),
        ).forEach { (material: RagiumMaterialType, properties: BlockBehaviour.Properties) ->
            put(HTMaterialVariant.STORAGE_BLOCK, material, REGISTER.registerSimple("${material.serializedName}_block", properties))
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
                REGISTER.registerSimple(
                    "${material.serializedName}_glass",
                    properties.apply { if (blastProof) strength(5f, 1200f) },
                    { prop: BlockBehaviour.Properties -> HTGlassBlock(canPlayerThrough, prop) },
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
                REGISTER.registerSimple(
                    "tinted_${material.serializedName}_glass",
                    properties.apply { if (blastProof) strength(5f, 1200f) },
                    { prop: BlockBehaviour.Properties -> HTTintedGlassBlock(canPlayerThrough, prop) },
                ),
            )
        }

        tintedGlass(HTVanillaMaterialType.QUARTZ, glass(), canPlayerThrough = false, blastProof = false)
        tintedGlass(HTVanillaMaterialType.SOUL, glass(), canPlayerThrough = true, blastProof = false)
        tintedGlass(HTVanillaMaterialType.OBSIDIAN, glass(), canPlayerThrough = false, blastProof = true)
    }

    @JvmStatic
    fun getMaterial(variant: HTMaterialVariant, material: HTMaterialType): HTSimpleDeferredBlockHolder = MATERIALS.get(variant, material)
        ?: error("Unregistered ${variant.serializedName} block for ${material.serializedName}")

    @JvmStatic
    fun getStorageBlock(material: HTMaterialType): HTSimpleDeferredBlockHolder = getMaterial(HTMaterialVariant.STORAGE_BLOCK, material)

    @JvmStatic
    fun getGlass(material: HTMaterialType): HTSimpleDeferredBlockHolder = getMaterial(HTMaterialVariant.GLASS_BLOCK, material)

    @JvmStatic
    fun getTintedGlass(material: HTMaterialType): HTSimpleDeferredBlockHolder = getMaterial(HTMaterialVariant.TINTED_GLASS_BLOCK, material)

    //    Buildings    //

    @JvmField
    val RAGI_BRICKS: HTSimpleDeferredBlockHolder =
        REGISTER.registerSimple("ragi_bricks", copyOf(Blocks.BRICKS, MapColor.COLOR_RED))

    @JvmField
    val AZURE_TILES: HTSimpleDeferredBlockHolder =
        REGISTER.registerSimple("azure_tiles", copyOf(Blocks.STONE, MapColor.TERRACOTTA_BLUE))

    @JvmField
    val ELDRITCH_STONE: HTSimpleDeferredBlockHolder =
        REGISTER.registerSimple("eldritch_stone", copyOf(Blocks.BLACKSTONE, MapColor.COLOR_PURPLE))

    @JvmField
    val POLISHED_ELDRITCH_STONE: HTSimpleDeferredBlockHolder =
        REGISTER.registerSimple("polished_eldritch_stone", copyOf(Blocks.BLACKSTONE, MapColor.COLOR_PURPLE))

    @JvmField
    val POLISHED_ELDRITCH_STONE_BRICKS: HTSimpleDeferredBlockHolder =
        REGISTER.registerSimple("polished_eldritch_stone_bricks", copyOf(Blocks.BLACKSTONE, MapColor.COLOR_PURPLE))

    @JvmField
    val PLASTIC_BRICKS: HTSimpleDeferredBlockHolder =
        REGISTER.registerSimple("plastic_bricks", copyOf(Blocks.BRICKS, MapColor.NONE))

    @JvmField
    val PLASTIC_TILES: HTSimpleDeferredBlockHolder =
        REGISTER.registerSimple("plastic_tiles", copyOf(Blocks.BRICKS, MapColor.NONE))

    @JvmField
    val BLUE_NETHER_BRICKS: HTSimpleDeferredBlockHolder =
        REGISTER.registerSimple("blue_nether_bricks", copyOf(Blocks.NETHER_BRICKS, MapColor.COLOR_BLUE))

    @JvmField
    val SPONGE_CAKE: HTSimpleDeferredBlockHolder = REGISTER.registerSimple("sponge_cake", copyOf(Blocks.YELLOW_WOOL))

    @JvmField
    val DECORATION_MAP: Map<HTDecorationVariant, HTSimpleDeferredBlockHolder> = mapOf(
        HTDecorationVariant.RAGI_BRICK to RAGI_BRICKS,
        HTDecorationVariant.AZURE_TILE to AZURE_TILES,
        HTDecorationVariant.ELDRITCH_STONE to ELDRITCH_STONE,
        HTDecorationVariant.POLISHED_ELDRITCH_STONE to POLISHED_ELDRITCH_STONE,
        HTDecorationVariant.POLISHED_ELDRITCH_STONE_BRICK to POLISHED_ELDRITCH_STONE_BRICKS,
        HTDecorationVariant.PLASTIC_BRICK to PLASTIC_BRICKS,
        HTDecorationVariant.PLASTIC_TILE to PLASTIC_TILES,
        HTDecorationVariant.BLUE_NETHER_BRICK to BLUE_NETHER_BRICKS,
        HTDecorationVariant.SPONGE_CAKE to SPONGE_CAKE,
    )

    @JvmField
    val SLABS: Map<HTDecorationVariant, HTDeferredBlockHolder<SlabBlock, BlockItem>> =
        DECORATION_MAP.mapValues { (variant: HTDecorationVariant, _) ->
            REGISTER.registerSimple(
                "${variant.serializedName}_slab",
                { SlabBlock(copyOf(variant.base.get())) },
            )
        }

    @JvmField
    val STAIRS: Map<HTDecorationVariant, HTDeferredBlockHolder<StairBlock, BlockItem>> =
        DECORATION_MAP.mapValues { (variant: HTDecorationVariant, base: HTSimpleDeferredBlockHolder) ->
            REGISTER.registerSimple(
                "${variant.serializedName}_stairs",
                { StairBlock(base.get().defaultBlockState(), copyOf(variant.base.get())) },
            )
        }

    @JvmField
    val WALLS: Map<HTDecorationVariant, HTDeferredBlockHolder<WallBlock, BlockItem>> =
        DECORATION_MAP.mapValues { (variant: HTDecorationVariant, _) ->
            REGISTER.registerSimple(
                "${variant.serializedName}_wall",
                { WallBlock(copyOf(variant.base.get()).forceSolidOn()) },
            )
        }

    @JvmField
    val LED_BLOCKS: Map<HTColorVariant, HTSimpleDeferredBlockHolder> =
        HTColorVariant.entries.associateWith { color: HTColorVariant ->
            REGISTER.registerSimple("${color.serializedName}_led_block", glass().mapColor(color.color).lightLevel { 15 })
        }

    @JvmStatic
    fun getLedBlock(color: HTColorVariant): HTSimpleDeferredBlockHolder = LED_BLOCKS[color]!!

    //    Foods    //

    @JvmField
    val SWEET_BERRIES_CAKE: HTDeferredBlockHolder<HTSweetBerriesCakeBlock, BlockItem> = REGISTER.registerSimple(
        "sweet_berries_cake",
        copyOf(Blocks.YELLOW_WOOL).forceSolidOn(),
        ::HTSweetBerriesCakeBlock,
    )

    //    Generators    //

    @JvmField
    val GENERATORS: Map<HTGeneratorVariant, HTDeferredBlockHolder<HTEntityBlock, BlockItem>> =
        createMap<HTGeneratorVariant>(machineProperty(), ::HTHorizontalEntityBlock)

    //    Machines    //

    @JvmField
    val BASIC_MACHINE_FRAME: HTDeferredBlockHolder<TransparentBlock, BlockItem> =
        REGISTER.registerSimple("basic_machine_frame", copyOf(Blocks.IRON_BLOCK).noOcclusion(), ::TransparentBlock)

    @JvmField
    val ADVANCED_MACHINE_FRAME: HTDeferredBlockHolder<TransparentBlock, BlockItem> =
        REGISTER.registerSimple("advanced_machine_frame", copyOf(Blocks.IRON_BLOCK).noOcclusion(), ::TransparentBlock)

    @JvmField
    val ELITE_MACHINE_FRAME: HTDeferredBlockHolder<TransparentBlock, BlockItem> =
        REGISTER.registerSimple("elite_machine_frame", machineProperty(), ::TransparentBlock)

    @JvmField
    val FRAMES: List<HTDeferredBlockHolder<TransparentBlock, BlockItem>> = listOf(
        BASIC_MACHINE_FRAME,
        ADVANCED_MACHINE_FRAME,
        ELITE_MACHINE_FRAME,
    )

    @JvmField
    val MACHINES: Map<HTMachineVariant, HTDeferredBlockHolder<HTEntityBlock, BlockItem>> =
        createMap<HTMachineVariant>(machineProperty(), ::HTHorizontalEntityBlock)

    //    Devices    //

    @JvmField
    val WOODEN_CASING: HTSimpleDeferredBlockHolder =
        REGISTER.registerSimple("wooden_casing", copyOf(Blocks.NOTE_BLOCK))

    @JvmField
    val STONE_CASING: HTSimpleDeferredBlockHolder =
        REGISTER.registerSimple("stone_casing", copyOf(Blocks.COBBLESTONE))

    @JvmField
    val REINFORCED_STONE_CASING: HTSimpleDeferredBlockHolder =
        REGISTER.registerSimple("reinforced_stone_casing", copyOf(Blocks.COBBLED_DEEPSLATE))

    @JvmField
    val DEVICE_CASING: HTSimpleDeferredBlockHolder =
        REGISTER.registerSimple("device_casing", machineProperty())

    @JvmField
    val CASINGS: List<HTSimpleDeferredBlockHolder> = listOf(
        WOODEN_CASING,
        STONE_CASING,
        REINFORCED_STONE_CASING,
        DEVICE_CASING,
    )

    @JvmField
    val DEVICES: Map<HTDeviceVariant, HTDeferredBlockHolder<HTEntityBlock, BlockItem>> =
        createMap<HTDeviceVariant>(machineProperty(), HTEntityBlock::Simple)

    @JvmStatic
    private inline fun <reified V> createMap(
        properties: BlockBehaviour.Properties,
        noinline factory: (HTDeferredBlockEntityType<*>, BlockBehaviour.Properties) -> HTEntityBlock,
    ): Map<V, HTDeferredBlockHolder<HTEntityBlock, BlockItem>> where V : HTVariantKey.WithBE<*>, V : Enum<V> =
        enumEntries<V>().associateWith { variant: V ->
            val type: HTDeferredBlockEntityType<*> = variant.blockEntityHolder
            registerEntity(type, properties, factory)
        }

    //    Storages    //

    @JvmField
    val DRUMS: Map<HTDrumVariant, HTDeferredBlockHolder<HTDrumBlock, BlockItem>> = HTDrumVariant.entries
        .associateWith { variant: HTDrumVariant ->
            val base: Block = when (variant) {
                HTDrumVariant.SMALL -> Blocks.IRON_BLOCK
                HTDrumVariant.MEDIUM -> Blocks.GOLD_BLOCK
                HTDrumVariant.LARGE -> Blocks.DIAMOND_BLOCK
                HTDrumVariant.HUGE -> Blocks.NETHERITE_BLOCK
            }
            val type: HTDeferredBlockEntityType<HTDrumBlockEntity> = variant.blockEntityHolder
            registerEntity(type, copyOf(base), ::HTDrumBlock)
        }
}
