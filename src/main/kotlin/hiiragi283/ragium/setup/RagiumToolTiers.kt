package hiiragi283.ragium.setup

import hiiragi283.ragium.api.util.material.HTItemMaterialVariant
import hiiragi283.ragium.util.material.RagiumMaterialType
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.tags.BlockTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Tiers
import net.minecraft.world.item.component.Tool
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.common.SimpleTier
import net.neoforged.neoforge.registries.holdersets.AnyHolderSet
import java.util.Optional

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
    val RAGI_CRYSTAL = SimpleTier(
        BlockTags.INCORRECT_FOR_DIAMOND_TOOL,
        Tiers.DIAMOND.uses * 4,
        Tiers.DIAMOND.speed,
        Tiers.DIAMOND.attackDamageBonus,
        Tiers.DIAMOND.enchantmentValue,
    ) { HTItemMaterialVariant.GEM.toIngredient(RagiumMaterialType.RAGI_CRYSTAL) }

    init {
        object : SimpleTier(
            BlockTags.INCORRECT_FOR_DIAMOND_TOOL,
            Tiers.DIAMOND.uses * 4,
            Tiers.DIAMOND.speed,
            Tiers.DIAMOND.attackDamageBonus,
            Tiers.DIAMOND.enchantmentValue,
            { HTItemMaterialVariant.GEM.toIngredient(RagiumMaterialType.RAGI_CRYSTAL) },
        ) {
            override fun createToolProperties(block: TagKey<Block>): Tool = Tool(
                listOf(
                    Tool.Rule.deniesDrops(block),
                    Tool.Rule(
                        AnyHolderSet(BuiltInRegistries.BLOCK.asLookup()),
                        Optional.of(12f),
                        Optional.of(true),
                    ),
                ),
                1f,
                1,
            )
        }
    }

    @JvmField
    val DEEP_STEEL = SimpleTier(
        BlockTags.INCORRECT_FOR_NETHERITE_TOOL,
        Tiers.NETHERITE.uses,
        Tiers.NETHERITE.speed,
        Tiers.NETHERITE.attackDamageBonus,
        Tiers.NETHERITE.enchantmentValue,
    ) { HTItemMaterialVariant.INGOT.toIngredient(RagiumMaterialType.DEEP_STEEL) }
}
