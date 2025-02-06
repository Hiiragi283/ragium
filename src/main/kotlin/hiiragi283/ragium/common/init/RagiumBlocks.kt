package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.HTEntityBlock
import hiiragi283.ragium.api.content.HTBlockContent
import hiiragi283.ragium.api.extension.blockProperty
import hiiragi283.ragium.api.extension.buildTable
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.CommonMaterials
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import hiiragi283.ragium.api.util.HTOreVariant
import hiiragi283.ragium.api.util.collection.HTTable
import hiiragi283.ragium.common.block.HTSoulMagmaBlock
import hiiragi283.ragium.common.block.HTSweetBerriesCakeBlock
import hiiragi283.ragium.common.block.addon.HTCatalystAddonBlockEntity
import hiiragi283.ragium.common.block.addon.HTEnergyNetworkBlock
import hiiragi283.ragium.common.block.addon.HTSlagCollectorBlockEntity
import hiiragi283.ragium.common.block.machine.HTManualGrinderBlock
import hiiragi283.ragium.common.block.machine.HTPrimitiveBlastFurnaceBlock
import hiiragi283.ragium.common.block.storage.HTDrumBlock
import net.minecraft.world.level.block.*
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.material.MapColor
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredRegister

object RagiumBlocks {
    @JvmField
    val REGISTER: DeferredRegister.Blocks = DeferredRegister.createBlocks(RagiumAPI.MOD_ID)

    init {
        Grates.entries
        Burners.entries

        Drums.entries

        LEDBlocks.entries
    }

    //    Components    //

    @JvmField
    val ORES: HTTable<HTOreVariant, HTMaterialKey, DeferredBlock<out Block>> = buildTable {
        val materials: List<HTMaterialKey> = listOf(
            RagiumMaterials.RAGINITE,
            RagiumMaterials.RAGI_CRYSTAL,
        )
        for (variant: HTOreVariant in HTOreVariant.entries) {
            for (key: HTMaterialKey in materials) {
                put(variant, key, REGISTER.registerSimpleBlock(variant.createId(key), variant.createProperty()))
            }
        }
    }

    @JvmField
    val SOUL_MAGMA_BLOCK: DeferredBlock<HTSoulMagmaBlock> = REGISTER.registerBlock(
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
        put(CommonMaterials.FLUORITE, gem(MapColor.EMERALD))
        put(CommonMaterials.CRYOLITE, gem(MapColor.NONE))
        // Other
        put(
            RagiumMaterials.FIERY_COAL,
            blockProperty().mapColor(MapColor.CRIMSON_HYPHAE).strength(5f).sound(SoundType.DEEPSLATE),
        )
    }.mapValues { (key: HTMaterialKey, properties: BlockBehaviour.Properties) ->
        REGISTER.registerSimpleBlock("${key.name}_block", properties)
    }

    @JvmStatic
    fun getStorageParent(key: HTMaterialKey): HTTagPrefix = when (key) {
        CommonMaterials.CRYOLITE -> HTTagPrefix.GEM
        CommonMaterials.FLUORITE -> HTTagPrefix.GEM
        RagiumMaterials.FIERY_COAL -> HTTagPrefix.GEM
        RagiumMaterials.RAGI_CRYSTAL -> HTTagPrefix.GEM
        else -> HTTagPrefix.INGOT
    }

    @JvmField
    val SLAG_BLOCK: DeferredBlock<Block> = REGISTER.registerSimpleBlock(
        "slag_block",
        blockProperty().mapColor(MapColor.TERRACOTTA_CYAN).strength(3f).sound(SoundType.DEEPSLATE),
    )

    enum class Grates(override val machineTier: HTMachineTier) : HTBlockContent.Tier {
        BASIC(HTMachineTier.BASIC),
        ADVANCED(HTMachineTier.ADVANCED),
        ELITE(HTMachineTier.ELITE),
        ULTIMATE(HTMachineTier.ULTIMATE),
        ;

        override val holder: DeferredBlock<TransparentBlock> =
            REGISTER.registerBlock("${name.lowercase()}_grate", ::TransparentBlock, blockProperty(Blocks.COPPER_GRATE))
        override val translationKey: String = RagiumTranslationKeys.GRATE
    }

    enum class Burners(override val machineTier: HTMachineTier) : HTBlockContent.Tier {
        ADVANCED(HTMachineTier.ADVANCED),
        ELITE(HTMachineTier.ELITE),
        ULTIMATE(HTMachineTier.ULTIMATE),
        ;

        override val holder: DeferredBlock<Block> = REGISTER.registerBlock(
            "${name.lowercase()}_burner",
            ::Block,
            blockProperty()
                .mapColor(MapColor.STONE)
                .strength(5f)
                .sound(SoundType.COPPER)
                .noOcclusion()
                .requiresCorrectToolForDrops(),
        )
        override val translationKey: String = RagiumTranslationKeys.BURNER
    }

    @JvmField
    val SHAFT: DeferredBlock<RotatedPillarBlock> = REGISTER.registerBlock(
        "shaft",
        ::RotatedPillarBlock,
        blockProperty()
            .forceSolidOn()
            .requiresCorrectToolForDrops()
            .strength(5f)
            .sound(SoundType.COPPER)
            .noOcclusion(),
    )

    //    Storage    //

    enum class Drums(override val machineTier: HTMachineTier) : HTBlockContent.Tier {
        BASIC(HTMachineTier.BASIC),
        ADVANCED(HTMachineTier.ADVANCED),
        ELITE(HTMachineTier.ELITE),
        ULTIMATE(HTMachineTier.ULTIMATE),
        ;

        override val holder: DeferredBlock<Block> = REGISTER.registerBlock(
            "${name.lowercase()}_drum",
            ::HTDrumBlock,
            blockProperty()
                .mapColor(MapColor.STONE)
                .strength(2f)
                .sound(SoundType.COPPER)
                .requiresCorrectToolForDrops(),
        )
        override val translationKey: String = RagiumTranslationKeys.DRUM
    }

    //    Buildings    //

    @JvmField
    val PLASTIC_BLOCK: DeferredBlock<Block> =
        REGISTER.registerSimpleBlock("plastic_block", blockProperty().strength(2f).sound(SoundType.COPPER))

    @JvmField
    val CHEMICAL_GLASS: DeferredBlock<TransparentBlock> =
        REGISTER.registerBlock("chemical_glass", ::TransparentBlock, blockProperty(Blocks.GLASS))

    @JvmField
    val OBSIDIAN_GLASS: DeferredBlock<TransparentBlock> =
        REGISTER.registerBlock(
            "obsidian_glass",
            ::TransparentBlock,
            blockProperty(Blocks.GLASS).strength(5f, 1200f),
        )

    @JvmField
    val GLASSES: List<DeferredBlock<TransparentBlock>> = listOf(
        CHEMICAL_GLASS,
        OBSIDIAN_GLASS,
    )

    enum class LEDBlocks(val baseBlock: Block) : HTBlockContent {
        RED(Blocks.RED_STAINED_GLASS),
        GREEN(Blocks.GREEN_STAINED_GLASS),
        BLUE(Blocks.BLUE_STAINED_GLASS),
        CYAN(Blocks.CYAN_STAINED_GLASS),
        MAGENTA(Blocks.MAGENTA_STAINED_GLASS),
        YELLOW(Blocks.YELLOW_STAINED_GLASS),
        WHITE(Blocks.GLASS),
        ;

        override val holder: DeferredBlock<Block> = REGISTER.registerSimpleBlock(
            "${name.lowercase()}_led_block",
            blockProperty(baseBlock)
                .mapColor(baseBlock.defaultMapColor())
                .lightLevel { 15 }
                .sound(SoundType.GLASS),
        )
    }

    //    Foods    //

    @JvmField
    val SPONGE_CAKE: DeferredBlock<HayBlock> = REGISTER.registerBlock(
        "sponge_cake",
        ::HayBlock,
        blockProperty().mapColor(MapColor.COLOR_YELLOW).strength(0.5f).sound(SoundType.WOOL),
    )

    @JvmField
    val SWEET_BERRIES_CAKE: DeferredBlock<HTSweetBerriesCakeBlock> = REGISTER.registerBlock(
        "sweet_berries_cake",
        ::HTSweetBerriesCakeBlock,
        blockProperty().forceSolidOn().strength(0.5f).sound(SoundType.WOOL),
    )

    //    Manual Machines    //

    @JvmField
    val MANUAL_GRINDER: DeferredBlock<HTManualGrinderBlock> = REGISTER.registerBlock(
        "manual_grinder",
        ::HTManualGrinderBlock,
        blockProperty(Blocks.BRICKS),
    )

    @JvmField
    val PRIMITIVE_BLAST_FURNACE: DeferredBlock<HTPrimitiveBlastFurnaceBlock> = REGISTER.registerBlock(
        "primitive_blast_furnace",
        ::HTPrimitiveBlastFurnaceBlock,
        blockProperty(Blocks.BRICKS),
    )

    //    Utility    //

    @JvmField
    val CATALYST_ADDON: DeferredBlock<HTEntityBlock> = REGISTER.registerBlock(
        "catalyst_addon",
        { prop: BlockBehaviour.Properties -> HTEntityBlock.of(::HTCatalystAddonBlockEntity, prop) },
        blockProperty()
            .mapColor(MapColor.COLOR_BLACK)
            .strength(2f)
            .sound(SoundType.COPPER)
            .requiresCorrectToolForDrops(),
    )

    @JvmField
    val ENERGY_NETWORK_INTERFACE: DeferredBlock<HTEnergyNetworkBlock> = REGISTER.registerBlock(
        "energy_network_interface",
        ::HTEnergyNetworkBlock,
        blockProperty()
            .mapColor(MapColor.STONE)
            .strength(2f)
            .sound(SoundType.COPPER)
            .requiresCorrectToolForDrops(),
    )

    @JvmField
    val SLAG_COLLECTOR: DeferredBlock<HTEntityBlock> = REGISTER.registerBlock(
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
}
