package hiiragi283.ragium.impl.data.recipe

import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.data.recipe.HTRecipeBuilder
import hiiragi283.ragium.api.item.alchemy.HTMobEffectInstance
import hiiragi283.ragium.api.item.alchemy.HTPotionHelper
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.recipe.multi.HTCombineRecipe
import hiiragi283.ragium.api.registry.idOrThrow
import hiiragi283.ragium.impl.recipe.HTBrewingRecipe
import hiiragi283.ragium.impl.recipe.HTEnchantingRecipe
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.item.enchantment.Enchantment
import java.util.function.Supplier

class HTCombineRecipeBuilder<RESULT : Any>(
    prefix: String,
    private val factory: Factory<RESULT, *>,
    private val idProvider: Supplier<ResourceLocation>,
    private val leftIngredient: HTItemIngredient,
    private val rightIngredient: HTItemIngredient,
    private val result: RESULT,
) : HTRecipeBuilder<HTCombineRecipeBuilder<RESULT>>(prefix) {
    companion object {
        @JvmStatic
        fun brewing(
            leftIngredient: HTItemIngredient,
            rightIngredient: HTItemIngredient,
            potion: Holder<Potion>,
        ): HTCombineRecipeBuilder<PotionContents> = HTCombineRecipeBuilder(
            RagiumConst.BREWING,
            ::HTBrewingRecipe,
            potion::idOrThrow,
            leftIngredient,
            rightIngredient,
            HTPotionHelper.contents(potion),
        )

        @JvmStatic
        fun brewing(
            leftIngredient: HTItemIngredient,
            rightIngredient: HTItemIngredient,
            builderAction: MutableList<HTMobEffectInstance>.() -> Unit,
        ): HTCombineRecipeBuilder<PotionContents> {
            val instances: List<HTMobEffectInstance> = buildList(builderAction)
            return HTCombineRecipeBuilder(
                RagiumConst.BREWING,
                ::HTBrewingRecipe,
                { instances.first().getId() },
                leftIngredient,
                rightIngredient,
                HTPotionHelper.contents(null, null, instances),
            )
        }

        @Suppress("DEPRECATION")
        @JvmStatic
        fun enchanting(ingredient: HTItemIngredient, holder: Holder<Enchantment>): HTCombineRecipeBuilder<Holder<Enchantment>> =
            HTCombineRecipeBuilder(
                RagiumConst.ENCHANTING,
                ::HTEnchantingRecipe,
                holder::idOrThrow,
                ingredient,
                HTItemIngredient.of(HolderSet.direct(Items.BOOK.builtInRegistryHolder())),
                holder,
            )
    }

    override fun getPrimalId(): ResourceLocation = idProvider.get()

    override fun createRecipe(): HTCombineRecipe = factory.create(leftIngredient to rightIngredient, result)

    fun interface Factory<RESULT : Any, RECIPE : HTCombineRecipe> {
        fun create(itemIngredients: Pair<HTItemIngredient, HTItemIngredient>, result: RESULT): RECIPE
    }
}
