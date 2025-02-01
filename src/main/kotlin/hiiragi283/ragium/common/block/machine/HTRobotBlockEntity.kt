package hiiragi283.ragium.common.block.machine

import hiiragi283.ragium.api.block.entity.HTBlockEntity
import hiiragi283.ragium.api.block.entity.HTBlockEntityHandlerProvider
import hiiragi283.ragium.api.extension.getFakePlayer
import hiiragi283.ragium.api.extension.getOrNull
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.Vec3
import net.neoforged.neoforge.common.util.FakePlayer
import net.neoforged.neoforge.items.IItemHandler
import net.neoforged.neoforge.items.wrapper.PlayerInvWrapper

class HTRobotBlockEntity(pos: BlockPos, state: BlockState) :
    HTBlockEntity(RagiumBlockEntityTypes.ROBOT, pos, state),
    HTBlockEntityHandlerProvider {
    val front: Direction? get() = blockState.getOrNull(BlockStateProperties.HORIZONTAL_FACING)

    override fun tickSecond(level: Level, pos: BlockPos, state: BlockState) {
        val front: Direction = front ?: return
        val fakePlayer: FakePlayer = level.getFakePlayer() ?: return
        val hitResult = BlockHitResult(Vec3(0.5, 0.5, 0.5), front.opposite, blockPos.relative(front), true)
        val result: InteractionResult = fakePlayer.gameMode.useItemOn(
            fakePlayer,
            level,
            fakePlayer.mainHandItem,
            InteractionHand.MAIN_HAND,
            hitResult,
        )
    }

    //    HTBlockEntityHandlerProvider    //

    override fun getItemHandler(direction: Direction?): IItemHandler? = level.getFakePlayer()?.inventory?.let(::PlayerInvWrapper)
}
