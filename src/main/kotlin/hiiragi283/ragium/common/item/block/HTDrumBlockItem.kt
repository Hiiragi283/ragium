package hiiragi283.ragium.common.item.block

import hiiragi283.ragium.api.block.attribute.getAttributeTier
import hiiragi283.ragium.api.item.HTBlockItem
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.stack.getTintColor
import hiiragi283.ragium.api.storage.HTStackView
import hiiragi283.ragium.api.storage.capability.RagiumCapabilities
import hiiragi283.ragium.common.block.HTDrumBlock
import hiiragi283.ragium.common.tier.HTDrumTier
import net.minecraft.world.item.ItemStack
import kotlin.math.roundToInt

class HTDrumBlockItem(block: HTDrumBlock, properties: Properties) : HTBlockItem<HTDrumBlock>(block, properties) {
    override fun getTier(): HTDrumTier = block.getAttributeTier<HTDrumTier>()

    override fun isBarVisible(stack: ItemStack): Boolean = RagiumCapabilities.FLUID.hasCapability(stack)

    override fun getBarWidth(stack: ItemStack): Int {
        val view: HTStackView<ImmutableFluidStack> = RagiumCapabilities.FLUID.getCapabilityView(stack, 0) ?: return 0
        return (13f * view.getStoredLevelAsFloat()).roundToInt()
    }

    override fun getBarColor(stack: ItemStack): Int {
        val stack: ImmutableFluidStack = RagiumCapabilities.FLUID.getCapabilityStack(stack, 0) ?: return 0
        return stack.getTintColor()
    }
}
