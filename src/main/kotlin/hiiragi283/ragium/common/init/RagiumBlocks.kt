package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.HTCatalystBlock
import hiiragi283.ragium.api.block.HTEntityBlock
import hiiragi283.ragium.api.block.HTHorizontalEntityBlock
import hiiragi283.ragium.api.block.entity.HTBlockEntity
import hiiragi283.ragium.api.data.HTCatalystConversion
import hiiragi283.ragium.api.extension.blockProperty
import hiiragi283.ragium.api.material.HTMaterialItemLike
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.keys.CommonMaterials
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import hiiragi283.ragium.api.material.prefix.HTTagPrefix
import hiiragi283.ragium.api.material.prefix.HTTagPrefixes
import hiiragi283.ragium.api.registry.HTBlockRegister
import hiiragi283.ragium.api.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.api.registry.HTItemRegister
import hiiragi283.ragium.common.block.*
import hiiragi283.ragium.common.util.HTBuildingBlockSets
import hiiragi283.ragium.common.util.HTOreSets
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.ColorRGBA
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.*
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.material.MapColor
import net.minecraft.world.level.material.PushReaction
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredBlock

object RagiumBlocks {
    @JvmField
    val REGISTER = HTBlockRegister(RagiumAPI.MOD_ID)

    @JvmField
    val ITEM_REGISTER = HTItemRegister(RagiumAPI.MOD_ID)

    @JvmStatic
    fun init(eventBus: IEventBus) {
        StorageBlocks.entries

        REGISTER.register(eventBus)
        ITEM_REGISTER.register(eventBus)

        RAGINITE_ORES.init(eventBus)
        RAGI_CRYSTAL_ORES.init(eventBus)

        RAGI_STONE_SETS.init(eventBus)
        RAGI_STONE_SQUARE_SETS.init(eventBus)
        AZURE_TILE_SETS.init(eventBus)
        EMBER_STONE_SETS.init(eventBus)
        PLASTIC_SETS.init(eventBus)
        BLUE_NETHER_BRICK_SETS.init(eventBus)
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
    private fun wooden(): BlockBehaviour.Properties = blockProperty()
        .mapColor(MapColor.WOOD)
        .requiresCorrectToolForDrops()
        .strength(0.8f)
        .sound(SoundType.WOOD)
        .ignitedByLava()

    @JvmStatic
    private fun stone(): BlockBehaviour.Properties = blockProperty()
        .requiresCorrectToolForDrops()
        .strength(2f)
        .sound(SoundType.DEEPSLATE_BRICKS)

    @JvmStatic
    private fun lightMetal(): BlockBehaviour.Properties = blockProperty()
        .requiresCorrectToolForDrops()
        .strength(3f)
        .sound(SoundType.COPPER)

    @JvmStatic
    private fun heavyMetal(): BlockBehaviour.Properties = blockProperty()
        .requiresCorrectToolForDrops()
        .strength(6f)
        .sound(SoundType.METAL)

    @JvmStatic
    private fun crystal(): BlockBehaviour.Properties = blockProperty()
        .requiresCorrectToolForDrops()
        .strength(2f)
        .sound(SoundType.AMETHYST)

    @JvmStatic
    private fun glass(): BlockBehaviour.Properties = blockProperty(Blocks.GLASS)

    @JvmStatic
    private fun soft(): BlockBehaviour.Properties = blockProperty()
        .strength(0.5f)
        .sound(SoundType.WOOL)

    //    Natural Resources    //

    @JvmField
    val RAGINITE_ORES = HTOreSets(RagiumMaterials.RAGINITE)

    @JvmField
    val RAGI_CRYSTAL_ORES = HTOreSets(RagiumMaterials.RAGI_CRYSTAL)

    @JvmField
    val SILT: DeferredBlock<ColoredFallingBlock> = register(
        "silt",
        blockProperty(Blocks.SAND),
    ) { prop: BlockBehaviour.Properties -> ColoredFallingBlock(ColorRGBA(0xccccff), prop) }

    @JvmField
    val MYSTERIOUS_OBSIDIAN: DeferredBlock<Block> = register("mysterious_obsidian", blockProperty(Blocks.OBSIDIAN))

    @JvmField
    val ASH_LOG: DeferredBlock<RotatedPillarBlock> = register(
        "ash_log",
        blockProperty().mapColor(MapColor.COLOR_GRAY).strength(1f).sound(SoundType.SAND),
        ::RotatedPillarBlock,
    )

    @JvmField
    val EXP_BERRY_BUSH: DeferredBlock<HTExpBerriesBushBlock> = REGISTER.registerBlock(
        "exp_berry_bush",
        ::HTExpBerriesBushBlock,
        blockProperty(Blocks.SWEET_BERRY_BUSH),
    )

    @JvmField
    val LILY_OF_THE_ENDER: DeferredBlock<HTEnderLilyBlock> = register(
        "lily_of_the_ender",
        blockProperty()
            .mapColor(MapColor.PLANT)
            .noCollission()
            .instabreak()
            .sound(SoundType.GRASS)
            .pushReaction(PushReaction.DESTROY),
        ::HTEnderLilyBlock,
    )

    //    Materials    //

    enum class StorageBlocks(properties: BlockBehaviour.Properties, override val key: HTMaterialKey) : HTMaterialItemLike {
        // Ingot
        RAGI_ALLOY(lightMetal().mapColor(MapColor.COLOR_RED), RagiumMaterials.RAGI_ALLOY),
        ADVANCED_RAGI_ALLOY(heavyMetal().mapColor(MapColor.COLOR_ORANGE), RagiumMaterials.ADVANCED_RAGI_ALLOY),
        RAGI_CRYSTAL(crystal().mapColor(MapColor.COLOR_PINK), RagiumMaterials.RAGI_CRYSTAL),
        AZURE_STEEL(heavyMetal().mapColor(MapColor.TERRACOTTA_BLUE), RagiumMaterials.AZURE_STEEL),
        DEEP_STEEL(heavyMetal().mapColor(MapColor.COLOR_CYAN), RagiumMaterials.DEEP_STEEL),

        // Gem
        CRIMSON_CRYSTAL(crystal(), RagiumMaterials.CRIMSON_CRYSTAL),
        WARPED_CRYSTAL(crystal(), RagiumMaterials.WARPED_CRYSTAL),

        // Food
        CHEESE(wooden().mapColor(MapColor.TERRACOTTA_YELLOW), CommonMaterials.CHEESE),
        CHOCOLATE(wooden().mapColor(MapColor.TERRACOTTA_BROWN), CommonMaterials.CHOCOLATE),
        ;

        val holder: DeferredBlock<HTMaterialStorageBlock> = register(
            "${key.name}_block",
            properties,
        ) { prop: BlockBehaviour.Properties -> HTMaterialStorageBlock(key, prop) }

        val baseItem: HTMaterialItemLike get() = when (this) {
            RAGI_ALLOY -> RagiumItems.Ingots.RAGI_ALLOY
            ADVANCED_RAGI_ALLOY -> RagiumItems.Ingots.ADVANCED_RAGI_ALLOY
            RAGI_CRYSTAL -> RagiumItems.RawResources.RAGI_CRYSTAL
            AZURE_STEEL -> RagiumItems.Ingots.AZURE_STEEL
            DEEP_STEEL -> RagiumItems.Ingots.DEEP_STEEL
            CRIMSON_CRYSTAL -> RagiumItems.RawResources.CRIMSON_CRYSTAL
            WARPED_CRYSTAL -> RagiumItems.RawResources.WARPED_CRYSTAL
            CHEESE -> RagiumItems.Ingots.CHEESE
            CHOCOLATE -> RagiumItems.Ingots.CHOCOLATE
        }

        override val prefix: HTTagPrefix = HTTagPrefixes.STORAGE_BLOCK
        override val id: ResourceLocation = holder.id

        override fun asItem(): Item = holder.asItem()

        companion object {
            @JvmStatic
            val blocks: List<DeferredBlock<*>> get() = entries.map(StorageBlocks::holder)
        }
    }

    //    Buildings    //

    @JvmField
    val RAGI_STONE_SETS = HTBuildingBlockSets("ragi_stone", stone().mapColor(MapColor.COLOR_RED))

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

    @JvmField
    val CRUSHER: DeferredBlock<*> = registerMachine(lightMetal(), RagiumBlockEntityTypes.CRUSHER)

    @JvmField
    val EXTRACTOR: DeferredBlock<*> = registerMachine(lightMetal(), RagiumBlockEntityTypes.EXTRACTOR)

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

    // Elite
    @JvmField
    val AZURE_CATALYST: DeferredBlock<HTCatalystBlock> =
        register("azure_catalyst", heavyMetal(), HTCatalystBlock.create(HTCatalystConversion.AZURE_TYPE))

    @JvmField
    val DEEP_CATALYST: DeferredBlock<HTCatalystBlock> =
        register("deep_catalyst", heavyMetal(), HTCatalystBlock.create(HTCatalystConversion.DEEP_TYPE))

    @JvmField
    val RAGIUM_CATALYST: DeferredBlock<HTCatalystBlock> =
        register("ragium_catalyst", heavyMetal(), HTCatalystBlock.create(HTCatalystConversion.RAGIUM_TYPE))

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
        // 触媒
        AZURE_CATALYST,
        DEEP_CATALYST,
        RAGIUM_CATALYST,
    )
}
