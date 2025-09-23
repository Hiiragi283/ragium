package hiiragi283.ragium.impl.data.recipe

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.recipe.HTRecipeBuilder
import hiiragi283.ragium.api.extension.idOrThrow
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.impl.recipe.HTBrewingRecipe
import net.minecraft.core.Holder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.item.crafting.Recipe

class HTBrewingRecipeBuilder(private val ingredients: List<HTItemIngredient>, private val potion: PotionContents) :
    HTRecipeBuilder.Prefixed(RagiumConst.BREWING) {
    companion object {
        @JvmStatic
        fun create(potion: PotionContents, vararg ingredients: HTItemIngredient): HTBrewingRecipeBuilder =
            HTBrewingRecipeBuilder(ingredients.toList(), potion)
    }

    override fun getPrimalId(): ResourceLocation = potion.allEffects
        .first()
        .let(MobEffectInstance::getEffect)
        .let(Holder<MobEffect>::idOrThrow)

    override fun createRecipe(): Recipe<*> = HTBrewingRecipe(ingredients, potion)
}
