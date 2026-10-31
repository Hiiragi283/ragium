package hiiragi283.ragium.data.recipe

import hiiragi283.lib.data.recipe.HTRecipeProvider
import hiiragi283.lib.recipe.ingredient.HTPotionFluidIngredient
import hiiragi283.lib.tag.CommonTagPrefixes
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.recipe.builder.RagiumRecipeBuilders
import hiiragi283.ragium.api.material.HTItemPart
import hiiragi283.ragium.api.material.RagiumMaterial
import hiiragi283.ragium.api.tag.RagiumTags
import hiiragi283.ragium.common.fluid.RagiumFluids
import hiiragi283.ragium.common.item.RagiumItems
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.Potions
import net.neoforged.neoforge.common.Tags
import java.util.concurrent.CompletableFuture

class RagiumChemicalRecipeProvider(packOutput: PackOutput, future: CompletableFuture<HolderLookup.Provider>) :
    HTRecipeProvider(packOutput, future, RagiumAPI.MOD_ID) {
    override fun exportValues() {
        washing()

        // Wood Pulp + Resin -> Particle Board
        RagiumRecipeBuilders.bathing {
            itemIngredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Other.WOOD) }
            fluidIngredient {
                +holderSet(RagiumTags.Fluids.RESINS)
                amount /= 8
            }
            result { +RagiumItems.PARTICLE_BOARD }
        }.save(exporter)
        // Piston + Resin -> Sticky Piston
        RagiumRecipeBuilders.bathing {
            itemIngredient { items { +Items.PISTON } }
            fluidIngredient {
                +holderSet(RagiumTags.Fluids.RESINS)
                amount /= 8
            }
            result { +Items.STICKY_PISTON }
        }.save(exporter)

        // Cement + Water -> Mortar
        RagiumRecipeBuilders.bathing {
            itemIngredient { items { +RagiumItems.CEMENT } }
            fluidIngredient {
                +waterSet()
                amount /= 4
            }
            result { +RagiumItems.MORTAR }
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
    }

    private fun washing() {
        // Gravel + Water -> Flint
        RagiumRecipeBuilders.washing {
            itemIngredient { +holderSet(Tags.Items.GRAVELS) }
            fluidIngredient { +waterSet() }
            result { +Items.FLINT }
        }.save(exporter)
        // Sand + Water -> SiO2
        RagiumRecipeBuilders.washing {
            itemIngredient {
                +holderSet(Tags.Items.SANDS)
                count = 8
            }
            fluidIngredient { +waterSet() }
            result {
                +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Chemical.SILICA)
                count = 3
            }
            recipeId suffix "_from_sands"
        }.save(exporter)
        // Clay + Water -> Al2O3 + SiO2
        RagiumRecipeBuilders.washing {
            itemIngredient {
                items { +Items.CLAY_BALL }
                count = 16
            }
            fluidIngredient { +waterSet() }
            result {
                +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Chemical.SILICA)
                count = 3
            }
            recipeId suffix "_from_clay_ball"
        }.save(exporter)

        RagiumRecipeBuilders.washing {
            itemIngredient {
                items { +Items.CLAY }
                count = 4
            }
            fluidIngredient { +waterSet() }
            result {
                +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Chemical.SILICA)
                count = 3
            }
            recipeId suffix "_from_clay"
        }.save(exporter)

        // Ancient Debris -> Netherite Scrap
        RagiumRecipeBuilders.washing {
            itemIngredient { items { +Items.ANCIENT_DEBRIS } }
            fluidIngredient {
                +holderSet(RagiumFluids.HYDROFLUORIC_ACID)
                amount /= 4
            }
            result {
                +Items.NETHERITE_SCRAP
                count = 3
            }
            recipeId suffix "_from_debris"
        }.save(exporter)
    }

    override fun getName(): String = "Chemical Recipes"
}
