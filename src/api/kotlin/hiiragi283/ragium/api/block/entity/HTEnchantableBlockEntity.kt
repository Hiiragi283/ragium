package hiiragi283.ragium.api.block.entity

import hiiragi283.ragium.api.extension.getLevel
import hiiragi283.ragium.api.util.HTEnchantmentListener
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.ItemEnchantments
import net.minecraft.world.level.block.entity.BlockEntity

/**
 * エンチャントを保持可能な[BlockEntity]
 */
interface HTEnchantableBlockEntity : HTEnchantmentListener {
    /**
     * 機械が保持しているエンチャントの一覧
     */
    val enchantments: ItemEnchantments

    /**
     * 指定した[key]のレベルを取得します。
     * @return 指定したエンチャントが登録されていない，または紐づいていない場合は`0`
     */
    fun getEnchantmentLevel(key: ResourceKey<Enchantment>): Int = enchantments.getLevel(key)
}
