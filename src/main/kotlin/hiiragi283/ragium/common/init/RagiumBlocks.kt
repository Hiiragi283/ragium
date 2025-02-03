package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.content.HTBlockContent
import hiiragi283.ragium.api.extension.blockProperty
import hiiragi283.ragium.api.extension.buildTable
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.CommonMaterials
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import hiiragi283.ragium.api.material.keys.VanillaMaterials
import hiiragi283.ragium.api.util.HTOreVariant
import hiiragi283.ragium.api.util.collection.HTTable
import hiiragi283.ragium.common.block.HTSoulMagmaBlock
import hiiragi283.ragium.common.block.HTSweetBerriesCakeBlock
import hiiragi283.ragium.common.block.addon.HTCatalystAddonBlock
import hiiragi283.ragium.common.block.addon.HTCoolantBlock
import hiiragi283.ragium.common.block.addon.HTEnergyNetworkBlock
import hiiragi283.ragium.common.block.machine.HTManualGrinderBlock
import hiiragi283.ragium.common.block.machine.HTPrimitiveBlastFurnaceBlock
import hiiragi283.ragium.common.block.storage.HTDrumBlock
import net.minecraft.world.level.block.*
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredRegister

object RagiumBlocks {
    @JvmField
    val REGISTER: DeferredRegister.Blocks = DeferredRegister.createBlocks(RagiumAPI.MOD_ID)

    init {
        Grates.entries
        Casings.entries
        Coils.entries
        Burners.entries

        Drums.entries

        Decorations.entries
        LEDBlocks.entries
    }

    //    Components    //

    @JvmField
    val ORES: HTTable<HTOreVariant, HTMaterialKey, DeferredBlock<out Block>> = buildTable {
        val materials: List<HTMaterialKey> = listOf(
            RagiumMaterials.CRUDE_RAGINITE,
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
    val SOUL_MAGMA_BLOCK: DeferredBlock<HTSoulMagmaBlock> =
        REGISTER.registerBlock("soul_magma_block", ::HTSoulMagmaBlock, blockProperty(Blocks.MAGMA_BLOCK))

    @JvmField
    val STORAGE_BLOCKS: Map<HTMaterialKey, DeferredBlock<Block>> = listOf(
        // Ragium
        RagiumMaterials.RAGI_ALLOY,
        RagiumMaterials.RAGI_STEEL,
        RagiumMaterials.RAGIUM,
        // Steel
        CommonMaterials.STEEL,
        CommonMaterials.STAINLESS_STEEL,
        RagiumMaterials.DEEP_STEEL,
        // Metal
        CommonMaterials.ALUMINUM,
        RagiumMaterials.ECHORIUM,
        // Gem
        RagiumMaterials.RAGI_CRYSTAL,
        CommonMaterials.FLUORITE,
        CommonMaterials.CRYOLITE,
        // Other
        RagiumMaterials.SLAG,
        RagiumMaterials.RESIDUAL_COKE,
        RagiumMaterials.FIERY_COAL,
    ).associateWith { key: HTMaterialKey ->
        REGISTER.registerSimpleBlock(
            "${key.name}_block",
            blockProperty(
                if (getStorageParent(key) == HTTagPrefix.GEM) {
                    Blocks.AMETHYST_BLOCK
                } else {
                    Blocks.IRON_BLOCK
                },
            ),
        )
    }

    @JvmStatic
    fun getStorageParent(key: HTMaterialKey): HTTagPrefix = when (key) {
        RagiumMaterials.RAGI_CRYSTAL -> HTTagPrefix.GEM
        CommonMaterials.FLUORITE -> HTTagPrefix.GEM
        CommonMaterials.CRYOLITE -> HTTagPrefix.GEM
        RagiumMaterials.SLAG -> HTTagPrefix.GEM
        RagiumMaterials.FIERY_COAL -> HTTagPrefix.GEM
        RagiumMaterials.RESIDUAL_COKE -> HTTagPrefix.GEM
        else -> HTTagPrefix.INGOT
    }

    @JvmField
    val CASINGS: Map<HTMaterialKey, DeferredBlock<Block>> = listOf(
        // Vanilla
        VanillaMaterials.COPPER,
        VanillaMaterials.IRON,
        VanillaMaterials.GOLD,
        // Steel
        CommonMaterials.STEEL,
        CommonMaterials.STAINLESS_STEEL,
        RagiumMaterials.DEEP_STEEL,
        VanillaMaterials.NETHERITE,
    ).associateWith { key: HTMaterialKey ->
        REGISTER.registerSimpleBlock(
            "${key.name}_casing",
            blockProperty(Blocks.NETHER_BRICKS),
        )
    }

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

    enum class Casings(override val machineTier: HTMachineTier) : HTBlockContent.Tier {
        BASIC(HTMachineTier.BASIC),
        ADVANCED(HTMachineTier.ADVANCED),
        ELITE(HTMachineTier.ELITE),
        ULTIMATE(HTMachineTier.ULTIMATE),
        ;

        override val holder: DeferredBlock<Block> =
            REGISTER.registerSimpleBlock("${name.lowercase()}_casing", blockProperty(Blocks.SMOOTH_STONE))
        override val translationKey: String = RagiumTranslationKeys.CASING
    }

    enum class Coils(override val machineTier: HTMachineTier) : HTBlockContent.Tier {
        BASIC(HTMachineTier.BASIC),
        ADVANCED(HTMachineTier.ADVANCED),
        ELITE(HTMachineTier.ELITE),
        ULTIMATE(HTMachineTier.ULTIMATE),
        ;

        override val holder: DeferredBlock<RotatedPillarBlock> =
            REGISTER.registerBlock("${name.lowercase()}_coil", ::RotatedPillarBlock, blockProperty(Blocks.COPPER_BLOCK))
        override val translationKey: String = RagiumTranslationKeys.COIL
    }

    enum class Burners(override val machineTier: HTMachineTier) : HTBlockContent.Tier {
        ADVANCED(HTMachineTier.ADVANCED),
        ELITE(HTMachineTier.ELITE),
        ULTIMATE(HTMachineTier.ULTIMATE),
        ;

        override val holder: DeferredBlock<Block> =
            REGISTER.registerBlock(
                "${name.lowercase()}_burner",
                ::Block,
                blockProperty(Blocks.COPPER_BLOCK).noOcclusion(),
            )
        override val translationKey: String = RagiumTranslationKeys.BURNER
    }

    @JvmField
    val SHAFT: DeferredBlock<RotatedPillarBlock> =
        REGISTER.registerBlock("shaft", ::RotatedPillarBlock, blockProperty(Blocks.CHAIN))

    @JvmField
    val CHEMICAL_GLASS: DeferredBlock<TransparentBlock> =
        REGISTER.registerBlock("chemical_glass", ::TransparentBlock, blockProperty(Blocks.GLASS))

    //    Storage    //

    enum class Drums(override val machineTier: HTMachineTier) : HTBlockContent.Tier {
        BASIC(HTMachineTier.BASIC),
        ADVANCED(HTMachineTier.ADVANCED),
        ELITE(HTMachineTier.ELITE),
        ULTIMATE(HTMachineTier.ULTIMATE),
        ;

        override val holder: DeferredBlock<Block> =
            REGISTER.registerBlock("${name.lowercase()}_drum", ::HTDrumBlock, blockProperty(Blocks.SMOOTH_STONE))
        override val translationKey: String = RagiumTranslationKeys.DRUM
    }

    //    Buildings    //

    @JvmField
    val PLASTIC_BLOCK: DeferredBlock<Block> =
        REGISTER.registerSimpleBlock("plastic_block", blockProperty(Blocks.SMOOTH_STONE))

    enum class Decorations(val parent: HTBlockContent) : HTBlockContent {
        // storage
        RAGI_ALLOY_BLOCK(RagiumMaterials.RAGI_ALLOY),
        RAGI_STEEL_BLOCK(RagiumMaterials.RAGI_STEEL),

        // casing
        BASIC_CASING(Casings.BASIC),
        ADVANCED_CASING(Casings.ADVANCED),
        ELITE_CASING(Casings.ELITE),
        ULTIMATE_CASING(Casings.ULTIMATE),

        // coil
        BASIC_COIL(Coils.BASIC),
        ADVANCED_COIL(Coils.ADVANCED),
        ELITE_COIL(Coils.ELITE),
        ULTIMATE_COIL(Coils.ULTIMATE),
        ;

        constructor(key: HTMaterialKey) : this(HTBlockContent.of(DeferredBlock.createBlock<Block>(RagiumAPI.id("${key.name}_block"))))

        override val holder: DeferredBlock<Block> =
            REGISTER.registerSimpleBlock("${name.lowercase()}_decoration", blockProperty(Blocks.SMOOTH_STONE))
    }

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
        blockProperty(Blocks.HAY_BLOCK).sound(SoundType.WOOL),
    )

    @JvmField
    val SWEET_BERRIES_CAKE: DeferredBlock<HTSweetBerriesCakeBlock> = REGISTER.registerBlock(
        "sweet_berries_cake",
        ::HTSweetBerriesCakeBlock,
        blockProperty(Blocks.CAKE),
    )

    //    Manual Machines    //

    @JvmField
    val MANUAL_GRINDER: DeferredBlock<HTManualGrinderBlock> =
        REGISTER.registerBlock("manual_grinder", ::HTManualGrinderBlock, blockProperty(Blocks.BRICKS))

    @JvmField
    val PRIMITIVE_BLAST_FURNACE: DeferredBlock<HTPrimitiveBlastFurnaceBlock> =
        REGISTER.registerBlock("primitive_blast_furnace", ::HTPrimitiveBlastFurnaceBlock, blockProperty(Blocks.BRICKS))

    //    Utility    //

    @JvmField
    val CATALYST_ADDON: DeferredBlock<HTCatalystAddonBlock> =
        REGISTER.registerBlock("catalyst_addon", ::HTCatalystAddonBlock, blockProperty(Blocks.SMOOTH_STONE))

    @JvmField
    val ENERGY_NETWORK_INTERFACE: DeferredBlock<HTEnergyNetworkBlock> =
        REGISTER.registerBlock("energy_network_interface", ::HTEnergyNetworkBlock, blockProperty(Blocks.SMOOTH_STONE))

    @JvmField
    val SUPERCONDUCTIVE_COOLANT: DeferredBlock<HTCoolantBlock> =
        REGISTER.registerBlock("superconductive_coolant", ::HTCoolantBlock, blockProperty().sound(SoundType.GLASS))

    @JvmField
    val ADDONS: List<DeferredBlock<out Block>> = listOf(
        CATALYST_ADDON,
        ENERGY_NETWORK_INTERFACE,
        SUPERCONDUCTIVE_COOLANT,
    )
}
