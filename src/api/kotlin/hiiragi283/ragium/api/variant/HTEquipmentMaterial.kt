package hiiragi283.ragium.api.variant

import hiiragi283.ragium.api.material.HTMaterialLike
import net.minecraft.core.Holder
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.item.ArmorItem
import net.minecraft.world.item.Tier

/**
 * @see mekanism.tools.common.material.BaseMekanismMaterial
 */
interface HTEquipmentMaterial :
    Tier,
    HTMaterialLike {
    fun getSwordDamage(): Float = 3f

    fun getSwordAttackSpeed(): Float = -2.4f

    fun getShovelDamage(): Float = 1.5f

    fun getShovelAttackSpeed(): Float = -3f

    fun getAxeDamage(): Float

    fun getAxeAttackSpeed(): Float

    fun getPickaxeDamage(): Float = 1f

    fun getPickaxeAttackSpeed(): Float = -2.8f

    fun getHoeDamage(): Float = -attackDamageBonus

    fun getHoeAttackSpeed(): Float = attackDamageBonus - 3f

    //    Armor    //

    fun getToughness(): Float

    fun getKnockbackResistance(): Float

    fun getEquipSound(): Holder<SoundEvent>

    fun getArmorDefence(type: ArmorItem.Type): Int

    fun getArmorMultiplier(): Int
}
