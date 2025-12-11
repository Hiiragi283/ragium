package hiiragi283.ragium.api.capability

import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.ImmutableItemStack
import hiiragi283.ragium.api.stack.toImmutable
import hiiragi283.ragium.api.storage.fluid.HTFluidHandler
import hiiragi283.ragium.api.storage.fluid.HTFluidView
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.entity.Entity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.neoforged.neoforge.capabilities.BlockCapability
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.capabilities.EntityCapability
import net.neoforged.neoforge.capabilities.ItemCapability
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem

object HTFluidCapabilities : HTMultiCapability<IFluidHandler, IFluidHandlerItem> {
    override val block: BlockCapability<IFluidHandler, Direction?> = Capabilities.FluidHandler.BLOCK
    override val entity: EntityCapability<IFluidHandler, Direction?> = Capabilities.FluidHandler.ENTITY
    override val item: ItemCapability<IFluidHandlerItem, Void?> = Capabilities.FluidHandler.ITEM

    fun wrapHandler(handler: IFluidHandler, context: Direction?): List<HTFluidView> = when (handler) {
        is HTFluidHandler -> handler.getFluidTanks(context)

        else -> handler.tankRange.map { tank: Int ->
            object : HTFluidView {
                override fun getStack(): ImmutableFluidStack? = handler.getFluidInTank(tank).toImmutable()

                override fun getCapacity(stack: ImmutableFluidStack?): Int = handler.getTankCapacity(tank)
            }
        }
    }

    //    Block    //

    fun getFluidViews(level: Level, pos: BlockPos, side: Direction?): List<HTFluidView> =
        getCapability(level, pos, side)?.let { wrapHandler(it, side) } ?: listOf()

    fun getFluidView(
        level: Level,
        pos: BlockPos,
        side: Direction?,
        tank: Int,
    ): HTFluidView? = getFluidViews(level, pos, side).getOrNull(tank)

    //    Entity    //

    fun getFluidViews(entity: Entity, side: Direction?): List<HTFluidView> =
        getCapability(entity, side)?.let { wrapHandler(it, side) } ?: listOf()

    fun getFluidView(entity: Entity, side: Direction?, tank: Int): HTFluidView? = getFluidViews(entity, side).getOrNull(tank)

    //    Item    //

    fun getFluidViews(stack: ItemStack): List<HTFluidView> = getCapability(stack)?.let { wrapHandler(it, null) } ?: listOf()

    fun getFluidView(stack: ItemStack, tank: Int): HTFluidView? = getFluidViews(stack).getOrNull(tank)

    // HTItemStorageStack

    fun getFluidViews(stack: ImmutableItemStack?): List<HTFluidView> = getCapability(stack)?.let { wrapHandler(it, null) } ?: listOf()

    fun getFluidView(stack: ImmutableItemStack?, tank: Int): HTFluidView? = getFluidViews(stack).getOrNull(tank)
}
