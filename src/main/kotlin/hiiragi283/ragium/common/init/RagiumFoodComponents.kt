package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.extension.foodComponent
import net.minecraft.component.type.FoodComponent
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.item.Items

object RagiumFoodComponents {
    @JvmField
    val SWEET_BERRIES_CAKE: FoodComponent = foodComponent(
        nutrition = 2,
        saturation = 0.1f,
    )

    @JvmField
    val MELON_PIE: FoodComponent = foodComponent(
        nutrition = 8,
        saturation = 0.3f,
        convertTo = Items.MELON_SEEDS.defaultStack,
    )

    @JvmField
    val CHOCOLATE: FoodComponent = foodComponent(
        nutrition = 3,
        saturation = 0.3f,
        alwaysEat = true,
        eatSeconds = 0.8f,
        effects = mapOf(
            StatusEffectInstance(StatusEffects.STRENGTH, 10 * 20, 0) to 1f,
        ),
    )

    @JvmField
    val CANNED_COOKED_MEAT: FoodComponent = foodComponent(
        nutrition = 8,
        saturation = 0.8f,
        eatSeconds = 0.8f,
        convertTo = Items.IRON_NUGGET.defaultStack,
    )

    @JvmField
    val YELLOW_CAKE_PIECE: FoodComponent = foodComponent(
        effects = mapOf(StatusEffectInstance(StatusEffects.WITHER, -1, 1) to 1f),
    )

    @JvmStatic
    val AMBROSIA: FoodComponent = foodComponent(
        nutrition = 20,
        saturation = 20f,
        alwaysEat = true,
    )
}
