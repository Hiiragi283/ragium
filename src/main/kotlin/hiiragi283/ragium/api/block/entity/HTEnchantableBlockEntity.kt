package hiiragi283.ragium.api.block.entity

import hiiragi283.ragium.api.extension.getLevel
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.TagKey
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.ItemEnchantments
import net.minecraft.world.level.block.entity.BlockEntity

/**
 * エンチャントを保持可能な[BlockEntity]
 */
interface HTEnchantableBlockEntity {
    /**
     * 機械が保持しているエンチャントの一覧
     */
    val enchantments: ItemEnchantments

    /**
     * 指定した[key]のレベルを取得します。
     * @return 指定したエンチャントが登録されていない，または紐づいていない場合は`0`
     */
    fun getEnchantmentLevel(key: ResourceKey<Enchantment>): Int = enchantments.getLevel(key)

    /**
     * 指定した[tagKey]に含まれるエンチャントのレベルの最大値を取得します。
     * @return 指定したタグが登録されていない，または紐づいていない場合は`0`
     */
    fun getEnchantmentLevel(tagKey: TagKey<Enchantment>): Int = enchantments.getLevel(tagKey)

    /**
     * 指定した[newEnchantments]でエンチャントを更新します。
     */
    fun updateEnchantments(newEnchantments: ItemEnchantments)
}
