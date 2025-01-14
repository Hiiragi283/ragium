package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.content.HTBlockContent
import hiiragi283.ragium.api.content.HTContent
import hiiragi283.ragium.api.extension.blockProperty
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineTierProvider
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.common.block.storage.HTDrumBlock
import net.minecraft.world.item.DyeColor
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.RotatedPillarBlock
import net.minecraft.world.level.block.TransparentBlock
import net.minecraft.world.level.block.state.BlockBehaviour
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

object RagiumBlocks {
    //    Components    //

    enum class StorageBlocks(override val material: HTMaterialKey) : HTBlockContent.Material {
        RAGI_ALLOY(RagiumMaterialKeys.RAGI_ALLOY),
        RAGI_STEEL(RagiumMaterialKeys.RAGI_STEEL),
        ALUMINUM(RagiumMaterialKeys.ALUMINUM),
        FLUORITE(RagiumMaterialKeys.FLUORITE),
        STEEL(RagiumMaterialKeys.STEEL),
        RAGI_CRYSTAL(RagiumMaterialKeys.RAGI_CRYSTAL),
        REFINED_RAGI_STEEL(RagiumMaterialKeys.REFINED_RAGI_STEEL),
        CRYOLITE(RagiumMaterialKeys.CRYOLITE),
        DEEP_STEEL(RagiumMaterialKeys.DEEP_STEEL),
        RAGIUM(RagiumMaterialKeys.RAGIUM),
        ;

        override val holder: DeferredHolder<Block, Block> = HTContent.blockHolder("${name.lowercase()}_block")
        override val tagPrefix: HTTagPrefix = HTTagPrefix.STORAGE_BLOCK
    }

    enum class Grates(override val machineTier: HTMachineTier) : HTBlockContent.Tier {
        SIMPLE(HTMachineTier.SIMPLE),
        BASIC(HTMachineTier.BASIC),
        ADVANCED(HTMachineTier.ADVANCED),
        ELITE(HTMachineTier.ELITE),
        ;

        override val holder: DeferredHolder<Block, Block> = HTContent.blockHolder("${name.lowercase()}_grate")
    }

    enum class Casings(override val machineTier: HTMachineTier) : HTBlockContent.Tier {
        SIMPLE(HTMachineTier.SIMPLE),
        BASIC(HTMachineTier.BASIC),
        ADVANCED(HTMachineTier.ADVANCED),
        ELITE(HTMachineTier.ELITE),
        ;

        override val holder: DeferredHolder<Block, Block> = HTContent.blockHolder("${name.lowercase()}_casing")
    }

    enum class Hulls(override val machineTier: HTMachineTier) : HTBlockContent.Tier {
        PRIMITIVE(HTMachineTier.PRIMITIVE),
        SIMPLE(HTMachineTier.SIMPLE),
        BASIC(HTMachineTier.BASIC),
        ADVANCED(HTMachineTier.ADVANCED),
        ELITE(HTMachineTier.ELITE),
        ;

        override val holder: DeferredHolder<Block, Block> = HTContent.blockHolder("${name.lowercase()}_hull")
    }

    enum class Coils(override val machineTier: HTMachineTier) : HTBlockContent.Tier {
        SIMPLE(HTMachineTier.SIMPLE),
        BASIC(HTMachineTier.BASIC),
        ADVANCED(HTMachineTier.ADVANCED),
        ELITE(HTMachineTier.ELITE),
        ;

        override val holder: DeferredHolder<Block, Block> = HTContent.blockHolder("${name.lowercase()}_coil")
    }

    //    Storage    //

    enum class Drums(override val machineTier: HTMachineTier) :
        HTBlockContent,
        HTMachineTierProvider {
        SIMPLE(HTMachineTier.SIMPLE),
        BASIC(HTMachineTier.BASIC),
        ADVANCED(HTMachineTier.ADVANCED),
        ELITE(HTMachineTier.ELITE),
        ;

        override val holder: DeferredHolder<Block, out Block> = HTContent.blockHolder("${name.lowercase()}_drum")
    }

    //    Buildings    //

    enum class Decorations(
        val parent: HTBlockContent,
        val factory: (BlockBehaviour.Properties) -> Block = ::Block,
        val cutout: Boolean = false,
        val isPillar: Boolean = false,
    ) : HTBlockContent {
        // storage
        RAGI_ALLOY_BLOCK(StorageBlocks.RAGI_ALLOY),
        RAGI_STEEL_BLOCK(StorageBlocks.RAGI_STEEL),
        REFINED_RAGI_STEEL_BLOCK(StorageBlocks.REFINED_RAGI_STEEL),

        // casing
        SIMPLE_CASING(Casings.SIMPLE),
        BASIC_CASING(Casings.BASIC),
        ADVANCED_CASING(Casings.ADVANCED),
        ELITE_CASING(Casings.ELITE),

        // hull
        PRIMITIVE_HULL(Hulls.PRIMITIVE, ::TransparentBlock, true),
        SIMPLE_HULL(Hulls.SIMPLE, ::TransparentBlock, true),
        BASIC_HULL(Hulls.BASIC, ::TransparentBlock, true),
        ADVANCED_HULL(Hulls.ADVANCED, ::TransparentBlock, true),

        // coil
        SIMPLE_COIL(Coils.SIMPLE, ::RotatedPillarBlock, isPillar = true),
        BASIC_COIL(Coils.BASIC, ::RotatedPillarBlock, isPillar = true),
        ADVANCED_COIL(Coils.ADVANCED, ::RotatedPillarBlock, isPillar = true),
        ELITE_COIL(Coils.ELITE, ::RotatedPillarBlock, isPillar = true),
        ;

        override val holder: DeferredHolder<Block, out Block> = HTContent.blockHolder("${name.lowercase()}_decoration")
    }

    enum class LEDBlocks(val colors: List<DyeColor>) : HTBlockContent {
        RED(DyeColor.RED),
        GREEN(DyeColor.GREEN),
        BLUE(DyeColor.BLUE),
        CYAN(DyeColor.GREEN, DyeColor.BLUE),
        MAGENTA(DyeColor.RED, DyeColor.BLUE),
        YELLOW(DyeColor.RED, DyeColor.GREEN),
        WHITE(),
        ;

        constructor(vararg colors: DyeColor) : this(colors.toList())

        override val holder: DeferredHolder<Block, out Block> = HTContent.blockHolder("${name.lowercase()}_led_block")
    }

    //    Register    //

    @JvmField
    val REGISTER: DeferredRegister.Blocks = DeferredRegister.createBlocks(RagiumAPI.MOD_ID)

    @JvmStatic
    private val ITEM_REGISTER: DeferredRegister.Items
        get() = RagiumItems.REGISTER

    @JvmStatic
    internal fun register(bus: IEventBus) {
        // storage block
        StorageBlocks.entries.forEach { storage: StorageBlocks ->
            storage.registerBlock(REGISTER, blockProperty(Blocks.IRON_BLOCK))
            storage.registerBlockItem(ITEM_REGISTER)
        }
        // grate
        Grates.entries.forEach { grate: Grates ->
            grate.registerBlock(REGISTER, blockProperty(Blocks.COPPER_GRATE), ::TransparentBlock)
            grate.registerBlockItem(ITEM_REGISTER)
        }
        // casing
        Casings.entries.forEach { casings: Casings ->
            casings.registerBlock(REGISTER, blockProperty(Blocks.SMOOTH_STONE))
            casings.registerBlockItem(ITEM_REGISTER)
        }
        // hull
        Hulls.entries.forEach { hull: Hulls ->
            hull.registerBlock(REGISTER, blockProperty(Blocks.SMOOTH_STONE), ::TransparentBlock)
            hull.registerBlockItem(ITEM_REGISTER)
        }
        // coil
        Coils.entries.forEach { coil: Coils ->
            coil.registerBlock(REGISTER, blockProperty(Blocks.COPPER_BLOCK), ::RotatedPillarBlock)
            coil.registerBlockItem(ITEM_REGISTER)
        }

        // drum
        Drums.entries.forEach { drum: Drums ->
            drum.registerBlock(
                REGISTER,
                blockProperty(Blocks.SMOOTH_STONE),
            ) { prop: BlockBehaviour.Properties -> HTDrumBlock(drum.machineTier, prop) }
            drum.registerBlockItem(ITEM_REGISTER)
        }

        // decoration
        Decorations.entries.forEach { decoration: Decorations ->
            decoration.registerBlock(
                REGISTER,
                blockProperty(Blocks.SMOOTH_STONE),
                decoration.factory,
            )
            decoration.registerBlockItem(ITEM_REGISTER)
        }
        // led
        LEDBlocks.entries.forEach { ledBlock: LEDBlocks ->
            ledBlock.registerBlock(
                REGISTER,
                blockProperty().lightLevel { 15 },
            )
            ledBlock.registerBlockItem(ITEM_REGISTER)
        }

        REGISTER.register(bus)
    }
}
