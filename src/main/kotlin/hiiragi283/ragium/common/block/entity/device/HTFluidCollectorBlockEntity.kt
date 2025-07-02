package hiiragi283.ragium.common.block.entity.device

import hiiragi283.ragium.api.RagiumConfig
import hiiragi283.ragium.api.network.HTNbtCodec
import hiiragi283.ragium.api.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.api.storage.fluid.HTFilteredFluidHandler
import hiiragi283.ragium.api.storage.fluid.HTFluidFilter
import hiiragi283.ragium.api.util.RagiumConstantValues
import hiiragi283.ragium.common.block.entity.HTTickAwareBlockEntity
import hiiragi283.ragium.common.inventory.HTFluidCollectorMenu
import hiiragi283.ragium.common.storage.fluid.HTFluidTank
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.MenuProvider
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.common.util.TriState
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.items.wrapper.EmptyItemHandler

abstract class HTFluidCollectorBlockEntity(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTTickAwareBlockEntity(type, pos, state),
    MenuProvider {
    protected val tank = HTFluidTank(RagiumConfig.COMMON.machineTankCapacity.get(), this::setChanged)

    final override fun writeNbt(writer: HTNbtCodec.Writer) {
        writer.write(RagiumConstantValues.TANK, tank)
    }

    final override fun readNbt(reader: HTNbtCodec.Reader) {
        reader.read(RagiumConstantValues.TANK, tank)
    }

    //    Ticking    //

    override fun onServerTick(level: ServerLevel, pos: BlockPos, state: BlockState): TriState {
        // 20 tickごとに実行する
        if (!canProcess()) return TriState.DEFAULT
        // 自動搬出する
        exportFluids(level, pos)
        // 液体を生成できるかチェック
        val stack: FluidStack = getGeneratedFluid(level, pos)
        if (stack.isEmpty) return TriState.DEFAULT
        // 液体を搬入できるかチェック
        if (!tank.canFill(stack, false)) return TriState.DEFAULT
        tank.fill(stack, IFluidHandler.FluidAction.EXECUTE)
        playSound(level, pos)
        return TriState.TRUE
    }

    final override val maxTicks: Int = 20

    protected abstract fun getGeneratedFluid(level: ServerLevel, pos: BlockPos): FluidStack

    protected abstract fun playSound(level: ServerLevel, pos: BlockPos)

    override fun getFluidHandler(direction: Direction?): HTFilteredFluidHandler = HTFilteredFluidHandler(
        listOf(tank),
        HTFluidFilter.DRAIN_ONLY,
    )

    //    MenuProvider    //

    final override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): HTFluidCollectorMenu =
        HTFluidCollectorMenu(containerId, playerInventory, blockPos, createDefinition(EmptyItemHandler.INSTANCE))

    final override fun getDisplayName(): Component = blockState.block.name
}
