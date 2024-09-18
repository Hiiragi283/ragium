package hiiragi283.ragium.common.init

import net.minecraft.block.Block
import net.minecraft.component.type.AttributeModifierSlot
import net.minecraft.component.type.AttributeModifiersComponent
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.item.Item
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
    private val repairment: () -> Ingredient,
) : ToolMaterial {
    STEEL(BlockTags.INCORRECT_FOR_IRON_TOOL, 750, 8.0f, 3.0f, 14, { Ingredient.fromTag(RagiumItemTags.STEEL_INGOTS) }),
    ;

    companion object {
        @JvmStatic
        fun createAttributeComponent(material: ToolMaterial, baseAttack: Double, attackSpeed: Double): AttributeModifiersComponent =
            AttributeModifiersComponent
                .builder()
                .add(
                    EntityAttributes.GENERIC_ATTACK_DAMAGE,
                    EntityAttributeModifier(
                        Item.BASE_ATTACK_DAMAGE_MODIFIER_ID,
                        baseAttack + material.attackDamage,
                        EntityAttributeModifier.Operation.ADD_VALUE,
                    ),
                    AttributeModifierSlot.MAINHAND,
                ).add(
                    EntityAttributes.GENERIC_ATTACK_SPEED,
                    EntityAttributeModifier(
                        Item.BASE_ATTACK_SPEED_MODIFIER_ID,
                        attackSpeed,
                        EntityAttributeModifier.Operation.ADD_VALUE,
                    ),
                    AttributeModifierSlot.MAINHAND,
                ).build()
    }

    override fun getDurability(): Int = durability

    override fun getMiningSpeedMultiplier(): Float = miningSpeed

    override fun getAttackDamage(): Float = attackDamage

    override fun getEnchantability(): Int = enchantability

    override fun getRepairIngredient(): Ingredient = repairment()

    override fun getInverseTag(): TagKey<Block> = inverseTag
}
