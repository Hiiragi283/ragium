package hiiragi283.ragium.common.recipe

import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.recipe.result.HTComplexResult
import hiiragi283.core.api.world.getTypedBlockEntity
import hiiragi283.ragium.common.block.entity.HTImitationSpawnerBlockEntity
import hiiragi283.ragium.common.recipe.base.HTSimulatingRecipe
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import net.minecraft.core.HolderSet
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.level.Level
import org.apache.commons.lang3.math.Fraction
import java.util.Optional

class HTEntitySimulatingRecipe(
    ingredient: Optional<HTItemIngredient>,
    catalyst: HolderSet<EntityType<*>>,
    result: HTComplexResult,
    time: Int,
    exp: Fraction,
) : HTSimulatingRecipe<HolderSet<EntityType<*>>>(ingredient, catalyst, result, time, exp) {
    override fun testCatalyst(input: Input, level: Level): Boolean =
        level.getTypedBlockEntity<HTImitationSpawnerBlockEntity>(input.pos)?.spawnerMob?.isOf(catalyst) ?: false

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.SIMULATING_ENTITY
}
