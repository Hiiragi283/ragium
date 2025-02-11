package hiiragi283.ragium.api.extension

import net.minecraft.core.component.DataComponentPatch
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.item.ItemStack
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

inline fun buildCompPatch(builderAction: DataComponentPatch.Builder.() -> Unit): DataComponentPatch =
    DataComponentPatch.builder().apply(builderAction).build()
