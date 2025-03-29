package hiiragi283.ragium.api.enchantment

import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.ItemEnchantments

/**
 * エンチャントを保持するインターフェース
 */
interface HTEnchantmentHolder {
    /**
     * 指定した[key]のレベルを取得します。
     * @return 指定したエンチャントが登録されていない，または紐づいていない場合は`0`
     */
    fun getEnchLevel(key: ResourceKey<Enchantment>): Int

    /**
     * 保持しているエンチャントの一覧を返します。
     * @return [HTEnchantmentEntry]の[Iterable]
     */
    fun getEnchEntries(): Iterable<HTEnchantmentEntry>

    /**
     * 保持しているエンチャントを[ItemEnchantments]として返します。
     */
    fun exportEnch(): ItemEnchantments {
        val mutable = ItemEnchantments.Mutable(ItemEnchantments.EMPTY)
        for (entry: HTEnchantmentEntry in getEnchEntries()) {
            mutable.set(entry.holder, entry.level)
        }
        return mutable.toImmutable()
    }
}
