package hiiragi283.ragium.common.variant

import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialPrefix
import hiiragi283.ragium.api.variant.HTEquipmentMaterial
import hiiragi283.ragium.setup.CommonMaterialPrefixes
import net.minecraft.core.Holder
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.tags.TagKey
import net.minecraft.world.item.ArmorItem
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.block.Block

@JvmRecord
data class HTBasicEquipmentMaterial(
    private val key: HTMaterialKey,
    private val axeDamage: Float,
    private val axeAttackSpeed: Float,
    private val durability: Int,
    private val miningSpeed: Float,
    private val baseAttackDamage: Float,
    private val enchantability: Int,
    private val incorrectBlockTag: TagKey<Block>,
    private val defenceMap: Map<ArmorItem.Type, Int>,
    private val armorMultiplier: Int,
    private val repairPrefix: HTMaterialPrefix = CommonMaterialPrefixes.INGOT,
    private val equipSound: Holder<SoundEvent> = SoundEvents.ARMOR_EQUIP_GENERIC,
    private val toughness: Float = 0f,
    private val resistance: Float = 0f,
) : HTEquipmentMaterial {
    override fun getAxeDamage(): Float = axeDamage

    override fun getAxeAttackSpeed(): Float = axeAttackSpeed

    override fun getToughness(): Float = toughness

    override fun getKnockbackResistance(): Float = resistance

    override fun getEquipSound(): Holder<SoundEvent> = equipSound

    override fun getArmorDefence(type: ArmorItem.Type): Int = defenceMap.getOrDefault(type, 0)

    override fun getArmorMultiplier(): Int = armorMultiplier

    //    Tier    //

    override fun getUses(): Int = durability

    override fun getSpeed(): Float = miningSpeed

    override fun getAttackDamageBonus(): Float = baseAttackDamage

    override fun getIncorrectBlocksForDrops(): TagKey<Block> = incorrectBlockTag

    override fun getEnchantmentValue(): Int = enchantability

    override fun getRepairIngredient(): Ingredient = repairPrefix.toIngredient(key)

    //    HTMaterialLike    //

    override fun asMaterialKey(): HTMaterialKey = key
}
