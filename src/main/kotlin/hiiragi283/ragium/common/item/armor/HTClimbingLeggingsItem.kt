package hiiragi283.ragium.common.item.armor

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.init.RagiumArmorMaterials
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.item.ArmorItem
import net.minecraft.world.item.component.ItemAttributeModifiers

class HTClimbingLeggingsItem(properties: Properties) :
    ArmorItem(
        RagiumArmorMaterials.DEFAULT,
        Type.LEGGINGS,
        properties
            .attributes(ATTRIBUTE),
    ) {
    companion object {
        @JvmStatic
        private val ATTRIBUTE: ItemAttributeModifiers = ItemAttributeModifiers
            .builder()
            .add(
                Attributes.MOVEMENT_SPEED,
                AttributeModifier(
                    RagiumAPI.id("climbing_leggings_speed"),
                    0.1,
                    AttributeModifier.Operation.ADD_VALUE,
                ),
                EquipmentSlotGroup.LEGS,
            ).add(
                Attributes.STEP_HEIGHT,
                AttributeModifier(
                    RagiumAPI.id("climbing_leggings_step"),
                    1.0,
                    AttributeModifier.Operation.ADD_VALUE,
                ),
                EquipmentSlotGroup.LEGS,
            ).build()
    }
}
