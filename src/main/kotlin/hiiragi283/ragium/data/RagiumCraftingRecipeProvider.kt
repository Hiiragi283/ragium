package hiiragi283.ragium.data

import hiiragi283.ragium.api.data.HTShapedRecipeJsonBuilder
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.RecipeProvider
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags
import java.util.concurrent.CompletableFuture

class RagiumCraftingRecipeProvider(
    registries: HolderLookup.Provider,
    output: RecipeOutput,
) : RecipeProvider(registries, output) {
    override fun buildRecipes() {
        return

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
    }

    //    Runner    //

    class Runner(
        packOutput: PackOutput,
        registries: CompletableFuture<HolderLookup.Provider>,
    ) : RecipeProvider.Runner(packOutput, registries) {
        override fun createRecipeProvider(
            registries: HolderLookup.Provider,
            output: RecipeOutput,
        ): RecipeProvider = RagiumCraftingRecipeProvider(registries, output)

        override fun getName(): String = "Ragium/Crafting"
    }
}
