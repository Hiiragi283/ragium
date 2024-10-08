package hiiragi283.ragium.common

import com.mojang.serialization.Codec
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.inventory.HTBackpackInventory
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.common.block.*
import hiiragi283.ragium.common.init.*
import hiiragi283.ragium.common.item.HTForgeHammerItem
import hiiragi283.ragium.common.item.HTMetaMachineBlockItem
import hiiragi283.ragium.common.util.*
import io.netty.buffer.ByteBuf
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBiomeTags
import net.minecraft.block.*
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.block.piston.PistonBehavior
import net.minecraft.component.type.AttributeModifiersComponent
import net.minecraft.component.type.FoodComponent
import net.minecraft.component.type.FoodComponents
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.item.*
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.tag.BlockTags
import net.minecraft.registry.tag.FluidTags
import net.minecraft.registry.tag.TagKey
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.Rarity
import net.minecraft.util.StringIdentifiable
import net.minecraft.util.function.ValueLists
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.biome.Biome
import java.awt.Color
import java.util.function.IntFunction
import kotlin.jvm.optionals.getOrNull

object RagiumContents {
    //    Blocks - Ores    //

    @JvmField
    val RAGINITE_ORE: Block =
        registerCopy("raginite_ore", Blocks.IRON_ORE)

    @JvmField
    val DEEPSLATE_RAGINITE_ORE: Block =
        registerCopy("deepslate_raginite_ore", Blocks.DEEPSLATE_IRON_ORE)

    @JvmField
    val POROUS_NETHERRACK: Block =
        registerBlock(
            "porous_netherrack",
            HTSpongeBlock(
                blockSettings(Blocks.NETHERRACK),
                Blocks.MAGMA_BLOCK::getDefaultState,
            ) { world: World, pos: BlockPos ->
                world.getFluidState(pos).isIn(FluidTags.LAVA)
            },
        )

    @JvmField
    val SNOW_SPONGE: Block =
        registerBlock(
            "snow_sponge",
            HTSpongeBlock(
                blockSettings(Blocks.SNOW_BLOCK),
                Blocks.SNOW_BLOCK::getDefaultState,
            ) { world: World, pos: BlockPos ->
                world.getBlockState(pos).isIn(BlockTags.SNOW)
            },
        )

    @JvmField
    val OBLIVION_CLUSTER: Block =
        registerBlock(
            "oblivion_cluster",
            AmethystClusterBlock(
                7.0f,
                3.0f,
                blockSettings()
                    .mapColor(MapColor.BLACK)
                    .strength(1.5f)
                    .sounds(BlockSoundGroup.AMETHYST_BLOCK)
                    .solid()
                    .nonOpaque()
                    .luminance { 5 }
                    .pistonBehavior(PistonBehavior.DESTROY),
            ),
        )

    //    Blocks - Foods    //

    @JvmField
    val SPONGE_CAKE: Block =
        registerBlock(
            "sponge_cake",
            HTDecreaseFallingBlock(0.2f, blockSettings(Blocks.HAY_BLOCK).sounds(BlockSoundGroup.WOOL)),
        )

    @JvmField
    val SWEET_BERRIES_CAKE: Block =
        registerBlock("sweet_berries_cake", CakeBlock(blockSettings(Blocks.CAKE)))

    //    Blocks - Utilities    //

    @JvmField
    val CREATIVE_SOURCE: Block =
        registerWithBE("creative_source", RagiumBlockEntityTypes.CREATIVE_SOURCE, Blocks.COMMAND_BLOCK)

    @JvmField
    val BASIC_CASING: Block =
        registerCopy("basic_casing", Blocks.IRON_BLOCK)

    @JvmField
    val ADVANCED_CASING: Block =
        registerCopy("advanced_casing", Blocks.IRON_BLOCK)

    @JvmField
    val MANUAL_GRINDER: Block =
        registerBlock("manual_grinder", HTManualGrinderBlock)

    @JvmField
    val DATA_DRIVE: Block =
        registerBlock("data_drive")

    @JvmStatic
    val DRIVE_SCANNER: Block =
        registerWithBE("drive_scanner", RagiumBlockEntityTypes.DRIVE_SCANNER)

    @JvmField
    val ITEM_DISPLAY: Block =
        registerBlock("item_display", HTItemDisplayBlock)

    @JvmField
    val NETWORK_INTERFACE: Block =
        registerBlock("network_interface")

    @JvmField
    val SHAFT: Block =
        registerBlock("shaft", HTThinPillarBlock(blockSettings(Blocks.CHAIN)))

    @JvmField
    val ALCHEMICAL_INFUSER: Block =
        registerBlock("alchemical_infuser", HTAlchemicalInfuserBlock)

    @JvmField
    val INFESTING: Block =
        registerBlock("infesting", HTInfectingBlock)

    @JvmField // TODO
    val META_MACHINE: Block =
        registerBlock("meta_machine", HTMetaMachineBlock)

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
    val BACKPACK: Item = registerItem(
        "backpack",
        itemSettings()
            .maxCount(1)
            .fireproof()
            .component(RagiumComponentTypes.INVENTORY, HTBackpackInventory(false)),
    )

    @JvmField
    val LARGE_BACKPACK: Item = registerItem(
        "large_backpack",
        itemSettings()
            .maxCount(1)
            .fireproof()
            .component(RagiumComponentTypes.INVENTORY, HTBackpackInventory(true)),
    )

    @JvmField
    val ENDER_BACKPACK: Item = registerItem(
        "ender_backpack",
        itemSettings()
            .maxCount(1)
            .fireproof(),
    )

    //    Items - Armors    //

    @JvmField
    val STEEL_HELMET: ArmorItem = registerItem(
        "steel_helmet",
        ArmorItem(
            RagiumArmorMaterials.STEEL,
            ArmorItem.Type.HELMET,
            itemSettings().maxDamage(ArmorItem.Type.HELMET.getMaxDamage(25)),
        ),
    )

    @JvmField
    val STEEL_CHESTPLATE: ArmorItem = registerItem(
        "steel_chestplate",
        ArmorItem(
            RagiumArmorMaterials.STEEL,
            ArmorItem.Type.CHESTPLATE,
            itemSettings().maxDamage(ArmorItem.Type.CHESTPLATE.getMaxDamage(25)),
        ),
    )

    @JvmField
    val STEEL_LEGGINGS: ArmorItem = registerItem(
        "steel_leggings",
        ArmorItem(
            RagiumArmorMaterials.STEEL,
            ArmorItem.Type.LEGGINGS,
            itemSettings().maxDamage(ArmorItem.Type.LEGGINGS.getMaxDamage(25)),
        ),
    )

    @JvmField
    val STEEL_BOOTS: ArmorItem = registerItem(
        "steel_boots",
        ArmorItem(
            RagiumArmorMaterials.STEEL,
            ArmorItem.Type.BOOTS,
            itemSettings().maxDamage(ArmorItem.Type.BOOTS.getMaxDamage(25)),
        ),
    )

    @JvmField
    val DIVING_GOGGLES: Item = registerItem("diving_goggles", itemSettings().maxCount(1))

    @JvmField
    val NIGHT_VISION_GOGGLES: Item = registerItem("night_vision_goggles", itemSettings().maxCount(1))

    @JvmField
    val PISTON_BOOTS: Item = registerItem("piston_boots", itemSettings().maxCount(1))

    @JvmField
    val PARACHUTE: Item = registerItem("parachute", itemSettings().maxCount(1))

    //    Items - Foods    //

    @JvmField
    val BEE_WAX: Item = registerItem("bee_wax")

    @JvmField
    val BUTTER: Item = registerFoodItem("butter", FoodComponents.APPLE)

    @JvmField
    val CHOCOLATE: Item = registerFoodItem(
        "chocolate",
        FoodComponent
            .Builder()
            .nutrition(3)
            .saturationModifier(0.3f)
            .statusEffect(
                StatusEffectInstance(StatusEffects.STRENGTH, 10 * 20, 0),
                1.0f,
            ).snack()
            .alwaysEdible()
            .build(),
    )

    @JvmField
    val FLOUR: Item = registerItem("flour")

    @JvmField
    val DOUGH: Item = registerItem("dough")

    @JvmField
    val MINCED_MEAT: Item = registerItem("minced_meat")

    @JvmField
    val PULP: Item = registerItem("pulp")

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
    val BASALT_FIBER: Item = registerItem("basalt_fiber")

    @JvmField
    val SOLAR_PANEL: Item = registerItem("solar_panel")

    @JvmField
    val RAGI_CRYSTAL: Item = registerItem("ragi_crystal")

    @JvmField
    val OBLIVION_CRYSTAL: Item = registerItem("oblivion_crystal", itemSettings().rarity(Rarity.EPIC))

    @JvmField
    val OBLIVION_CUBE_SPAWN_EGG: Item = registerItem(
        "oblivion_cube_spawn_egg",
        SpawnEggItem(
            RagiumEntityTypes.OBLIVION_CUBE,
            0x000000,
            0xffffff,
            itemSettings(),
        ),
    )

    //    Register    //

    @JvmStatic
    fun init() {
        initBlockItems()
        initHulls()
        initCoils()
        // initMachines()
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
        Registry.register(registry, RagiumAPI.id(name), value)

    @JvmStatic
    private fun <T : Block> registerBlock(name: String, block: T): T = register(Registries.BLOCK, name, block)

    @JvmStatic
    private fun registerBlock(content: HTBlockContent): Block = registerBlock(content.id.path, content.block)

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
    private fun registerItem(item: HTItemContent): Item = registerItem(item.id.path, item.item)

    @JvmStatic
    private fun registerFoodItem(name: String, component: FoodComponent): Item = registerItem(name, Item(itemSettings().food(component)))

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
        registerBlockItem(POROUS_NETHERRACK)
        registerBlockItem(SNOW_SPONGE)
        registerBlockItem(OBLIVION_CLUSTER, itemSettings().rarity(Rarity.EPIC))

        registerBlockItem(SPONGE_CAKE)
        registerBlockItem(SWEET_BERRIES_CAKE)

        registerBlockItem(CREATIVE_SOURCE)
        registerBlockItem(BASIC_CASING)
        registerBlockItem(ADVANCED_CASING)
        registerBlockItem(MANUAL_GRINDER)
        registerBlockItem(DATA_DRIVE)
        registerBlockItem(DRIVE_SCANNER)
        registerBlockItem(SHAFT)
        registerBlockItem(ITEM_DISPLAY)
        registerBlockItem(NETWORK_INTERFACE)

        registerBlockItem(ALCHEMICAL_INFUSER, itemSettings().rarity(Rarity.EPIC))

        registerItem("meta_machine", HTMetaMachineBlockItem)
    }

    //    Hulls    //

    enum class Hulls(override val material: RagiumMaterials) : HTBlockContent {
        RAGI_ALLOY(RagiumMaterials.RAGI_ALLOY),
        RAGI_STEEL(RagiumMaterials.RAGI_STEEL),
        REFINED_RAGI_STEEL(RagiumMaterials.REFINED_RAGI_STEEL),
        ;

        override val block = Block(blockSettings(material.tier.getBaseBlock()))
        override val id: Identifier = RagiumAPI.id("${name.lowercase()}_hull")
        override val enPattern: String = "%s Hull"
        override val jaPattern: String = "%s筐体"
    }

    @JvmStatic
    fun initHulls() {
        Hulls.entries.forEach { hull: Hulls ->
            registerBlock(hull)
            registerBlockItem(hull.block, itemSettings().tier(hull.material.tier))
        }
    }

    //    Coils    //

    enum class Coils(override val material: RagiumMaterials) : HTBlockContent {
        COPPER(RagiumMaterials.COPPER),
        GOLD(RagiumMaterials.GOLD),
        RAGI_ALLOY(RagiumMaterials.RAGI_ALLOY),
        ;

        override val block = PillarBlock(blockSettings(Blocks.COPPER_BLOCK))
        override val id: Identifier = RagiumAPI.id("${name.lowercase()}_coil")
        override val enPattern: String = "%s Coil"
        override val jaPattern: String = "%sコイル"
    }

    @JvmStatic
    fun initCoils() {
        Coils.entries.forEach { coil: Coils ->
            registerBlock(coil)
            registerBlockItem(coil.block, itemSettings())
        }
    }

    //    Machines    //

    /*@JvmStatic
    private fun initMachines() {
        HTMachineTier.entries.forEach { tier: HTMachineTier ->
            RagiumAPI.getInstance().machineTypeRegistry.types.forEach { type: HTMachineType ->
                registerMachine(type, tier, ::HTMachineBlock)
            }
        }
    }

    @JvmStatic
    private fun registerMachine(
        type: HTMachineConvertible,
        tier: HTMachineTier,
        factory: (HTMachineConvertible, HTMachineTier) -> HTMachineBlock,
    ) {
        val name: String = tier.createId(type).path
        val machineBlock: HTMachineBlock = registerBlock(name, factory(type, tier))
        registerItem(name, HTMachineBlockItem(machineBlock, itemSettings()))
        HTMachineBlockRegistry.register(machineBlock)
    }*/

    //    Storage Blocks    //

    enum class StorageBlocks(override val material: RagiumMaterials) : HTBlockContent {
        RAGI_ALLOY(RagiumMaterials.RAGI_ALLOY),
        RAGI_STEEL(RagiumMaterials.RAGI_STEEL),
        STEEL(RagiumMaterials.STEEL),
        REFINED_RAGI_STEEL(RagiumMaterials.REFINED_RAGI_STEEL),
        ;

        override val block = Block(blockSettings(Blocks.IRON_BLOCK))
        override val id: Identifier = RagiumAPI.id("${name.lowercase()}_block")
        override val enPattern: String = "Block of %s"
        override val jaPattern: String = "%sブロック"
    }

    @JvmStatic
    fun initStorageBlocks() {
        StorageBlocks.entries.forEach { block: StorageBlocks ->
            registerBlock(block)
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
        NITER(RagiumMaterials.NITER),
        SULFUR(RagiumMaterials.SULFUR),
        ;

        override val item = Item(itemSettings())
        override val id: Identifier = RagiumAPI.id("${name.lowercase()}_dust")
        override val enPattern: String = "%s Dust"
        override val jaPattern: String = "%sの粉"
    }

    @JvmStatic
    private fun initDusts() {
        Dusts.entries.forEach(::registerItem)
    }

    //    Ingots    //

    enum class Ingots(override val material: RagiumMaterials) : HTItemContent {
        RAGI_ALLOY(RagiumMaterials.RAGI_ALLOY),
        RAGI_STEEL(RagiumMaterials.RAGI_STEEL),
        STEEL(RagiumMaterials.STEEL),
        REFINED_RAGI_STEEL(RagiumMaterials.REFINED_RAGI_STEEL),
        ;

        override val item = Item(itemSettings())
        override val id: Identifier = RagiumAPI.id("${name.lowercase()}_ingot")
        override val enPattern: String = "%s Ingot"
        override val jaPattern: String = "%sインゴット"
    }

    @JvmStatic
    private fun initIngots() {
        Ingots.entries.forEach(::registerItem)
    }

    //    Plates    //

    enum class Plates(override val material: RagiumMaterials) : HTItemContent {
        // tier1
        RAGI_ALLOY(RagiumMaterials.RAGI_ALLOY),
        IRON(RagiumMaterials.IRON),
        COPPER(RagiumMaterials.COPPER),

        // tier2
        RAGI_STEEL(RagiumMaterials.RAGI_STEEL),
        BASALT_FIBER(RagiumMaterials.BASALT_FIBER),
        GOLD(RagiumMaterials.GOLD),
        SILICON(RagiumMaterials.SILICON),
        STEEL(RagiumMaterials.STEEL),

        // tier3
        REFINED_RAGI_STEEL(RagiumMaterials.REFINED_RAGI_STEEL),
        PE(RagiumMaterials.PE),
        PVC(RagiumMaterials.PVC),
        PTFE(RagiumMaterials.PTFE),
        ;

        override val item = Item(itemSettings())
        override val id: Identifier = RagiumAPI.id("${name.lowercase()}_plate")
        override val enPattern: String = "%s Plate"
        override val jaPattern: String = "%s板"
    }

    @JvmStatic
    private fun initPlates() {
        Plates.entries.forEach(::registerItem)
    }

    //    Elements    //

    enum class Element(
        private val suitableBiome: TagKey<Biome>,
        override val enName: String,
        override val jaName: String,
        mapColor: MapColor,
    ) : HTTranslationProvider,
        StringIdentifiable {
        RAGIUM(ConventionalBiomeTags.IS_NETHER, "Ragium", "ラギウム", MapColor.RED),
        RIGIUM(ConventionalBiomeTags.IS_WASTELAND, "Rigium", "リギウム", MapColor.YELLOW),
        RUGIUM(ConventionalBiomeTags.IS_JUNGLE, "Rugium", "ルギウム", MapColor.GREEN),
        REGIUM(ConventionalBiomeTags.IS_OCEAN, "Regium", "レギウム", MapColor.BLUE),
        ROGIUM(ConventionalBiomeTags.IS_END, "Rogium", "ロギウム", MapColor.PURPLE),
        ;

        companion object {
            @JvmField
            val CODEC: Codec<Element> = StringIdentifiable.createCodec(Element::values)

            @JvmField
            val INT_FUNCTION: IntFunction<Element> =
                ValueLists.createIdToValueFunction(
                    Element::ordinal,
                    Element.entries.toTypedArray(),
                    ValueLists.OutOfBoundsHandling.WRAP,
                )

            @JvmField
            val PACKET_CODEC: PacketCodec<ByteBuf, Element> =
                PacketCodecs.indexed(INT_FUNCTION, Element::ordinal)
        }

        private val settings: AbstractBlock.Settings =
            blockSettings().mapColor(mapColor).strength(1.5f).sounds(BlockSoundGroup.AMETHYST_BLOCK)
        val buddingBlock: HTBuddingCrystalBlock = HTBuddingCrystalBlock(this, settings.ticksRandomly().requiresTool())
        val clusterBlock: AmethystClusterBlock = AmethystClusterBlock(
            7.0f,
            3.0f,
            settings
                .solid()
                .nonOpaque()
                .luminance { 5 }
                .pistonBehavior(PistonBehavior.DESTROY),
        )
        val dustItem = Item(itemSettings().element(this))

        val pendantItem = Item(itemSettings().element(this).maxDamage(63))
        val ringItem = Item(itemSettings().element(this).maxDamage(63))

        val translationKey = "element.${asString()}"
        val text: Text = Text.translatable(translationKey)

        fun isSuitableBiome(world: World, pos: BlockPos): Boolean = world.getBiome(pos).isIn(suitableBiome)

        //    StringIdentifiable    //

        override fun asString(): String = name.lowercase()
    }

    @JvmStatic
    private fun initElements() {
        Element.entries.forEach { element: Element ->
            // Budding Block
            registerBlock("budding_${element.asString()}", element.buddingBlock)
            registerBlockItem(element.buddingBlock)
            // Cluster Block
            registerBlock("${element.asString()}_cluster", element.clusterBlock)
            registerBlockItem(element.clusterBlock)
            // dust item
            registerItem("${element.asString()}_dust", element.dustItem)
            // pendant item
            registerItem("${element.asString()}_pendant", element.pendantItem)
            // ring item
            registerItem("${element.asString()}_ring", element.ringItem)
        }
    }

    //    Fluids    //

    enum class Fluids(val color: Color, override val enName: String, override val jaName: String) :
        ItemConvertible,
        HTTranslationProvider {
        // Vanilla
        WATER(Color(0x0033ff), "Water", "水"),
        LAVA(Color(0xff6600), "Lava", "溶岩"),
        MILK(Color(0xffffff), "Milk", "牛乳"),
        HONEY(Color(0xffcc33), "Honey", "蜂蜜"),

        // Molten Materials
        MOLTEN_BASALT(Color(0x333333), "Molten Basalt", "溶融玄武岩"),

        // Organics
        TALLOW(Color(0xcc9933), "Tallow", "獣脂"),
        SEED_OIL(Color(0x99cc33), "Seed Oil", "種油"),
        GLYCEROL(Color(0x99cc66), "Glycerol", "グリセロール"),

        // Foods
        BATTER(Color(0xffcc66), "Batter", "バッター液"),
        SWEET_BERRIES(Color(0x990000), "Sweet Berries", "スイートベリー"),

        // Natural Resources
        SALT_WATER(Color(0x003399), "Salt Water", "塩水"),
        PETROLEUM(Color(0x000000), "Petroleum", "石油"),
        CRUDE_OIL(Color(0x000000), "Crude Oil", "原油"),

        // Elements
        HYDROGEN(Color(0x0000cc), "Hydrogen", "水素"),
        NITROGEN(Color(0x66cccc), "Nitrogen", "窒素"),
        OXYGEN(Color(0x99ccff), "Oxygen", "酸素"),
        FLUORINE(Color(0x66cc99), "Fluorine", "フッ素"),
        CHLORINE(Color(0xccff33), "Chlorine", "塩素"),

        // Non-organic Chemical Compounds
        NITRIC_ACID(Color(0xcc99ff), "Nitric Acid", "硝酸"),
        SODIUM_HYDROXIDE(Color(0x000099), "Sodium Hydroxide Solution", "水酸化ナトリウム水溶液"),
        SULFURIC_ACID(Color(0xff3300), "Sulfuric Acid", "硫酸"),
        MIXTURE_ACID(Color(0xcc6633), "Mixture Acid", "混酸"),

        // Oil products
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

        NITRO_GLYCERIN(Color(0x99cc66), "Nitroglycerin", "ニトログリセリン"),
        TRINITROTOLUENE(Color(0x666699), "Trinitrotoluene", "トリニトロトルエン"),
        ;

        val fluidName: String = name.lowercase()

        private val item: Item = Item(itemSettings())

        override fun asItem(): Item = item
    }

    @JvmStatic
    private fun initFluids() {
        Fluids.entries.forEach { fluid: Fluids ->
            registerItem("${fluid.fluidName}_fluid_cube", fluid.asItem())
        }
    }
}
