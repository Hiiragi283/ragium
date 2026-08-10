package hiiragi283.ragium.data.recipe

import hiiragi283.lib.data.recipe.HTRecipeProvider
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.recipe.RagiumRecipeBuilders
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

class RagiumHeatRecipeProvider(packOutput: PackOutput, future: CompletableFuture<HolderLookup.Provider>) : HTRecipeProvider(packOutput, future, RagiumAPI.MOD_ID) {
    override fun buildRecipes() {
        melting()
    }

    private fun melting() {
        // Snow / Ice -> Water
        RagiumRecipeBuilders.melting {
            ingredient {
                items { +Items.SNOWBALL }
                count = 4
            }
            result { water() }
            time /= 2
            recipeId suffix "_from_snowball"
        }.save(exporter)
        RagiumRecipeBuilders.melting {
            ingredient {
                items {
                    +Items.SNOW_BLOCK
                    +Items.ICE
                }
            }
            result { water() }
        }.save(exporter)
        RagiumRecipeBuilders.melting {
            ingredient { items { +Items.PACKED_ICE } }
            result {
                water()
                amount *= 9
            }
            time *= 6
            recipeId suffix "_from_packed_ice"
        }.save(exporter)
        RagiumRecipeBuilders.melting {
            ingredient { items { +Items.BLUE_ICE } }
            result {
                water()
                amount *= 81
            }
            time *= 36
            recipeId suffix "_from_blue_ice"
        }.save(exporter)

        // Stone -> Lava
        RagiumRecipeBuilders.melting {
            ingredient { +holderSet(Tags.Items.STONES, Tags.Items.COBBLESTONES) }
            result {
                lava()
                amount /= 8
            }
            recipeId suffix "_from_stone"
        }.save(exporter)
        // Magma block -> Lava
        RagiumRecipeBuilders.melting {
            ingredient { items { +Items.MAGMA_BLOCK } }
            result {
                lava()
                amount /= 2
            }
            recipeId suffix "_from_magma_block"
        }.save(exporter)
    }

    override fun getName(): String = "Heat Recipes - $modId"
}
