package hiiragi283.ragium.data.server

import hiiragi283.ragium.api.data.HTMachineRecipeBuilder
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.data.server.recipe.*
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.RecipeProvider
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.crafting.SizedIngredient
import java.util.concurrent.CompletableFuture

class RagiumRecipeProvider(output: PackOutput, registries: CompletableFuture<HolderLookup.Provider>) :
    RecipeProvider(output, registries) {
    override fun buildRecipes(recipeOutput: RecipeOutput) {
        HTBuildingRecipeProvider.buildRecipes(recipeOutput)
        HTChemicalRecipeProvider.buildRecipes(recipeOutput)
        HTDistillationRecipeProvider.buildRecipes(recipeOutput)
        HTFoodRecipeProvider.buildRecipes(recipeOutput)
        HTIngredientRecipeProvider.buildRecipes(recipeOutput)
        HTMachineRecipeProvider.buildRecipes(recipeOutput)
        HTMaterialRecipeProvider.buildRecipes(recipeOutput)

        registerVanilla(recipeOutput)
    }

    private fun registerVanilla(output: RecipeOutput) {
        // Skulls
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.LASER_TRANSFORMER)
            .itemInput(Tags.Items.STORAGE_BLOCKS_BONE_MEAL)
            .itemOutput(Items.SKELETON_SKULL)
            .save(output)

        registerSkull(
            output,
            SizedIngredient.of(Tags.Items.STORAGE_BLOCKS_COAL, 1),
            Items.WITHER_SKELETON_SKULL,
            HTMachineTier.ELITE,
        )
        registerSkull(output, SizedIngredient.of(Items.GOLDEN_APPLE, 8), Items.PLAYER_HEAD)
        registerSkull(output, SizedIngredient.of(Items.ROTTEN_FLESH, 16), Items.ZOMBIE_HEAD)
        registerSkull(output, SizedIngredient.of(Tags.Items.GUNPOWDERS, 16), Items.CREEPER_HEAD)
        registerSkull(output, SizedIngredient.of(Tags.Items.INGOTS_GOLD, 8), Items.PIGLIN_HEAD)
    }

    private fun registerSkull(
        output: RecipeOutput,
        input: SizedIngredient,
        skull: Item,
        tier: HTMachineTier = HTMachineTier.ADVANCED,
    ) {
        HTMachineRecipeBuilder
            .create(RagiumMachineKeys.LASER_TRANSFORMER, tier)
            .itemInput(Items.SKELETON_SKULL)
            .itemInput(input)
            .itemOutput(skull)
            .save(output)
    }
}
