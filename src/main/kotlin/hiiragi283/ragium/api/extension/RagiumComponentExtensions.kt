package hiiragi283.ragium.api.extension

import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.machine.HTMachineType
import net.minecraft.component.Component
import net.minecraft.component.ComponentHolder
import net.minecraft.component.ComponentMap
import net.minecraft.component.type.AttributeModifierSlot
import net.minecraft.component.type.AttributeModifiersComponent
import net.minecraft.component.type.FoodComponent
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.ToolMaterial
import java.util.*
import java.util.stream.Collectors

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

val ComponentHolder.machineType: HTMachineType
    get() = getOrDefault(HTMachineType.COMPONENT_TYPE, HTMachineType.DEFAULT)

val ComponentMap.machineType: HTMachineType
    get() = getOrDefault(HTMachineType.COMPONENT_TYPE, HTMachineType.DEFAULT)

val ComponentHolder.machineTypeOrNull: HTMachineType?
    get() = get(HTMachineType.COMPONENT_TYPE)

val ComponentMap.machineTypeOrNull: HTMachineType?
    get() = get(HTMachineType.COMPONENT_TYPE)

val ComponentHolder.machineTier: HTMachineTier
    get() = getOrDefault(HTMachineTier.COMPONENT_TYPE, HTMachineTier.PRIMITIVE)

val ComponentMap.machineTier: HTMachineTier
    get() = getOrDefault(HTMachineTier.COMPONENT_TYPE, HTMachineTier.PRIMITIVE)

val ComponentHolder.machineTierOrNull: HTMachineTier?
    get() = get(HTMachineTier.COMPONENT_TYPE)

val ComponentMap.machineTierOrNull: HTMachineTier?
    get() = get(HTMachineTier.COMPONENT_TYPE)

fun ComponentMap.asString(): String = "{${stream().map(Component<*>::toString).collect(Collectors.joining(", "))}}"
