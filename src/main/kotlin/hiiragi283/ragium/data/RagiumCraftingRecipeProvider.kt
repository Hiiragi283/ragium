package hiiragi283.ragium.data

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTShapedRecipeJsonBuilder
import hiiragi283.ragium.api.machine.HTMachineDefinition
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.recipe.HTMachineRecipe
import hiiragi283.ragium.common.init.RagiumMachineKeys
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.RecipeProvider
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.material.Fluids
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.crafting.SizedIngredient
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient
import java.util.*
import java.util.concurrent.CompletableFuture

class RagiumCraftingRecipeProvider(registries: HolderLookup.Provider, output: RecipeOutput) : RecipeProvider(registries, output) {
    override fun buildRecipes() {
        HTShapedRecipeJsonBuilder
            .create(
                registries.lookupOrThrow(Registries.ITEM),
                Items.DIAMOND,
            ).patterns(
                "AAA",
                "ABA",
                "AAA",
            ).input('A', Items.DIRT)
            .input('B', Tags.Items.INGOTS)
            .save(output)

        output.accept(
            ResourceKey.create(Registries.RECIPE, RagiumAPI.id("test_recipe")),
            HTMachineRecipe(
                HTMachineDefinition(RagiumMachineKeys.ASSEMBLER, HTMachineTier.ADVANCED),
                listOf(
                    SizedIngredient.of(Items.COAL, 1),
                    SizedIngredient.of(Items.BLAZE_ROD, 9),
                ),
                listOf(
                    SizedFluidIngredient.of(Fluids.WATER, 1000),
                ),
                Optional.of(Ingredient.of(registries.lookupOrThrow(Registries.ITEM).getOrThrow(ItemTags.WOOL))),
                listOf(
                    ItemStack(Items.DIAMOND),
                    ItemStack(Items.DIAMOND, 3),
                ),
                listOf(
                    FluidStack(Fluids.WATER, 114514),
                ),
            ),
            null,
        )
    }

    //    Runner    //

    class Runner(packOutput: PackOutput, registries: CompletableFuture<HolderLookup.Provider>) :
        RecipeProvider.Runner(packOutput, registries) {
        override fun createRecipeProvider(registries: HolderLookup.Provider, output: RecipeOutput): RecipeProvider =
            RagiumCraftingRecipeProvider(registries, output)

        override fun getName(): String = "Ragium/Crafting"
    }
}
