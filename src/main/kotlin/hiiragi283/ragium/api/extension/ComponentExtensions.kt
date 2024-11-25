package hiiragi283.ragium.api.extension

import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineTier
import net.minecraft.component.Component
import net.minecraft.component.ComponentHolder
import net.minecraft.component.ComponentMap
import net.minecraft.component.type.AttributeModifierSlot
import net.minecraft.component.type.AttributeModifiersComponent
import net.minecraft.component.type.FoodComponent
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.item.*
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.util.Identifier
import java.util.*
import java.util.stream.Collectors

fun createArmorAttribute(material: RegistryEntry<ArmorMaterial>, type: ArmorItem.Type): AttributeModifiersComponent.Builder {
    val protection: Int = material.value().getProtection(type)
    val toughness: Float = material.value().toughness
    val kResistance: Float = material.value().knockbackResistance
    val builder: AttributeModifiersComponent.Builder = AttributeModifiersComponent.builder()
    val slot: AttributeModifierSlot = AttributeModifierSlot.forEquipmentSlot(type.equipmentSlot)
    val id: Identifier = Identifier.of("armor.${type.getName()}")
    builder.add(
        EntityAttributes.GENERIC_ARMOR,
        EntityAttributeModifier(
            id,
            protection.toDouble(),
            EntityAttributeModifier.Operation.ADD_VALUE,
        ),
        slot,
    )
    builder.add(
        EntityAttributes.GENERIC_ARMOR_TOUGHNESS,
        EntityAttributeModifier(
            id,
            toughness.toDouble(),
            EntityAttributeModifier.Operation.ADD_VALUE,
        ),
        slot,
    )
    if (kResistance > 0f) {
        builder.add(
            EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE,
            EntityAttributeModifier(
                id,
                kResistance.toDouble(),
                EntityAttributeModifier.Operation.ADD_VALUE,
            ),
            slot,
        )
    }
    return builder
}

fun createToolAttribute(material: ToolMaterial, baseAttack: Double, attackSpeed: Double): AttributeModifiersComponent.Builder =
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
        )

fun foodComponent(
    nutrition: Int,
    saturation: Float,
    alwaysEat: Boolean = false,
    eatSeconds: Float = 1.0f,
    convertTo: ItemStack? = null,
    effects: Map<StatusEffectInstance, Float> = mapOf(),
): FoodComponent = FoodComponent(
    nutrition,
    saturation,
    alwaysEat,
    eatSeconds,
    Optional.ofNullable(convertTo),
    effects.map { (effect: StatusEffectInstance, chance: Float) ->
        FoodComponent.StatusEffectEntry(effect, chance)
    },
)

val ComponentHolder.machineKeyOrNull: HTMachineKey?
    get() = get(HTMachineKey.COMPONENT_TYPE)

val ComponentHolder.machineTier: HTMachineTier
    get() = getOrDefault(HTMachineTier.COMPONENT_TYPE, HTMachineTier.PRIMITIVE)

val ComponentMap.machineTier: HTMachineTier
    get() = getOrDefault(HTMachineTier.COMPONENT_TYPE, HTMachineTier.PRIMITIVE)

fun ComponentMap.asString(): String = "{${stream().map(Component<*>::toString).collect(Collectors.joining(", "))}}"
