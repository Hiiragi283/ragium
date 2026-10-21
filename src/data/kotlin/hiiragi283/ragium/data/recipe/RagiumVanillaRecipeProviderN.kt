package hiiragi283.ragium.data.recipe

import hiiragi283.lib.data.recipe.HTRecipeProviderN
import hiiragi283.lib.data.recipe.builder.VanillaRecipeBuilders
import hiiragi283.ragium.common.item.RagiumItems
import net.minecraft.core.registries.MultiRegistryBootstrap
import net.minecraft.world.item.Items

class RagiumVanillaRecipeProviderN(registries: MultiRegistryBootstrap.BootstrapGetter) : HTRecipeProviderN(registries) {
    override fun buildRecipes() {
        // Bamboo Charcoal
        VanillaRecipeBuilders.smelting {
            ingredient { items { +Items.BAMBOO } }
            result { +RagiumItems.BAMBOO_CHARCOAL }
            exp = 0.5f
            inventory(getHasName(Items.BAMBOO)) { predicate { items { +Items.BAMBOO } } }
        }.save(output)
    }
}
