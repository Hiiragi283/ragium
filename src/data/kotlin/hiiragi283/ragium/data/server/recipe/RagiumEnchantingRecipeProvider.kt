package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTResultHelper
import hiiragi283.ragium.api.extension.enchLookup
import hiiragi283.ragium.api.recipe.result.HTItemResult
import net.minecraft.core.HolderGetter
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.EnchantedBookItem
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.EnchantmentInstance

object RagiumEnchantingRecipeProvider : HTRecipeProvider.Direct() {
    private lateinit var enchLookup: HolderGetter<Enchantment>

    override fun buildRecipeInternal() {
        enchLookup = provider.enchLookup()
    }

    @JvmStatic
    private fun ench(key: ResourceKey<Enchantment>, level: Int): HTItemResult = enchLookup
        .get(key)
        .map { EnchantmentInstance(it, level) }
        .map(EnchantedBookItem::createForEnchantment)
        .map(HTResultHelper::item)
        .orElseThrow()
}
