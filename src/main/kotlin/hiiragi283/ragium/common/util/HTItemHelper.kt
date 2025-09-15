package hiiragi283.ragium.common.util

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.getOrNull
import net.minecraft.core.Holder
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.Enchantments

object HTItemHelper {
    @JvmStatic
    fun getFixedUsage(stack: ItemStack, amount: Int): Int {
        var result: Int = amount
        RagiumAPI
            .INSTANCE
            .enchLookup()
            .getOrNull(Enchantments.UNBREAKING)
            ?.let { holder: Holder<Enchantment> ->
                val level: Int = stack.getEnchantmentLevel(holder)
                if (level > 0) {
                    result /= (level + 1)
                }
            }
        return result
    }
}
