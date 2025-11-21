package hiiragi283.ragium.impl.data.recipe

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.recipe.HTRecipeBuilder
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.result.HTFluidResult
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.recipe.single.HTSingleItemRecipe
import hiiragi283.ragium.api.registry.idOrThrow
import hiiragi283.ragium.common.util.HTPotionHelper
import hiiragi283.ragium.impl.recipe.HTBrewingRecipe
import hiiragi283.ragium.impl.recipe.HTCompressingRecipe
import hiiragi283.ragium.impl.recipe.HTMeltingRecipe
import hiiragi283.ragium.impl.recipe.HTPulverizingRecipe
import net.minecraft.core.Holder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionContents
import java.util.function.Supplier

class HTItemToObjRecipeBuilder<RESULT : Any>(
    prefix: String,
    private val factory: Factory<RESULT, *>,
    private val idProvider: Supplier<ResourceLocation>,
    val ingredient: HTItemIngredient,
    val result: RESULT,
) : HTRecipeBuilder<HTItemToObjRecipeBuilder<RESULT>>(prefix) {
    companion object {
        @JvmStatic
        fun brewing(ingredient: HTItemIngredient, potion: Holder<Potion>): HTItemToObjRecipeBuilder<PotionContents> =
            HTItemToObjRecipeBuilder(RagiumConst.BREWING, ::HTBrewingRecipe, potion::idOrThrow, ingredient, HTPotionHelper.content(potion))

        @JvmStatic
        fun brewing(
            ingredient: HTItemIngredient,
            builderAction: MutableList<MobEffectInstance>.() -> Unit,
        ): HTItemToObjRecipeBuilder<PotionContents> {
            val instances: List<MobEffectInstance> = buildList(builderAction)
            return HTItemToObjRecipeBuilder(
                RagiumConst.BREWING,
                ::HTBrewingRecipe,
                { instances.first().effect.idOrThrow },
                ingredient,
                HTPotionHelper.content(instances),
            )
        }

        @JvmStatic
        fun compressing(ingredient: HTItemIngredient, result: HTItemResult): HTItemToObjRecipeBuilder<HTItemResult> =
            HTItemToObjRecipeBuilder(RagiumConst.COMPRESSING, ::HTCompressingRecipe, result::id, ingredient, result)

        @JvmStatic
        fun pulverizing(ingredient: HTItemIngredient, result: HTItemResult): HTItemToObjRecipeBuilder<HTItemResult> =
            HTItemToObjRecipeBuilder(RagiumConst.CRUSHING, ::HTPulverizingRecipe, result::id, ingredient, result)

        @JvmStatic
        fun melting(ingredient: HTItemIngredient, result: HTFluidResult): HTItemToObjRecipeBuilder<HTFluidResult> =
            HTItemToObjRecipeBuilder(RagiumConst.MELTING, ::HTMeltingRecipe, result::id, ingredient, result)
    }

    override fun getPrimalId(): ResourceLocation = idProvider.get()

    override fun createRecipe(): HTSingleItemRecipe = factory.create(ingredient, result)

    fun interface Factory<RESULT : Any, RECIPE : HTSingleItemRecipe> {
        fun create(ingredient: HTItemIngredient, result: RESULT): RECIPE
    }
}
