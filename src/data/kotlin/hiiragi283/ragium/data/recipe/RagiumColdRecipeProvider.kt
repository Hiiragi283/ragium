package hiiragi283.ragium.data.recipe

import hiiragi283.core.api.data.recipe.HTRecipeProvider
import hiiragi283.core.common.recipe.ingredient.HTBluePrintIngredient
import hiiragi283.core.setup.HCFluids
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.data.recipe.RagiumRecipeBuilder
import hiiragi283.ragium.setup.RagiumItems
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.world.item.Items

class RagiumColdRecipeProvider(packOutput: PackOutput, future: CompletableFuture<HolderLookup.Provider>) : HTRecipeProvider(packOutput, future, RagiumAPI.MOD_ID) {
    override fun buildRecipes() {
        freezing()
    }

    override fun getName(): String = "Cold Recipes"

    //    Freezing    //

    private fun freezing() {
        // Water -> Snowball
        RagiumRecipeBuilder.freezing {
            fluidIngredient { water() }
            itemIngredient { +HTBluePrintIngredient(0) }
            result {
                +Items.SNOWBALL
                count = 4
            }
            time /= 4
        }.save(exporter)
        // Water -> Ice
        RagiumRecipeBuilder.freezing {
            fluidIngredient { water() }
            itemIngredient { +HTBluePrintIngredient(1) }
            result { +Items.ICE }
        }.save(exporter)

        // Lava -> Obsidian
        RagiumRecipeBuilder.freezing {
            fluidIngredient { lava() }
            itemIngredient { +HTBluePrintIngredient(0) }
            result { +Items.OBSIDIAN }
        }.save(exporter)
        // Lava -> Magma Block
        RagiumRecipeBuilder.freezing {
            fluidIngredient {
                lava()
                amount = 250
            }
            itemIngredient { +HTBluePrintIngredient(1) }
            result { +Items.MAGMA_BLOCK }
        }.save(exporter)

        // Honey -> Honey Block
        RagiumRecipeBuilder.freezing {
            fluidIngredient { +HCFluids.HONEY }
            itemIngredient { +HTBluePrintIngredient(0) }
            result { +Items.HONEY_BLOCK }
        }.save(exporter)

        // Meat -> Meat Ingot
        RagiumRecipeBuilder.freezing {
            fluidIngredient { +HCFluids.MEAT }
            itemIngredient { +HTBluePrintIngredient(0) }
            result { +RagiumItems.MEAT_INGOT }
        }.save(exporter)
    }
}
