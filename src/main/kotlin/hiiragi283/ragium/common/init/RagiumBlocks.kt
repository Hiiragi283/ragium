package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.extension.blockSettings
import hiiragi283.ragium.common.block.*
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.block.HayBlock
import net.minecraft.registry.tag.FluidTags
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

object RagiumBlocks {
    //    Minerals    //
    @JvmField
    val POROUS_NETHERRACK: Block = HTSpongeBlock(
        blockSettings(Blocks.NETHERRACK),
        Blocks.MAGMA_BLOCK::getDefaultState,
    ) { world: World, pos: BlockPos ->
        world.getFluidState(pos).isIn(FluidTags.LAVA)
    }

    //    Buildings    //

    @JvmField
    val ASPHALT: Block = Block(blockSettings(Blocks.SMOOTH_STONE))

    //    Foods    //

    @JvmField
    val SPONGE_CAKE: Block = HayBlock(blockSettings(Blocks.HAY_BLOCK).sounds(BlockSoundGroup.WOOL))

    @JvmField
    val SWEET_BERRIES_CAKE: Block = HTSweetBerriesCakeBlock

    @JvmField
    val FOODS: List<Block> = listOf(
        SPONGE_CAKE,
        SWEET_BERRIES_CAKE,
    )

    //    Mechanics    //

    @JvmField
    val AUTO_ILLUMINATOR: Block =
        HTBlockWithEntity.build(RagiumBlockEntityTypes.AUTO_ILLUMINATOR, blockSettings(Blocks.SMOOTH_STONE))

    @JvmField
    val CREATIVE_SOURCE: Block =
        HTBlockWithEntity.build(RagiumBlockEntityTypes.CREATIVE_SOURCE, blockSettings(Blocks.COMMAND_BLOCK))

    @JvmField
    val LARGE_PROCESSOR: Block = HTLargeProcessorBlock

    @JvmField
    val MANUAL_FORGE: Block = HTBlockWithEntity.build(RagiumBlockEntityTypes.MANUAL_FORGE, blockSettings(Blocks.BRICKS).nonOpaque())

    @JvmField
    val MANUAL_GRINDER: Block = HTManualGrinderBlock

    @JvmField
    val MANUAL_MIXER: Block = HTBlockWithEntity.build(RagiumBlockEntityTypes.MANUAL_MIXER, blockSettings(Blocks.BRICKS))

    @JvmField
    val NETWORK_INTERFACE: Block = HTNetworkInterfaceBlock

    @JvmField
    val OPEN_CRATE: Block = Block(blockSettings(Blocks.SMOOTH_STONE))

    @JvmField
    val TELEPORT_ANCHOR: Block = Block(blockSettings(Blocks.SMOOTH_STONE))

    @JvmField
    val TRASH_BOX: Block = Block(blockSettings(Blocks.SMOOTH_STONE))

    @JvmField
    val MECHANICS: List<Block> = listOf(
        // colored
        LARGE_PROCESSOR, // red
        AUTO_ILLUMINATOR, // yellow
        OPEN_CRATE, // green
        TELEPORT_ANCHOR, // blue
        CREATIVE_SOURCE, // purple
        TRASH_BOX, // gray
        NETWORK_INTERFACE, // white
        // manual machines
        MANUAL_FORGE,
        MANUAL_GRINDER,
        MANUAL_MIXER,
    )

    //    Misc    //

    @JvmField
    val BACKPACK_INTERFACE: Block = HTBackpackInterfaceBlock

    @JvmField
    val BUFFER: Block =
        HTBlockWithEntity.build(RagiumBlockEntityTypes.BUFFER, blockSettings(Blocks.SMOOTH_STONE))

    @JvmField
    val ENCHANTMENT_BOOKSHELF: Block =
        HTBlockWithEntity.build(RagiumBlockEntityTypes.ENCHANTMENT_BOOKSHELF, blockSettings(Blocks.BOOKSHELF))

    @JvmField
    val ITEM_DISPLAY: Block = HTItemDisplayBlock

    @JvmField
    val SHAFT: Block = HTThinPillarBlock(blockSettings(Blocks.CHAIN))

    @JvmField
    val INFESTING: Block = HTInfectingBlock

    @JvmField
    val MISC: List<Block> = listOf(
        BACKPACK_INTERFACE,
        BUFFER,
        ENCHANTMENT_BOOKSHELF,
        ITEM_DISPLAY,
        SHAFT,
    )
}
