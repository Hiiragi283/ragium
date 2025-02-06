package hiiragi283.ragium.api.fluid

import hiiragi283.ragium.api.block.entity.HTEnchantableBlockEntity
import hiiragi283.ragium.api.capability.HTSlotHandler
import hiiragi283.ragium.api.capability.HTStorageIO
import hiiragi283.ragium.common.init.RagiumEnchantments
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Player
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidUtil
import net.neoforged.neoforge.fluids.capability.templates.FluidTank

/**
 * Ragiumで使用する[FluidTank]の拡張クラス
 * @param callback [FluidTank.onContentsChanged]で呼び出されるブロック
 */
open class HTMachineFluidTank(private val baseCapacity: Int, private val callback: () -> Unit) :
    FluidTank(baseCapacity),
    HTSlotHandler<FluidStack> {
    constructor(callback: () -> Unit) : this(8000, callback)

    override fun onContentsChanged() {
        callback()
    }

    fun updateCapacity(blockEntity: HTEnchantableBlockEntity) {
        val level: Int = blockEntity.getEnchantmentLevel(RagiumEnchantments.CAPACITY) + 1
        capacity = level * baseCapacity
    }

    fun interactWithFluidStorage(player: Player, storageIO: HTStorageIO): Boolean =
        FluidUtil.interactWithFluidHandler(player, InteractionHand.MAIN_HAND, storageIO.wrapFluidHandler(this))

    //    HTSlotHandler    //

    override var stack: FluidStack
        get() = fluid
        set(value) = setFluid(value)
}
