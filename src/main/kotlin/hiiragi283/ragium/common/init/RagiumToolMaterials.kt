package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.CommonMaterials
import hiiragi283.ragium.api.material.keys.RagiumMaterials
import net.minecraft.tags.BlockTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Tier
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.block.Block

enum class RagiumToolMaterials : Tier {
    EMBER_ALLOY() {
        override fun getUses(): Int = 256

        override fun getSpeed(): Float = 5f

        override fun getAttackDamageBonus(): Float = 2f

        override fun getIncorrectBlocksForDrops(): TagKey<Block> = BlockTags.INCORRECT_FOR_IRON_TOOL

        override fun getEnchantmentValue(): Int = 15

        override fun getRepairIngredient(): Ingredient = HTTagPrefix.INGOT.createIngredient(RagiumMaterials.EMBER_ALLOY)
    },
    STEEL() {
        override fun getUses(): Int = 512

        override fun getSpeed(): Float = 7f

        override fun getAttackDamageBonus(): Float = 2.5f

        override fun getIncorrectBlocksForDrops(): TagKey<Block> = BlockTags.INCORRECT_FOR_DIAMOND_TOOL

        override fun getEnchantmentValue(): Int = 12

        override fun getRepairIngredient(): Ingredient = HTTagPrefix.INGOT.createIngredient(CommonMaterials.STEEL)
    },
}
