package hiiragi283.ragium.api.item

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.addFluidTooltip
import hiiragi283.ragium.api.storage.HTMultiCapability
import hiiragi283.ragium.api.storage.fluid.HTFluidHandler
import net.minecraft.core.Holder
import net.minecraft.core.HolderGetter
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.Enchantments
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem
import kotlin.math.roundToInt

abstract class HTFluidItem(properties: Properties) : Item(properties) {
    companion object {
        @JvmStatic
        fun getHandler(stack: ItemStack): IFluidHandlerItem? = HTMultiCapability.FLUID.getCapability(stack)

        @JvmStatic
        fun hasHandler(stack: ItemStack): Boolean = getHandler(stack) != null

        @JvmStatic
        fun getFluidInTank(stack: ItemStack, tank: Int): FluidStack = getHandler(stack)?.getFluidInTank(tank) ?: FluidStack.EMPTY

        @JvmStatic
        fun getTankCapacity(stack: ItemStack, tank: Int): Int = getHandler(stack)?.getTankCapacity(tank) ?: 0

        @JvmStatic
        fun drainFluid(stack: ItemStack, resource: FluidStack, simulate: Boolean): FluidStack =
            getHandler(stack)?.drain(resource, HTFluidHandler.toAction(simulate)) ?: FluidStack.EMPTY

        @JvmStatic
        fun drainFluid(stack: ItemStack, maxDrain: Int, simulate: Boolean): FluidStack =
            getHandler(stack)?.drain(maxDrain, HTFluidHandler.toAction(simulate)) ?: FluidStack.EMPTY

        @JvmStatic
        fun getFluidUsage(stack: ItemStack, fluid: FluidStack): Int {
            val enchGetter: HolderGetter<Enchantment> =
                RagiumAPI.getInstance().resolveLookup(Registries.ENCHANTMENT) ?: return fluid.amount
            enchGetter
                .get(Enchantments.UNBREAKING)
                .ifPresent { holder: Holder.Reference<Enchantment> ->
                    val level: Int = stack.getEnchantmentLevel(holder)
                    if (level > 0) {
                        fluid.amount /= (level + 1)
                    }
                }
            return fluid.amount
        }

        @JvmStatic
        fun canConsumeFluid(stack: ItemStack, tank: Int, fluid: FluidStack): Boolean {
            val fluidIn: FluidStack = getFluidInTank(stack, tank)
            if (fluidIn.isEmpty) return false
            if (!FluidStack.isSameFluidSameComponents(fluid, fluidIn)) return false
            return fluidIn.amount >= getFluidUsage(stack, fluid.copy())
        }
    }

    //    Item    //

    override fun isBarVisible(stack: ItemStack): Boolean = hasHandler(stack)

    override fun getBarWidth(stack: ItemStack): Int {
        val handler: IFluidHandlerItem = getHandler(stack) ?: return 0
        return (13f / handler.getTankCapacity(0) * handler.getFluidInTank(0).amount).roundToInt()
    }

    override fun getBarColor(stack: ItemStack): Int {
        val handler: IFluidHandlerItem = getHandler(stack) ?: return 0x9999cc
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
