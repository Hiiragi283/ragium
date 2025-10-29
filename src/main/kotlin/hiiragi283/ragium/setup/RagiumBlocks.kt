package hiiragi283.ragium.setup

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.type.HTEntityBlockType
import hiiragi283.ragium.api.collection.ImmutableTable
import hiiragi283.ragium.api.collection.buildTable
import hiiragi283.ragium.api.function.partially1
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.registry.impl.HTBasicDeferredBlock
import hiiragi283.ragium.api.registry.impl.HTDeferredBlock
import hiiragi283.ragium.api.registry.impl.HTDeferredBlockRegister
import hiiragi283.ragium.api.registry.impl.HTSimpleDeferredBlock
import hiiragi283.ragium.api.variant.HTMaterialVariant
import hiiragi283.ragium.common.block.AzureClusterBlock
import hiiragi283.ragium.common.block.HTCrimsonSoilBlock
import hiiragi283.ragium.common.block.HTEnchantPowerBlock
import hiiragi283.ragium.common.block.HTExpBerriesBushBlock
import hiiragi283.ragium.common.block.HTGlassBlock
import hiiragi283.ragium.common.block.HTSiltBlock
import hiiragi283.ragium.common.block.HTSimpleTypedEntityBlock
import hiiragi283.ragium.common.block.HTSpongeCakeBlock
import hiiragi283.ragium.common.block.HTSweetBerriesCakeBlock
import hiiragi283.ragium.common.block.HTTintedGlassBlock
import hiiragi283.ragium.common.block.HTTypedEntityBlock
import hiiragi283.ragium.common.block.HTWarpedWartBlock
import hiiragi283.ragium.common.block.consumer.HTRefineryBlock
import hiiragi283.ragium.common.block.storage.HTCrateBlock
import hiiragi283.ragium.common.block.storage.HTDrumBlock
import hiiragi283.ragium.common.item.block.HTCrateBlockItem
import hiiragi283.ragium.common.item.block.HTDrumBlockItem
import hiiragi283.ragium.common.item.block.HTExpBerriesItem
import hiiragi283.ragium.common.item.block.HTMachineBlockItem
import hiiragi283.ragium.common.item.block.HTWarpedWartItem
import hiiragi283.ragium.common.material.HTColorMaterial
import hiiragi283.ragium.common.material.HTVanillaMaterialType
import hiiragi283.ragium.common.material.RagiumMaterialType
import hiiragi283.ragium.common.tier.HTCrateTier
import hiiragi283.ragium.common.tier.HTDrumTier
import hiiragi283.ragium.common.variant.HTDecorationVariant
import hiiragi283.ragium.common.variant.HTGlassVariant
import hiiragi283.ragium.common.variant.HTOreVariant
import hiiragi283.ragium.common.variant.HTStorageMaterialVariant
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
import java.util.function.UnaryOperator

object RagiumBlocks {
    @JvmField
    val REGISTER = HTDeferredBlockRegister(RagiumAPI.MOD_ID)

    @JvmStatic
    fun init(eventBus: IEventBus) {
        REGISTER.register(eventBus)

        // Eldritch Stone
        REGISTER.addFirstAlias("polished_eldritch_stone", "eldritch_stone")
        REGISTER.addFirstAlias("polished_eldritch_stone_bricks", "eldritch_stone_bricks")

        for (suffix: String in listOf("_slab", "_stairs", "_wall")) {
            REGISTER.addFirstAlias("polished_eldritch_stone$suffix", "eldritch_stone$suffix")
            REGISTER.addFirstAlias("polished_eldritch_stone_brick$suffix", "eldritch_stone_brick$suffix")
        }
    }

    @JvmStatic
    fun <BLOCK : HTTypedEntityBlock<*>> registerMachineTier(
        name: String,
        blockGetter: () -> BLOCK,
    ): HTDeferredBlock<BLOCK, HTMachineBlockItem> = REGISTER.register(name, blockGetter, ::HTMachineBlockItem)

    @JvmStatic
    fun registerMachineTier(
        name: String,
        blockType: HTEntityBlockType,
        properties: BlockBehaviour.Properties,
    ): HTDeferredBlock<HTSimpleTypedEntityBlock, HTMachineBlockItem> =
        registerMachineTier(name) { HTTypedEntityBlock(blockType, properties) }

    @JvmStatic
    fun registerSimpleEntity(
        name: String,
        blockType: HTEntityBlockType,
        operator: UnaryOperator<BlockBehaviour.Properties>,
    ): HTBasicDeferredBlock<HTSimpleTypedEntityBlock> = REGISTER.registerSimple(name, { HTTypedEntityBlock(blockType, operator) })

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
    val AZURE_CLUSTER: HTBasicDeferredBlock<AzureClusterBlock> = REGISTER.registerSimple(
        "azure_cluster",
        copyOf(Blocks.AMETHYST_CLUSTER, MapColor.TERRACOTTA_BLUE),
        ::AzureClusterBlock,
    )

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
    val MYSTERIOUS_OBSIDIAN: HTBasicDeferredBlock<HTEnchantPowerBlock> =
        REGISTER.registerSimple("mysterious_obsidian", copyOf(Blocks.OBSIDIAN), ::HTEnchantPowerBlock.partially1(15f))

    // val ELDRITCH_PORTAL: DeferredBlock<Block> = REGISTER.registerBlock("eldritch_portal", ::HTEldritchPortalBlock, copyOf(Blocks.END_GATEWAY))

    //    Materials    //

    val ORES: ImmutableTable<HTOreVariant, HTMaterialType, HTSimpleDeferredBlock> = buildTable {
        HTOreVariant.entries.forEach { variant: HTOreVariant ->
            val pattern: String = when (variant) {
                HTOreVariant.Default -> "%s_ore"
                HTOreVariant.Others.DEEP -> "deepslate_%s_ore"
                HTOreVariant.Others.NETHER -> "nether_%s_ore"
                HTOreVariant.Others.END -> "end_%s_ore"
            }
            val stone: Block = when (variant) {
                HTOreVariant.Default -> Blocks.DIAMOND_ORE
                HTOreVariant.Others.DEEP -> Blocks.DEEPSLATE_DIAMOND_ORE
                HTOreVariant.Others.NETHER -> Blocks.NETHER_QUARTZ_ORE
                HTOreVariant.Others.END -> Blocks.END_STONE
            } ?: return@forEach

            fun register(material: HTMaterialType) {
                this[variant, material] = REGISTER.registerSimple(pattern.replace("%s", material.materialName()), copyOf(stone))
            }
            register(RagiumMaterialType.RAGINITE)
            register(RagiumMaterialType.RAGI_CRYSTAL)
            register(RagiumMaterialType.CRIMSON_CRYSTAL)
            register(RagiumMaterialType.WARPED_CRYSTAL)
        }
    }

    val MATERIALS: ImmutableTable<HTMaterialVariant.BlockTag, HTMaterialType, HTSimpleDeferredBlock> = buildTable {
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
            this[HTStorageMaterialVariant, material] =
                REGISTER.registerSimple("${material.materialName()}_block", properties)
        }

        // Glasses
        fun glass(
            material: HTMaterialType,
            properties: BlockBehaviour.Properties,
            canPlayerThrough: Boolean,
            blastProof: Boolean,
        ) {
            this[HTGlassVariant.COLORLESS, material] = REGISTER.registerSimple(
                "${material.materialName()}_glass",
                properties.apply { if (blastProof) strength(5f, 1200f) },
                ::HTGlassBlock.partially1(canPlayerThrough),
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
            this[HTGlassVariant.TINTED, material] = REGISTER.registerSimple(
                "tinted_${material.materialName()}_glass",
                properties.apply { if (blastProof) strength(5f, 1200f) },
                ::HTTintedGlassBlock.partially1(canPlayerThrough),
            )
        }

        tintedGlass(HTVanillaMaterialType.QUARTZ, glass(), canPlayerThrough = false, blastProof = false)
        tintedGlass(HTVanillaMaterialType.SOUL, glass(), canPlayerThrough = true, blastProof = false)
        tintedGlass(HTVanillaMaterialType.OBSIDIAN, glass(), canPlayerThrough = false, blastProof = true)
    }

    @JvmStatic
    fun getMaterial(variant: HTMaterialVariant.BlockTag, material: HTMaterialType): HTSimpleDeferredBlock = MATERIALS[variant, material]
        ?: error("Unknown ${variant.variantName()} block for ${material.materialName()}")

    @JvmStatic
    fun getStorageBlock(material: HTMaterialType): HTSimpleDeferredBlock = getMaterial(HTStorageMaterialVariant, material)

    @JvmStatic
    fun getGlass(material: HTMaterialType): HTSimpleDeferredBlock = getMaterial(HTGlassVariant.COLORLESS, material)

    @JvmStatic
    fun getTintedGlass(material: HTMaterialType): HTSimpleDeferredBlock = getMaterial(HTGlassVariant.TINTED, material)

    @JvmField
    val COILS: Map<HTMaterialType, HTBasicDeferredBlock<RotatedPillarBlock>> = arrayOf(
        RagiumMaterialType.RAGI_ALLOY,
        RagiumMaterialType.ADVANCED_RAGI_ALLOY,
    ).associateWith { material: HTMaterialType ->
        REGISTER.registerSimple("${material.materialName()}_coil_block", copyOf(Blocks.COPPER_BLOCK), ::RotatedPillarBlock)
    }

    @JvmStatic
    fun getCoilBlock(material: HTMaterialType): HTBasicDeferredBlock<RotatedPillarBlock> = COILS[material]
        ?: error("Unknown coil block for ${material.materialName()}")

    //    Buildings    //

    @JvmField
    val RAGI_BRICKS: HTSimpleDeferredBlock =
        REGISTER.registerSimple("ragi_bricks", copyOf(Blocks.BRICKS, MapColor.COLOR_RED))

    @JvmField
    val AZURE_TILES: HTSimpleDeferredBlock =
        REGISTER.registerSimple("azure_tiles", copyOf(Blocks.STONE, MapColor.TERRACOTTA_BLUE))

    @JvmField
    val ELDRITCH_STONE: HTSimpleDeferredBlock =
        REGISTER.registerSimple("eldritch_stone", copyOf(Blocks.END_STONE, MapColor.COLOR_PURPLE))

    @JvmField
    val ELDRITCH_STONE_BRICKS: HTSimpleDeferredBlock =
        REGISTER.registerSimple("eldritch_stone_bricks", copyOf(Blocks.END_STONE_BRICKS, MapColor.COLOR_PURPLE))

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
        HTDecorationVariant.ELDRITCH_STONE_BRICK to ELDRITCH_STONE_BRICKS,
        HTDecorationVariant.PLASTIC_BRICK to PLASTIC_BRICKS,
        HTDecorationVariant.PLASTIC_TILE to PLASTIC_TILES,
        HTDecorationVariant.BLUE_NETHER_BRICK to BLUE_NETHER_BRICKS,
        HTDecorationVariant.SPONGE_CAKE to SPONGE_CAKE,
    )

    @JvmField
    val SLABS: Map<HTDecorationVariant, HTBasicDeferredBlock<SlabBlock>> =
        HTDecorationVariant.entries.associateWith { variant: HTDecorationVariant ->
            REGISTER.registerSimple("${variant.variantName()}_slab", { copyOf(variant.base.get()) }, ::SlabBlock)
        }

    @JvmField
    val STAIRS: Map<HTDecorationVariant, HTBasicDeferredBlock<StairBlock>> =
        HTDecorationVariant.entries.associateWith { variant: HTDecorationVariant ->
            REGISTER.registerSimple(
                "${variant.variantName()}_stairs",
                {
                    val block: Block = variant.base.get()
                    StairBlock(block.defaultBlockState(), copyOf(block))
                },
            )
        }

    @JvmField
    val WALLS: Map<HTDecorationVariant, HTBasicDeferredBlock<WallBlock>> =
        HTDecorationVariant.entries.associateWith { variant: HTDecorationVariant ->
            REGISTER.registerSimple(
                "${variant.variantName()}_wall",
                { copyOf(variant.base.get()) },
                ::WallBlock,
            )
        }

    @JvmField
    val LED_BLOCKS: Map<HTColorMaterial, HTSimpleDeferredBlock> =
        HTColorMaterial.entries.associateWith { color: HTColorMaterial ->
            REGISTER.registerSimple("${color.materialName()}_led_block", glass().mapColor(color.dyeColor).lightLevel { 15 })
        }

    @JvmStatic
    fun getLedBlock(color: HTColorMaterial): HTSimpleDeferredBlock = LED_BLOCKS[color]!!

    //    Foods    //

    @JvmField
    val SWEET_BERRIES_CAKE: HTBasicDeferredBlock<HTSweetBerriesCakeBlock> =
        REGISTER.registerSimple(
            "sweet_berries_cake",
            { copyOf(SPONGE_CAKE.get()).forceSolidOn() },
            ::HTSweetBerriesCakeBlock,
        )

    //    Generators    //

    // Basic
    @JvmField
    val THERMAL_GENERATOR: HTDeferredBlock<HTSimpleTypedEntityBlock, HTMachineBlockItem> = registerMachineTier(
        "thermal_generator",
        RagiumBlockTypes.THERMAL_GENERATOR,
        machineProperty().noOcclusion(),
    )

    // Advanced
    @JvmField
    val COMBUSTION_GENERATOR: HTDeferredBlock<HTSimpleTypedEntityBlock, HTMachineBlockItem> = registerMachineTier(
        "combustion_generator",
        RagiumBlockTypes.COMBUSTION_GENERATOR,
        machineProperty().noOcclusion(),
    )

    // Elite
    @JvmField
    val SOLAR_PANEL_CONTROLLER: HTDeferredBlock<HTSimpleTypedEntityBlock, HTMachineBlockItem> = registerMachineTier(
        "solar_panel_controller",
        RagiumBlockTypes.SOLAR_PANEL_CONTROLLER,
        machineProperty().noOcclusion(),
    )

    // Ultimate
    @JvmField
    val ENCHANTMENT_GENERATOR: HTDeferredBlock<HTSimpleTypedEntityBlock, HTMachineBlockItem> = registerMachineTier(
        "enchantment_generator",
        RagiumBlockTypes.ENCHANTMENT_GENERATOR,
        machineProperty().noOcclusion(),
    )

    @JvmField
    val NUCLEAR_REACTOR: HTDeferredBlock<HTSimpleTypedEntityBlock, HTMachineBlockItem> = registerMachineTier(
        "nuclear_reactor",
        RagiumBlockTypes.NUCLEAR_REACTOR,
        machineProperty().noOcclusion(),
    )

    //    Consumer    //

    // Basic
    @JvmField
    val ALLOY_SMELTER: HTDeferredBlock<HTSimpleTypedEntityBlock, HTMachineBlockItem> =
        registerMachineTier("alloy_smelter", RagiumBlockTypes.ALLOY_SMELTER, machineProperty())

    @JvmField
    val BLOCK_BREAKER: HTDeferredBlock<HTSimpleTypedEntityBlock, HTMachineBlockItem> =
        registerMachineTier("block_breaker", RagiumBlockTypes.BLOCK_BREAKER, machineProperty())

    @JvmField
    val CUTTING_MACHINE: HTDeferredBlock<HTSimpleTypedEntityBlock, HTMachineBlockItem> =
        registerMachineTier("cutting_machine", RagiumBlockTypes.CUTTING_MACHINE, machineProperty())

    @JvmField
    val COMPRESSOR: HTDeferredBlock<HTSimpleTypedEntityBlock, HTMachineBlockItem> =
        registerMachineTier("compressor", RagiumBlockTypes.COMPRESSOR, machineProperty())

    @JvmField
    val EXTRACTOR: HTDeferredBlock<HTSimpleTypedEntityBlock, HTMachineBlockItem> =
        registerMachineTier("extractor", RagiumBlockTypes.EXTRACTOR, machineProperty())

    @JvmField
    val PULVERIZER: HTDeferredBlock<HTSimpleTypedEntityBlock, HTMachineBlockItem> =
        registerMachineTier("pulverizer", RagiumBlockTypes.PULVERIZER, machineProperty())

    // Advanced
    @JvmField
    val CRUSHER: HTDeferredBlock<HTSimpleTypedEntityBlock, HTMachineBlockItem> =
        registerMachineTier("crusher", RagiumBlockTypes.CRUSHER, machineProperty())

    @JvmField
    val MELTER: HTDeferredBlock<HTSimpleTypedEntityBlock, HTMachineBlockItem> =
        registerMachineTier("melter", RagiumBlockTypes.MELTER, machineProperty().noOcclusion())

    @JvmField
    val REFINERY: HTDeferredBlock<HTSimpleTypedEntityBlock, HTMachineBlockItem> =
        registerMachineTier("refinery") { HTRefineryBlock(RagiumBlockTypes.REFINERY, machineProperty().noOcclusion()) }

    @JvmField
    val WASHER: HTDeferredBlock<HTSimpleTypedEntityBlock, HTMachineBlockItem> =
        registerMachineTier("washer", RagiumBlockTypes.WASHER, machineProperty().noOcclusion())

    // Elite
    @JvmField
    val BREWERY: HTDeferredBlock<HTSimpleTypedEntityBlock, HTMachineBlockItem> =
        registerMachineTier("brewery", RagiumBlockTypes.BREWERY, machineProperty())

    @JvmField
    val MULTI_SMELTER: HTDeferredBlock<HTSimpleTypedEntityBlock, HTMachineBlockItem> =
        registerMachineTier("multi_smelter", RagiumBlockTypes.MULTI_SMELTER, machineProperty())

    @JvmField
    val PLANTER: HTDeferredBlock<HTSimpleTypedEntityBlock, HTMachineBlockItem> =
        registerMachineTier("planter", RagiumBlockTypes.PLANTER, machineProperty().noOcclusion())

    @JvmField
    val SIMULATOR: HTDeferredBlock<HTSimpleTypedEntityBlock, HTMachineBlockItem> =
        registerMachineTier("simulator", RagiumBlockTypes.SIMULATOR, machineProperty().noOcclusion())

    // Ultimate

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
    val CASINGS: List<HTSimpleDeferredBlock> = listOf(
        WOODEN_CASING,
        STONE_CASING,
        REINFORCED_STONE_CASING,
    )

    //    Device    //

    @JvmField
    val DEVICE_CASING: HTSimpleDeferredBlock =
        REGISTER.registerSimple("device_casing", machineProperty())

    // Basic
    @JvmField
    val ITEM_BUFFER: HTDeferredBlock<HTSimpleTypedEntityBlock, HTMachineBlockItem> =
        registerMachineTier("item_buffer", RagiumBlockTypes.ITEM_BUFFER, machineProperty())

    @JvmField
    val MILK_COLLECTOR: HTDeferredBlock<HTSimpleTypedEntityBlock, HTMachineBlockItem> =
        registerMachineTier("milk_collector", RagiumBlockTypes.MILK_COLLECTOR, machineProperty())

    @JvmField
    val WATER_COLLECTOR: HTDeferredBlock<HTSimpleTypedEntityBlock, HTMachineBlockItem> =
        registerMachineTier("water_collector", RagiumBlockTypes.WATER_COLLECTOR, machineProperty())

    // Advanced
    @JvmField
    val EXP_COLLECTOR: HTDeferredBlock<HTSimpleTypedEntityBlock, HTMachineBlockItem> =
        registerMachineTier("exp_collector", RagiumBlockTypes.EXP_COLLECTOR, machineProperty())

    @JvmField
    val LAVA_COLLECTOR: HTDeferredBlock<HTSimpleTypedEntityBlock, HTMachineBlockItem> =
        registerMachineTier("lava_collector", RagiumBlockTypes.LAVA_COLLECTOR, machineProperty())

    // Elite
    @JvmField
    val DIM_ANCHOR: HTDeferredBlock<HTSimpleTypedEntityBlock, HTMachineBlockItem> =
        registerMachineTier("dimensional_anchor", RagiumBlockTypes.DIM_ANCHOR, machineProperty())

    @JvmField
    val ENI: HTDeferredBlock<HTSimpleTypedEntityBlock, HTMachineBlockItem> =
        registerMachineTier("energy_network_interface", RagiumBlockTypes.ENI, machineProperty())

    // Ultimate
    @JvmField
    val MOB_CAPTURER: HTDeferredBlock<HTSimpleTypedEntityBlock, HTMachineBlockItem> =
        registerMachineTier("mob_capturer", RagiumBlockTypes.MOB_CAPTURER, machineProperty())

    @JvmField
    val TELEPAD: HTDeferredBlock<HTSimpleTypedEntityBlock, HTMachineBlockItem> =
        registerMachineTier("telepad", RagiumBlockTypes.TELEPAD, machineProperty())

    // Creative
    @JvmField
    val CEU: HTDeferredBlock<HTSimpleTypedEntityBlock, HTMachineBlockItem> =
        registerMachineTier("creative_energy_unit", RagiumBlockTypes.CEU, machineProperty())

    //    Storages    //

    @JvmField
    val CRATES: Map<HTCrateTier, HTDeferredBlock<HTCrateBlock, HTCrateBlockItem>> =
        HTCrateTier.entries.associateWith { tier: HTCrateTier ->
            REGISTER.register(
                tier.path,
                when (tier) {
                    HTCrateTier.SMALL -> Blocks.IRON_BLOCK
                    HTCrateTier.MEDIUM -> Blocks.GOLD_BLOCK
                    HTCrateTier.LARGE -> Blocks.DIAMOND_BLOCK
                    HTCrateTier.HUGE -> Blocks.NETHERITE_BLOCK
                }.let(::copyOf),
                { HTCrateBlock(tier, it) },
                ::HTCrateBlockItem,
            )
        }

    @JvmField
    val DRUMS: Map<HTDrumTier, HTDeferredBlock<HTDrumBlock, HTDrumBlockItem>> =
        HTDrumTier.entries.associateWith { tier: HTDrumTier ->
            REGISTER.register(
                tier.path,
                when (tier) {
                    HTDrumTier.MEDIUM -> Blocks.GOLD_BLOCK
                    HTDrumTier.LARGE -> Blocks.DIAMOND_BLOCK
                    HTDrumTier.HUGE -> Blocks.NETHERITE_BLOCK
                    else -> Blocks.IRON_BLOCK
                }.let(::copyOf),
                { HTDrumBlock(tier, it) },
                ::HTDrumBlockItem,
            )
        }
}
