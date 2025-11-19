package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.api.item.component.HTIntrinsicEnchantment
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.text.HTTextResult
import hiiragi283.ragium.common.recipe.HTExpExtractingRecipe
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.enchantment.Enchantment

object RagiumEnchantingRecipeProvider : HTRecipeProvider.Direct() {
    override fun buildRecipeInternal() {
        save(
            RagiumAPI.id(RagiumConst.EXTRACTING, "experience_from_items"),
            HTExpExtractingRecipe,
        )
    }

    @JvmStatic
    private fun ench(key: ResourceKey<Enchantment>, level: Int): HTTextResult<HTItemResult> = HTIntrinsicEnchantment(key, level)
        .toEnchBook(provider)
        .map(resultHelper::item)
}
