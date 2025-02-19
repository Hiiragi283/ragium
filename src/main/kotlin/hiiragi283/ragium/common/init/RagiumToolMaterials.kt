package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.api.material.keys.CommonMaterials
import net.minecraft.tags.BlockTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Tier
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.block.Block

enum class RagiumToolMaterials : Tier {
    STEEL() {
        override fun getUses(): Int = 512

        override fun getSpeed(): Float = 7f

        override fun getAttackDamageBonus(): Float = 2.5f

        override fun getIncorrectBlocksForDrops(): TagKey<Block> = BlockTags.INCORRECT_FOR_DIAMOND_TOOL

        override fun getEnchantmentValue(): Int = 12

        override fun getRepairIngredient(): Ingredient = HTTagPrefix.INGOT.createIngredient(CommonMaterials.STEEL)
    },
}
