package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.content.HTBlockContent
import hiiragi283.ragium.api.extension.blockProperty
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineTierProvider
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.common.block.storage.HTDrumBlock
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.RotatedPillarBlock
import net.minecraft.world.level.block.TransparentBlock
import net.minecraft.world.level.block.state.BlockBehaviour
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredItem
import net.neoforged.neoforge.registries.DeferredRegister

object RagiumBlocks {
    @JvmField
    val REGISTER: DeferredRegister.Blocks = DeferredRegister.createBlocks(RagiumAPI.MOD_ID)

    init {
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

        override val holder: DeferredBlock<Block> =
            REGISTER.registerSimpleBlock("${name.lowercase()}_block", blockProperty(Blocks.IRON_BLOCK))
        override val itemHolder: DeferredItem<out Item> = RagiumItems.REGISTER.registerSimpleBlockItem(holder)
        override val tagPrefix: HTTagPrefix = HTTagPrefix.STORAGE_BLOCK
    }

    enum class Grates(override val machineTier: HTMachineTier) : HTBlockContent.Tier {
        SIMPLE(HTMachineTier.SIMPLE),
        BASIC(HTMachineTier.BASIC),
        ADVANCED(HTMachineTier.ADVANCED),
        ELITE(HTMachineTier.ELITE),
        ;

        override val holder: DeferredBlock<TransparentBlock> =
            REGISTER.registerBlock("${name.lowercase()}_grate", ::TransparentBlock, blockProperty(Blocks.COPPER_GRATE))
        override val itemHolder: DeferredItem<out Item> = RagiumItems.REGISTER.registerSimpleBlockItem(holder)
    }

    enum class Casings(override val machineTier: HTMachineTier) : HTBlockContent.Tier {
        SIMPLE(HTMachineTier.SIMPLE),
        BASIC(HTMachineTier.BASIC),
        ADVANCED(HTMachineTier.ADVANCED),
        ELITE(HTMachineTier.ELITE),
        ;

        override val holder: DeferredBlock<Block> =
            REGISTER.registerSimpleBlock("${name.lowercase()}_casing", blockProperty(Blocks.SMOOTH_STONE))
        override val itemHolder: DeferredItem<out Item> = RagiumItems.REGISTER.registerSimpleBlockItem(holder)
    }

    enum class Hulls(override val machineTier: HTMachineTier) : HTBlockContent.Tier {
        PRIMITIVE(HTMachineTier.PRIMITIVE),
        SIMPLE(HTMachineTier.SIMPLE),
        BASIC(HTMachineTier.BASIC),
        ADVANCED(HTMachineTier.ADVANCED),
        ELITE(HTMachineTier.ELITE),
        ;

        override val holder: DeferredBlock<TransparentBlock> =
            REGISTER.registerBlock("${name.lowercase()}_hull", ::TransparentBlock, blockProperty(Blocks.SMOOTH_STONE))
        override val itemHolder: DeferredItem<out Item> = RagiumItems.REGISTER.registerSimpleBlockItem(holder)
    }

    enum class Coils(override val machineTier: HTMachineTier) : HTBlockContent.Tier {
        SIMPLE(HTMachineTier.SIMPLE),
        BASIC(HTMachineTier.BASIC),
        ADVANCED(HTMachineTier.ADVANCED),
        ELITE(HTMachineTier.ELITE),
        ;

        override val holder: DeferredBlock<RotatedPillarBlock> =
            REGISTER.registerBlock("${name.lowercase()}_coil", ::RotatedPillarBlock, blockProperty(Blocks.COPPER_BLOCK))
        override val itemHolder: DeferredItem<out Item> = RagiumItems.REGISTER.registerSimpleBlockItem(holder)
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

        override val holder: DeferredBlock<Block> = REGISTER.registerBlock(
            "${name.lowercase()}_drum",
            { prop: BlockBehaviour.Properties -> HTDrumBlock(machineTier, prop) },
            blockProperty(Blocks.SMOOTH_STONE),
        )
        override val itemHolder: DeferredItem<out Item> = RagiumItems.REGISTER.registerSimpleBlockItem(holder)
    }

    //    Buildings    //

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

        override val holder: DeferredBlock<Block> = REGISTER.registerBlock(
            "${name.lowercase()}_decoration",
            factory,
            blockProperty(Blocks.SMOOTH_STONE),
        )
        override val itemHolder: DeferredItem<out Item> = RagiumItems.REGISTER.registerSimpleBlockItem(holder)
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

        override val holder: DeferredBlock<Block> =
            REGISTER.registerSimpleBlock("${name.lowercase()}_led_block", blockProperty().lightLevel { 15 })
        override val itemHolder: DeferredItem<out Item> = RagiumItems.REGISTER.registerSimpleBlockItem(holder)
    }
}
