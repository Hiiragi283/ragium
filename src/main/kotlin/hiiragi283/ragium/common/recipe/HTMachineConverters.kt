package hiiragi283.ragium.common.recipe

import hiiragi283.ragium.api.data.HTMachineRecipeBuilder
import hiiragi283.ragium.api.recipe.HTMachineRecipe
import hiiragi283.ragium.common.init.RagiumMachineKeys
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.AbstractCookingRecipe
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.SmeltingRecipe
import net.minecraft.world.item.crafting.StonecutterRecipe

object HTMachineConverters {
    @JvmStatic
    fun fromCooking(holder: RecipeHolder<SmeltingRecipe>, provider: HolderLookup.Provider): RecipeHolder<HTMachineRecipe> {
        val recipe: AbstractCookingRecipe = holder.value
        return HTMachineRecipeBuilder
            .create(RagiumMachineKeys.MULTI_SMELTER)
            .itemInput(recipe.ingredients[0])
            .itemOutput(recipe.getResultItem(provider))
            .export(holder.id.withSuffix("_from_smelting"))
    }

    @JvmStatic
    fun fromCutting(holder: RecipeHolder<StonecutterRecipe>, provider: HolderLookup.Provider): RecipeHolder<HTMachineRecipe> {
        val recipe: StonecutterRecipe = holder.value
        val output: ItemStack = recipe.getResultItem(provider)
        return HTMachineRecipeBuilder
            .create(RagiumMachineKeys.CUTTING_MACHINE)
            .itemInput(recipe.ingredients[0])
            .catalyst(output.item)
            .itemOutput(recipe.getResultItem(provider))
            .export(holder.id.withSuffix("_from_cutting"))
    }
}
