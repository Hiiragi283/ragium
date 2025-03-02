package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.blockProperty
import hiiragi283.ragium.api.extension.itemProperty
import hiiragi283.ragium.api.extension.lore
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.CommonMaterials
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import hiiragi283.ragium.api.util.*
import hiiragi283.ragium.common.block.*
import hiiragi283.ragium.common.block.addon.HTEnergyNetworkBlock
import hiiragi283.ragium.common.block.addon.HTSlagCollectorBlockEntity
import hiiragi283.ragium.common.block.machine.HTDisenchantingTableBlock
import hiiragi283.ragium.common.block.machine.HTManualGrinderBlock
import hiiragi283.ragium.common.block.machine.HTPrimitiveBlastFurnaceBlock
import hiiragi283.ragium.common.block.storage.HTCrateBlockEntity
import hiiragi283.ragium.common.block.storage.HTDrumBlockEntity
import hiiragi283.ragium.common.item.HTBlockItem
import net.minecraft.core.BlockPos
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.*
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.MapColor
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredRegister

object RagiumBlocks {
    @JvmField
    val REGISTER: DeferredRegister.Blocks = DeferredRegister.createBlocks(RagiumAPI.MOD_ID)

    @JvmField
    val ITEM_REGISTER: DeferredRegister.Items = DeferredRegister.createItems(RagiumAPI.MOD_ID)

    private class Builder(val name: String) {
        private lateinit var blockProperties: BlockBehaviour.Properties
        private val itemProperties: Item.Properties = itemProperty()

        fun properties(parent: Block): Builder = properties(blockProperty(parent))

        fun properties(blockProperties: BlockBehaviour.Properties): Builder = apply {
            this.blockProperties = blockProperties
        }

        fun addLore(vararg keys: String): Builder = apply {
            itemProperties.lore(*keys)
        }

        fun addLore(vararg lines: Component): Builder = apply {
            itemProperties.lore(*lines)
        }

        fun <T : Any> component(type: DataComponentType<T>, value: T): Builder = apply {
            itemProperties.component(type, value)
        }

        fun build(): DeferredBlock<Block> {
            val holder: DeferredBlock<Block> = REGISTER.registerSimpleBlock(name, blockProperties)
            ITEM_REGISTER.register(name) { _: ResourceLocation -> HTBlockItem(holder.get(), itemProperties) }
            return holder
        }

        fun <T : Block> build(factory: (BlockBehaviour.Properties) -> T): DeferredBlock<T> {
            val holder: DeferredBlock<T> = REGISTER.registerBlock(name, factory, blockProperties)
            ITEM_REGISTER.register(name) { _: ResourceLocation -> HTBlockItem(holder.get(), itemProperties) }
            return holder
        }
    }

    //    Natural Resources    //

    @JvmField
    val RAGINITE_ORES = HTOreSets(REGISTER, ITEM_REGISTER, RagiumMaterials.RAGINITE)

    @JvmField
    val RAGI_CRYSTAL_ORES = HTOreSets(REGISTER, ITEM_REGISTER, RagiumMaterials.RAGI_CRYSTAL)

    @JvmField
    val SOUL_MAGMA_BLOCK: DeferredBlock<HTSoulMagmaBlock> = Builder("soul_magma_block")
        .properties(blockProperty(Blocks.MAGMA_BLOCK).mapColor(MapColor.LAPIS))
        .build(::HTSoulMagmaBlock)

    @JvmField
    val CRUDE_OIL: DeferredBlock<LiquidBlock> = Builder("crude_oil")
        .properties(blockProperty(Blocks.WATER).mapColor(MapColor.COLOR_BLACK))
        .build { properties: BlockBehaviour.Properties -> LiquidBlock(RagiumFluids.CRUDE_OIL.get(), properties) }

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
        put(RagiumMaterials.EMBER_ALLOY, metal(MapColor.COLOR_ORANGE))
        put(CommonMaterials.ALUMINUM, metal(MapColor.ICE))
        put(RagiumMaterials.DURALUMIN, metal(MapColor.COLOR_PURPLE))
        // Gem
        put(RagiumMaterials.RAGI_CRYSTAL, gem(MapColor.COLOR_RED))
        put(CommonMaterials.CRYOLITE, gem(MapColor.NONE))
        // Other
        put(
            RagiumMaterials.FIERY_COAL,
            blockProperty().mapColor(MapColor.CRIMSON_HYPHAE).strength(5f).sound(SoundType.DEEPSLATE),
        )
    }.mapValues { (key: HTMaterialKey, properties: BlockBehaviour.Properties) ->
        Builder("${key.name}_block")
            .properties(properties)
            .component(DataComponents.ITEM_NAME, HTTagPrefix.BLOCK.createText(key))
            .build()
    }

    @JvmStatic
    fun getStorageParent(key: HTMaterialKey): HTTagPrefix = when (key) {
        CommonMaterials.CRYOLITE -> HTTagPrefix.GEM
        RagiumMaterials.FIERY_COAL -> HTTagPrefix.GEM
        RagiumMaterials.RAGI_CRYSTAL -> HTTagPrefix.GEM
        else -> HTTagPrefix.INGOT
    }

    @JvmField
    val SLAG_BLOCK: DeferredBlock<Block> = Builder("slag_block")
        .properties(blockProperty().mapColor(MapColor.TERRACOTTA_CYAN).strength(3f).sound(SoundType.DEEPSLATE))
        .build()

    //    Buildings    //

    @JvmField
    val RAGI_BRICKS: DeferredBlock<Block> = Builder("ragi_bricks")
        .properties(Blocks.BRICKS)
        .build()

    @JvmField
    val RAGI_BRICK_FAMILY = HTBlockFamily(
        REGISTER,
        ITEM_REGISTER,
        RAGI_BRICKS,
        blockProperty(Blocks.BRICKS),
        "ragi_brick",
    )

    @JvmField
    val PLASTIC_BLOCK: DeferredBlock<Block> = Builder("plastic_block")
        .properties(blockProperty().strength(2f).sound(SoundType.COPPER))
        .build()

    @JvmField
    val PLASTIC_FAMILY = HTBlockFamily(
        REGISTER,
        ITEM_REGISTER,
        PLASTIC_BLOCK,
        blockProperty().strength(2f).sound(SoundType.COPPER),
    )

    @JvmField
    val BLUE_NETHER_BRICKS: DeferredBlock<Block> = Builder("blue_nether_bricks")
        .properties(Blocks.RED_NETHER_BRICKS)
        .build()

    @JvmField
    val BLUE_NETHER_BRICK_FAMILY = HTBlockFamily(
        REGISTER,
        ITEM_REGISTER,
        BLUE_NETHER_BRICKS,
        blockProperty().strength(2f, 6f).sound(SoundType.NETHER_BRICKS),
        "blue_nether_brick",
    )

    @JvmField
    val SHAFT: DeferredBlock<RotatedPillarBlock> = Builder("shaft")
        .properties(
            blockProperty()
                .forceSolidOn()
                .requiresCorrectToolForDrops()
                .strength(5f)
                .sound(SoundType.COPPER)
                .noOcclusion(),
        ).build(::RotatedPillarBlock)

    @JvmField
    val CHEMICAL_GLASS: DeferredBlock<TransparentBlock> =
        Builder("chemical_glass").properties(Blocks.GLASS).build(::TransparentBlock)

    @JvmField
    val MOB_GLASS: DeferredBlock<HTMobGlassBlock> =
        Builder("mob_glass")
            .properties(Blocks.GLASS)
            .addLore(RagiumTranslationKeys.MOB_GLASS)
            .build(::HTMobGlassBlock)

    @JvmField
    val OBSIDIAN_GLASS: DeferredBlock<TransparentBlock> =
        Builder("obsidian_glass").properties(blockProperty(Blocks.GLASS).strength(5f, 1200f)).build(::TransparentBlock)

    @JvmField
    val SOUL_GLASS: DeferredBlock<HTSoulGlassBlock> =
        Builder("soul_glass")
            .properties(Blocks.GLASS)
            .addLore(RagiumTranslationKeys.SOUL_GLASS)
            .build(::HTSoulGlassBlock)

    @JvmField
    val GLASSES: List<DeferredBlock<out TransparentBlock>> = listOf(
        CHEMICAL_GLASS,
        MOB_GLASS,
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
        Builder("${color.serializedName}_led_block")
            .properties(
                blockProperty(Blocks.GLASS)
                    .mapColor(color)
                    .lightLevel { 15 }
                    .sound(SoundType.GLASS),
            ).build()
    }

    @JvmStatic
    fun getLedBlock(color: DyeColor): DeferredBlock<Block> = LED_BLOCKS[color] ?: error("Unregistered color: ${color.serializedName}")

    //    Foods    //

    @JvmField
    val SPONGE_CAKE: DeferredBlock<HayBlock> = Builder("sponge_cake")
        .properties(blockProperty().mapColor(MapColor.COLOR_YELLOW).strength(0.5f).sound(SoundType.WOOL))
        .addLore(RagiumTranslationKeys.SPONGE_CAKE)
        .build(::HayBlock)

    @JvmField
    val SWEET_BERRIES_CAKE: DeferredBlock<HTSweetBerriesCakeBlock> = Builder("sweet_berries_cake")
        .properties(blockProperty().forceSolidOn().strength(0.5f).sound(SoundType.WOOL))
        .build(::HTSweetBerriesCakeBlock)

    //    Manual Machines    //

    @JvmField
    val MANUAL_GRINDER: DeferredBlock<HTManualGrinderBlock> = Builder("manual_grinder")
        .properties(Blocks.BRICKS)
        .addLore(RagiumTranslationKeys.MANUAL_GRINDER, RagiumTranslationKeys.MANUAL_GRINDER_1)
        .build(::HTManualGrinderBlock)

    @JvmField
    val PRIMITIVE_BLAST_FURNACE: DeferredBlock<HTPrimitiveBlastFurnaceBlock> = Builder("primitive_blast_furnace")
        .properties(Blocks.BRICKS)
        .addLore(RagiumTranslationKeys.PRIMITIVE_BLAST_FURNACE, RagiumTranslationKeys.PRIMITIVE_BLAST_FURNACE_1)
        .build(::HTPrimitiveBlastFurnaceBlock)

    @JvmField
    val DISENCHANTING_TABLE: DeferredBlock<HTDisenchantingTableBlock> = Builder("disenchanting_table")
        .properties(Blocks.OBSIDIAN)
        .build(::HTDisenchantingTableBlock)

    //    Storage    //

    @JvmField
    val CRATES: Map<HTCrateVariant, DeferredBlock<HTEntityBlock.Horizontal>> =
        HTCrateVariant.entries.associateWith { variant: HTCrateVariant ->
            Builder("${variant.serializedName}_crate")
                .properties(
                    blockProperty()
                        .mapColor(MapColor.STONE)
                        .strength(2f)
                        .sound(SoundType.COPPER)
                        .requiresCorrectToolForDrops(),
                ).build { properties: BlockBehaviour.Properties ->
                    HTEntityBlock.horizontal(
                        { pos: BlockPos, state: BlockState -> HTCrateBlockEntity(pos, state, variant) },
                        properties,
                    )
                }
        }

    @JvmStatic
    fun getCrate(variant: HTCrateVariant): DeferredBlock<HTEntityBlock.Horizontal> = CRATES[variant]!!

    @JvmField
    val DRUMS: Map<HTDrumVariant, DeferredBlock<HTEntityBlock>> =
        HTDrumVariant.entries.associateWith { variant: HTDrumVariant ->
            Builder("${variant.serializedName}_drum")
                .properties(
                    blockProperty()
                        .mapColor(MapColor.STONE)
                        .strength(2f)
                        .sound(SoundType.COPPER)
                        .requiresCorrectToolForDrops(),
                ).build { properties: BlockBehaviour.Properties -> HTEntityBlock.of(::HTDrumBlockEntity, properties) }
        }

    @JvmStatic
    fun getDrum(variant: HTDrumVariant): DeferredBlock<HTEntityBlock> = DRUMS[variant]!!

    //    Utility    //

    @JvmField
    val ENERGY_NETWORK_INTERFACE: DeferredBlock<HTEnergyNetworkBlock> = Builder("energy_network_interface")
        .properties(
            blockProperty()
                .mapColor(MapColor.STONE)
                .strength(2f)
                .sound(SoundType.COPPER)
                .requiresCorrectToolForDrops(),
        ).addLore(RagiumTranslationKeys.ENERGY_NETWORK_INTERFACE)
        .build(::HTEnergyNetworkBlock)

    @JvmField
    val SLAG_COLLECTOR: DeferredBlock<HTEntityBlock> = Builder("slag_collector")
        .properties(
            blockProperty()
                .mapColor(MapColor.STONE)
                .strength(2f)
                .sound(SoundType.COPPER)
                .requiresCorrectToolForDrops(),
        ).addLore(RagiumTranslationKeys.SLAG_COLLECTOR)
        .build { prop: BlockBehaviour.Properties -> HTEntityBlock.of(::HTSlagCollectorBlockEntity, prop) }

    @JvmField
    val ADDONS: List<DeferredBlock<out Block>> = listOf(
        ENERGY_NETWORK_INTERFACE,
        SLAG_COLLECTOR,
    )

    @JvmField
    val MAGMA_BURNER: DeferredBlock<Block> = Builder("magma_burner")
        .properties(
            blockProperty()
                .mapColor(MapColor.STONE)
                .strength(5f)
                .sound(SoundType.COPPER)
                .noOcclusion()
                .requiresCorrectToolForDrops(),
        ).build()

    @JvmField
    val SOUL_BURNER: DeferredBlock<Block> = Builder("soul_burner")
        .properties(
            blockProperty()
                .mapColor(MapColor.STONE)
                .strength(5f)
                .sound(SoundType.COPPER)
                .noOcclusion()
                .requiresCorrectToolForDrops(),
        ).build()

    @JvmField
    val FIERY_BURNER: DeferredBlock<Block> = Builder("fiery_burner")
        .properties(
            blockProperty()
                .mapColor(MapColor.STONE)
                .strength(5f)
                .sound(SoundType.COPPER)
                .noOcclusion()
                .requiresCorrectToolForDrops(),
        ).build()

    @JvmField
    val BURNERS: List<DeferredBlock<Block>> = listOf(
        MAGMA_BURNER,
        SOUL_BURNER,
        FIERY_BURNER,
    )
}
