package hiiragi283.ragium.setup

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.HTEntityBlock
import hiiragi283.ragium.api.block.HTHorizontalEntityBlock
import hiiragi283.ragium.api.collection.HTTable
import hiiragi283.ragium.api.extension.andThen
import hiiragi283.ragium.api.extension.buildTable
import hiiragi283.ragium.api.extension.partially1
import hiiragi283.ragium.api.material.HTBlockMaterialVariant
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.material.HTMaterialVariant
import hiiragi283.ragium.api.registry.impl.HTBasicDeferredBlock
import hiiragi283.ragium.api.registry.impl.HTDeferredBlock
import hiiragi283.ragium.api.registry.impl.HTDeferredBlockEntityType
import hiiragi283.ragium.api.registry.impl.HTDeferredBlockRegister
import hiiragi283.ragium.api.registry.impl.HTSimpleDeferredBlock
import hiiragi283.ragium.api.variant.HTVariantKey
import hiiragi283.ragium.common.block.HTCrimsonSoilBlock
import hiiragi283.ragium.common.block.HTDrumBlock
import hiiragi283.ragium.common.block.HTExpBerriesBushBlock
import hiiragi283.ragium.common.block.HTGlassBlock
import hiiragi283.ragium.common.block.HTSiltBlock
import hiiragi283.ragium.common.block.HTSpongeCakeBlock
import hiiragi283.ragium.common.block.HTSweetBerriesCakeBlock
import hiiragi283.ragium.common.block.HTTintedGlassBlock
import hiiragi283.ragium.common.block.HTWarpedWartBlock
import hiiragi283.ragium.common.block.entity.HTBlockEntity
import hiiragi283.ragium.common.item.block.HTDrumItem
import hiiragi283.ragium.common.item.block.HTExpBerriesItem
import hiiragi283.ragium.common.item.block.HTWarpedWartItem
import hiiragi283.ragium.common.material.HTVanillaMaterialType
import hiiragi283.ragium.common.material.RagiumMaterialType
import hiiragi283.ragium.common.variant.HTColorMaterial
import hiiragi283.ragium.common.variant.HTDecorationVariant
import hiiragi283.ragium.common.variant.HTDeviceVariant
import hiiragi283.ragium.common.variant.HTDrumVariant
import hiiragi283.ragium.common.variant.HTGeneratorVariant
import hiiragi283.ragium.common.variant.HTMachineVariant
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.RotatedPillarBlock
import net.minecraft.world.level.block.SlabBlock
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.StairBlock
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
    ): HTBasicDeferredBlock<B> = REGISTER.registerSimple(type.getPath(), properties, factory.partially1(type))

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
    val SILT: HTBasicDeferredBlock<HTSiltBlock> = REGISTER.registerSimple("silt", copyOf(Blocks.SAND), ::HTSiltBlock)

    @JvmField
    val CRIMSON_SOIL: HTBasicDeferredBlock<HTCrimsonSoilBlock> = REGISTER.registerSimple(
        "crimson_soil",
        copyOf(Blocks.SOUL_SOIL),
        ::HTCrimsonSoilBlock,
    )

    @JvmField
    val EXP_BERRIES: HTDeferredBlock<HTExpBerriesBushBlock, HTExpBerriesItem> =
        REGISTER.register("exp_berries", copyOf(Blocks.SWEET_BERRY_BUSH), ::HTExpBerriesBushBlock, ::HTExpBerriesItem)

    @JvmField
    val WARPED_WART: HTDeferredBlock<HTWarpedWartBlock, HTWarpedWartItem> = REGISTER.register(
        "warped_wart",
        copyOf(Blocks.NETHER_WART),
        ::HTWarpedWartBlock,
        ::HTWarpedWartItem,
        Item.Properties().food(RagiumFoods.WARPED_WART),
    )

    @JvmField
    val RESONANT_DEBRIS: HTSimpleDeferredBlock =
        REGISTER.registerSimple("resonant_debris", copyOf(Blocks.ANCIENT_DEBRIS))

    @JvmField
    val MYSTERIOUS_OBSIDIAN: HTSimpleDeferredBlock =
        REGISTER.registerSimple("mysterious_obsidian", copyOf(Blocks.OBSIDIAN))

    // val ELDRITCH_PORTAL: DeferredBlock<Block> = REGISTER.registerBlock("eldritch_portal", ::HTEldritchPortalBlock, copyOf(Blocks.END_GATEWAY))

    //    Materials    //

    @JvmField
    val ORES: HTTable<HTMaterialVariant.BlockTag, HTMaterialType, HTSimpleDeferredBlock> = buildTable {
        listOf(
            HTBlockMaterialVariant.ORE,
            HTBlockMaterialVariant.DEEP_ORE,
            HTBlockMaterialVariant.NETHER_ORE,
            HTBlockMaterialVariant.END_ORE,
        ).forEach { variant: HTBlockMaterialVariant ->
            val pattern: String = when (variant) {
                HTBlockMaterialVariant.ORE -> "%s_ore"
                HTBlockMaterialVariant.DEEP_ORE -> "deepslate_%s_ore"
                HTBlockMaterialVariant.NETHER_ORE -> "nether_%s_ore"
                HTBlockMaterialVariant.END_ORE -> "end_%s_ore"
                else -> return@forEach
            }
            val stone: Block = when (variant) {
                HTBlockMaterialVariant.ORE -> Blocks.DIAMOND_ORE
                HTBlockMaterialVariant.DEEP_ORE -> Blocks.DEEPSLATE_DIAMOND_ORE
                HTBlockMaterialVariant.NETHER_ORE -> Blocks.NETHER_QUARTZ_ORE
                HTBlockMaterialVariant.END_ORE -> Blocks.END_STONE
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
    val MATERIALS: HTTable<HTMaterialVariant.BlockTag, HTMaterialType, HTSimpleDeferredBlock> = buildTable {
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
            RagiumMaterialType.GILDIUM to copyOf(Blocks.GOLD_BLOCK),
            RagiumMaterialType.IRIDESCENTIUM to copyOf(Blocks.IRON_BLOCK),
            // Foods
            RagiumMaterialType.CHOCOLATE to copyOf(Blocks.MUD, MapColor.TERRACOTTA_BROWN),
            RagiumMaterialType.MEAT to copyOf(Blocks.MUD).sound(SoundType.HONEY_BLOCK),
            RagiumMaterialType.COOKED_MEAT to copyOf(Blocks.PACKED_MUD).sound(SoundType.HONEY_BLOCK),
            // Misc
            RagiumMaterialType.PLASTIC to copyOf(Blocks.TUFF, MapColor.NONE),
        ).forEach { (material: RagiumMaterialType, properties: BlockBehaviour.Properties) ->
            put(HTBlockMaterialVariant.STORAGE_BLOCK, material, REGISTER.registerSimple("${material.serializedName}_block", properties))
        }

        // Glasses
        fun glass(
            material: HTMaterialType,
            properties: BlockBehaviour.Properties,
            canPlayerThrough: Boolean,
            blastProof: Boolean,
        ) {
            put(
                HTBlockMaterialVariant.GLASS_BLOCK,
                material,
                REGISTER.registerSimple(
                    "${material.serializedName}_glass",
                    properties.apply { if (blastProof) strength(5f, 1200f) },
                    ::HTGlassBlock.partially1(canPlayerThrough),
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
                HTBlockMaterialVariant.TINTED_GLASS_BLOCK,
                material,
                REGISTER.registerSimple(
                    "tinted_${material.serializedName}_glass",
                    properties.apply { if (blastProof) strength(5f, 1200f) },
                    ::HTTintedGlassBlock.partially1(canPlayerThrough),
                ),
            )
        }

        tintedGlass(HTVanillaMaterialType.QUARTZ, glass(), canPlayerThrough = false, blastProof = false)
        tintedGlass(HTVanillaMaterialType.SOUL, glass(), canPlayerThrough = true, blastProof = false)
        tintedGlass(HTVanillaMaterialType.OBSIDIAN, glass(), canPlayerThrough = false, blastProof = true)
    }

    @JvmStatic
    fun getMaterial(variant: HTMaterialVariant.BlockTag, material: HTMaterialType): HTSimpleDeferredBlock = MATERIALS.get(variant, material)
        ?: error("Unknown ${variant.serializedName} block for ${material.serializedName}")

    @JvmStatic
    fun getStorageBlock(material: HTMaterialType): HTSimpleDeferredBlock = getMaterial(HTBlockMaterialVariant.STORAGE_BLOCK, material)

    @JvmStatic
    fun getGlass(material: HTMaterialType): HTSimpleDeferredBlock = getMaterial(HTBlockMaterialVariant.GLASS_BLOCK, material)

    @JvmStatic
    fun getTintedGlass(material: HTMaterialType): HTSimpleDeferredBlock = getMaterial(HTBlockMaterialVariant.TINTED_GLASS_BLOCK, material)

    @JvmField
    val COILS: Map<HTMaterialType, HTBasicDeferredBlock<RotatedPillarBlock>> = listOf(
        RagiumMaterialType.RAGI_ALLOY,
        RagiumMaterialType.ADVANCED_RAGI_ALLOY,
    ).associateWith { material: HTMaterialType ->
        REGISTER.registerSimple("${material.serializedName}_coil_block", copyOf(Blocks.COPPER_BLOCK), ::RotatedPillarBlock)
    }

    @JvmStatic
    fun getCoilBlock(material: HTMaterialType): HTBasicDeferredBlock<RotatedPillarBlock> = COILS[material]
        ?: error("Unknown coil block for ${material.serializedName}")

    //    Buildings    //

    @JvmField
    val RAGI_BRICKS: HTSimpleDeferredBlock =
        REGISTER.registerSimple("ragi_bricks", copyOf(Blocks.BRICKS, MapColor.COLOR_RED))

    @JvmField
    val AZURE_TILES: HTSimpleDeferredBlock =
        REGISTER.registerSimple("azure_tiles", copyOf(Blocks.STONE, MapColor.TERRACOTTA_BLUE))

    @JvmField
    val ELDRITCH_STONE: HTSimpleDeferredBlock =
        REGISTER.registerSimple("eldritch_stone", copyOf(Blocks.BLACKSTONE, MapColor.COLOR_PURPLE))

    @JvmField
    val POLISHED_ELDRITCH_STONE: HTSimpleDeferredBlock =
        REGISTER.registerSimple("polished_eldritch_stone", copyOf(Blocks.BLACKSTONE, MapColor.COLOR_PURPLE))

    @JvmField
    val POLISHED_ELDRITCH_STONE_BRICKS: HTSimpleDeferredBlock =
        REGISTER.registerSimple("polished_eldritch_stone_bricks", copyOf(Blocks.BLACKSTONE, MapColor.COLOR_PURPLE))

    @JvmField
    val PLASTIC_BRICKS: HTSimpleDeferredBlock =
        REGISTER.registerSimple("plastic_bricks", copyOf(Blocks.BRICKS, MapColor.NONE))

    @JvmField
    val PLASTIC_TILES: HTSimpleDeferredBlock =
        REGISTER.registerSimple("plastic_tiles", copyOf(Blocks.BRICKS, MapColor.NONE))

    @JvmField
    val BLUE_NETHER_BRICKS: HTSimpleDeferredBlock =
        REGISTER.registerSimple("blue_nether_bricks", copyOf(Blocks.NETHER_BRICKS, MapColor.COLOR_BLUE))

    @JvmField
    val SPONGE_CAKE: HTBasicDeferredBlock<HTSpongeCakeBlock> =
        REGISTER.registerSimple("sponge_cake", copyOf(Blocks.YELLOW_WOOL), ::HTSpongeCakeBlock)

    @JvmField
    val DECORATION_MAP: Map<HTDecorationVariant, HTDeferredBlock<*, *>> = mapOf(
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
    val SLABS: Map<HTDecorationVariant, HTBasicDeferredBlock<SlabBlock>> =
        HTDecorationVariant.entries.associateWith { variant: HTDecorationVariant ->
            REGISTER.registerSimple(
                "${variant.serializedName}_slab",
                variant.base::get.andThen(::copyOf).andThen(::SlabBlock),
            )
        }

    @JvmField
    val STAIRS: Map<HTDecorationVariant, HTBasicDeferredBlock<StairBlock>> =
        HTDecorationVariant.entries.associateWith { variant: HTDecorationVariant ->
            val base: HTDeferredBlock<*, *> = variant.base
            REGISTER.registerSimple(
                "${variant.serializedName}_stairs",
                {
                    val block: Block = base.get()
                    StairBlock(block.defaultBlockState(), copyOf(block))
                },
            )
        }

    @JvmField
    val WALLS: Map<HTDecorationVariant, HTBasicDeferredBlock<WallBlock>> =
        HTDecorationVariant.entries.associateWith { variant: HTDecorationVariant ->
            REGISTER.registerSimple(
                "${variant.serializedName}_wall",
                variant.base::get.andThen(::copyOf).andThen(::WallBlock),
            )
        }

    @JvmField
    val LED_BLOCKS: Map<HTColorMaterial, HTSimpleDeferredBlock> =
        HTColorMaterial.entries.associateWith { color: HTColorMaterial ->
            REGISTER.registerSimple("${color.serializedName}_led_block", glass().mapColor(color.color).lightLevel { 15 })
        }

    @JvmStatic
    fun getLedBlock(color: HTColorMaterial): HTSimpleDeferredBlock = LED_BLOCKS[color]!!

    //    Foods    //

    @JvmField
    val SWEET_BERRIES_CAKE: HTBasicDeferredBlock<HTSweetBerriesCakeBlock> =
        REGISTER.registerSimple("sweet_berries_cake", copyOf(Blocks.YELLOW_WOOL).forceSolidOn(), ::HTSweetBerriesCakeBlock)

    //    Generators    //

    @JvmField
    val GENERATORS: Map<HTGeneratorVariant, HTBasicDeferredBlock<HTEntityBlock>> =
        createMap<HTGeneratorVariant>(machineProperty(), ::HTHorizontalEntityBlock)

    //    Machines    //

    @JvmField
    val MACHINES: Map<HTMachineVariant, HTBasicDeferredBlock<HTEntityBlock>> =
        createMap<HTMachineVariant>(machineProperty().noOcclusion(), ::HTHorizontalEntityBlock)

    //    Parts    //

    @JvmField
    val WOODEN_CASING: HTSimpleDeferredBlock =
        REGISTER.registerSimple("wooden_casing", copyOf(Blocks.NOTE_BLOCK))

    @JvmField
    val STONE_CASING: HTSimpleDeferredBlock =
        REGISTER.registerSimple("stone_casing", copyOf(Blocks.COBBLESTONE))

    @JvmField
    val REINFORCED_STONE_CASING: HTSimpleDeferredBlock =
        REGISTER.registerSimple("reinforced_stone_casing", copyOf(Blocks.COBBLED_DEEPSLATE))

    @JvmField
    val DEVICE_CASING: HTSimpleDeferredBlock =
        REGISTER.registerSimple("device_casing", machineProperty())

    @JvmField
    val CASINGS: List<HTSimpleDeferredBlock> = listOf(
        WOODEN_CASING,
        STONE_CASING,
        REINFORCED_STONE_CASING,
        DEVICE_CASING,
    )

    @JvmField
    val DEVICES: Map<HTDeviceVariant, HTBasicDeferredBlock<HTEntityBlock>> =
        createMap<HTDeviceVariant>(machineProperty(), HTEntityBlock::Simple)

    @JvmStatic
    private inline fun <reified V> createMap(
        properties: BlockBehaviour.Properties,
        noinline factory: (HTDeferredBlockEntityType<*>, BlockBehaviour.Properties) -> HTEntityBlock,
    ): Map<V, HTBasicDeferredBlock<HTEntityBlock>> where V : HTVariantKey.WithBE<*>, V : Enum<V> =
        enumEntries<V>().associateWith { variant: V ->
            val type: HTDeferredBlockEntityType<*> = variant.blockEntityHolder
            registerEntity(type, properties, factory)
        }

    //    Storages    //

    @JvmField
    val DRUMS: Map<HTDrumVariant, HTDeferredBlock<HTDrumBlock, HTDrumItem>> = HTDrumVariant.entries
        .associateWith { variant: HTDrumVariant ->
            val base: Block = when (variant) {
                HTDrumVariant.SMALL -> Blocks.IRON_BLOCK
                HTDrumVariant.MEDIUM -> Blocks.GOLD_BLOCK
                HTDrumVariant.LARGE -> Blocks.DIAMOND_BLOCK
                HTDrumVariant.HUGE -> Blocks.NETHERITE_BLOCK
            }
            val type: HTDeferredBlockEntityType<HTBlockEntity> = variant.blockEntityHolder
            REGISTER.register(
                type.getPath(),
                { HTDrumBlock(type, copyOf(base)) },
                ::HTDrumItem,
            )
        }
}
