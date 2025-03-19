package hiiragi283.ragium.api.enchantment

import net.minecraft.core.Holder
import net.minecraft.world.item.enchantment.Enchantment

/**
 * [Enchantment]の[Holder.Reference]とそのレベルを束ねたデータクラス
 */
data class HTEnchantmentEntry(val holder: Holder.Reference<Enchantment>, val level: Int)
