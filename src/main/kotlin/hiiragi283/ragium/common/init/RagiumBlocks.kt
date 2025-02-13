package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.blockProperty
import hiiragi283.ragium.api.extension.buildTable
import hiiragi283.ragium.api.extension.itemProperty
import hiiragi283.ragium.api.extension.name
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.CommonMaterials
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import hiiragi283.ragium.api.util.HTOreVariant
import hiiragi283.ragium.api.util.HTTable
import hiiragi283.ragium.common.block.HTEntityBlock
import hiiragi283.ragium.common.block.HTSoulMagmaBlock
import hiiragi283.ragium.common.block.HTSweetBerriesCakeBlock
import hiiragi283.ragium.common.block.addon.HTCatalystAddonBlockEntity
import hiiragi283.ragium.common.block.addon.HTEnergyNetworkBlock
import hiiragi283.ragium.common.block.addon.HTSlagCollectorBlockEntity
import hiiragi283.ragium.common.block.machine.HTDisenchantingTableBlock
import hiiragi283.ragium.common.block.machine.HTManualGrinderBlock
import hiiragi283.ragium.common.block.machine.HTPrimitiveBlastFurnaceBlock
import hiiragi283.ragium.common.block.storage.HTDrumBlockEntity
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.*
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.material.MapColor
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredRegister

object RagiumBlocks {
    @JvmField
    val REGISTER: DeferredRegister.Blocks = DeferredRegister.createBlocks(RagiumAPI.MOD_ID)

    @JvmField
    val ITEM_REGISTER: DeferredRegister.Items = DeferredRegister.createItems(RagiumAPI.MOD_ID)

    @JvmStatic
    fun register(
        name: String,
        properties: BlockBehaviour.Properties,
        properties1: Item.Properties = itemProperty(),
    ): DeferredBlock<Block> {
        val holder: DeferredBlock<Block> = REGISTER.registerSimpleBlock(name, properties)
        ITEM_REGISTER.registerSimpleBlockItem(holder, properties1)
        return holder
    }

    @JvmStatic
    fun <T : Block> register(
        name: String,
        factory: (BlockBehaviour.Properties) -> T,
        properties: BlockBehaviour.Properties,
        properties1: Item.Properties = itemProperty(),
    ): DeferredBlock<T> {
        val holder: DeferredBlock<T> = REGISTER.registerBlock(name, factory, properties)
        ITEM_REGISTER.registerSimpleBlockItem(holder, properties1)
        return holder
    }

    //    Natural Resources    //

    @JvmField
    val ORES: HTTable<HTOreVariant, HTMaterialKey, DeferredBlock<out Block>> = buildTable {
        val materials: List<HTMaterialKey> = listOf(
            RagiumMaterials.RAGINITE,
            RagiumMaterials.RAGI_CRYSTAL,
        )
        for (variant: HTOreVariant in HTOreVariant.entries) {
            for (key: HTMaterialKey in materials) {
                put(
                    variant,
                    key,
                    register(
                        variant.createId(key),
                        variant.createProperty(),
                        itemProperty().name(variant.createText(key)),
                    ),
                )
            }
        }
    }

    @JvmField
    val SOUL_MAGMA_BLOCK: DeferredBlock<HTSoulMagmaBlock> = register(
        "soul_magma_block",
        ::HTSoulMagmaBlock,
        blockProperty(Blocks.MAGMA_BLOCK).mapColor(MapColor.LAPIS),
    )

    @JvmField
    val CRUDE_OIL: DeferredBlock<LiquidBlock> = REGISTER.registerBlock(
        "crude_oil",
        { properties: BlockBehaviour.Properties -> LiquidBlock(RagiumFluids.CRUDE_OIL.get(), properties) },
        blockProperty(Blocks.WATER).mapColor(MapColor.COLOR_BLACK),
    )

    //    Materials    //

    @JvmField
    val STORAGE_BLOCKS: Map<HTMaterialKey, DeferredBlock<Block>> = buildMap {
        fun metal(color: MapColor): BlockBehaviour.Properties = blockProperty()
            .mapColor(color)
            .strength(5f)
            .sound(SoundType.METAL)
            .requiresCorrectToolForDrops()

        fun gem(color: MapColor): BlockBehaviour.Properties = blockProperty()
            .mapColor(color)
            .strength(5f)
            .sound(SoundType.AMETHYST)
            .requiresCorrectToolForDrops()

        // Ragium
        put(RagiumMaterials.RAGI_ALLOY, metal(MapColor.COLOR_RED))
        put(RagiumMaterials.RAGIUM, metal(MapColor.COLOR_RED))
        // Steel
        put(CommonMaterials.STEEL, metal(MapColor.DEEPSLATE))
        put(RagiumMaterials.DEEP_STEEL, metal(MapColor.COLOR_CYAN))
        // Metal
        put(CommonMaterials.ALUMINUM, metal(MapColor.ICE))
        put(RagiumMaterials.ECHORIUM, metal(MapColor.COLOR_CYAN))
        // Gem
        put(RagiumMaterials.RAGI_CRYSTAL, gem(MapColor.COLOR_RED))
        put(CommonMaterials.CRYOLITE, gem(MapColor.NONE))
        // Other
        put(
            RagiumMaterials.FIERY_COAL,
            blockProperty().mapColor(MapColor.CRIMSON_HYPHAE).strength(5f).sound(SoundType.DEEPSLATE),
        )
    }.mapValues { (key: HTMaterialKey, properties: BlockBehaviour.Properties) ->
        register("${key.name}_block", properties, itemProperty().name(HTTagPrefix.STORAGE_BLOCK.createText(key)))
    }

    @JvmStatic
    fun getStorageParent(key: HTMaterialKey): HTTagPrefix = when (key) {
        CommonMaterials.CRYOLITE -> HTTagPrefix.GEM
        RagiumMaterials.FIERY_COAL -> HTTagPrefix.GEM
        RagiumMaterials.RAGI_CRYSTAL -> HTTagPrefix.GEM
        else -> HTTagPrefix.INGOT
    }

    @JvmField
    val SLAG_BLOCK: DeferredBlock<Block> = register(
        "slag_block",
        blockProperty().mapColor(MapColor.TERRACOTTA_CYAN).strength(3f).sound(SoundType.DEEPSLATE),
    )

    //    Buildings    //

    @JvmField
    val SHAFT: DeferredBlock<RotatedPillarBlock> = register(
        "shaft",
        ::RotatedPillarBlock,
        blockProperty()
            .forceSolidOn()
            .requiresCorrectToolForDrops()
            .strength(5f)
            .sound(SoundType.COPPER)
            .noOcclusion(),
    )

    @JvmField
    val CHEMICAL_GLASS: DeferredBlock<TransparentBlock> =
        register("chemical_glass", ::TransparentBlock, blockProperty(Blocks.GLASS))

    @JvmField
    val OBSIDIAN_GLASS: DeferredBlock<TransparentBlock> =
        register("obsidian_glass", ::TransparentBlock, blockProperty(Blocks.GLASS).strength(5f, 1200f))

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

    @JvmField
    val PLASTIC_BLOCK: DeferredBlock<Block> =
        register("plastic_block", blockProperty().strength(2f).sound(SoundType.COPPER))

    //    Foods    //

    @JvmField
    val SPONGE_CAKE: DeferredBlock<HayBlock> = register(
        "sponge_cake",
        ::HayBlock,
        blockProperty().mapColor(MapColor.COLOR_YELLOW).strength(0.5f).sound(SoundType.WOOL),
    )

    @JvmField
    val SWEET_BERRIES_CAKE: DeferredBlock<HTSweetBerriesCakeBlock> = register(
        "sweet_berries_cake",
        ::HTSweetBerriesCakeBlock,
        blockProperty().forceSolidOn().strength(0.5f).sound(SoundType.WOOL),
    )

    //    Manual Machines    //

    @JvmField
    val MANUAL_GRINDER: DeferredBlock<HTManualGrinderBlock> = register(
        "manual_grinder",
        ::HTManualGrinderBlock,
        blockProperty(Blocks.BRICKS),
    )

    @JvmField
    val PRIMITIVE_BLAST_FURNACE: DeferredBlock<HTPrimitiveBlastFurnaceBlock> = register(
        "primitive_blast_furnace",
        ::HTPrimitiveBlastFurnaceBlock,
        blockProperty(Blocks.BRICKS),
    )

    @JvmField
    val DISENCHANTING_TABLE: DeferredBlock<HTDisenchantingTableBlock> = register(
        "disenchanting_table",
        ::HTDisenchantingTableBlock,
        blockProperty(Blocks.OBSIDIAN),
    )

    //    Storage    //

    @JvmField
    val COPPER_DRUM: DeferredBlock<HTEntityBlock> = register(
        "copper_drum",
        { properties: BlockBehaviour.Properties -> HTEntityBlock.of(::HTDrumBlockEntity, properties) },
        blockProperty()
            .mapColor(MapColor.STONE)
            .strength(2f)
            .sound(SoundType.COPPER)
            .requiresCorrectToolForDrops(),
    )

    //    Utility    //

    @JvmField
    val CATALYST_ADDON: DeferredBlock<HTEntityBlock> = register(
        "catalyst_addon",
        { prop: BlockBehaviour.Properties -> HTEntityBlock.of(::HTCatalystAddonBlockEntity, prop) },
        blockProperty()
            .mapColor(MapColor.COLOR_BLACK)
            .strength(2f)
            .sound(SoundType.COPPER)
            .requiresCorrectToolForDrops(),
    )

    @JvmField
    val ENERGY_NETWORK_INTERFACE: DeferredBlock<HTEnergyNetworkBlock> = register(
        "energy_network_interface",
        ::HTEnergyNetworkBlock,
        blockProperty()
            .mapColor(MapColor.STONE)
            .strength(2f)
            .sound(SoundType.COPPER)
            .requiresCorrectToolForDrops(),
    )

    @JvmField
    val SLAG_COLLECTOR: DeferredBlock<HTEntityBlock> = register(
        "slag_collector",
        { prop: BlockBehaviour.Properties -> HTEntityBlock.of(::HTSlagCollectorBlockEntity, prop) },
        blockProperty()
            .mapColor(MapColor.STONE)
            .strength(2f)
            .sound(SoundType.COPPER)
            .requiresCorrectToolForDrops(),
    )

    @JvmField
    val ADDONS: List<DeferredBlock<out Block>> = listOf(
        CATALYST_ADDON,
        ENERGY_NETWORK_INTERFACE,
        SLAG_COLLECTOR,
    )

    @JvmField
    val MAGMA_BURNER: DeferredBlock<Block> = register(
        "magma_burner",
        blockProperty()
            .mapColor(MapColor.STONE)
            .strength(5f)
            .sound(SoundType.COPPER)
            .noOcclusion()
            .requiresCorrectToolForDrops(),
    )

    @JvmField
    val SOUL_BURNER: DeferredBlock<Block> = register(
        "soul_burner",
        blockProperty()
            .mapColor(MapColor.STONE)
            .strength(5f)
            .sound(SoundType.COPPER)
            .noOcclusion()
            .requiresCorrectToolForDrops(),
    )

    @JvmField
    val FIERY_BURNER: DeferredBlock<Block> = register(
        "fiery_burner",
        blockProperty()
            .mapColor(MapColor.STONE)
            .strength(5f)
            .sound(SoundType.COPPER)
            .noOcclusion()
            .requiresCorrectToolForDrops(),
    )

    @JvmField
    val BURNERS: List<DeferredBlock<Block>> = listOf(
        MAGMA_BURNER,
        SOUL_BURNER,
        FIERY_BURNER,
    )
}
