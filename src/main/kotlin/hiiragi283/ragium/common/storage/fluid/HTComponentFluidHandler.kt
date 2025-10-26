package hiiragi283.ragium.common.storage.fluid

import hiiragi283.ragium.api.storage.fluid.HTFluidHandler
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.common.storage.fluid.tank.HTComponentFluidTank
import net.minecraft.core.Direction
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem

/**
 * [HTFluidHandler]に基づいたコンポーネント向けの実装
 * @see mekanism.common.attachments.containers.fluid.ComponentBackedFluidHandler
 */
class HTComponentFluidHandler(private val stack: ItemStack, private val tank: HTComponentFluidTank) :
    IFluidHandlerItem,
    HTFluidHandler {
    override fun getContainer(): ItemStack = stack

    override fun getFluidTanks(side: Direction?): List<HTFluidTank> = listOf(tank)
}
