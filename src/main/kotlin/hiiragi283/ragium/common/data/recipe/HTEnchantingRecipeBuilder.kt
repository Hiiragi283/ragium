package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.api.data.recipe.builder.HTRecipeBuilder
import hiiragi283.core.api.function.andThen
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.registry.toLike
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.recipe.base.HTEnchantingRecipe
import hiiragi283.ragium.common.recipe.HTHolderEnchantingRecipe
import net.minecraft.core.Holder
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.enchantment.Enchantment

class HTEnchantingRecipeBuilder<ENCH : Any>(private val factory: Factory<ENCH, *>, private val idFactory: (ENCH) -> ResourceLocation) :
    HTRecipeBuilder(RagiumConst.ENCHANTING) {
    companion object {
        @JvmStatic
        inline fun create(output: RecipeOutput, builderAction: HTEnchantingRecipeBuilder<Holder<Enchantment>>.() -> Unit) {
            HTEnchantingRecipeBuilder(
                ::HTHolderEnchantingRecipe,
                Holder<Enchantment>::toLike.andThen(HTIdLike::getId),
            ).apply(builderAction)
                .save(output)
        }
    }

    lateinit var ingredient: HTItemIngredient
    lateinit var enchantment: ENCH

    override fun getPrimalId(): ResourceLocation = idFactory(enchantment)

    override fun createRecipe(): HTEnchantingRecipe = factory.create(ingredient, enchantment)

    //    Factory    //

    fun interface Factory<ENCH : Any, RECIPE : HTEnchantingRecipe> {
        fun create(ingredient: HTItemIngredient, enchantment: ENCH): RECIPE
    }
}
