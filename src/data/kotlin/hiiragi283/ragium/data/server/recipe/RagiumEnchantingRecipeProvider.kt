package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.api.data.recipe.HTResultHelper
import hiiragi283.ragium.api.extension.enchLookup
import hiiragi283.ragium.api.item.component.HTIntrinsicEnchantment
import hiiragi283.ragium.api.recipe.result.HTItemResult
import net.minecraft.core.HolderGetter
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.enchantment.Enchantment

object RagiumEnchantingRecipeProvider : HTRecipeProvider.Direct() {
    @JvmStatic
    private lateinit var enchLookup: HolderGetter<Enchantment>

    override fun buildRecipeInternal() {
        enchLookup = provider.enchLookup()
    }

    @JvmStatic
    private fun ench(key: ResourceKey<Enchantment>, level: Int): HTItemResult? = HTIntrinsicEnchantment(key, level)
        .toEnchBook(enchLookup)
        ?.let(HTResultHelper.INSTANCE::item)
        ?: error("Unknown enchantment: ${key.location()}")
}
