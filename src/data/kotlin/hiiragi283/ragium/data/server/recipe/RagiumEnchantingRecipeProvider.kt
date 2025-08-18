package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.HTRecipeProvider
import hiiragi283.ragium.api.extension.enchLookup
import hiiragi283.ragium.api.recipe.result.HTEnchantedBookResult
import net.minecraft.core.HolderGetter
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.enchantment.Enchantment

object RagiumEnchantingRecipeProvider : HTRecipeProvider.Direct() {
    private lateinit var enchLookup: HolderGetter<Enchantment>

    override fun buildRecipeInternal() {
        enchLookup = provider.enchLookup()
    }

    @JvmStatic
    private fun ench(key: ResourceKey<Enchantment>, level: Int): HTEnchantedBookResult =
        HTEnchantedBookResult(enchLookup.getOrThrow(key), level)
}
