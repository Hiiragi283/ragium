package hiiragi283.ragium.common.block.entity.device

import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.toImmutable
import hiiragi283.ragium.api.storage.HTContentListener
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.HTStorageAction
import hiiragi283.ragium.api.storage.fluid.HTFluidInteractable
import hiiragi283.ragium.api.storage.holder.HTFluidTankHolder
import hiiragi283.ragium.common.storage.fluid.tank.HTVariableFluidStackTank
import hiiragi283.ragium.common.storage.holder.HTSimpleFluidTankHolder
import hiiragi283.ragium.common.variant.HTDeviceVariant
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumMenuTypes
import net.minecraft.core.BlockPos
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
    HTDeviceBlockEntity.Tickable(variant, pos, state),
    HTFluidInteractable {
    private lateinit var tank: HTVariableFluidStackTank

    override fun initializeFluidHandler(listener: HTContentListener): HTFluidTankHolder {
        tank = HTVariableFluidStackTank.output(listener, RagiumConfig.COMMON.deviceCollectorTankCapacity)
        return HTSimpleFluidTankHolder.output(null, tank)
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
        val stack: ImmutableFluidStack = getGeneratedFluid(level, pos).toImmutable()
        if (stack.isEmpty()) return false
        // 液体を搬入できるかチェック
        if (!tank.insert(stack, HTStorageAction.SIMULATE, HTStorageAccess.INTERNAL).isEmpty()) return false
        tank.insert(stack, HTStorageAction.EXECUTE, HTStorageAccess.INTERNAL)
        playSound(level, pos)
        return true
    }

    protected abstract fun getGeneratedFluid(level: ServerLevel, pos: BlockPos): FluidStack

    protected abstract fun playSound(level: ServerLevel, pos: BlockPos)

    //    HTFluidInteractable    //

    override fun interactWith(level: Level, player: Player, hand: InteractionHand): ItemInteractionResult = interactWith(player, hand, tank)
}
