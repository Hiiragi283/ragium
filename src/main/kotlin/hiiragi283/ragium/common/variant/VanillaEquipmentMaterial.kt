package hiiragi283.ragium.common.variant

import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.variant.HTEquipmentMaterial
import hiiragi283.ragium.common.material.VanillaMaterialKeys
import net.minecraft.core.Holder
import net.minecraft.sounds.SoundEvent
import net.minecraft.tags.TagKey
import net.minecraft.world.item.ArmorItem
import net.minecraft.world.item.ArmorMaterial
import net.minecraft.world.item.ArmorMaterials
import net.minecraft.world.item.Tier
import net.minecraft.world.item.Tiers
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.block.Block

enum class VanillaEquipmentMaterial(
    private val key: HTMaterialKey,
    private val tier: Tier,
    private val axeDamage: Float,
    private val axeAttackSpeed: Float,
    private val armor: Holder<ArmorMaterial>,
    private val armorMultiplier: Int,
) : HTEquipmentMaterial {
    IRON(VanillaMaterialKeys.IRON, Tiers.IRON, 6f, -3.1f, ArmorMaterials.IRON, 15),
    GOLD(VanillaMaterialKeys.GOLD, Tiers.GOLD, 6f, -3f, ArmorMaterials.GOLD, 7),
    DIAMOND(VanillaMaterialKeys.DIAMOND, Tiers.DIAMOND, 5f, -3f, ArmorMaterials.DIAMOND, 33),
    NETHERITE(VanillaMaterialKeys.NETHERITE, Tiers.NETHERITE, 5f, -3f, ArmorMaterials.NETHERITE, 37),
    ;

    override fun getAxeDamage(): Float = axeDamage

    override fun getAxeAttackSpeed(): Float = axeAttackSpeed

    override fun getToughness(): Float = armor.value().toughness

    override fun getKnockbackResistance(): Float = armor.value().knockbackResistance

    override fun getEquipSound(): Holder<SoundEvent> = armor.value().equipSound

    override fun getArmorDefence(type: ArmorItem.Type): Int = armor.value().getDefense(type)

    override fun getArmorMultiplier(): Int = armorMultiplier

    override fun getUses(): Int = tier.uses

    override fun getSpeed(): Float = tier.speed

    override fun getAttackDamageBonus(): Float = tier.attackDamageBonus

    override fun getIncorrectBlocksForDrops(): TagKey<Block> = tier.incorrectBlocksForDrops

    override fun getEnchantmentValue(): Int = tier.enchantmentValue

    override fun getRepairIngredient(): Ingredient = tier.repairIngredient

    override fun asMaterialKey(): HTMaterialKey = key
}
