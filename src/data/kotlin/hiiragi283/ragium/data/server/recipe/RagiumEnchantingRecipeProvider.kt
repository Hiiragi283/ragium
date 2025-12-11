package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.common.data.recipe.HTCombineRecipeBuilder
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.common.material.VanillaMaterialKeys
import hiiragi283.ragium.common.recipe.custom.HTCopyEnchantingRecipe
import hiiragi283.ragium.setup.RagiumEnchantments
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.core.HolderGetter
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Items
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.Enchantments
import net.neoforged.neoforge.common.Tags

object RagiumEnchantingRecipeProvider : HTRecipeProvider.Direct() {
    private val enchLookup: HolderGetter<Enchantment> by lazy { provider.lookupOrThrow(Registries.ENCHANTMENT) }

    override fun buildRecipeInternal() {
        save(HTCopyEnchantingRecipe.RECIPE_ID, HTCopyEnchantingRecipe)

        // Vanilla
        enchanting(
            itemCreator.fromTagKey(CommonMaterialPrefixes.INGOT, VanillaMaterialKeys.IRON, 64),
            Enchantments.PROTECTION,
        )
        enchanting(
            itemCreator.fromTagKey(Tags.Items.BRICKS_NETHER, 64),
            Enchantments.FIRE_PROTECTION,
        )
        enchanting(
            itemCreator.fromTagKey(Tags.Items.FEATHERS, 64),
            Enchantments.FEATHER_FALLING,
        )
        enchanting(
            itemCreator.fromTagKey(Tags.Items.OBSIDIANS, 64),
            Enchantments.BLAST_PROTECTION,
        )
        enchanting(
            itemCreator.fromItem(Items.PUFFERFISH, 16),
            Enchantments.RESPIRATION,
        )
        // Aqua Affinity
        enchanting(
            itemCreator.fromTagKey(Tags.Items.CROPS_CACTUS, 64),
            Enchantments.THORNS,
        )

        enchanting(
            itemCreator.fromItem(Items.WIND_CHARGE, 64),
            Enchantments.WIND_BURST,
        )
        // Ragium
        enchanting(
            itemCreator.fromTagKey(CommonMaterialPrefixes.INGOT, RagiumMaterialKeys.DEEP_STEEL, 16),
            RagiumEnchantments.NOISE_CANCELING,
        )

        enchanting(itemCreator.fromItem(RagiumItems.ECHO_STAR), RagiumEnchantments.SONIC_PROTECTION)
    }

    @JvmStatic
    private fun enchanting(ingredient: HTItemIngredient, key: ResourceKey<Enchantment>) {
        HTCombineRecipeBuilder
            .enchanting(
                ingredient,
                enchLookup.getOrThrow(key),
            ).save(output)
    }
}
