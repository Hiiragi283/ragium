package hiiragi283.ragium.api.enchantment

import net.minecraft.world.item.enchantment.ItemEnchantments

fun interface HTEnchantmentListener {
    /**
     * 指定した[newEnchantments]でエンチャントを更新します。
     */
    fun onUpdateEnchantment(newEnchantments: ItemEnchantments)
}
