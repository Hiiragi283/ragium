package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.HTEntityBlock
import hiiragi283.ragium.api.extension.blockProperty
import hiiragi283.ragium.api.extension.buildTable
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.CommonMaterials
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import hiiragi283.ragium.api.util.HTOreVariant
import hiiragi283.ragium.api.util.HTTable
import hiiragi283.ragium.common.block.HTSoulMagmaBlock
import hiiragi283.ragium.common.block.HTSweetBerriesCakeBlock
import hiiragi283.ragium.common.block.addon.HTCatalystAddonBlockEntity
import hiiragi283.ragium.common.block.addon.HTEnergyNetworkBlock
import hiiragi283.ragium.common.block.addon.HTSlagCollectorBlockEntity
import hiiragi283.ragium.common.block.machine.HTManualGrinderBlock
import hiiragi283.ragium.common.block.machine.HTPrimitiveBlastFurnaceBlock
import hiiragi283.ragium.common.block.storage.HTDrumBlock
import net.minecraft.world.item.DyeColor
import net.minecraft.world.level.block.*
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.material.MapColor
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredRegister

object RagiumBlocks {
    @JvmField
    val REGISTER: DeferredRegister.Blocks = DeferredRegister.createBlocks(RagiumAPI.MOD_ID)

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
        RagiumMaterials.FIERY_COAL -> HTTagPrefix.GEM
        RagiumMaterials.RAGI_CRYSTAL -> HTTagPrefix.GEM
        else -> HTTagPrefix.INGOT
    }

    @JvmField
    val SLAG_BLOCK: DeferredBlock<Block> = REGISTER.registerSimpleBlock(
        "slag_block",
        blockProperty().mapColor(MapColor.TERRACOTTA_CYAN).strength(3f).sound(SoundType.DEEPSLATE),
    )

    @JvmField
    val GRATES: Map<HTMachineTier, DeferredBlock<TransparentBlock>> =
        HTMachineTier.entries.associateWith { tier: HTMachineTier ->
            REGISTER.registerBlock(
                "${tier.serializedName}_grate",
                ::TransparentBlock,
                blockProperty(Blocks.COPPER_GRATE),
            )
        }

    @JvmField
    val BURNERS: Map<HTMachineTier, DeferredBlock<Block>> =
        listOf(
            HTMachineTier.ADVANCED,
            HTMachineTier.ELITE,
            HTMachineTier.ULTIMATE,
        ).associateWith { tier: HTMachineTier ->
            REGISTER.registerSimpleBlock(
                "${tier.serializedName}_burner",
                blockProperty()
                    .mapColor(MapColor.STONE)
                    .strength(5f)
                    .sound(SoundType.COPPER)
                    .noOcclusion()
                    .requiresCorrectToolForDrops(),
            )
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

    @JvmField
    val DRUMS: Map<HTMachineTier, DeferredBlock<HTDrumBlock>> =
        HTMachineTier.entries.associateWith { tier: HTMachineTier ->
            REGISTER.registerBlock(
                "${tier.serializedName}_drum",
                { properties: BlockBehaviour.Properties -> HTDrumBlock(tier, properties) },
                blockProperty()
                    .mapColor(MapColor.STONE)
                    .strength(2f)
                    .sound(SoundType.COPPER)
                    .requiresCorrectToolForDrops(),
            )
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
        REGISTER.registerSimpleBlock(
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
