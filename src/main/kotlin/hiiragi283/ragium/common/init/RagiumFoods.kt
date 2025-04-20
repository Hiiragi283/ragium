package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.extension.foodComponent
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.item.Items
import thedarkcolour.kotlinforforge.neoforge.kotlin.supply

object RagiumFoods {
    //    Ragium    //

    @JvmStatic
    val AMBROSIA: FoodProperties = foodComponent(
        nutrition = 20,
        alwaysEat = true,
    )

    @JvmField
    val CANNED_COOKED_MEAT: FoodProperties = foodComponent(
        nutrition = 8,
        saturation = 0.8f,
        eatSeconds = 0.8f,
        convertTo = Items.IRON_NUGGET.defaultInstance,
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

    @JvmStatic
    val FEVER_CHERRY: FoodProperties = foodComponent(
        nutrition = 20,
        effects = mapOf(
            supply(MobEffectInstance(MobEffects.HEALTH_BOOST, 10 * 20, 6)) to 1f,
            supply(MobEffectInstance(MobEffects.REGENERATION, 10 * 20, 6)) to 1f,
        ),
        alwaysEat = true,
    )

    @JvmField
    val ICE_CREAM: FoodProperties = foodComponent(
        nutrition = 4,
        saturation = 0.3f,
        alwaysEat = true,
    )

    @JvmField
    val MELON_PIE: FoodProperties = foodComponent(
        nutrition = 8,
        saturation = 0.3f,
        convertTo = Items.MELON_SEEDS.defaultInstance,
    )

    @JvmStatic
    val RAGI_CHERRY: FoodProperties = foodComponent(
        nutrition = 2,
        saturation = 0.1f,
        effects = mapOf(supply(MobEffectInstance(MobEffects.HEALTH_BOOST, 60 * 20, 0)) to 1f),
        alwaysEat = true,
    )

    @JvmField
    val SWEET_BERRIES_CAKE: FoodProperties = foodComponent(
        nutrition = 2,
        saturation = 0.1f,
    )

    @JvmStatic
    val WARPED_WART: FoodProperties = foodComponent(alwaysEat = true)

    //    Mekanism    //

    @JvmField
    val YELLOW_CAKE: FoodProperties = foodComponent(
        alwaysEat = true,
        effects = mapOf(supply(MobEffectInstance(MobEffects.WITHER, -1, 1)) to 1f),
    )

    //    Delight    //

    @JvmStatic
    val RAGI_CHERRY_JAM: FoodProperties = foodComponent(
        nutrition = 3,
        saturation = 0.6f,
        effects = mapOf(supply(MobEffectInstance(MobEffects.HEALTH_BOOST, 60 * 20, 1)) to 1f),
        alwaysEat = true,
    )

    @JvmStatic
    val RAGI_CHERRY_POPSICLE: FoodProperties = foodComponent(
        nutrition = 3,
        saturation = 0.2f,
        effects = mapOf(supply(MobEffectInstance(MobEffects.HEALTH_BOOST, 60 * 20, 0)) to 1f),
        alwaysEat = true,
    )

    @JvmStatic
    val RAGI_CHERRY_PULP: FoodProperties = foodComponent(
        nutrition = 1,
        saturation = 0.1f,
        effects = mapOf(supply(MobEffectInstance(MobEffects.HEALTH_BOOST, 30 * 20, 0)) to 1f),
        alwaysEat = true,
    )
}
