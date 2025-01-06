package hiiragi283.ragium.api.extension

import hiiragi283.ragium.api.machine.HTMachineTier
import net.minecraft.component.Component
import net.minecraft.component.ComponentHolder
import net.minecraft.component.ComponentMap
import net.minecraft.component.ComponentType
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

/**
 * 指定した[material]と[type]から[AttributeModifiersComponent.Builder]を返します。
 * @param material 防具の素材
 * @param type 防具の種類
 */
@Suppress("UsePropertyAccessSyntax")
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

/**
 * 指定した[material], [baseAttack], [attackSpeed]から[AttributeModifiersComponent.Builder]を返します。
 * @param material 道具の素材
 * @param baseAttack 基礎攻撃
 * @param attackSpeed 攻撃速度
 */
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

/**
 * 指定した値から[FoodComponent]を返します。
 * @param nutrition 満腹度
 * @param saturation 隠し満腹度
 * @param alwaysEat 常に食べられるかどうか
 * @param eatSeconds 食べ終わるまでの時間
 * @param convertTo 食べ終わった後に手に入る[ItemStack]
 * @param effects 食べた時の効果の一覧
 */
fun foodComponent(
    nutrition: Int = 0,
    saturation: Float = 0f,
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

//    ComponentHolder    //

/**
 * [HTMachineTier]を返します。
 */
val ComponentHolder.machineTier: HTMachineTier
    get() = getOrDefault(HTMachineTier.COMPONENT_TYPE, HTMachineTier.PRIMITIVE)

/**
 * 指定した[type]に紐づいた値を[action]で変換します。
 * @param T コンポーネントのクラス
 * @param R 戻り値のクラス
 * @return [ComponentHolder.get]がnullの場合はnull
 */
fun <T : Any, R : Any> ComponentHolder.ifPresent(type: ComponentType<T>, action: (T) -> R?): R? = get(type)?.let(action)

/**
 * 指定した[type]に紐づいた値を[action]で変換します。
 * @param T コンポーネントのクラス
 * @param R 戻り値のクラス
 * @return [ComponentHolder.get]がnullの場合は[defaultValue]
 **/
fun <T : Any, R : Any> ComponentHolder.ifPresent(type: ComponentType<T>, defaultValue: R, action: (T) -> R?): R =
    ifPresent(type, action) ?: defaultValue

//    ComponentMap    //

fun ComponentMap.asString(): String = "{${stream().map(Component<*>::toString).collect(Collectors.joining(", "))}}"

/**
 * 指定した[type]に紐づいた値を[action]で変換します。
 * @param T コンポーネントのクラス
 * @param R 戻り値のクラス
 * @return [ComponentMap.get]がnullの場合はnull
 */
fun <T : Any, R : Any> ComponentMap.ifPresent(type: ComponentType<T>, action: (T) -> R?): R? = get(type)?.let(action)

/**
 * 指定した[type]に紐づいた値を[action]で変換します。
 * @param T コンポーネントのクラス
 * @param R 戻り値のクラス
 * @return [ComponentMap.get]がnullの場合は[defaultValue]
 **/
fun <T : Any, R : Any> ComponentMap.ifPresent(type: ComponentType<T>, defaultValue: R, action: (T) -> R?): R =
    ifPresent(type, action) ?: defaultValue
