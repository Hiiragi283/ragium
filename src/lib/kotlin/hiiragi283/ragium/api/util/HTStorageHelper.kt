package hiiragi283.ragium.api.util

import hiiragi283.lib.capability.HTEnergyCapabilities
import hiiragi283.lib.capability.HTFluidCapabilities
import hiiragi283.lib.color.HTDefaultColor
import hiiragi283.lib.item.ItemStack
import hiiragi283.lib.registry.getKeyOrThrow
import hiiragi283.lib.text.HTCommonTranslation
import hiiragi283.lib.text.HTTextUtil
import hiiragi283.lib.text.Text
import hiiragi283.lib.text.withStyle
import hiiragi283.lib.transfer.HTResourceView
import hiiragi283.lib.transfer.energy.HTEnergyHandler
import hiiragi283.lib.util.fixedFraction
import hiiragi283.ragium.api.data.RagiumDataComponents
import java.util.function.Consumer
import kotlin.math.roundToInt
import net.minecraft.ChatFormatting
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.util.Mth
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.redstone.Redstone
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.SimpleFluidContent
import net.neoforged.neoforge.transfer.access.ItemAccess

/**
 * 参照 : [Mekanism - StorageUtils](https://github.com/mekanism/Mekanism/blob/1.21.x/src/main/java/mekanism/common/util/StorageUtils.java)
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
data object HTStorageHelper {
    //    Amount    //

    /**
     * 参照 : [NeoForge - ResourceHandlerUtil.getRedstoneSignalFromResourceHandler][net.neoforged.neoforge.transfer.ResourceHandlerUtil.getRedstoneSignalFromResourceHandler]
     */
    @JvmStatic
    fun calculateRedstoneLevel(views: Iterable<HTResourceView<*>>): Int {
        var proportion = 0.0f
        var sampleCount = 0 // Number of samples in proportion
        for (view: HTResourceView<*> in views) {
            val indexFill: Int = view.amount
            if (indexFill > 0) {
                val capacity: Int = view.getCapacity(view.resource)
                if (capacity > 0) {
                    proportion += fixedFraction(indexFill, capacity)
                    sampleCount++
                }
            }
        }
        if (sampleCount == 0) return Redstone.SIGNAL_NONE
        proportion /= sampleCount.toFloat()
        return Mth.lerpDiscrete(proportion, Redstone.SIGNAL_NONE, Redstone.SIGNAL_MAX)
    }

    /**
     * 参照 : [Mekanism - MekanismUtils.redstoneLevelFromContents](https://github.com/mekanism/Mekanism/blob/1.21.x/src/main/java/mekanism/common/util/MekanismUtils.java)
     */
    @JvmStatic
    fun calculateRedstoneLevel(amount: Int, capacity: Int): Int = Mth.lerpDiscrete(fixedFraction(amount, capacity), Redstone.SIGNAL_NONE, Redstone.SIGNAL_MAX)

    @JvmStatic
    fun calculateRedstoneLevel(view: HTResourceView<*>): Int = Mth.lerpDiscrete(view.currentFilledLevel, Redstone.SIGNAL_NONE, Redstone.SIGNAL_MAX)

    @JvmStatic
    fun calculateRedstoneLevel(handler: HTEnergyHandler): Int = Mth.lerpDiscrete(handler.filledLevel, Redstone.SIGNAL_NONE, Redstone.SIGNAL_MAX)

    //    Energy    //

    @JvmStatic
    fun createStackWithEnergy(
        item: ItemLike,
        amount: Int,
        count: Int = 1,
        patch: DataComponentPatch = DataComponentPatch.EMPTY,
    ): ItemStack {
        val stack = ItemStack(item, count, patch)
        updateEnergy(stack, amount)
        return stack
    }

    @JvmStatic
    fun getEnergy(container: ItemStack): Int = container.getOrDefault(RagiumDataComponents.ENERGY, 0)

    @JvmStatic
    fun updateEnergy(container: ItemStack, newAmount: Int) {
        if (newAmount <= 0) {
            container.remove(RagiumDataComponents.ENERGY)
        } else {
            container.set(RagiumDataComponents.ENERGY, newAmount)
        }
    }

    @JvmStatic
    fun addEnergyTooltip(handler: HTEnergyHandler, consumer: Consumer<Text>, isCreative: Boolean) {
        // Empty name if amount is not positive
        if (handler.isEmpty) {
            consumer.accept(HTCommonTranslation.EMPTY.translate())
            return
        }
        // Fluid Name and Amount
        when {
            isCreative -> HTCommonTranslation.STORED_FE.translate(HTCommonTranslation.INFINITE)
            else -> HTCommonTranslation.STORED_FE.translate(HTCommonTranslation.FRACTION.translate(handler.amount, handler.capacity))
        }.let(consumer::accept)
    }

    @JvmStatic
    fun getEnergyBarWidth(container: ItemStack): Int = when {
        container.count > 1 -> 0
        else -> (13.0 - 13.0 * getEnergyDurability(container)).roundToInt()
    }

    @JvmStatic
    private fun getEnergyDurability(container: ItemStack): Double {
        val handler: HTEnergyHandler = HTEnergyCapabilities.getHandler(ItemAccess.forStack(container)) ?: return 1.0
        val bestRatio: Double = handler.filledLevel.toDouble()
        return 1 - bestRatio
    }

    //    Fluid    //

    @JvmStatic
    fun createStackWithFluid(
        item: ItemLike,
        fluidStack: FluidStack,
        count: Int = 1,
        patch: DataComponentPatch = DataComponentPatch.EMPTY,
    ): ItemStack {
        val stack = ItemStack(item, count, patch)
        updateFluid(stack, fluidStack)
        return stack
    }

    @JvmStatic
    fun getFluid(container: ItemStack): FluidStack = container.getOrDefault(RagiumDataComponents.FLUID, SimpleFluidContent.EMPTY).copy()

    @JvmStatic
    fun updateFluid(container: ItemStack, newStack: FluidStack) {
        if (newStack.isEmpty) {
            container.remove(RagiumDataComponents.FLUID)
        } else {
            container[RagiumDataComponents.FLUID] = SimpleFluidContent.copyOf(newStack)
        }
    }

    @JvmStatic
    fun addFluidTooltip(
        stack: FluidStack,
        consumer: Consumer<Text>,
        context: Item.TooltipContext,
        player: Player?,
        flag: TooltipFlag,
        isCreative: Boolean,
    ) {
        // Empty name if stack is empty
        if (stack.isEmpty) {
            consumer.accept(HTCommonTranslation.EMPTY.translate())
            return
        }
        // Fluid Name and Amount
        if (isCreative) {
            HTCommonTranslation.STORED.translate(stack, HTCommonTranslation.INFINITE)
        } else {
            HTCommonTranslation.STORED_MB.translate(stack, stack.amount)
        }.let(consumer::accept)
        // Default Fluid Tooltips
        val tooltips: MutableList<Text> = stack.getTooltipLines(context, player, flag)
        tooltips.removeFirst() // remove fluid name
        tooltips.forEach(consumer)
        // Mod Name
        stack.typeHolder()
            .getKeyOrThrow()
            .identifier()
            .namespace
            .let(HTTextUtil::getModNameText)
            .withStyle(HTDefaultColor.BLUE)
            .withStyle(ChatFormatting.ITALIC)
            .let(consumer::accept)
    }

    @JvmStatic
    fun getFluidBarWidth(container: ItemStack): Int = when {
        container.count > 1 -> 0
        else -> (13.0 - 13.0 * getFluidDurability(container)).roundToInt()
    }

    @JvmStatic
    private fun getFluidDurability(container: ItemStack): Double {
        val bestRatio: Double = HTFluidCapabilities.getSlot(ItemAccess.forStack(container), 0)?.currentFilledLevel?.toDouble() ?: 0.0
        return 1 - bestRatio
    }

    //    Item    //
}
