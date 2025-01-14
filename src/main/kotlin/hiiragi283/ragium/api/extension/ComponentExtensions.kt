package hiiragi283.ragium.api.extension

import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialProvider
import hiiragi283.ragium.api.material.HTTagPrefix
import hiiragi283.ragium.common.init.RagiumComponentTypes
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.registries.DeferredHolder
import java.util.*
import java.util.function.Supplier

/**
 * 指定した値から[FoodProperties]を返します。
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
    effects: Map<Supplier<MobEffectInstance>, Float> = mapOf(),
): FoodProperties = FoodProperties(
    nutrition,
    saturation,
    alwaysEat,
    eatSeconds,
    Optional.ofNullable(convertTo),
    effects.map { (effect: Supplier<MobEffectInstance>, chance: Float) ->
        FoodProperties.PossibleEffect(effect, chance)
    },
)

//    DataComponentPatch.Builder    //

fun <T : Any> DataComponentPatch.Builder.set(
    type: DeferredHolder<DataComponentType<*>, DataComponentType<T>>,
    value: T,
): DataComponentPatch.Builder = set(type.get(), value)

/**
 * 指定した[tier]とそのレアリティを設定します。
 */
fun DataComponentPatch.Builder.tier(tier: HTMachineTier): DataComponentPatch.Builder = this
    .set(RagiumComponentTypes.MACHINE_TIER, tier)

/**
 * 指定した[text]をアイテムの名前に設定します。
 */
fun DataComponentPatch.Builder.name(text: Component): DataComponentPatch.Builder = set(DataComponents.ITEM_NAME, text)

/**
 * 指定した[prefix]と[material]，そしてその名前を設定します。
 */
fun DataComponentPatch.Builder.material(prefix: HTTagPrefix, material: HTMaterialKey): DataComponentPatch.Builder = this
    .set(RagiumComponentTypes.TAG_PREFIX, prefix)
    .set(RagiumComponentTypes.MATERIAL, material)
    .name(prefix.createText(material))

fun DataComponentPatch.Builder.material(provider: HTMaterialProvider): DataComponentPatch.Builder =
    material(provider.tagPrefix, provider.material)

/**
 * 指定した[translationKey]と[tier]をアイテムの名前として設定します。
 */
fun DataComponentPatch.Builder.tieredText(translationKey: String, tier: HTMachineTier): DataComponentPatch.Builder =
    name(tier.createPrefixedText(translationKey)).tier(tier)
