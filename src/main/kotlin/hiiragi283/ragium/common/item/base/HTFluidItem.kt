package hiiragi283.ragium.common.item.base

import hiiragi283.ragium.api.network.addFluidTooltip
import hiiragi283.ragium.api.storage.HTMultiCapability
import hiiragi283.ragium.api.storage.fluid.HTFluidHandler
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.storage.fluid.getTintColor
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import kotlin.math.roundToInt

abstract class HTFluidItem(properties: Properties) : Item(properties) {
    companion object {
        @JvmStatic
        fun getHandler(stack: ItemStack): HTFluidHandler? = HTMultiCapability.FLUID.getCapability(stack) as? HTFluidHandler

        @JvmStatic
        fun getFluidTank(stack: ItemStack, tank: Int): HTFluidTank? {
            val handler: HTFluidHandler = getHandler(stack) ?: return null
            return handler.getFluidTank(tank, handler.getFluidSideFor())
        }

        @JvmStatic
        fun hasHandler(stack: ItemStack): Boolean {
            val handler: HTFluidHandler = getHandler(stack) ?: return false
            return !handler.getFluidTanks(handler.getFluidSideFor()).isEmpty()
        }
    }

    //    Item    //

    override fun isBarVisible(stack: ItemStack): Boolean = hasHandler(stack)

    override fun getBarWidth(stack: ItemStack): Int {
        val tank: HTFluidTank = getFluidTank(stack, 0) ?: return 0
        return (13f * tank.getStoredLevelAsFloat(tank.getStack())).roundToInt()
    }

    override fun getBarColor(stack: ItemStack): Int = getFluidTank(stack, 0)?.getStack()?.getTintColor() ?: 0

    override fun appendHoverText(
        stack: ItemStack,
        context: TooltipContext,
        tooltips: MutableList<Component>,
        flag: TooltipFlag,
    ) {
        val handler: HTFluidHandler = getHandler(stack) ?: return
        addFluidTooltip(handler, tooltips::add, flag)
    }
}
