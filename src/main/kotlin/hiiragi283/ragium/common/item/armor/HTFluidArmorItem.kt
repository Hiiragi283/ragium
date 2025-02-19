package hiiragi283.ragium.common.item.armor

import hiiragi283.ragium.api.extension.addFluidTooltip
import hiiragi283.ragium.common.init.RagiumArmorMaterials
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ArmorItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem
import thedarkcolour.kotlinforforge.neoforge.forge.runForDist
import kotlin.math.roundToInt

abstract class HTFluidArmorItem(type: Type, properties: Properties) :
    ArmorItem(RagiumArmorMaterials.DEFAULT, type, properties.setNoRepair().stacksTo(1)) {
    protected fun getHandler(stack: ItemStack): IFluidHandlerItem? = stack.getCapability(Capabilities.FluidHandler.ITEM)

    //    ArmorItem    //

    override fun appendHoverText(
        stack: ItemStack,
        context: TooltipContext,
        tooltips: MutableList<Component>,
        flag: TooltipFlag,
    ) {
        addFluidTooltip(stack, tooltips::add)
    }

    override fun inventoryTick(
        stack: ItemStack,
        level: Level,
        entity: Entity,
        slotId: Int,
        isSelected: Boolean,
    ) {
        if (level.isClientSide) return
        if (level.gameTime % 20 != 0L) return
        if (entity is Player && stack == entity.getItemBySlot(equipmentSlot)) {
            onTick(stack, entity)
        }
    }

    protected abstract fun onTick(stack: ItemStack, player: Player)

    override fun isBarVisible(stack: ItemStack): Boolean = getHandler(stack) != null

    override fun getBarWidth(stack: ItemStack): Int {
        if (stack.count > 1) return 0
        val oxygenTank: IFluidHandlerItem = getHandler(stack) ?: return 0
        val percent: Float = 13f / oxygenTank.getTankCapacity(0).toFloat() * oxygenTank.getFluidInTank(0).amount
        return percent.roundToInt()
    }

    override fun getBarColor(stack: ItemStack): Int {
        val stackIn: FluidStack = getHandler(stack)?.getFluidInTank(0) ?: return 0
        val type: FluidType = stackIn.fluidType
        return runForDist(
            { IClientFluidTypeExtensions.of(type).getTintColor(stackIn) },
            { 0 },
        )
    }
}
