package hiiragi283.ragium.common

import hiiragi283.ragium.common.alchemy.RagiElement
import hiiragi283.ragium.common.block.*
import hiiragi283.ragium.common.init.*
import hiiragi283.ragium.common.item.*
import hiiragi283.ragium.common.machine.HTMachineBlockRegistry
import hiiragi283.ragium.common.machine.HTMachineTier
import hiiragi283.ragium.common.machine.HTMachineType
import hiiragi283.ragium.common.util.*
import net.minecraft.block.*
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.component.type.AttributeModifiersComponent
import net.minecraft.item.*
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.util.Rarity
import java.awt.Color
import kotlin.jvm.optionals.getOrNull

object RagiumContents {
    //    Blocks - Ores    //

    @JvmField
    val RAGINITE_ORE: Block =
        registerCopy("raginite_ore", Blocks.IRON_ORE)

    @JvmField
    val DEEPSLATE_RAGINITE_ORE: Block =
        registerCopy("deepslate_raginite_ore", Blocks.DEEPSLATE_IRON_ORE)

    //    Blocks - Plants    //

    @JvmField
    val RUBBER_LOG: Block =
        registerBlock("rubber_log", Blocks.createLogBlock(MapColor.OAK_TAN, MapColor.SPRUCE_BROWN))

    @JvmField
    val RUBBER_LEAVES: Block =
        registerBlock("rubber_leaves", Blocks.createLeavesBlock(BlockSoundGroup.GRASS))

    @JvmField
    val RUBBER_SAPLING: Block =
        registerBlock("rubber_sapling", SaplingBlock(RagiumSaplingGenerators.RUBBER, blockSettings(Blocks.OAK_SAPLING)))

    //    Blocks - Utilities    //

    @JvmField
    val CREATIVE_SOURCE: Block =
        registerWithBE("creative_source", RagiumBlockEntityTypes.CREATIVE_SOURCE, Blocks.COMMAND_BLOCK)

    @JvmField
    val MANUAL_GRINDER: Block =
        registerBlock("manual_grinder", HTManualGrinderBlock)

    @JvmField
    val BRICK_ALLOY_FURNACE: Block =
        registerHorizontalWithBE(
            "brick_alloy_furnace",
            RagiumBlockEntityTypes.BRICK_ALLOY_FURNACE,
            Blocks.BRICKS,
        )

    @JvmField
    val BURNING_BOX: Block =
        registerHorizontalWithBE("burning_box", RagiumBlockEntityTypes.BURNING_BOX, Blocks.BRICKS)

    @JvmField
    val WATER_GENERATOR: Block =
        registerHorizontalWithBE("water_generator", RagiumBlockEntityTypes.WATER_GENERATOR)

    @JvmField
    val WIND_GENERATOR: Block =
        registerHorizontalWithBE("wind_generator", RagiumBlockEntityTypes.WIND_GENERATOR)

    @JvmField
    val SHAFT: Block =
        registerBlock("shaft", HTThinPillarBlock(blockSettings(Blocks.CHAIN)))

    @JvmField
    val CABLE: Block =
        registerBlock("cable", HTThinPillarBlock(blockSettings(Blocks.COPPER_BLOCK)))

    @JvmField
    val GEAR_BOX: Block =
        registerBlock("gear_box", HTGearBoxBlock)

    @JvmField
    val BLAZING_BOX: Block =
        registerHorizontalWithBE("blazing_box", RagiumBlockEntityTypes.BLAZING_BOX, Blocks.POLISHED_BLACKSTONE_BRICKS)

    @JvmField
    val ALCHEMICAL_INFUSER: Block =
        registerBlock("alchemical_infuser", HTAlchemicalInfuserBlock)

    @JvmField
    val ITEM_DISPLAY: Block =
        registerBlock("item_display", HTItemDisplayBlock)

    @JvmField
    val INFESTING: Block =
        registerBlock("infesting", HTInfectingBlock)

    //    Item - Tools    //

    @JvmField
    val FORGE_HAMMER: Item = registerItem("forge_hammer", HTForgeHammerItem)

    @JvmField
    val STEEL_SWORD: Item = registerSwordItem("steel_sword", RagiumToolMaterials.STEEL)

    @JvmField
    val STEEL_SHOVEL: Item = registerShovelItem("steel_shovel", RagiumToolMaterials.STEEL)

    @JvmField
    val STEEL_PICKAXE: Item = registerPickaxeItem("steel_pickaxe", RagiumToolMaterials.STEEL)

    @JvmField
    val STEEL_AXE: Item = registerAxeItem("steel_axe", RagiumToolMaterials.STEEL)

    @JvmField
    val STEEL_HOE: Item = registerHoeItem("steel_hoe", RagiumToolMaterials.STEEL)

    @JvmField
    val BACKPACK: Item = registerItem("backpack", HTBackpackItem.NORMAL)

    @JvmField
    val LARGE_BACKPACK: Item = registerItem("large_backpack", HTBackpackItem.LARGE)

    @JvmField
    val ENDER_BACKPACK: Item = registerItem("ender_backpack", HTEnderBackpackItem)

    //    Items - Ingredient    //

    @JvmField
    val RAW_RAGINITE: Item = registerItem("raw_raginite")

    @JvmField
    val RAGI_ALLOY_COMPOUND: Item = registerItem("ragi_alloy_compound")

    @JvmField
    val EMPTY_FLUID_CUBE: Item = registerItem("empty_fluid_cube")

    @JvmField
    val SOAP_INGOT: Item = registerItem("soap_ingot")

    @JvmField
    val RAW_RUBBER_BALL: Item = registerItem("raw_rubber_ball")

    @JvmField
    val RAGI_CRYSTAL: Item = registerItem("ragi_crystal")

    //    Register    //

    @JvmStatic
    fun init() {
        initBlockItems()
        initHulls()
        initCoils()
        initMachines()
        initStorageBlocks()
        initCircuits()
        initDusts()
        initIngots()
        initPlates()
        initElements()
        initFluids()
    }

    @JvmStatic
    private fun <T : Any> register(registry: Registry<in T>, name: String, value: (T)): T =
        Registry.register(registry, Ragium.id(name), value)

    @JvmStatic
    private fun <T : Block> registerBlock(name: String, block: T): T = register(Registries.BLOCK, name, block)

    @JvmStatic
    private fun registerBlock(name: String, settings: AbstractBlock.Settings = blockSettings()): Block =
        registerBlock(name, Block(settings))

    @JvmStatic
    private fun registerCopy(name: String, parent: Block): Block = registerBlock(name, blockSettings(parent))

    @JvmStatic
    private fun registerWithBE(name: String, type: BlockEntityType<*>, parent: Block): Block =
        registerWithBE(name, type, blockSettings(parent))

    @JvmStatic
    private fun registerWithBE(name: String, type: BlockEntityType<*>, settings: AbstractBlock.Settings = blockSettings()): Block =
        registerBlock(name, HTBlockWithEntity.build(type, settings))

    @JvmStatic
    private fun registerHorizontalWithBE(name: String, type: BlockEntityType<*>, parent: Block): Block =
        registerHorizontalWithBE(name, type, blockSettings(parent))

    @JvmStatic
    private fun registerHorizontalWithBE(
        name: String,
        type: BlockEntityType<*>,
        settings: AbstractBlock.Settings = blockSettings(),
    ): Block = registerBlock(name, HTBlockWithEntity.buildHorizontal(type, settings))

    @JvmStatic
    private fun <T : Item> registerItem(name: String, item: T): T = register(Registries.ITEM, name, item)

    @JvmStatic
    private fun registerItem(name: String, settings: Item.Settings = itemSettings()): Item = registerItem(name, Item(settings))

    @JvmStatic
    private fun registerBlockItem(
        block: Block,
        settings: Item.Settings = itemSettings(),
        factory: (Block, Item.Settings) -> Item = ::BlockItem,
    ) {
        Registries.BLOCK
            .getKey(block)
            .getOrNull()
            ?.value
            ?.let { registerItem(it.path, factory(block, settings)) }
    }

    @JvmStatic
    private fun <T : ToolItem> registerToolItem(
        name: String,
        material: ToolMaterial,
        factory: (ToolMaterial, Item.Settings) -> T,
        attributeComponent: AttributeModifiersComponent,
        settings: Item.Settings = itemSettings(),
    ): T = registerItem(name, factory(material, settings.attributeModifiers(attributeComponent)))

    @JvmStatic
    private fun registerSwordItem(name: String, material: ToolMaterial, settings: Item.Settings = itemSettings()): SwordItem =
        registerToolItem(
            name,
            material,
            ::SwordItem,
            RagiumToolMaterials.createAttributeComponent(material, 3.0, -2.0),
            settings,
        )

    @JvmStatic
    private fun registerShovelItem(name: String, material: ToolMaterial, settings: Item.Settings = itemSettings()): ShovelItem =
        registerToolItem(
            name,
            material,
            ::ShovelItem,
            RagiumToolMaterials.createAttributeComponent(material, -2.0, -3.0),
            settings,
        )

    @JvmStatic
    private fun registerPickaxeItem(name: String, material: ToolMaterial, settings: Item.Settings = itemSettings()): PickaxeItem =
        registerToolItem(
            name,
            material,
            ::PickaxeItem,
            RagiumToolMaterials.createAttributeComponent(material, -2.0, -2.8),
            settings,
        )

    @JvmStatic
    private fun registerAxeItem(name: String, material: ToolMaterial, settings: Item.Settings = itemSettings()): AxeItem = registerToolItem(
        name,
        material,
        ::AxeItem,
        RagiumToolMaterials.createAttributeComponent(material, 3.0, -2.9),
        settings,
    )

    @JvmStatic
    private fun registerHoeItem(name: String, material: ToolMaterial, settings: Item.Settings = itemSettings()): HoeItem = registerToolItem(
        name,
        material,
        ::HoeItem,
        RagiumToolMaterials.createAttributeComponent(material, -4.0, 0.0),
        settings,
    )

    @JvmStatic
    private fun initBlockItems() {
        registerBlockItem(RAGINITE_ORE)
        registerBlockItem(DEEPSLATE_RAGINITE_ORE)

        registerBlockItem(RUBBER_LOG)
        registerBlockItem(RUBBER_LEAVES)
        registerBlockItem(RUBBER_SAPLING)

        registerBlockItem(CREATIVE_SOURCE)
        registerBlockItem(MANUAL_GRINDER)
        registerBlockItem(BRICK_ALLOY_FURNACE)
        registerBlockItem(BURNING_BOX)
        registerBlockItem(WATER_GENERATOR)
        registerBlockItem(WIND_GENERATOR)
        registerBlockItem(SHAFT)
        registerBlockItem(CABLE)
        registerBlockItem(GEAR_BOX)
        registerBlockItem(BLAZING_BOX)
        registerBlockItem(ALCHEMICAL_INFUSER, itemSettings().rarity(Rarity.EPIC))
        registerBlockItem(ITEM_DISPLAY)
    }

    //    Hulls    //

    enum class Hulls(override val material: RagiumMaterials) : HTBlockContent {
        RAGI_ALLOY(RagiumMaterials.RAGI_ALLOY),
        RAGI_STEEL(RagiumMaterials.RAGI_STEEL),
        REFINED_RAGI_STEEL(RagiumMaterials.REFINED_RAGI_STEEL),
        ;

        override val block = Block(blockSettings(material.tier.baseBlock))
        override val enPattern: String = "%s Hull"
        override val jaPattern: String = "%s筐体"
    }

    @JvmStatic
    fun initHulls() {
        Hulls.entries.forEach { hull: Hulls ->
            registerBlock("${hull.name.lowercase()}_hull", hull.block)
            registerBlockItem(hull.block, itemSettings().tier(hull.material.tier))
        }
    }

    //    Coils    //

    enum class Coils(override val material: RagiumMaterials) : HTBlockContent {
        COPPER(RagiumMaterials.COPPER),
        GOLD(RagiumMaterials.GOLD),
        RAGI_ALLOY(RagiumMaterials.RAGI_ALLOY),
        ;

        override val block = PillarBlock(blockSettings())
        override val enPattern: String = "%s Coil"
        override val jaPattern: String = "%sコイル"
    }

    @JvmStatic
    fun initCoils() {
        Coils.entries.forEach { coil: Coils ->
            registerBlock("${coil.name.lowercase()}_coil", coil.block)
            registerBlockItem(coil.block, itemSettings())
        }
    }

    //    Machines    //

    @JvmStatic
    private fun initMachines() {
        HTMachineTier.entries.forEach { tier: HTMachineTier ->
            // single
            RagiumMachineTypes.Single.entries.forEach { type: RagiumMachineTypes.Single ->
                registerMachine(type, tier, ::HTSingleMachineBlock)
            }
            // blast furnace
            registerMachine(
                RagiumMachineTypes.BLAST_FURNACE,
                tier,
            ) { _: HTMachineType<*>, tier1: HTMachineTier -> HTBlastFurnaceBlock(tier1) }
            // distillation tower
            registerMachine(
                RagiumMachineTypes.DISTILLATION_TOWER,
                tier,
            ) { _: HTMachineType<*>, tier1: HTMachineTier -> HTDistillationTowerBlock(tier1) }
        }
    }

    @JvmStatic
    private fun registerMachine(
        type: HTMachineType<*>,
        tier: HTMachineTier,
        factory: (HTMachineType<*>, HTMachineTier) -> HTBaseMachineBlock,
    ) {
        val name: String = tier.createId(type).path
        val machineBlock: HTBaseMachineBlock = registerBlock(name, factory(type, tier))
        registerItem(name, HTMachineBlockItem(machineBlock, itemSettings()))
        HTMachineBlockRegistry.register(machineBlock)
    }

    //    Storage Blocks    //

    enum class StorageBlocks(override val material: RagiumMaterials) : HTBlockContent {
        RAGI_ALLOY(RagiumMaterials.RAGI_ALLOY),
        RAGI_STEEL(RagiumMaterials.RAGI_STEEL),
        STEEL(RagiumMaterials.STEEL),
        REFINED_RAGI_STEEL(RagiumMaterials.REFINED_RAGI_STEEL),
        ;

        override val block = Block(blockSettings(Blocks.IRON_BLOCK))
        override val enPattern: String = "Block of %s"
        override val jaPattern: String = "%sブロック"
    }

    @JvmStatic
    fun initStorageBlocks() {
        StorageBlocks.entries.forEach { block: StorageBlocks ->
            registerBlock("${block.name.lowercase()}_block", block.block)
            registerBlockItem(block.block, itemSettings().tier(block.material.tier))
        }
    }

    //    Circuits    //

    enum class Circuit(val tier: HTMachineTier) :
        HTTranslationProvider by tier,
        ItemConvertible {
        PRIMITIVE(HTMachineTier.PRIMITIVE),
        BASIC(HTMachineTier.BASIC),
        ADVANCED(HTMachineTier.ADVANCED),
        ;

        private val item: Item = Item(itemSettings())

        override fun asItem(): Item = item
    }

    @JvmStatic
    private fun initCircuits() {
        Circuit.entries.forEach { circuit: Circuit ->
            registerItem("${circuit.name.lowercase()}_circuit", circuit.asItem())
        }
    }

    //    Dusts    //

    enum class Dusts(override val material: RagiumMaterials) : HTItemContent {
        RAW_RAGINITE(RagiumMaterials.RAW_RAGINITE),
        RAGINITE(RagiumMaterials.RAGINITE),
        RAGI_CRYSTAL(RagiumMaterials.RAGI_CRYSTAL),
        ASH(RagiumMaterials.ASH),
        SULFUR(RagiumMaterials.SULFUR),
        ;

        override val item = Item(itemSettings())
        override val enPattern: String = "%s Dust"
        override val jaPattern: String = "%sの粉"
    }

    @JvmStatic
    private fun initDusts() {
        Dusts.entries.forEach { dust: Dusts ->
            registerItem("${dust.name.lowercase()}_dust", dust.asItem())
        }
    }

    //    Ingots    //

    enum class Ingots(override val material: RagiumMaterials) : HTItemContent {
        RAGI_ALLOY(RagiumMaterials.RAGI_ALLOY),
        RAGI_STEEL(RagiumMaterials.RAGI_STEEL),
        STEEL(RagiumMaterials.STEEL),
        REFINED_RAGI_STEEL(RagiumMaterials.REFINED_RAGI_STEEL),
        ;

        override val item = Item(itemSettings())
        override val enPattern: String = "%s Ingot"
        override val jaPattern: String = "%sインゴット"
    }

    @JvmStatic
    private fun initIngots() {
        Ingots.entries.forEach { ingot: Ingots ->
            registerItem("${ingot.name.lowercase()}_ingot", ingot.asItem())
        }
    }

    //    Plates    //

    enum class Plates(override val material: RagiumMaterials) : HTItemContent {
        // tier1
        RAGI_ALLOY(RagiumMaterials.RAGI_ALLOY),
        IRON(RagiumMaterials.IRON),
        COPPER(RagiumMaterials.COPPER),
        WOOD(RagiumMaterials.WOOD),

        // tier2
        RAGI_STEEL(RagiumMaterials.RAGI_STEEL),
        GOLD(RagiumMaterials.GOLD),
        RUBBER(RagiumMaterials.RUBBER),
        SILICON(RagiumMaterials.SILICON),
        STEEL(RagiumMaterials.STEEL),

        // tier3
        REFINED_RAGI_STEEL(RagiumMaterials.REFINED_RAGI_STEEL),
        NETHERITE(RagiumMaterials.NETHERITE),
        PE(RagiumMaterials.PE),
        PVC(RagiumMaterials.PVC),
        PTFE(RagiumMaterials.PTFE),
        ;

        override val item = Item(itemSettings())
        override val enPattern: String = "%s Plate"
        override val jaPattern: String = "%s板"
    }

    @JvmStatic
    private fun initPlates() {
        Plates.entries.forEach { plate: Plates ->
            registerItem("${plate.name.lowercase()}_plate", plate.asItem())
        }
    }

    //    Elements    //

    @JvmStatic
    private fun initElements() {
        RagiElement.entries.forEach { element: RagiElement ->
            // Budding Block
            registerBlock("budding_${element.asString()}", element.buddingBlock)
            registerBlockItem(element.buddingBlock)
            // Cluster Block
            registerBlock("${element.asString()}_cluster", element.clusterBlock)
            registerBlockItem(element.clusterBlock)
            // dust item
            registerItem("${element.asString()}_dust", element.dustItem)
        }
    }

    //    Fluids    //

    enum class Fluids(val color: Color, override val enName: String, override val jaName: String) :
        ItemConvertible,
        HTTranslationProvider {
        // tier1
        WATER(Color(0x0033ff), "Water", "水"),
        LAVA(Color(0xff6600), "Lava", "溶岩"),
        MILK(Color(0xffffff), "Milk", "牛乳"),
        HONEY(Color(0xffcc33), "Honey", "蜂蜜"),

        // tier2
        TALLOW(Color(0xcc9933), "Tallow", "獣脂"),
        SEED_OIL(Color(0x99cc33), "Seed Oil", "種油"),
        GLYCEROL(Color(0x99cc66), "Glycerol", "グリセロール"),

        // tier3 - resources
        SALT_WATER(Color(0x003399), "Salt Water", "塩水"),
        OIL(Color(0x000000), "Oil", "石油"),

        // tier3 - elements
        HYDROGEN(Color(0x0000cc), "Hydrogen", "水素"),
        NITROGEN(Color(0x66cccc), "Nitrogen", "窒素"),
        OXYGEN(Color(0x99ccff), "Oxygen", "酸素"),
        FLUORINE(Color(0x66cc99), "Fluorine", "フッ素"),
        CHLORINE(Color(0xccff33), "Chlorine", "塩素"),

        // tier3 - oil products
        REFINED_GAS(Color(0xcccccc), "Refined Gas", "精製ガス"),
        NAPHTHA(Color(0xff9900), "Naphtha", "ナフサ"),
        TAR(Color(0x000033), "Tar", "タール"),
        METHANE(Color(0xcc0099), "Methane", "メタン"),
        METHANOL(Color(0xcc00ff), "Methanol", "メタノール"),
        LPG(Color(0xffff33), "LPG", "LGP"),
        HELIUM(Color(0xffff99), "Helium", "ヘリウム"),
        ETHYLENE(Color(0x999999), "Ethylene", "エチレン"),
        VINYL_CHLORIDE(Color(0x99cc99), "Vinyl Chloride", "塩化ビニル"),
        TETRA_FLUORO_ETHYLENE(Color(0x669999), "Tetra Fluoro Ethylene", "テトラフルオロエチレン"),
        DIESEL(Color(0xcccc00), "Diesel", "ディーゼル"),
        BENZENE(Color(0x000066), "Benzene", "ベンゼン"),
        TOLUENE(Color(0x666699), "Toluene", "トルエン"),
        PHENOL(Color(0x996633), "Phenol", "フェノール"),
        ;

        val fluidName: String = name.lowercase()

        private val item: HTFluidCubeItem = HTFluidCubeItem.create(fluidName)

        override fun asItem(): Item = item
    }

    @JvmStatic
    private fun initFluids() {
        Fluids.entries.forEach { fluid: Fluids ->
            registerItem("${fluid.fluidName}_fluid_cube", fluid.asItem())
        }
    }
}
