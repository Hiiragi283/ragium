package hiiragi283.ragium.common.block

import hiiragi283.ragium.api.block.HTBlockWithEntity
import hiiragi283.ragium.common.block.machine.HTMachineInterfaceBlockEntity
import net.minecraft.block.BlockState
import net.minecraft.block.InventoryProvider
import net.minecraft.block.entity.BlockEntity
import net.minecraft.inventory.SidedInventory
import net.minecraft.util.math.BlockPos
import net.minecraft.world.WorldAccess

class HTMachineInterfaceBlock(settings: Settings) :
    HTBlockWithEntity.Facing(settings),
    InventoryProvider {
    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity = HTMachineInterfaceBlockEntity(pos, state)

    //    InventoryProvider

    override fun getInventory(state: BlockState, world: WorldAccess, pos: BlockPos): SidedInventory? =
        (world.getBlockEntity(pos) as? HTMachineInterfaceBlockEntity)?.targetMachine?.asInventory()
}
