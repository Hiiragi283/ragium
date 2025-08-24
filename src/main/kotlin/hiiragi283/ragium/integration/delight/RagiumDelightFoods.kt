package hiiragi283.ragium.integration.delight

import hiiragi283.ragium.api.item.HTFoodBuilder
import hiiragi283.ragium.setup.RagiumFoods
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
        addEffect(MobEffects.HEALTH_BOOST, 60 * 20, 1)
    }

    @JvmStatic
    val RAGI_CHERRY_PIE_SLICE: FoodProperties = HTFoodBuilder.create {
        nutrition = 3
        saturation = FoodConstants.FOOD_SATURATION_LOW
        alwaysEat = true
        fastFood()
        addEffect(MobEffects.HEALTH_BOOST, 30 * 20, 0)
    }
}
