package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.content.HTContentRegister
import hiiragi283.ragium.api.extension.blockSettings
import hiiragi283.ragium.common.block.*
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.registry.tag.FluidTags
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

object RagiumBlocks : HTContentRegister {
    //    Blocks - Minerals    //
    @JvmField
    val POROUS_NETHERRACK: Block =
        registerBlock(
            "porous_netherrack",
            HTSpongeBlock(
                blockSettings(Blocks.NETHERRACK),
                Blocks.MAGMA_BLOCK::getDefaultState,
            ) { world: World, pos: BlockPos ->
                world.getFluidState(pos).isIn(FluidTags.LAVA)
            },
        )

    //    Blocks - Buildings    //

    @JvmField
    val ASPHALT: Block = registerCopy("asphalt", Blocks.STONE)

    //    Blocks - Foods    //

    @JvmField
    val SPONGE_CAKE: Block =
        registerBlock(
            "sponge_cake",
            HTDecreaseFallingBlock(0.2f, blockSettings(Blocks.HAY_BLOCK).sounds(BlockSoundGroup.WOOL)),
        )

    @JvmField
    val SWEET_BERRIES_CAKE: Block =
        registerBlock("sweet_berries_cake", HTSweetBerriesCakeBlock)

    //    Blocks - Utilities    //

    @JvmField
    val BACKPACK_INTERFACE: Block = registerBlock("backpack_interface", HTBackpackInterfaceBlock)

    @JvmField
    val CREATIVE_SOURCE: Block =
        registerWithBE("creative_source", RagiumBlockEntityTypes.CREATIVE_SOURCE, Blocks.COMMAND_BLOCK)

    @JvmField
    val ITEM_DISPLAY: Block =
        registerBlock("item_display", HTItemDisplayBlock)

    @JvmField
    val LARGE_PROCESSOR: Block =
        registerBlock("large_processor", HTLargeProcessorBlock(blockSettings()))

    @JvmField
    val MANUAL_FORGE: Block =
        registerWithBE("manual_forge", RagiumBlockEntityTypes.MANUAL_FORGE, blockSettings(Blocks.ANVIL).nonOpaque())

    @JvmField
    val MANUAL_GRINDER: Block =
        registerBlock("manual_grinder", HTManualGrinderBlock)

    @JvmField
    val MANUAL_MIXER: Block =
        registerWithBE("manual_mixer", RagiumBlockEntityTypes.MANUAL_MIXER)

    @JvmField
    val NETWORK_INTERFACE: Block =
        registerBlock("network_interface", HTNetworkInterfaceBlock)

    @JvmField
    val OPEN_CRATE: Block =
        registerCopy("open_crate", Blocks.SMOOTH_STONE)

    @JvmField
    val SHAFT: Block =
        registerBlock("shaft", HTThinPillarBlock(blockSettings(Blocks.CHAIN)))

    @JvmField
    val TRASH_BOX: Block =
        registerBlock("trash_box")

    @JvmField
    val INFESTING: Block =
        registerBlock("infesting", HTInfectingBlock)
}
