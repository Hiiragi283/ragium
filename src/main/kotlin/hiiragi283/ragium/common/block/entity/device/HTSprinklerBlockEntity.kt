package hiiragi283.ragium.common.block.entity.device

import hiiragi283.ragium.api.RagiumConfig
import hiiragi283.ragium.api.network.HTNbtCodec
import hiiragi283.ragium.api.storage.fluid.HTFilteredFluidHandler
import hiiragi283.ragium.api.storage.fluid.HTFluidFilter
import hiiragi283.ragium.api.util.RagiumConstantValues
import hiiragi283.ragium.common.block.entity.HTTickAwareBlockEntity
import hiiragi283.ragium.common.storage.fluid.HTFluidTank
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.BoneMealItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.util.TriState
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.IFluidTank
import net.neoforged.neoforge.fluids.capability.IFluidHandler

class HTSprinklerBlockEntity(pos: BlockPos, state: BlockState) : HTTickAwareBlockEntity(RagiumBlockEntityTypes.SPRINKLER, pos, state) {
    private val tank = HTFluidTank(RagiumConfig.COMMON.machineTankCapacity.get(), this::setChanged)

    override fun writeNbt(writer: HTNbtCodec.Writer) {
        writer.write(RagiumConstantValues.TANK, tank)
    }

    override fun readNbt(reader: HTNbtCodec.Reader) {
        reader.read(RagiumConstantValues.TANK, tank)
    }

    //    Ticking    //

    override var maxTicks: Int = 100

    override fun onServerTick(level: ServerLevel, pos: BlockPos, state: BlockState): TriState {
        // 20 tickごとに実行する
        currentTicks++
        if (currentTicks < maxTicks) return TriState.DEFAULT
        currentTicks = 0
        // 高さを0~2の範囲でチェックする
        for (height: Int in (0..2)) {
            if (glowCrop(level, pos, height).isTrue) {
                return TriState.TRUE
            }
        }
        return TriState.DEFAULT
    }

    private fun glowCrop(level: ServerLevel, pos: BlockPos, height: Int): TriState {
        // 範囲内のランダムなブロックを対象とする
        val targetPos: BlockPos = BlockPos
            .betweenClosedStream(-4, height, -4, 4, height, 4)
            .map(pos::offset)
            .filter { posIn: BlockPos -> posIn != pos }
            .toList()
            .random()
        // 水を消費できない場合はスキップ
        val stack: FluidStack = tank.fluid
        if (!tank.canDrain(50, true)) return TriState.DEFAULT
        // ランダムチックを呼び出す
        if (BoneMealItem.applyBonemeal(ItemStack.EMPTY, level, targetPos, null)) {
            tank.drain(stack.copyWithAmount(50), IFluidHandler.FluidAction.EXECUTE)
            return TriState.TRUE
        }
        return TriState.DEFAULT
    }

    override fun getFluidHandler(direction: Direction?): IFluidHandler? = HTFilteredFluidHandler(
        listOf(tank),
        object : HTFluidFilter {
            override fun canFill(tank: IFluidTank, stack: FluidStack): Boolean = stack.`is`(Tags.Fluids.WATER)

            override fun canDrain(tank: IFluidTank, stack: FluidStack): Boolean = false

            override fun canDrain(tank: IFluidTank, maxDrain: Int): Boolean = false
        },
    )
}
