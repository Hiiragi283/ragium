package hiiragi283.ragium.common.item.upgrade

import hiiragi283.ragium.api.RagiumPlatform
import hiiragi283.ragium.api.item.HTDynamicUpgradeItem
import hiiragi283.ragium.api.item.component.HTMachineUpgrade
import net.minecraft.core.Holder
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.Enchantments

class HTFortuneUpgradeItem(properties: Properties) :
    Item(properties),
    HTDynamicUpgradeItem {
    override fun supportsEnchantment(stack: ItemStack, enchantment: Holder<Enchantment?>): Boolean = enchantment.key == Enchantments.FORTUNE

    override fun getUpgrade(stack: ItemStack): HTMachineUpgrade {
        val fortune: Int = RagiumPlatform.INSTANCE
            .getHolder(null, Enchantments.FORTUNE)
            ?.let(stack::getEnchantmentLevel)
            ?.takeIf { it > 0 }
            ?: 1
        return HTMachineUpgrade.create(HTMachineUpgrade.Key.SUBPRODUCT_CHANCE to 0.3f * fortune)
    }
}
