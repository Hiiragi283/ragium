package hiiragi283.ragium.common.item

import hiiragi283.ragium.api.extension.addFluidTooltip
import hiiragi283.ragium.api.item.HTBlockItem
import hiiragi283.ragium.common.block.HTDrumBlock
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem
import kotlin.math.roundToInt

class HTDrumItem(block: HTDrumBlock, properties: Properties) : HTBlockItem<HTDrumBlock>(block, properties) {
    override fun isBarVisible(stack: ItemStack): Boolean = HTFluidItem.hasHandler(stack)

    override fun getBarWidth(stack: ItemStack): Int {
        val handler: IFluidHandlerItem = HTFluidItem.getHandler(stack) ?: return 0
        return (13f / handler.getTankCapacity(0) * handler.getFluidInTank(0).amount).roundToInt()
    }

    override fun getBarColor(stack: ItemStack): Int {
        val handler: IFluidHandlerItem = HTFluidItem.getHandler(stack) ?: return 0x9999cc
        val fluid: FluidStack = handler.getFluidInTank(0)
        return fluid.fluid.let(IClientFluidTypeExtensions::of).getTintColor(fluid)
    }

    override fun appendHoverText(
        stack: ItemStack,
        context: TooltipContext,
        tooltips: MutableList<Component>,
        flag: TooltipFlag,
    ) {
        addFluidTooltip(stack, tooltips::add, flag)
    }
}
