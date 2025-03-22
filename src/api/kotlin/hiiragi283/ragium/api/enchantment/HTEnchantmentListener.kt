package hiiragi283.ragium.api.enchantment

import net.minecraft.world.item.enchantment.ItemEnchantments

fun interface HTEnchantmentListener : (ItemEnchantments) -> Unit {
    /**
     * 指定した[newEnchantments]でエンチャントを更新します。
     */
    fun onUpdateEnchantment(newEnchantments: ItemEnchantments)

    override fun invoke(newEnchantments: ItemEnchantments) {
        onUpdateEnchantment(newEnchantments)
    }
}
