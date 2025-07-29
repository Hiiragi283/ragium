package hiiragi283.ragium.api.item

import com.google.common.base.Suppliers
import net.minecraft.core.Holder
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.food.FoodConstants
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import java.util.*
import java.util.function.Supplier
import kotlin.jvm.optionals.getOrNull

/**
 * 指定した値から[FoodProperties]を返します。
 * @param nutrition 満腹度
 * @param saturation 隠し満腹度
 * @param alwaysEat 常に食べられるかどうか
 * @param eatSeconds 食べ終わるまでの時間
 * @param convertTo 食べ終わった後に手に入る[ItemStack]
 * @param effects 食べた時の効果の一覧
 */
class HTFoodBuilder private constructor() {
    companion object {
        @JvmStatic
        fun create(builderAction: HTFoodBuilder.() -> Unit): FoodProperties = HTFoodBuilder().apply(builderAction).build()

        @JvmStatic
        fun copyOf(parent: FoodProperties, builderAction: HTFoodBuilder.() -> Unit): FoodProperties = create {
            nutrition = parent.nutrition
            saturation = parent.saturation / nutrition / 2f
            alwaysEat = parent.canAlwaysEat
            eatSeconds = parent.eatSeconds
            convertTo = parent.usingConvertsTo().getOrNull()
            effects.addAll(parent.effects)
            builderAction()
        }
    }

    var nutrition: Int = 0
    var saturation: Float = 1f
    var alwaysEat: Boolean = false
    var eatSeconds: Float = 1.6f
    var convertTo: ItemStack? = null
    private val effects: MutableList<FoodProperties.PossibleEffect> = mutableListOf()

    fun fastFood() {
        eatSeconds = 0.8f
    }

    fun convertTo(item: ItemLike, count: Int = 1) {
        convertTo = ItemStack(item, count)
    }

    fun addEffect(effect: Supplier<MobEffectInstance>, chance: Float = 1f) {
        effects.add(FoodProperties.PossibleEffect(effect, chance))
    }

    fun addEffect(effect: MobEffectInstance, chance: Float = 1f) {
        addEffect(Suppliers.ofInstance(effect), chance)
    }

    fun addEffect(
        effect: Holder<MobEffect>,
        ticks: Int,
        amplifier: Int,
        chance: Float = 1f,
    ) {
        addEffect(MobEffectInstance(effect, ticks, amplifier), chance)
    }

    fun addInfinityEffect(effect: Holder<MobEffect>, amplifier: Int, chance: Float = 1f) {
        addEffect(effect, -1, amplifier, chance)
    }

    private fun build(): FoodProperties = FoodProperties(
        nutrition,
        FoodConstants.saturationByModifier(nutrition, saturation),
        alwaysEat,
        eatSeconds,
        Optional.ofNullable(convertTo),
        effects,
    )
}
