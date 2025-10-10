package hiiragi283.ragium.common.item.block

import hiiragi283.ragium.api.item.HTBlockItem
import hiiragi283.ragium.api.network.addFluidTooltip
import hiiragi283.ragium.api.storage.fluid.HTFluidHandler
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.storage.fluid.getFluidStack
import hiiragi283.ragium.common.block.HTDrumBlock
import hiiragi283.ragium.common.item.base.HTFluidItem
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions
import net.neoforged.neoforge.fluids.FluidStack
import kotlin.math.roundToInt

class HTDrumItem(block: HTDrumBlock, properties: Properties) : HTBlockItem<HTDrumBlock>(block, properties) {
    override fun isBarVisible(stack: ItemStack): Boolean = HTFluidItem.hasHandler(stack)

    override fun getBarWidth(stack: ItemStack): Int {
        val tank: HTFluidTank = HTFluidItem.getFluidTank(stack, 0) ?: return 0
        return (13f * tank.getStoredLevelAsFloat(tank.getStack())).roundToInt()
    }

    override fun getBarColor(stack: ItemStack): Int {
        val tank: HTFluidTank = HTFluidItem.getFluidTank(stack, 0) ?: return 0
        val fluid: FluidStack = tank.getFluidStack()
        return fluid.fluid.let(IClientFluidTypeExtensions::of).getTintColor(fluid)
    }

    override fun appendHoverText(
        stack: ItemStack,
        context: TooltipContext,
        tooltips: MutableList<Component>,
        flag: TooltipFlag,
    ) {
        val handler: HTFluidHandler = HTFluidItem.getHandler(stack) ?: return
        addFluidTooltip(handler, tooltips::add, flag)
    }
}
