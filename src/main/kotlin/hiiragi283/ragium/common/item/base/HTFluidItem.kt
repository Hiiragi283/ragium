package hiiragi283.ragium.common.item.base

import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.getTintColor
import hiiragi283.ragium.api.storage.HTStackView
import hiiragi283.ragium.api.storage.capability.HTFluidCapabilities
import hiiragi283.ragium.api.text.addFluidTooltip
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import kotlin.math.roundToInt

abstract class HTFluidItem(properties: Properties) : Item(properties) {
    override fun isBarVisible(stack: ItemStack): Boolean = HTFluidCapabilities.hasCapability(stack)

    override fun getBarWidth(stack: ItemStack): Int {
        val view: HTStackView<ImmutableFluidStack> = HTFluidCapabilities.getCapabilityView(stack, 0) ?: return 0
        return (13f * view.getStoredLevelAsFloat()).roundToInt()
    }

    override fun getBarColor(stack: ItemStack): Int = HTFluidCapabilities.getCapabilityStack(stack, 0)?.getTintColor() ?: 0

    override fun appendHoverText(
        stack: ItemStack,
        context: TooltipContext,
        tooltips: MutableList<Component>,
        flag: TooltipFlag,
    ) {
        addFluidTooltip(HTFluidCapabilities.getCapabilityStacks(stack), tooltips::add, flag)
    }
}
