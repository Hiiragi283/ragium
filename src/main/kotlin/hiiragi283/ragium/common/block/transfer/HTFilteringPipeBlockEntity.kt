package hiiragi283.ragium.common.block.transfer

import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.util.HTPipeType
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.screen.HTFilteringPipeScreenHandler
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandler
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

class HTFilteringPipeBlockEntity(pos: BlockPos, state: BlockState) :
    HTPipeBlockEntityBase(RagiumBlockEntityTypes.FILTERING_PIPE, pos, state),
    ExtendedScreenHandlerFactory<HTPipeType> {
    init {
        this.tier = HTMachineTier.ADVANCED
    }

    constructor(pos: BlockPos, state: BlockState, type: HTPipeType) : this(pos, state) {
        this.type = type
    }

    override fun tickSecond(world: World, pos: BlockPos, state: BlockState) {
        if (world.isClient) return
        // transfer for side storages
        val others: List<Direction> =
            Direction.entries.filterNot { directionIn: Direction -> directionIn.axis == front.axis }
        others.forEach { directionIn: Direction ->
            if (type.isItem) {
                if (moveItem(world, pos, directionIn)) {
                    return
                }
            }
            if (type.isFluid) {
                if (moveFluid(world, pos, directionIn)) {
                    return
                }
            }
        }
        super.tickSecond(world, pos, state)
    }

    //    ExtendedScreenHandlerFactory    //

    override fun getDisplayName(): Text = Text.empty()

    override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity): ScreenHandler =
        HTFilteringPipeScreenHandler(syncId, playerInventory, type)

    override fun getScreenOpeningData(player: ServerPlayerEntity): HTPipeType = type
}
