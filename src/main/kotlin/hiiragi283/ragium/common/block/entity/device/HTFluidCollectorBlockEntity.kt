package hiiragi283.ragium.common.block.entity.device

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.entity.HTFluidInteractable
import hiiragi283.ragium.api.network.HTNbtCodec
import hiiragi283.ragium.api.storage.fluid.HTFilteredFluidHandler
import hiiragi283.ragium.api.storage.fluid.HTFluidFilter
import hiiragi283.ragium.api.util.RagiumConst
import hiiragi283.ragium.common.storage.fluid.HTFluidStackTank
import hiiragi283.ragium.setup.RagiumMenuTypes
import hiiragi283.ragium.util.variant.HTDeviceVariant
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.neoforged.neoforge.fluids.FluidStack

abstract class HTFluidCollectorBlockEntity(variant: HTDeviceVariant, pos: BlockPos, state: BlockState) :
    HTDeviceBlockEntity(variant, pos, state),
    HTFluidInteractable {
    protected val tank = HTFluidStackTank(RagiumAPI.getConfig().getDeviceTankCapacity(), this)

    final override fun writeNbt(writer: HTNbtCodec.Writer) {
        writer.write(RagiumConst.TANK, tank)
    }

    final override fun readNbt(reader: HTNbtCodec.Reader) {
        reader.read(RagiumConst.TANK, tank)
    }

    override fun onRightClicked(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hitResult: BlockHitResult,
    ): InteractionResult = RagiumMenuTypes.FLUID_COLLECTOR.openMenu(player, name, this, ::writeExtraContainerData)

    //    Ticking    //

    override fun actionServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean {
        // 液体を生成できるかチェック
        val stack: FluidStack = getGeneratedFluid(level, pos)
        if (stack.isEmpty) return false
        // 液体を搬入できるかチェック
        if (!tank.canFill(stack, false)) return false
        tank.fill(stack, false)
        playSound(level, pos)
        return true
    }

    protected abstract fun getGeneratedFluid(level: ServerLevel, pos: BlockPos): FluidStack

    protected abstract fun playSound(level: ServerLevel, pos: BlockPos)

    override fun getFluidHandler(direction: Direction?): HTFilteredFluidHandler = HTFilteredFluidHandler(tank, HTFluidFilter.DRAIN_ONLY)

    //    HTFluidInteractable    //

    override fun interactWith(level: Level, player: Player, hand: InteractionHand): ItemInteractionResult =
        interactWith(player, hand, getFluidHandler(null))
}
