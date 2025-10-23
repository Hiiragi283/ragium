package hiiragi283.ragium.common.item.block

import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.getClientExtensions
import hiiragi283.ragium.api.storage.HTStackView
import hiiragi283.ragium.api.storage.capability.RagiumCapabilities
import hiiragi283.ragium.api.text.addFluidTooltip
import hiiragi283.ragium.common.block.HTDrumBlock
import hiiragi283.ragium.common.variant.HTDrumVariant
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import kotlin.math.roundToInt

class HTDrumBlockItem(variant: HTDrumVariant, properties: Properties) :
    HTVariantBlockItem<HTDrumVariant, HTDrumBlock>(variant, properties) {
    override fun isBarVisible(stack: ItemStack): Boolean = RagiumCapabilities.FLUID.hasCapability(stack)

    override fun getBarWidth(stack: ItemStack): Int {
        val view: HTStackView<ImmutableFluidStack> = RagiumCapabilities.FLUID.getCapabilityView(stack, 0) ?: return 0
        return (13f * view.getStoredLevelAsFloat(view.getStack())).roundToInt()
    }

    override fun getBarColor(stack: ItemStack): Int {
        val stack: ImmutableFluidStack = RagiumCapabilities.FLUID.getCapabilityStack(stack, 0) ?: return 0
        return stack.getClientExtensions().getTintColor(stack.stack)
    }

    override fun appendHoverText(
        stack: ItemStack,
        context: TooltipContext,
        tooltips: MutableList<Component>,
        flag: TooltipFlag,
    ) {
        addFluidTooltip(RagiumCapabilities.FLUID.getCapabilityStacks(stack), tooltips::add, flag)
    }
}
