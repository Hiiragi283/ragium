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
        RAGI_ALLOY(metal(MapColor.COLOR_RED), RagiumMaterials.RAGI_ALLOY),
        ADVANCED_RAGI_ALLOY(metal(MapColor.COLOR_ORANGE), RagiumMaterials.ADVANCED_RAGI_ALLOY),
        RAGI_CRYSTAL(gem(MapColor.COLOR_PINK), RagiumMaterials.RAGI_CRYSTAL),
        AZURE_STEEL(metal(MapColor.TERRACOTTA_BLUE), RagiumMaterials.AZURE_STEEL),
        DEEP_STEEL(metal(MapColor.COLOR_CYAN), RagiumMaterials.DEEP_STEEL),
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

    private fun metal(color: MapColor): BlockBehaviour.Properties = blockProperty()
        .mapColor(color)
        .strength(5f)
        .sound(SoundType.METAL)
        .requiresCorrectToolForDrops()

    private fun gem(color: MapColor): BlockBehaviour.Properties = blockProperty()
        .mapColor(color)
        .strength(5f)
        .sound(SoundType.AMETHYST)
        .requiresCorrectToolForDrops()

    //    Buildings    //

    @JvmField
    val RAGI_BRICK_SETS = HTBuildingBlockSets(
        "ragi_bricks",
        blockProperty(Blocks.BRICKS),
        prefix = "ragi_brick",
    )

    @JvmField
    val AZURE_TILE_SETS = HTBuildingBlockSets(
        "azure_tiles",
        blockProperty(Blocks.DEEPSLATE_TILES),
        prefix = "azure_tile",
    )

    @JvmField
    val PLASTIC_SETS = HTBuildingBlockSets(
        "plastic_block",
        blockProperty().strength(2f).sound(SoundType.COPPER),
    )

    @JvmField
    val BLUE_NETHER_BRICK_SETS = HTBuildingBlockSets(
        "blue_nether_bricks",
        blockProperty().strength(2f, 6f).sound(SoundType.NETHER_BRICKS),
        prefix = "blue_nether_brick",
    )

    @JvmField
    val QUARTZ_GLASS: DeferredBlock<TransparentBlock> =
        register("quartz_glass", blockProperty(Blocks.GLASS), ::TransparentBlock)

    @JvmField
    val OBSIDIAN_GLASS: DeferredBlock<TransparentBlock> =
        register("obsidian_glass", blockProperty(Blocks.GLASS).strength(5f, 1200f), ::TransparentBlock)

    @JvmField
    val SOUL_GLASS: DeferredBlock<HTSoulGlassBlock> =
        register("soul_glass", blockProperty(Blocks.GLASS), ::HTSoulGlassBlock)

    @JvmField
    val GLASSES: List<DeferredBlock<out TransparentBlock>> = listOf(
        QUARTZ_GLASS,
        OBSIDIAN_GLASS,
        SOUL_GLASS,
    )

    @JvmField
    val LED_BLOCKS: Map<DyeColor, DeferredBlock<Block>> = listOf(
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
            blockProperty(Blocks.GLASS)
                .mapColor(color)
                .lightLevel { 15 }
                .sound(SoundType.GLASS),
        )
    }

    @JvmStatic
    fun getLedBlock(color: DyeColor): DeferredBlock<Block> = LED_BLOCKS[color] ?: error("Unregistered color: ${color.serializedName}")

    //    Foods    //

    @JvmField
    val SPONGE_CAKE: DeferredBlock<HTSpongeCakeBlock> = register(
        "sponge_cake",
        blockProperty().mapColor(MapColor.COLOR_YELLOW).strength(0.5f).sound(SoundType.WOOL),
        ::HTSpongeCakeBlock,
    )

    @JvmField
    val SPONGE_CAKE_SLAB: DeferredBlock<HTSpongeCakeSlabBlock> = register(
        "sponge_cake_slab",
        blockProperty().mapColor(MapColor.COLOR_YELLOW).strength(0.5f).sound(SoundType.WOOL),
        ::HTSpongeCakeSlabBlock,
    )

    @JvmField
    val SWEET_BERRIES_CAKE: DeferredBlock<HTSweetBerriesCakeBlock> = register(
        "sweet_berries_cake",
        blockProperty().forceSolidOn().strength(0.5f).sound(SoundType.WOOL),
        ::HTSweetBerriesCakeBlock,
    )

    //    Casing    //

    @JvmField
    val WOODEN_CASING: DeferredBlock<Block> = register("wooden_casing", blockProperty(Blocks.NOTE_BLOCK))

    @JvmField
    val STONE_CASING: DeferredBlock<Block> = register("stone_casing", blockProperty(Blocks.COBBLESTONE))

    @JvmField
    val MACHINE_CASING: DeferredBlock<Block> = register(
        "machine_casing",
        blockProperty()
            .mapColor(MapColor.STONE)
            .strength(2f)
            .sound(SoundType.COPPER)
            .requiresCorrectToolForDrops(),
    )

    @JvmField
    val DEVICE_CASING: DeferredBlock<Block> = register(
        "device_casing",
        blockProperty()
            .mapColor(MapColor.STONE)
            .strength(2f)
            .sound(SoundType.COPPER)
            .requiresCorrectToolForDrops(),
    )

    @JvmField
    val CASINGS: List<DeferredBlock<Block>> = listOf(WOODEN_CASING, STONE_CASING, MACHINE_CASING, DEVICE_CASING)
}
