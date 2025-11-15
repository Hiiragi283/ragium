package hiiragi283.ragium.impl.data.recipe

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.recipe.HTRecipeBuilder
import hiiragi283.ragium.api.item.component.unwrap
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.registry.idOrThrow
import hiiragi283.ragium.common.util.HTPotionHelper
import hiiragi283.ragium.impl.recipe.HTBrewingRecipe
import net.minecraft.core.Holder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionContents

class HTBrewingRecipeBuilder private constructor(val ingredient: HTItemIngredient, val content: PotionContents) :
    HTRecipeBuilder.Prefixed(RagiumConst.BREWING) {
        companion object {
            @JvmStatic
            fun create(ingredient: HTItemIngredient, potion: Holder<Potion>): HTBrewingRecipeBuilder =
                HTBrewingRecipeBuilder(ingredient, HTPotionHelper.content(potion))

            @JvmStatic
            fun create(ingredient: HTItemIngredient, builderAction: MutableList<MobEffectInstance>.() -> Unit): HTBrewingRecipeBuilder =
                HTBrewingRecipeBuilder(ingredient, HTPotionHelper.content(builderAction))
        }

        override fun createRecipe(): HTBrewingRecipe = HTBrewingRecipe(ingredient, content)

        override fun getPrimalId(): ResourceLocation = content
            .unwrap()
            .map(Holder<Potion>::idOrThrow) { instances: Iterable<MobEffectInstance> ->
                instances.first().effect.idOrThrow
            }
    }
