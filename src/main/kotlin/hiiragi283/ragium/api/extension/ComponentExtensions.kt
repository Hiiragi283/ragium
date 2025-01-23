package hiiragi283.ragium.api.extension

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
 * 指定した[text]をアイテムの名前に設定します。
 */
fun DataComponentPatch.Builder.name(text: Component): DataComponentPatch.Builder = set(DataComponents.ITEM_NAME, text)
