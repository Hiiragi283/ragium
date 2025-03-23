package hiiragi283.ragium.api.block.entity

import hiiragi283.ragium.api.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.storage.fluid.HTFluidTankHandler
import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.resources.RegistryOps
import net.minecraft.world.InteractionHand
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.neoforged.neoforge.common.util.TriState
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidUtil

abstract class HTFluidWellBlockEntity(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTTickAwareBlockEntity(type, pos, state),
    HTFluidTankHandler {
    protected val outputTank: HTFluidTank = HTFluidTank.Companion.create("output_tank", this)

    final override fun writeNbt(nbt: CompoundTag, registryOps: RegistryOps<Tag>) {
        outputTank.writeNbt(nbt, registryOps)
    }

    final override fun readNbt(nbt: CompoundTag, registryOps: RegistryOps<Tag>) {
        outputTank.readNbt(nbt, registryOps)
    }

    override fun onRightClickedWithItem(
        stack: ItemStack,
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hand: InteractionHand,
        hitResult: BlockHitResult,
    ): ItemInteractionResult {
        if (FluidUtil.interactWithFluidHandler(player, hand, this)) {
            return ItemInteractionResult.sidedSuccess(level.isClientSide)
        }
        return super.onRightClickedWithItem(stack, state, level, pos, player, hand, hitResult)
    }

    //    Ticking    //

    override fun updateShouldTick(flag: Boolean) {
        // 常に実行する
    }

    override fun onServerTick(level: Level, pos: BlockPos, state: BlockState): TriState {
        // 20 tick毎に一度実行する
        if (totalTick % 20 != 0) return TriState.DEFAULT
        // 液体を生成できるかチェック
        val stack: FluidStack = getGeneratedFluid(level, pos)
        if (stack.isEmpty) return TriState.FALSE
        // 液体を搬入できるかチェック
        if (outputTank.insert(stack, true) == 0) return TriState.FALSE
        outputTank.insert(stack, false)
        return TriState.TRUE
    }

    protected abstract fun getGeneratedFluid(level: Level, pos: BlockPos): FluidStack

    //    Fluid    //

    final override fun getFluidIoFromSlot(tank: Int): HTStorageIO = HTStorageIO.OUTPUT

    final override fun getFluidTank(tank: Int): HTFluidTank? = outputTank

    final override fun getTanks(): Int = 1
}
