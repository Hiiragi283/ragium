package hiiragi283.lib.transfer.fluid

import hiiragi283.lib.capability.HTFluidCapabilities
import hiiragi283.lib.data.buildDataPatch
import hiiragi283.lib.registry.getKeyOrThrow
import hiiragi283.lib.text.HTCommonTranslation
import hiiragi283.lib.text.HTTextUtil
import hiiragi283.lib.text.Text
import hiiragi283.ragium.api.data.RagiumDataComponents
import net.minecraft.ChatFormatting
import net.minecraft.core.component.DataComponentGetter
import net.minecraft.core.component.DataComponentMap
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.SimpleFluidContent
import kotlin.collections.forEach
import kotlin.math.roundToInt

/**
 * @author Hiiragi Tsubasa
 * @since 26.1.13
 * @see net.neoforged.neoforge.transfer.fluid.FluidUtil
 */
data object HTFluidTankUtil {
    @JvmStatic
    fun getFluid(container: DataComponentGetter): FluidStack = container.getOrDefault(RagiumDataComponents.FLUID, SimpleFluidContent.EMPTY).copy()

    @JvmStatic
    fun createUpdatedPatch(newStack: FluidStack): DataComponentPatch = buildDataPatch {
        if (newStack.isEmpty) {
            remove(RagiumDataComponents.FLUID)
        } else {
            set(RagiumDataComponents.FLUID, SimpleFluidContent.copyOf(newStack))
        }
    }

    /**
     * @since 26.1.8
     */
    @JvmStatic
    fun updateFluid(container: DataComponentMap.Builder, newStack: FluidStack) {
        container.set(
            RagiumDataComponents.FLUID,
            newStack.takeUnless(FluidStack::isEmpty)?.let(SimpleFluidContent::copyOf),
        )
    }

    @JvmStatic
    inline fun addFluidTooltip(stack: FluidStack, consumer: (Text) -> Unit, context: Item.TooltipContext, flag: TooltipFlag, isCreative: Boolean) {
        // Empty name if stack is empty
        if (stack.isEmpty) {
            consumer(HTCommonTranslation.EMPTY.translate())
            return
        }
        // Fluid Name and Amount
        if (isCreative) {
            HTCommonTranslation.STORED.translate(stack, HTCommonTranslation.INFINITE)
        } else {
            HTCommonTranslation.STORED_MB.translate(stack, stack.amount)
        }.let(consumer)
        // Default Fluid Tooltips
        val tooltips: MutableList<Text> = stack.getTooltipLines(context, context.player(), flag)
        tooltips.removeFirst() // remove fluid name
        tooltips.forEach(consumer)
        // Mod Name
        stack.typeHolder()
            .getKeyOrThrow()
            .identifier()
            .namespace
            .let(HTTextUtil::getModNameText)
            .withStyle(ChatFormatting.BLUE)
            .withStyle(ChatFormatting.ITALIC)
            .let(consumer)
    }

    @JvmStatic
    fun getFluidBarWidth(container: ItemStack): Int = when {
        container.count > 1 -> 0
        else -> (13.0 - 13.0 * getFluidDurability(container)).roundToInt()
    }

    @JvmStatic
    private fun getFluidDurability(container: ItemStack): Double {
        val bestRatio: Double = HTFluidCapabilities.getSlot(container, 0)?.currentFilledLevel?.toDouble() ?: 0.0
        return 1 - bestRatio
    }
}
