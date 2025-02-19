package hiiragi283.ragium.common.item

import hiiragi283.ragium.api.item.HTSingleEnchantmentAwareItem
import hiiragi283.ragium.common.init.RagiumArmorMaterials
import net.minecraft.resources.ResourceKey
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ArmorItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.level.Level
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidType
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem
import thedarkcolour.kotlinforforge.neoforge.forge.runForDist
import kotlin.math.roundToInt

class HTDivingGoggleItem(properties: Properties) :
    ArmorItem(
        RagiumArmorMaterials.DEFAULT,
        Type.HELMET,
        properties.setNoRepair().stacksTo(1),
    ),
    HTSingleEnchantmentAwareItem {
    //    Water Breath    //

    private fun getOxygenTank(stack: ItemStack): IFluidHandlerItem? = stack.getCapability(Capabilities.FluidHandler.ITEM)

    override fun inventoryTick(
        stack: ItemStack,
        level: Level,
        entity: Entity,
        slotId: Int,
        isSelected: Boolean,
    ) {
        if (level.isClientSide) return
        if (level.gameTime % 20 != 0L) return
        if (entity is Player && stack == entity.getItemBySlot(EquipmentSlot.HEAD)) {
            applyWaterBreath(stack, entity)
        }
    }

    private fun applyWaterBreath(stack: ItemStack, player: Player) {
        val maxAir: Int = player.maxAirSupply
        val currentAir: Int = player.airSupply
        if (currentAir == maxAir) return
        val oxygenTank: IFluidHandlerItem = getOxygenTank(stack) ?: return
        val extracted: FluidStack = oxygenTank.drain(maxAir - currentAir, IFluidHandler.FluidAction.EXECUTE)
        if (!extracted.isEmpty) {
            player.airSupply += extracted.amount
        }
    }

    override fun isBarVisible(stack: ItemStack): Boolean = getOxygenTank(stack) != null

    override fun getBarWidth(stack: ItemStack): Int {
        if (stack.count > 1) return 0
        val oxygenTank: IFluidHandlerItem = getOxygenTank(stack) ?: return 0
        val percent: Float = 13f / oxygenTank.getTankCapacity(0).toFloat() * oxygenTank.getFluidInTank(0).amount
        return percent.roundToInt()
    }

    override fun getBarColor(stack: ItemStack): Int {
        val oxygenTank: IFluidHandlerItem = getOxygenTank(stack) ?: return 0
        val stackIn: FluidStack = oxygenTank.getFluidInTank(0)
        val type: FluidType = stackIn.fluidType
        return runForDist(
            { IClientFluidTypeExtensions.of(type).getTintColor(stackIn) },
            { 0 },
        )
    }

    //    HTSingleEnchantmentAwareItem    //

    override val targetEnchantment: ResourceKey<Enchantment> = Enchantments.AQUA_AFFINITY

    override val targetLevel: Int = 1
}
