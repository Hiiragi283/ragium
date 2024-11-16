package hiiragi283.ragium.common.init

import hiiragi283.ragium.common.RagiumContents
import net.minecraft.block.Block
import net.minecraft.item.ToolMaterial
import net.minecraft.item.ToolMaterials
import net.minecraft.recipe.Ingredient
import net.minecraft.registry.tag.TagKey

enum class RagiumToolMaterials(
    private val inverseTag: TagKey<Block>,
    private val durability: Int,
    private val miningSpeed: Float,
    private val attackDamage: Float,
    private val enchantability: Int,
    private val repairment: Ingredient,
) : ToolMaterial {
    STEEL(
        ToolMaterials.DIAMOND,
        Ingredient.fromTag(RagiumContents.Ingots.STEEL.prefixedTagKey),
    ),
    STELLA(
        ToolMaterials.NETHERITE,
        Ingredient.ofItems(RagiumItems.STELLA_PLATE),
    ),
    ;

    constructor(from: ToolMaterial, repairment: Ingredient) : this(
        from.inverseTag,
        from.durability,
        from.miningSpeedMultiplier,
        from.attackDamage,
        from.enchantability,
        repairment,
    )

    override fun getDurability(): Int = durability

    override fun getMiningSpeedMultiplier(): Float = miningSpeed

    override fun getAttackDamage(): Float = attackDamage

    override fun getEnchantability(): Int = enchantability

    override fun getRepairIngredient(): Ingredient = repairment

    override fun getInverseTag(): TagKey<Block> = inverseTag
}
