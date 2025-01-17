package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.content.HTBlockContent
import hiiragi283.ragium.api.extension.blockProperty
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineTierProvider
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.util.HTOreVariant
import hiiragi283.ragium.common.block.machine.HTManualGrinderBlock
import hiiragi283.ragium.common.block.storage.HTDrumBlock
import net.minecraft.world.level.block.*
import net.minecraft.world.level.block.state.BlockBehaviour
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredRegister

object RagiumBlocks {
    @JvmField
    val REGISTER: DeferredRegister.Blocks = DeferredRegister.createBlocks(RagiumAPI.MOD_ID)

    init {
        Ores.entries

        StorageBlocks.entries
        Grates.entries
        Casings.entries
        Hulls.entries
        Coils.entries

        Drums.entries

        Decorations.entries
        LEDBlocks.entries
    }

    //    Components    //

    enum class Ores(override val oreVariant: HTOreVariant, override val material: HTMaterialKey) : HTOreVariant.Content {
        // overworld
        CRUDE_RAGINITE(HTOreVariant.OVERWORLD, RagiumMaterialKeys.CRUDE_RAGINITE),
        RAGINITE(HTOreVariant.OVERWORLD, RagiumMaterialKeys.RAGINITE),
        RAGI_CRYSTAL(HTOreVariant.OVERWORLD, RagiumMaterialKeys.RAGI_CRYSTAL),

        // deepslate
        DEEPSLATE_CRUDE_RAGINITE(HTOreVariant.DEEP, RagiumMaterialKeys.CRUDE_RAGINITE),
        DEEPSLATE_RAGINITE(HTOreVariant.DEEP, RagiumMaterialKeys.RAGINITE),
        DEEPSLATE_RAGI_CRYSTAL(HTOreVariant.DEEP, RagiumMaterialKeys.RAGI_CRYSTAL),

        // nether
        NETHER_CRUDE_RAGINITE(HTOreVariant.NETHER, RagiumMaterialKeys.CRUDE_RAGINITE),
        NETHER_RAGINITE(HTOreVariant.NETHER, RagiumMaterialKeys.RAGINITE),
        NETHER_RAGI_CRYSTAL(HTOreVariant.NETHER, RagiumMaterialKeys.RAGI_CRYSTAL),

        // end
        END_CRUDE_RAGINITE(HTOreVariant.END, RagiumMaterialKeys.CRUDE_RAGINITE),
        END_RAGINITE(HTOreVariant.END, RagiumMaterialKeys.RAGINITE),
        END_RAGI_CRYSTAL(HTOreVariant.END, RagiumMaterialKeys.RAGI_CRYSTAL),
        ;

        override val holder: DeferredBlock<out Block> =
            REGISTER.registerSimpleBlock("${name.lowercase()}_ore", oreVariant.createProperty())
    }

    enum class StorageBlocks(isGem: Boolean, override val material: HTMaterialKey) : HTBlockContent.Material {
        RAGI_ALLOY(false, RagiumMaterialKeys.RAGI_ALLOY),
        RAGI_STEEL(false, RagiumMaterialKeys.RAGI_STEEL),
        ALUMINUM(false, RagiumMaterialKeys.ALUMINUM),
        FLUORITE(true, RagiumMaterialKeys.FLUORITE),
        STEEL(false, RagiumMaterialKeys.STEEL),
        RAGI_CRYSTAL(true, RagiumMaterialKeys.RAGI_CRYSTAL),
        REFINED_RAGI_STEEL(false, RagiumMaterialKeys.REFINED_RAGI_STEEL),
        CRYOLITE(true, RagiumMaterialKeys.CRYOLITE),
        DEEP_STEEL(false, RagiumMaterialKeys.DEEP_STEEL),
        RAGIUM(false, RagiumMaterialKeys.RAGIUM),
        ;

        override val holder: DeferredBlock<Block> = REGISTER.registerSimpleBlock(
            "${name.lowercase()}_block",
            when (isGem) {
                true -> Blocks.AMETHYST_BLOCK
                false -> Blocks.IRON_BLOCK
            }.let(::blockProperty),
        )
        override val tagPrefix: HTTagPrefix = HTTagPrefix.STORAGE_BLOCK

        val basePrefix: HTTagPrefix = when (isGem) {
            true -> HTTagPrefix.GEM
            false -> HTTagPrefix.INGOT
        }
    }

    enum class Grates(override val machineTier: HTMachineTier) : HTBlockContent.Tier {
        BASIC(HTMachineTier.BASIC),
        ADVANCED(HTMachineTier.ADVANCED),
        ELITE(HTMachineTier.ELITE),
        ULTIMATE(HTMachineTier.ULTIMATE),
        ;

        override val holder: DeferredBlock<TransparentBlock> =
            REGISTER.registerBlock("${name.lowercase()}_grate", ::TransparentBlock, blockProperty(Blocks.COPPER_GRATE))
    }

    enum class Casings(override val machineTier: HTMachineTier) : HTBlockContent.Tier {
        BASIC(HTMachineTier.BASIC),
        ADVANCED(HTMachineTier.ADVANCED),
        ELITE(HTMachineTier.ELITE),
        ULTIMATE(HTMachineTier.ULTIMATE),
        ;

        override val holder: DeferredBlock<Block> =
            REGISTER.registerSimpleBlock("${name.lowercase()}_casing", blockProperty(Blocks.SMOOTH_STONE))
    }

    enum class Hulls(override val machineTier: HTMachineTier) : HTBlockContent.Tier {
        BASIC(HTMachineTier.BASIC),
        ADVANCED(HTMachineTier.ADVANCED),
        ELITE(HTMachineTier.ELITE),
        ULTIMATE(HTMachineTier.ULTIMATE),
        ;

        override val holder: DeferredBlock<TransparentBlock> =
            REGISTER.registerBlock("${name.lowercase()}_hull", ::TransparentBlock, blockProperty(Blocks.IRON_BLOCK))
    }

    enum class Coils(override val machineTier: HTMachineTier) : HTBlockContent.Tier {
        BASIC(HTMachineTier.BASIC),
        ADVANCED(HTMachineTier.ADVANCED),
        ELITE(HTMachineTier.ELITE),
        ULTIMATE(HTMachineTier.ULTIMATE),
        ;

        override val holder: DeferredBlock<RotatedPillarBlock> =
            REGISTER.registerBlock("${name.lowercase()}_coil", ::RotatedPillarBlock, blockProperty(Blocks.COPPER_BLOCK))
    }

    @JvmField
    val SHAFT: DeferredBlock<RotatedPillarBlock> =
        REGISTER.registerBlock("shaft", ::RotatedPillarBlock, blockProperty(Blocks.CHAIN))

    //    Storage    //

    enum class Drums(override val machineTier: HTMachineTier) :
        HTBlockContent,
        HTMachineTierProvider {
        BASIC(HTMachineTier.BASIC),
        ADVANCED(HTMachineTier.ADVANCED),
        ELITE(HTMachineTier.ELITE),
        ULTIMATE(HTMachineTier.ULTIMATE),
        ;

        override val holder: DeferredBlock<Block> = REGISTER.registerBlock(
            "${name.lowercase()}_drum",
            { prop: BlockBehaviour.Properties -> HTDrumBlock(machineTier, prop) },
            blockProperty(Blocks.SMOOTH_STONE),
        )
    }

    //    Buildings    //

    @JvmField
    val PLASTIC_BLOCK: DeferredBlock<Block> =
        REGISTER.registerSimpleBlock("plastic_block", blockProperty(Blocks.SMOOTH_STONE))

    enum class Decorations(
        val parent: HTBlockContent,
        factory: (BlockBehaviour.Properties) -> Block = ::Block,
        val cutout: Boolean = false,
        val isPillar: Boolean = false,
    ) : HTBlockContent {
        // storage
        RAGI_ALLOY_BLOCK(StorageBlocks.RAGI_ALLOY),
        RAGI_STEEL_BLOCK(StorageBlocks.RAGI_STEEL),
        REFINED_RAGI_STEEL_BLOCK(StorageBlocks.REFINED_RAGI_STEEL),

        // casing
        BASIC_CASING(Casings.BASIC),
        ADVANCED_CASING(Casings.ADVANCED),
        ELITE_CASING(Casings.ELITE),
        ULTIMATE_CASING(Casings.ULTIMATE),

        // hull
        BASIC_HULL(Hulls.BASIC, ::TransparentBlock, true),
        ADVANCED_HULL(Hulls.ADVANCED, ::TransparentBlock, true),
        ELITE_HULL(Hulls.ELITE, ::TransparentBlock, true),
        ULTIMATE_HULL(Hulls.ULTIMATE, ::TransparentBlock, true),

        // coil
        BASIC_COIL(Coils.BASIC, ::RotatedPillarBlock, isPillar = true),
        ADVANCED_COIL(Coils.ADVANCED, ::RotatedPillarBlock, isPillar = true),
        ELITE_COIL(Coils.ELITE, ::RotatedPillarBlock, isPillar = true),
        ULTIMATE_COIL(Coils.ULTIMATE, ::RotatedPillarBlock, isPillar = true),
        ;

        override val holder: DeferredBlock<Block> = REGISTER.registerBlock(
            "${name.lowercase()}_decoration",
            factory,
            blockProperty(Blocks.SMOOTH_STONE),
        )
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

    //    Manual Machines    //

    @JvmField
    val MANUAL_GRINDER: DeferredBlock<HTManualGrinderBlock> =
        REGISTER.registerBlock("manual_grinder", ::HTManualGrinderBlock, blockProperty(Blocks.BRICKS))

    //    Utility    //

    @JvmField
    val ENERGY_NETWORK_INTERFACE: DeferredBlock<Block> =
        REGISTER.registerSimpleBlock("energy_network_interface", blockProperty(Blocks.SMOOTH_STONE))
}
