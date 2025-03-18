package hiiragi283.ragium.api.block.entity

import hiiragi283.ragium.api.storage.fluid.HTFluidSlotHandler
import hiiragi283.ragium.api.storage.item.HTItemSlotHandler
import net.minecraft.core.Direction
import net.minecraft.world.level.block.entity.BlockEntity
import net.neoforged.neoforge.energy.IEnergyStorage
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.items.IItemHandler

/**
 * [BlockEntity]に実装するインターフェース
 */
interface HTHandlerBlockEntity {
    /**
     * 指定した[direction]から[IItemHandler]を返します。
     */
    fun getItemHandler(direction: Direction?): IItemHandler? = this as? HTItemSlotHandler

    /**
     * 指定した[direction]から[IFluidHandler]を返します。
     */
    fun getFluidHandler(direction: Direction?): IFluidHandler? = this as? HTFluidSlotHandler

    /**
     * 指定した[direction]から[IEnergyStorage]を返します。
     */
    fun getEnergyStorage(direction: Direction?): IEnergyStorage? = null
}
