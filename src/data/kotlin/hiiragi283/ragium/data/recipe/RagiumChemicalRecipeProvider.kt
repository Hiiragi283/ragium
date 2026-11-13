package hiiragi283.ragium.data.recipe

import hiiragi283.lib.data.recipe.HTRecipeProvider
import hiiragi283.lib.data.recipe.builder.result
import hiiragi283.lib.recipe.ingredient.HTPotionFluidIngredient
import hiiragi283.lib.registry.HTSimpleDeferredItem
import hiiragi283.lib.tag.CommonTagPrefixes
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.recipe.builder.RagiumRecipeBuilders
import hiiragi283.ragium.api.material.RagiumMaterial
import hiiragi283.ragium.common.fluid.RagiumFluids
import hiiragi283.ragium.common.item.RagiumItems
import hiiragi283.ragium.common.material.RagiumMaterialHelper
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.Potions
import net.neoforged.neoforge.common.Tags
import java.util.concurrent.CompletableFuture

class RagiumChemicalRecipeProvider(packOutput: PackOutput, future: CompletableFuture<HolderLookup.Provider>) :
    HTRecipeProvider(packOutput, future, RagiumAPI.MOD_ID) {
    override fun exportValues() {
        autoclaving()
        bathing()
    }

    private fun autoclaving() {
        // XX Dust -> XX Gem
        for (gem: RagiumMaterial.Gem in RagiumMaterial.Gem.entries) {
            val item: HTSimpleDeferredItem = RagiumMaterialHelper.getGem(gem) ?: continue
            RagiumRecipeBuilders.autoclaving {
                itemIngredient { +holderSet(CommonTagPrefixes.DUST, gem) }
                fluidIngredient { +waterSet() }
                result { +item }
                condition { itemTagPresent(CommonTagPrefixes.DUST, gem) }
                recipeId suffix "_from_dust"
            }.save(exporter)
        }
        // Silica + Water -> Quartz
        RagiumRecipeBuilders.autoclaving {
            itemIngredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Chemical.SILICA) }
            fluidIngredient { +waterSet() }
            result { +Items.QUARTZ }
            recipeId suffix "_from_silica"
        }.save(exporter)
        // Silica + Purple Dye -> Amethyst
        RagiumRecipeBuilders.autoclaving {
            itemIngredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Chemical.SILICA) }
            fluidIngredient { +holderSet(RagiumFluids.DYES.purple) }
            result { +Items.AMETHYST_SHARD }
            recipeId suffix "_from_silica"
        }.save(exporter)

        // Amethyst -> Amethyst Bud -> Amethyst Cluster
        RagiumRecipeBuilders.autoclaving {
            itemIngredient { +holderSet(CommonTagPrefixes.GEM, RagiumMaterial.Gem.AMETHYST) }
            fluidIngredient {
                +waterSet()
                amount /= 4
            }
            result { +Items.SMALL_AMETHYST_BUD }
        }.save(exporter)
        RagiumRecipeBuilders.autoclaving {
            itemIngredient { items { +Items.SMALL_AMETHYST_BUD } }
            fluidIngredient {
                +waterSet()
                amount /= 4
            }
            result { +Items.MEDIUM_AMETHYST_BUD }
        }.save(exporter)
        RagiumRecipeBuilders.autoclaving {
            itemIngredient { items { +Items.MEDIUM_AMETHYST_BUD } }
            fluidIngredient {
                +waterSet()
                amount /= 4
            }
            result { +Items.LARGE_AMETHYST_BUD }
        }.save(exporter)
        RagiumRecipeBuilders.autoclaving {
            itemIngredient { items { +Items.LARGE_AMETHYST_BUD } }
            fluidIngredient {
                +waterSet()
                amount /= 4
            }
            result { +Items.AMETHYST_CLUSTER }
        }.save(exporter)

        // Molten Glass + Niter -> Ghast Tear
        RagiumRecipeBuilders.autoclaving {
            itemIngredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Chemical.NITER) }
            fluidIngredient { +holderSet(RagiumFluids.MOLTEN_GLASS) }
            result { +Items.GHAST_TEAR }
        }.save(exporter)
    }

    private fun bathing() {
        // Wood Pulp + Glue -> Particle Board
        RagiumRecipeBuilders.bathing {
            itemIngredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Other.WOOD) }
            fluidIngredient {
                +holderSet(RagiumFluids.GLUE)
                amount /= 8
            }
            result { +RagiumItems.PARTICLE_BOARD }
        }.save(exporter)
        // Piston + Glue -> Sticky Piston
        RagiumRecipeBuilders.bathing {
            itemIngredient { items { +Items.PISTON } }
            fluidIngredient {
                +holderSet(RagiumFluids.GLUE)
                amount /= 8
            }
            result { +Items.STICKY_PISTON }
        }.save(exporter)

        // Poison + Spider Eye -> Fermented Spider Eye
        RagiumRecipeBuilders.bathing {
            itemIngredient { items { +Items.SPIDER_EYE } }
            fluidIngredient {
                +HTPotionFluidIngredient(Potions.POISON)
                amount = 250
            }
            result { +Items.FERMENTED_SPIDER_EYE }
        }.save(exporter)
        // Poison + Potato -> Poisonous Potato
        RagiumRecipeBuilders.bathing {
            itemIngredient { +holderSet(Tags.Items.CROPS_POTATO) }
            fluidIngredient {
                +HTPotionFluidIngredient(Potions.POISON)
                amount = 250
            }
            result { +Items.POISONOUS_POTATO }
        }.save(exporter)

        // Gravel + Water -> Flint
        RagiumRecipeBuilders.bathing {
            itemIngredient { +holderSet(Tags.Items.GRAVELS) }
            fluidIngredient {
                +waterSet()
                amount /= 4
            }
            result { +Items.FLINT }
            time /= 4
        }.save(exporter)
    }

    override fun getName(): String = "Chemical Recipes"
}
