package hiiragi283.ragium.data.recipe

import hiiragi283.lib.data.recipe.HTRecipeProvider
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.recipe.RagiumRecipeBuilders
import hiiragi283.ragium.fluid.RagiumFluids
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponents
import net.minecraft.data.PackOutput
import net.minecraft.world.item.Items
import net.minecraft.world.item.component.OminousBottleAmplifier
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.crafting.DataComponentIngredient

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

        // Honey
        RagiumRecipeBuilders.melting {
            ingredient { items { +Items.HONEY_BLOCK } }
            result { +RagiumFluids.HONEY }
            recipeId suffix "_from_block"
        }.save(exporter)

        // Ominous Bottle -> Ominous Flux
        (OminousBottleAmplifier.MIN_AMPLIFIER..OminousBottleAmplifier.MAX_AMPLIFIER)
            .forEach { amplifier: Int ->
                RagiumRecipeBuilders.melting {
                    ingredient {
                        +DataComponentIngredient.of(
                            false,
                            DataComponents.OMINOUS_BOTTLE_AMPLIFIER,
                            OminousBottleAmplifier(amplifier),
                            Items.OMINOUS_BOTTLE,
                        )
                    }
                    result {
                        +RagiumFluids.OMINOUS_FLUX
                        amount = 250 * (amplifier + 1)
                    }
                    recipeId prefix "${amplifier}x_"
                }.save(exporter)
            }

        // Molten Glass
        RagiumRecipeBuilders.melting {
            ingredient { +holderSet(Tags.Items.GLASS_BLOCKS) }
            result { +RagiumFluids.MOLTEN_GLASS }
            recipeId suffix "_from_block"
        }.save(exporter)
        RagiumRecipeBuilders.melting {
            ingredient { +holderSet(Tags.Items.GLASS_PANES) }
            result {
                +RagiumFluids.MOLTEN_GLASS
                amount = 375
            }
            recipeId suffix "_from_pane"
        }.save(exporter)
        RagiumRecipeBuilders.melting {
            ingredient {
                +holderSet(Tags.Items.GLASS_PANES)
                count = 8
            }
            result {
                +RagiumFluids.MOLTEN_GLASS
                amount *= 3
            }
            recipeId suffix "_from_panes"
        }.save(exporter)
    }

    override fun getName(): String = "Heat Recipes"
}
