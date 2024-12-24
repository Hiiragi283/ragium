package hiiragi283.ragium.common.init

import net.minecraft.component.type.FoodComponent
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.item.Items

object RagiumFoodComponents {
    @JvmField
    val SWEET_BERRIES_CAKE: FoodComponent = FoodComponent
        .Builder()
        .nutrition(2)
        .saturationModifier(0.1f)
        .build()

    @JvmField
    val MELON_PIE: FoodComponent = FoodComponent
        .Builder()
        .nutrition(8)
        .saturationModifier(0.3f)
        .usingConvertsTo(Items.MELON_SEEDS)
        .build()

    @JvmField
    val CHOCOLATE: FoodComponent = FoodComponent
        .Builder()
        .nutrition(3)
        .saturationModifier(0.3f)
        .statusEffect(
            StatusEffectInstance(
                StatusEffects.STRENGTH,
                10 * 20,
                0,
            ),
            1.0f,
        ).snack()
        .alwaysEdible()
        .build()

    @JvmField
    val CANNED_COOKED_MEAT: FoodComponent = FoodComponent
        .Builder()
        .nutrition(8)
        .saturationModifier(0.8f)
        .snack()
        .usingConvertsTo(Items.IRON_NUGGET)
        .build()

    @JvmField
    val YELLOW_CAKE_PIECE: FoodComponent = FoodComponent
        .Builder()
        .statusEffect(StatusEffectInstance(StatusEffects.WITHER, -1, 1), 1f)
        .build()
}
