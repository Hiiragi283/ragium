package hiiragi283.ragium.common.item.base

import hiiragi283.ragium.api.storage.capability.RagiumCapabilities
import hiiragi283.ragium.api.storage.fluid.HTFluidHandler
import hiiragi283.ragium.api.storage.fluid.HTFluidTank
import hiiragi283.ragium.api.storage.fluid.getTintColor
import hiiragi283.ragium.api.text.addFluidTooltip
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import kotlin.math.roundToInt

abstract class HTFluidItem(properties: Properties) : Item(properties) {
    override fun isBarVisible(stack: ItemStack): Boolean = RagiumCapabilities.FLUID.hasCapability(stack)

    override fun getBarWidth(stack: ItemStack): Int {
        val tank: HTFluidTank = RagiumCapabilities.FLUID.getCapabilitySlot(stack, 0) ?: return 0
        return (13f * tank.getStoredLevelAsFloat(tank.getStack())).roundToInt()
    }

    override fun getBarColor(stack: ItemStack): Int = RagiumCapabilities.FLUID
        .getCapabilitySlot(stack, 0)
        ?.getStack()
        ?.getTintColor() ?: 0

    override fun appendHoverText(
        stack: ItemStack,
        context: TooltipContext,
        tooltips: MutableList<Component>,
        flag: TooltipFlag,
    ) {
        val handler: HTFluidHandler = RagiumCapabilities.FLUID.getSlottedCapability(stack) ?: return
        addFluidTooltip(handler, tooltips::add, flag)
    }
}
