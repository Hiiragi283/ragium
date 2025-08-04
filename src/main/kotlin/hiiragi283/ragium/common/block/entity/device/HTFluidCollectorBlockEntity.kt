package hiiragi283.ragium.common.block.entity.device

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.network.HTNbtCodec
import hiiragi283.ragium.api.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.api.storage.fluid.HTFilteredFluidHandler
import hiiragi283.ragium.api.storage.fluid.HTFluidFilter
import hiiragi283.ragium.api.util.RagiumConst
import hiiragi283.ragium.common.inventory.HTFluidCollectorMenu
import hiiragi283.ragium.common.network.HTFluidSlotUpdatePacket
import hiiragi283.ragium.common.storage.fluid.HTFluidTank
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.common.util.TriState
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.items.wrapper.EmptyItemHandler

abstract class HTFluidCollectorBlockEntity(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTDeviceBlockEntity(type, pos, state) {
    protected val tank = HTFluidTank(RagiumAPI.getConfig().getDefaultTankCapacity(), this::setChanged)

    final override fun writeNbt(writer: HTNbtCodec.Writer) {
        writer.write(RagiumConst.TANK, tank)
    }

    final override fun readNbt(reader: HTNbtCodec.Reader) {
        reader.read(RagiumConst.TANK, tank)
    }

    override fun sendUpdatePacket(serverLevel: ServerLevel, consumer: (CustomPacketPayload) -> Unit) {
        super.sendUpdatePacket(serverLevel, consumer)
        consumer(HTFluidSlotUpdatePacket(0, tank.fluid))
    }

    //    Ticking    //

    override fun serverTick(level: ServerLevel, pos: BlockPos, state: BlockState): TriState {
        // 液体を生成できるかチェック
        val stack: FluidStack = getGeneratedFluid(level, pos)
        if (stack.isEmpty) return TriState.DEFAULT
        // 液体を搬入できるかチェック
        if (!tank.canFill(stack, false)) return TriState.DEFAULT
        tank.fill(stack, IFluidHandler.FluidAction.EXECUTE)
        playSound(level, pos)
        return TriState.TRUE
    }

    protected abstract fun getGeneratedFluid(level: ServerLevel, pos: BlockPos): FluidStack

    protected abstract fun playSound(level: ServerLevel, pos: BlockPos)

    override fun getFluidHandler(direction: Direction?): HTFilteredFluidHandler = HTFilteredFluidHandler(
        listOf(tank),
        HTFluidFilter.DRAIN_ONLY,
    )

    //    Menu    //

    override val containerData: ContainerData = createData()

    final override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): HTFluidCollectorMenu =
        HTFluidCollectorMenu(containerId, playerInventory, blockPos, createDefinition(EmptyItemHandler.INSTANCE))
}
