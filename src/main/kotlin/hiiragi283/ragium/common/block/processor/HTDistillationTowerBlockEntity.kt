package hiiragi283.ragium.common.block.processor

import hiiragi283.ragium.api.block.entity.HTRecipeProcessorBlockEntity
import hiiragi283.ragium.api.capability.HTStorageIO
import hiiragi283.ragium.api.capability.LimitedFluidHandler
import hiiragi283.ragium.api.capability.LimitedItemHandler
import hiiragi283.ragium.api.fluid.HTMachineFluidTank
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.recipe.HTMachineRecipeProcessor
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.common.inventory.HTDistillationTowerContainerMenu
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.items.ItemStackHandler

class HTDistillationTowerBlockEntity(pos: BlockPos, state: BlockState) :
    HTRecipeProcessorBlockEntity(RagiumBlockEntityTypes.DISTILLATION_TOWER, pos, state) {
    override val machineKey: HTMachineKey = RagiumMachineKeys.DISTILLATION_TOWER

    override val itemHandler: ItemStackHandler = ItemStackHandler(1)
    override val tanks: Array<out HTMachineFluidTank> =
        Array(4) { HTMachineFluidTank(FluidType.BUCKET_VOLUME * 8, this::setChanged) }
    override val processor: HTMachineRecipeProcessor = createMachineProcessor(
        intArrayOf(),
        intArrayOf(0),
        intArrayOf(0),
        intArrayOf(1, 2, 3),
    )

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu? =
        HTDistillationTowerContainerMenu(containerId, playerInventory, blockPos, itemHandler)

    //    HTBlockEntityHandlerProvider    //

    override fun getItemHandler(direction: Direction?): LimitedItemHandler = LimitedItemHandler(
        mapOf(0 to HTStorageIO.INTERNAL, 1 to HTStorageIO.OUTPUT),
        ::itemHandler,
    )

    override fun getFluidHandler(direction: Direction?): IFluidHandler? = LimitedFluidHandler(
        mapOf(
            0 to HTStorageIO.INPUT,
            1 to HTStorageIO.OUTPUT,
            2 to HTStorageIO.OUTPUT,
            3 to HTStorageIO.OUTPUT,
        ),
        tanks,
    )
}
