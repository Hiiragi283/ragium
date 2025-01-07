package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.api.block.HTBlockEntityBase
import hiiragi283.ragium.api.block.HTMachineBlockEntityBase
import hiiragi283.ragium.api.extension.error
import hiiragi283.ragium.api.extension.getMachineEntity
import hiiragi283.ragium.api.extension.getResult
import hiiragi283.ragium.api.util.DelegatedLogger
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant
import net.fabricmc.fabric.api.transfer.v1.storage.Storage
import net.fabricmc.fabric.api.transfer.v1.storage.base.SidedStorageBlockEntity
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.state.property.Properties
import net.minecraft.util.ActionResult
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import org.slf4j.Logger

class HTMachineInterfaceBlockEntity(pos: BlockPos, state: BlockState) :
    HTBlockEntityBase(RagiumBlockEntityTypes.MACHINE_INTERFACE, pos, state),
    SidedStorageBlockEntity {
    companion object {
        @JvmStatic
        private val logger: Logger by DelegatedLogger()
    }

    val front: Direction?
        get() = cachedState.getResult(Properties.FACING).onFailure(logger::error).getOrNull()
    val targetPos: BlockPos?
        get() {
            val front: Direction = front ?: return null
            return pos.offset(front.opposite)
        }
    val targetMachine: HTMachineBlockEntityBase?
        get() {
            val pos: BlockPos = targetPos ?: return null
            return world?.getMachineEntity(pos)
        }

    override fun onRightClicked(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hit: BlockHitResult,
    ): ActionResult {
        val targetPos: BlockPos = targetPos ?: return ActionResult.FAIL
        return targetMachine
            ?.onRightClicked(world.getBlockState(targetPos), world, targetPos, player, hit)
            ?: ActionResult.FAIL
    }

    //    SidedStorageBlockEntity    //

    override fun getItemStorage(side: Direction?): Storage<ItemVariant>? = targetMachine?.getItemStorage(side)

    override fun getFluidStorage(side: Direction?): Storage<FluidVariant>? = targetMachine?.getFluidStorage(side)
}
