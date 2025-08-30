package hiiragi283.ragium.common.block.entity.device

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.network.HTNbtCodec
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.storage.fluid.HTFilteredFluidHandler
import hiiragi283.ragium.api.storage.fluid.HTFluidFilter
import hiiragi283.ragium.api.util.RagiumConst
import hiiragi283.ragium.common.storage.fluid.HTFluidStackTank
import hiiragi283.ragium.util.variant.HTDeviceVariant
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.BoneMealItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.common.util.TriState
import net.neoforged.neoforge.fluids.FluidStack

class HTSprinklerBlockEntity(pos: BlockPos, state: BlockState) : HTDeviceBlockEntity(TODO() as HTDeviceVariant, pos, state) {
    private val tank: HTFluidStackTank = HTFluidStackTank(RagiumAPI.getConfig().getDeviceTankCapacity(), this)
        .setValidator(HTFluidContent.WATER)

    override fun writeNbt(writer: HTNbtCodec.Writer) {
        writer.write(RagiumConst.TANK, tank)
    }

    override fun readNbt(reader: HTNbtCodec.Reader) {
        reader.read(RagiumConst.TANK, tank)
    }

    //    Ticking    //

    override fun actionServer(level: ServerLevel, pos: BlockPos, state: BlockState): Boolean = false

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
            tank.drain(stack.copyWithAmount(50), false)
            return TriState.TRUE
        }
        return TriState.DEFAULT
    }

    override fun getFluidHandler(direction: Direction?): HTFilteredFluidHandler = HTFilteredFluidHandler(tank, HTFluidFilter.FILL_ONLY)
}
