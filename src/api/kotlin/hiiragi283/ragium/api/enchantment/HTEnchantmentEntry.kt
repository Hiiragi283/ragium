package hiiragi283.ragium.api.enchantment

import net.minecraft.core.Holder
import net.minecraft.world.item.enchantment.Enchantment

/**
 * [Enchantment]の[Holder]とそのレベルを束ねたデータクラス
 */
data class HTEnchantmentEntry(val holder: Holder<Enchantment>, val level: Int) {
    constructor(entry: Map.Entry<Holder<Enchantment>, Int>) : this(entry.key, entry.value)
}
