package hiiragi283.ragium.setup

import hiiragi283.ragium.api.item.component.HTFoodBuilder
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.food.FoodConstants
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.food.Foods
import net.minecraft.world.item.Items

object RagiumFoods {
    //    Ragium    //

    @JvmStatic
    val AMBROSIA: FoodProperties = HTFoodBuilder.create {
        nutrition = FoodConstants.MAX_FOOD
        saturation = 0.5f
        alwaysEat = true
    }

    @JvmField
    val CANNED_COOKED_MEAT: FoodProperties = HTFoodBuilder.copyOf(Foods.COOKED_BEEF) {
        fastFood()
        convertTo(Items.IRON_NUGGET)
    }

    @JvmField
    val CHOCOLATE: FoodProperties = HTFoodBuilder.copyOf(Foods.APPLE) {
        alwaysEat = true
        fastFood()
        addEffect(MobEffects.DAMAGE_BOOST, 10 * 20, 0)
    }

    @JvmStatic
    val FEVER_CHERRY: FoodProperties = HTFoodBuilder.create {
        nutrition = FoodConstants.MAX_FOOD
        alwaysEat = true
        eatSeconds = 3f
        addEffect(MobEffects.HEALTH_BOOST, 60 * 20, 4)
        addEffect(MobEffects.REGENERATION, 10 * 20, 4)
    }

    @JvmField
    val ICE_CREAM: FoodProperties = HTFoodBuilder.copyOf(Foods.APPLE) {
        alwaysEat = true
    }

    @JvmField
    val MELON_PIE: FoodProperties = HTFoodBuilder.copyOf(Foods.PUMPKIN_PIE) {
        convertTo(Items.MELON_SEEDS)
    }

    @JvmStatic
    val RAGI_CHERRY: FoodProperties = HTFoodBuilder.copyOf(Foods.SWEET_BERRIES) {
        alwaysEat = true
        addEffect(MobEffects.HEALTH_BOOST, 30 * 20, 0)
    }

    @JvmStatic
    val RAGI_CHERRY_PULP: FoodProperties = HTFoodBuilder.copyOf(RAGI_CHERRY) {
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
    val RAGI_CHERRY_PIE_SLICE: FoodProperties = HTFoodBuilder.copyOf(RAGI_CHERRY) {
        nutrition = 3
        saturation = FoodConstants.FOOD_SATURATION_LOW
        fastFood()
    }

    @JvmField
    val SWEET_BERRIES_CAKE: FoodProperties = HTFoodBuilder.create {
        nutrition = 2
        saturation = FoodConstants.FOOD_SATURATION_POOR
    }

    @JvmStatic
    val WARPED_WART: FoodProperties = HTFoodBuilder.create {
        alwaysEat = true
    }

    //    Mekanism    //

    @JvmField
    val YELLOW_CAKE: FoodProperties = HTFoodBuilder.create {
        alwaysEat = true
        addInfinityEffect(MobEffects.WITHER, 1)
    }
}
