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

    /*val SNOW_SPONGE: Block =
        registerBlock(
            "snow_sponge",
            HTSpongeBlock(
                blockSettings(Blocks.SNOW_BLOCK),
                Blocks.SNOW_BLOCK::getDefaultState,
            ) { world: World, pos: BlockPos ->
                world.getBlockState(pos).isIn(BlockTags.SNOW)
            },
        )*/

    /*val OBLIVION_CLUSTER: Block =
        registerBlock(
            "oblivion_cluster",
            AmethystClusterBlock(7.0f, 3.0f, blockSettings(Blocks.AMETHYST_CLUSTER).mapColor(MapColor.BLACK)),
        )*/

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
    val ADVANCED_CASING: Block =
        registerCopy("advanced_casing", Blocks.IRON_BLOCK)

    @JvmField
    val BACKPACK_INTERFACE: Block = registerBlock("backpack_interface", HTBackpackInterfaceBlock)

    @JvmField
    val BASIC_CASING: Block =
        registerCopy("basic_casing", Blocks.IRON_BLOCK)

    @JvmField
    val CREATIVE_SOURCE: Block =
        registerWithBE("creative_source", RagiumBlockEntityTypes.CREATIVE_SOURCE, Blocks.COMMAND_BLOCK)

    @JvmField
    val ITEM_DISPLAY: Block =
        registerBlock("item_display", HTItemDisplayBlock)

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
    val LARGE_PROCESSOR: Block =
        registerHorizontalWithBE("large_processor", RagiumBlockEntityTypes.LARGE_PROCESSOR)

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
