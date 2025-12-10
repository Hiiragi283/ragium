package hiiragi283.ragium.common.recipe

import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.input.HTRecipeInput
import hiiragi283.ragium.api.recipe.result.HTComplexResult
import hiiragi283.ragium.common.block.entity.HTImitationSpawnerBlockEntity
import hiiragi283.ragium.common.recipe.base.HTBasicSimulatingRecipe
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import net.minecraft.core.HolderSet
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.level.Level
import java.util.Optional

class HTEntitySimulatingRecipe(ingredient: Optional<HTItemIngredient>, catalyst: HolderSet<EntityType<*>>, results: HTComplexResult) :
    HTBasicSimulatingRecipe<HolderSet<EntityType<*>>>(ingredient, catalyst, results) {
    override fun testCatalyst(input: HTRecipeInput, level: Level): Boolean = input.pos
        ?.below()
        ?.let(level::getBlockEntity)
        ?.let { it as? HTImitationSpawnerBlockEntity }
        ?.spawnerMob
        ?.isOf(catalyst)
        ?: false

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.SIMULATING_ENTITY
}
