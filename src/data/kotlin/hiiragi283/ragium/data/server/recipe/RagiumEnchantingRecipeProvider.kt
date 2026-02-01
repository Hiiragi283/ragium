package hiiragi283.ragium.data.server.recipe

import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.data.recipe.HTSingleRecipeBuilder
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.Enchantments

object RagiumEnchantingRecipeProvider : HTSubRecipeProvider.Direct(RagiumAPI.MOD_ID) {
    override fun buildRecipeInternal() {
        val enchLookup: HolderLookup.RegistryLookup<Enchantment> = provider.lookupOrThrow(Registries.ENCHANTMENT)
        // Protection
        HTSingleRecipeBuilder.enchanting(output) {
            ingredient = inputCreator.create(CommonTagPrefixes.INGOT, VanillaMaterialKeys.IRON, 64)
            result = enchLookup.getOrThrow(Enchantments.PROTECTION)
        }
    }
}
