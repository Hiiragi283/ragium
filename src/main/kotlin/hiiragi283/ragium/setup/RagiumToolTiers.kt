package hiiragi283.ragium.setup

import hiiragi283.ragium.api.util.material.HTItemMaterialVariant
import hiiragi283.ragium.util.material.RagiumMaterialType
import net.minecraft.tags.BlockTags
import net.minecraft.world.item.Tiers
import net.neoforged.neoforge.common.SimpleTier

object RagiumToolTiers {
    @JvmField
    val RAGI_ALLOY = SimpleTier(
        BlockTags.INCORRECT_FOR_STONE_TOOL,
        Tiers.IRON.uses,
        Tiers.STONE.speed,
        Tiers.STONE.attackDamageBonus,
        Tiers.STONE.enchantmentValue,
    ) { HTItemMaterialVariant.INGOT.toIngredient(RagiumMaterialType.RAGI_ALLOY) }

    @JvmField
    val AZURE_STEEL = SimpleTier(
        BlockTags.INCORRECT_FOR_IRON_TOOL,
        Tiers.IRON.uses * 4,
        Tiers.IRON.speed,
        Tiers.IRON.attackDamageBonus,
        Tiers.IRON.enchantmentValue,
    ) { HTItemMaterialVariant.INGOT.toIngredient(RagiumMaterialType.AZURE_STEEL) }

    @JvmField
    val DEEP_STEEL = SimpleTier(
        BlockTags.INCORRECT_FOR_NETHERITE_TOOL,
        Tiers.NETHERITE.uses,
        Tiers.NETHERITE.speed,
        Tiers.NETHERITE.attackDamageBonus,
        Tiers.NETHERITE.enchantmentValue,
    ) { HTItemMaterialVariant.INGOT.toIngredient(RagiumMaterialType.DEEP_STEEL) }
}
