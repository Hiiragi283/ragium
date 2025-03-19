package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.blockProperty
import hiiragi283.ragium.api.material.HTMaterialItemLike
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import hiiragi283.ragium.api.registry.HTBlockRegister
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

        RAGI_BRICK_SETS.init(eventBus)
        AZURE_TILE_SETS.init(eventBus)
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
        .strength(5f)
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

    //    Materials    //

    enum class StorageBlocks(properties: BlockBehaviour.Properties, override val key: HTMaterialKey) : HTMaterialItemLike {
        RAGI_ALLOY(lightMetal().mapColor(MapColor.COLOR_RED), RagiumMaterials.RAGI_ALLOY),
        ADVANCED_RAGI_ALLOY(heavyMetal().mapColor(MapColor.COLOR_ORANGE), RagiumMaterials.ADVANCED_RAGI_ALLOY),
        RAGI_CRYSTAL(crystal().mapColor(MapColor.COLOR_PINK), RagiumMaterials.RAGI_CRYSTAL),
        AZURE_STEEL(heavyMetal().mapColor(MapColor.TERRACOTTA_BLUE), RagiumMaterials.AZURE_STEEL),
        DEEP_STEEL(heavyMetal().mapColor(MapColor.COLOR_CYAN), RagiumMaterials.DEEP_STEEL),
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
        }

        override val prefix: HTTagPrefix = HTTagPrefix.STORAGE_BLOCK
        override val id: ResourceLocation = holder.id

        override fun asItem(): Item = holder.asItem()

        companion object {
            @JvmStatic
            val blocks: List<DeferredBlock<*>> get() = entries.map(StorageBlocks::holder)
        }
    }

    //    Buildings    //

    @JvmField
    val RAGI_BRICK_SETS = HTBuildingBlockSets(
        "ragi_bricks",
        stone().mapColor(MapColor.COLOR_RED),
        prefix = "ragi_brick",
    )

    @JvmField
    val AZURE_TILE_SETS = HTBuildingBlockSets(
        "azure_tiles",
        stone().mapColor(MapColor.TERRACOTTA_BLUE),
        prefix = "azure_tile",
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
    val DEVICE_CASING: DeferredBlock<Block> = register(
        "device_casing",
        heavyMetal(),
    )

    @JvmField
    val CASINGS: List<DeferredBlock<*>> = listOf(WOODEN_CASING, STONE_CASING, MACHINE_CASING, DEVICE_CASING)

    //    Machines    //

    @JvmField
    val CRUSHER: DeferredBlock<HTCrusherBlock> = register("crusher", heavyMetal(), ::HTCrusherBlock)

    @JvmField
    val EXTRACTOR: DeferredBlock<HTExtractorBlock> = register("extractor", heavyMetal(), ::HTExtractorBlock)

    @JvmField
    val MACHINES: List<DeferredBlock<*>> = listOf(
        CRUSHER,
        EXTRACTOR,
    )
}
