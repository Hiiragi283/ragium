package hiiragi283.ragium.common.item

import hiiragi283.ragium.api.extension.createAttributeComponent
import hiiragi283.ragium.api.extension.itemSettings
import hiiragi283.ragium.common.init.RagiumToolMaterials
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.item.Equipment
import net.minecraft.item.ItemStack
import net.minecraft.item.SwordItem
import net.minecraft.util.Rarity

object HTBujinItem : SwordItem(
    RagiumToolMaterials.STEEL,
    itemSettings().rarity(Rarity.EPIC)
        .attributeModifiers(createAttributeComponent(RagiumToolMaterials.STEEL, 6.0, 0.0))
), Equipment {
    override fun postHit(stack: ItemStack, target: LivingEntity, attacker: LivingEntity): Boolean {
        target.damage(attacker.world.damageSources.magic(), 4.0f)
        return super.postHit(stack, target, attacker)
    }

    //    Equipment    //

    override fun getSlotType(): EquipmentSlot = EquipmentSlot.HEAD
}
