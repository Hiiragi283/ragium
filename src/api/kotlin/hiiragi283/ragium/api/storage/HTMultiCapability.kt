package hiiragi283.ragium.api.storage

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.capabilities.BlockCapability
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.capabilities.ItemCapability
import net.neoforged.neoforge.common.extensions.ILevelExtension
import net.neoforged.neoforge.energy.IEnergyStorage
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem
import net.neoforged.neoforge.items.IItemHandler

/**
 * @see [mekanism.common.capabilities.IMultiTypeCapability]
 */
class HTMultiCapability<HANDLER : Any, ITEM_HANDLER : HANDLER>(
    val blockCapability: BlockCapability<HANDLER, Direction?>,
    val itemCapability: ItemCapability<ITEM_HANDLER, Void?>,
) {
    companion object {
        @JvmField
        val ITEM: HTMultiCapability<IItemHandler, IItemHandler> =
            HTMultiCapability(Capabilities.ItemHandler.BLOCK, Capabilities.ItemHandler.ITEM)

        @JvmField
        val FLUID: HTMultiCapability<IFluidHandler, IFluidHandlerItem> =
            HTMultiCapability(Capabilities.FluidHandler.BLOCK, Capabilities.FluidHandler.ITEM)

        @JvmField
        val ENERGY: HTMultiCapability<IEnergyStorage, IEnergyStorage> =
            HTMultiCapability(Capabilities.EnergyStorage.BLOCK, Capabilities.EnergyStorage.ITEM)
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    fun getCapability(level: ILevelExtension, pos: BlockPos, side: Direction?): HANDLER? = level.getCapability(blockCapability, pos, side)

    fun getCapability(stack: ItemStack): ITEM_HANDLER? = stack.getCapability(itemCapability)
}
