package hiiragi283.ragium.client.integration.emi.recipe.processor

import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import hiiragi283.ragium.api.item.createEnchantedBook
import hiiragi283.ragium.client.integration.emi.category.HTEmiRecipeCategory
import hiiragi283.ragium.client.integration.emi.recipe.base.HTCombineEmiRecipe
import hiiragi283.ragium.client.integration.emi.toEmi
import hiiragi283.ragium.client.integration.emi.toFluidEmi
import hiiragi283.ragium.impl.recipe.HTEnchantingRecipe
import hiiragi283.ragium.setup.RagiumFluidContents
import net.minecraft.world.item.crafting.RecipeHolder

class HTEnchantingEmiRecipe(category: HTEmiRecipeCategory, holder: RecipeHolder<HTEnchantingRecipe>) :
    HTCombineEmiRecipe<HTEnchantingRecipe>(category, holder) {
    override fun getFluidIngredient(recipe: HTEnchantingRecipe): EmiIngredient =
        RagiumFluidContents.EXPERIENCE.toFluidEmi(recipe.getRequiredExpFluid())

    override fun getResult(recipe: HTEnchantingRecipe): EmiStack = recipe.holder.let(::createEnchantedBook).toEmi()
}
