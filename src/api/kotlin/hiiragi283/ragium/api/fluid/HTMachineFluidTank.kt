package hiiragi283.ragium.api.fluid

import hiiragi283.ragium.api.block.entity.HTEnchantableBlockEntity
import hiiragi283.ragium.api.capability.HTSlotHandler
import hiiragi283.ragium.api.capability.HTStorageIO
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Player
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidUtil
import net.neoforged.neoforge.fluids.IFluidTank
import net.neoforged.neoforge.fluids.capability.IFluidHandler

/**
 * Ragiumで使用する[IFluidTank]の拡張クラス
 */
interface HTMachineFluidTank :
    IFluidHandler,
    IFluidTank,
    HTSlotHandler<FluidStack> {
    fun setFluid(stack: FluidStack)

    fun updateCapacity(blockEntity: HTEnchantableBlockEntity)

    fun interactWithFluidStorage(player: Player, storageIO: HTStorageIO): Boolean =
        FluidUtil.interactWithFluidHandler(player, InteractionHand.MAIN_HAND, storageIO.wrapFluidHandler(this))
}
