package hiiragi283.ragium.common.item.armor

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.init.RagiumArmorMaterials
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.item.ArmorItem
import net.minecraft.world.item.component.ItemAttributeModifiers

class HTSlimeBootsItem(properties: Properties) :
    ArmorItem(
        RagiumArmorMaterials.DEFAULT,
        Type.BOOTS,
        properties
            .attributes(ATTRIBUTE),
    ) {
    companion object {
        @JvmStatic
        private val ATTRIBUTE: ItemAttributeModifiers = ItemAttributeModifiers
            .builder()
            .add(
                Attributes.FALL_DAMAGE_MULTIPLIER,
                AttributeModifier(
                    RagiumAPI.id("slime_boots_fall"),
                    -100.0,
                    AttributeModifier.Operation.ADD_VALUE,
                ),
                EquipmentSlotGroup.FEET,
            ).add(
                Attributes.JUMP_STRENGTH,
                AttributeModifier(
                    RagiumAPI.id("slime_boots_jump"),
                    1.0,
                    AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL,
                ),
                EquipmentSlotGroup.FEET,
            ).build()
    }
}
