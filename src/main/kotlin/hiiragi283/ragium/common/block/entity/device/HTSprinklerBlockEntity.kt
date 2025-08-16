package hiiragi283.ragium.common.block.entity.device

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.network.HTNbtCodec
import hiiragi283.ragium.api.storage.fluid.HTFilteredFluidHandler
import hiiragi283.ragium.api.storage.fluid.HTFluidFilter
import hiiragi283.ragium.api.util.RagiumConst
import hiiragi283.ragium.common.storage.fluid.HTFluidStackTank
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.item.BoneMealItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.util.TriState
import net.neoforged.neoforge.fluids.FluidStack

class HTSprinklerBlockEntity(pos: BlockPos, state: BlockState) : HTDeviceBlockEntity(RagiumBlockEntityTypes.SPRINKLER, pos, state) {
    private val tank: HTFluidStackTank =
        object : HTFluidStackTank(RagiumAPI.getConfig().getDefaultTankCapacity(), this::setChanged) {
            override fun isFluidValid(stack: FluidStack): Boolean = stack.`is`(Tags.Fluids.WATER)
        }

    override fun writeNbt(writer: HTNbtCodec.Writer) {
        writer.write(RagiumConst.TANK, tank)
    }

    override fun readNbt(reader: HTNbtCodec.Reader) {
        reader.read(RagiumConst.TANK, tank)
    }

    //    Ticking    //

    override fun serverTick(level: ServerLevel, pos: BlockPos, state: BlockState): TriState {
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
            tank.drain(stack.copyWithAmount(50), false)
            return TriState.TRUE
        }
        return TriState.DEFAULT
    }

    override fun getFluidHandler(direction: Direction?): HTFilteredFluidHandler = HTFilteredFluidHandler(tank, HTFluidFilter.FILL_ONLY)

    //    Menu    //

    override val containerData: ContainerData = createData()

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu? = null
}
