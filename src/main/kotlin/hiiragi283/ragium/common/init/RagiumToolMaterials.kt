package hiiragi283.ragium.common.init

import hiiragi283.ragium.common.RagiumContents
import net.minecraft.block.Block
import net.minecraft.item.ToolMaterial
import net.minecraft.recipe.Ingredient
import net.minecraft.registry.tag.BlockTags
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
        BlockTags.INCORRECT_FOR_IRON_TOOL,
        750,
        8.0f,
        3.0f,
        14,
        Ingredient.fromTag(RagiumContents.Ingots.STEEL.prefixedTagKey),
    ),
    ;

    constructor(from: ToolMaterial) : this(
        from.inverseTag,
        from.durability,
        from.miningSpeedMultiplier,
        from.attackDamage,
        from.enchantability,
        from.repairIngredient,
    )

    override fun getDurability(): Int = durability

    override fun getMiningSpeedMultiplier(): Float = miningSpeed

    override fun getAttackDamage(): Float = attackDamage

    override fun getEnchantability(): Int = enchantability

    override fun getRepairIngredient(): Ingredient = repairment

    override fun getInverseTag(): TagKey<Block> = inverseTag
}
