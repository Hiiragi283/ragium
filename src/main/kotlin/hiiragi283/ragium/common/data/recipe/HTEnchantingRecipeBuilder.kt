package hiiragi283.ragium.common.data.recipe

import hiiragi283.core.api.data.recipe.builder.HTRecipeBuilder
import hiiragi283.core.api.function.andThen
import hiiragi283.core.api.recipe.ingredient.HTItemIngredient
import hiiragi283.core.api.registry.toLike
import hiiragi283.core.api.resource.HTIdLike
import hiiragi283.core.api.util.HTDelegates
import hiiragi283.core.impl.recipe.HTSerializableRecipe
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.common.recipe.RTEnchantingRecipe
import net.minecraft.core.Holder
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.enchantment.Enchantment

class HTEnchantingRecipeBuilder<ENCH : Any>(
    private val factory: Factory<ENCH, out HTSerializableRecipe<*>>,
    private val idFactory: (ENCH) -> ResourceLocation,
) : HTRecipeBuilder(RagiumConst.ENCHANTING) {
    companion object {
        @JvmStatic
        inline fun create(output: RecipeOutput, builderAction: HTEnchantingRecipeBuilder<Holder<Enchantment>>.() -> Unit) {
            HTEnchantingRecipeBuilder(::RTEnchantingRecipe, Holder<Enchantment>::toLike.andThen(HTIdLike::getId))
                .apply(builderAction)
                .save(output)
        }
    }

    var ingredient: HTItemIngredient by HTDelegates.onceInitialize()
    var enchantment: ENCH by HTDelegates.onceInitialize()

    override fun getPrimalId(): ResourceLocation = idFactory(enchantment)

    override fun createRecipe(): HTSerializableRecipe<*> = factory.create(ingredient, enchantment)

    //    Factory    //

    fun interface Factory<ENCH : Any, RECIPE : Any> {
        fun create(ingredient: HTItemIngredient, enchantment: ENCH): RECIPE
    }
}
