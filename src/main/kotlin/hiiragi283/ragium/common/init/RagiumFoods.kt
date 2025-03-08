package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.extension.foodComponent
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.item.Items
import thedarkcolour.kotlinforforge.neoforge.kotlin.supply

object RagiumFoods {
    @JvmField
    val SWEET_BERRIES_CAKE: FoodProperties = foodComponent(
        nutrition = 2,
        saturation = 0.1f,
    )

    @JvmField
    val MELON_PIE: FoodProperties = foodComponent(
        nutrition = 8,
        saturation = 0.3f,
        convertTo = Items.MELON_SEEDS.defaultInstance,
    )

    @JvmField
    val CHOCOLATE: FoodProperties = foodComponent(
        nutrition = 3,
        saturation = 0.3f,
        alwaysEat = true,
        eatSeconds = 0.8f,
        effects = mapOf(
            supply(MobEffectInstance(MobEffects.DAMAGE_BOOST, 10 * 20, 0)) to 1f,
        ),
    )

    @JvmField
    val MEAT_SANDWICH: FoodProperties = foodComponent(
        nutrition = 14,
        saturation = 0.8f,
        eatSeconds = 1.2f,
        effects = mapOf(
            supply(MobEffectInstance(MobEffects.SATURATION, 10 * 30, 0)) to 1f,
        ),
    )

    @JvmField
    val CANNED_COOKED_MEAT: FoodProperties = foodComponent(
        nutrition = 8,
        saturation = 0.8f,
        eatSeconds = 0.8f,
        convertTo = Items.IRON_NUGGET.defaultInstance,
    )

    @JvmField
    val YELLOW_CAKE_PIECE: FoodProperties = foodComponent(
        effects = mapOf(supply(MobEffectInstance(MobEffects.WITHER, -1, 1)) to 1f),
    )

    @JvmStatic
    val WARPED_WART: FoodProperties = foodComponent(alwaysEat = true)

    @JvmStatic
    val AMBROSIA: FoodProperties = foodComponent(
        nutrition = 20,
        saturation = 20f,
        alwaysEat = true,
    )
}
