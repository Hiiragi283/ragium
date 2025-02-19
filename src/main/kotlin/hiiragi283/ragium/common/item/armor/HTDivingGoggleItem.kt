package hiiragi283.ragium.common.item.armor

import hiiragi283.ragium.api.item.HTSingleEnchantmentAwareItem
import net.minecraft.resources.ResourceKey
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.Enchantments
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.capability.IFluidHandler

class HTDivingGoggleItem(properties: Properties) :
    HTFluidArmorItem(Type.HELMET, properties),
    HTSingleEnchantmentAwareItem {
    override fun onTick(stack: ItemStack, player: Player) {
        val maxAir: Int = player.maxAirSupply
        val currentAir: Int = player.airSupply
        if (currentAir == maxAir) return
        val extracted: FluidStack =
            getHandler(stack)?.drain(maxAir - currentAir, IFluidHandler.FluidAction.EXECUTE) ?: return
        if (!extracted.isEmpty) {
            player.airSupply += extracted.amount
        }
    }

    //    HTSingleEnchantmentAwareItem    //

    override val targetEnchantment: ResourceKey<Enchantment> = Enchantments.AQUA_AFFINITY

    override val targetLevel: Int = 1
}
