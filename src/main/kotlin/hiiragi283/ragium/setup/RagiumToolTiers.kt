package hiiragi283.ragium.setup

import hiiragi283.ragium.api.tag.RagiumItemTags
import net.minecraft.tags.BlockTags
import net.minecraft.world.item.Tiers
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.common.SimpleTier

object RagiumToolTiers {
    @JvmField
    val RAGI_ALLOY = SimpleTier(
        BlockTags.INCORRECT_FOR_STONE_TOOL,
        Tiers.IRON.uses,
        Tiers.STONE.speed,
        Tiers.STONE.attackDamageBonus,
        Tiers.STONE.enchantmentValue,
    ) { Ingredient.of(RagiumItemTags.INGOTS_RAGI_ALLOY) }

    @JvmField
    val AZURE_STEEL = SimpleTier(
        BlockTags.INCORRECT_FOR_IRON_TOOL,
        Tiers.IRON.uses * 4,
        Tiers.IRON.speed,
        Tiers.IRON.attackDamageBonus,
        Tiers.IRON.enchantmentValue,
    ) { Ingredient.of(RagiumItemTags.INGOTS_AZURE_STEEL) }
}
