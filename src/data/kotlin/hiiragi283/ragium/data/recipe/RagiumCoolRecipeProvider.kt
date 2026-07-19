package hiiragi283.ragium.data.recipe

import hiiragi283.core.api.data.recipe.HTRecipeProvider
import hiiragi283.core.common.recipe.ingredient.HTBluePrintIngredient
import hiiragi283.core.setup.HCFluids
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.data.recipe.HTFreezingRecipeBuilder
import hiiragi283.ragium.setup.RagiumItems
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.world.item.Items

class RagiumCoolRecipeProvider(packOutput: PackOutput, future: CompletableFuture<HolderLookup.Provider>) : HTRecipeProvider(packOutput, future, RagiumAPI.MOD_ID) {
    override fun buildRecipes() {
        freezing()
    }

    private fun freezing() {
        // Water -> Snowball
        HTFreezingRecipeBuilder.create {
            ingredient { water() }
            catalyst { +HTBluePrintIngredient(0) }
            result {
                +Items.SNOWBALL
                count = 4
            }
            time /= 4
        }.save(exporter)
        // Water -> Ice
        HTFreezingRecipeBuilder.create {
            ingredient { water() }
            catalyst { +HTBluePrintIngredient(1) }
            result { +Items.ICE }
        }.save(exporter)

        // Lava -> Obsidian
        HTFreezingRecipeBuilder.create {
            ingredient { lava() }
            catalyst { +HTBluePrintIngredient(0) }
            result { +Items.OBSIDIAN }
        }.save(exporter)
        // Lava -> Magma Block
        HTFreezingRecipeBuilder.create {
            ingredient {
                lava()
                amount = 250
            }
            catalyst { +HTBluePrintIngredient(1) }
            result { +Items.MAGMA_BLOCK }
        }.save(exporter)

        // Honey -> Honey Block
        HTFreezingRecipeBuilder.create {
            ingredient { +HCFluids.HONEY }
            catalyst { +HTBluePrintIngredient(0) }
            result { +Items.HONEY_BLOCK }
        }.save(exporter)

        // Meat -> Meat Ingot
        HTFreezingRecipeBuilder.create {
            ingredient { +HCFluids.MEAT }
            catalyst { +HTBluePrintIngredient(0) }
            result { +RagiumItems.MEAT_INGOT }
        }.save(exporter)
    }

    override fun getName(): String = "Cool Recipes"
}
