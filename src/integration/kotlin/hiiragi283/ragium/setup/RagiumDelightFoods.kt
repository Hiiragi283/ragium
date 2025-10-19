package hiiragi283.ragium.setup

import hiiragi283.ragium.api.item.component.HTFoodBuilder
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.food.FoodConstants
import net.minecraft.world.food.FoodProperties

object RagiumDelightFoods {
    @JvmStatic
    val RAGI_CHERRY_PULP: FoodProperties = HTFoodBuilder.copyOf(RagiumFoods.RAGI_CHERRY) {
        nutrition = 1
        saturation = FoodConstants.FOOD_SATURATION_POOR
    }

    @JvmStatic
    val RAGI_CHERRY_JAM: FoodProperties = HTFoodBuilder.create {
        nutrition = 3
        saturation = FoodConstants.FOOD_SATURATION_NORMAL
        alwaysEat = true
        addEffect(MobEffects.HEALTH_BOOST, 15 * 20, 1)
    }

    @JvmStatic
    val RAGI_CHERRY_PIE_SLICE: FoodProperties = HTFoodBuilder.copyOf(RagiumFoods.RAGI_CHERRY) {
        nutrition = 3
        saturation = FoodConstants.FOOD_SATURATION_LOW
        fastFood()
    }
}
