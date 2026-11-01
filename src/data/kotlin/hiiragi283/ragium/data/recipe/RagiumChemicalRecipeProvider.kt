package hiiragi283.ragium.data.recipe

import hiiragi283.lib.data.recipe.HTRecipeProvider
import hiiragi283.lib.data.recipe.builder.result
import hiiragi283.lib.recipe.ingredient.HTPotionFluidIngredient
import hiiragi283.lib.tag.CommonTagPrefixes
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.recipe.builder.RagiumRecipeBuilders
import hiiragi283.ragium.api.material.RagiumMaterial
import hiiragi283.ragium.api.tag.RagiumTags
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

        // Sand + CaO + Water -> Mortar
        RagiumRecipeBuilders.mixing {
            itemIngredient { +holderSet(Tags.Items.SANDS) }
            itemIngredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Chemical.QUICK_LIME) }
            fluidIngredient {
                +waterSet()
                amount /= 2
            }
            itemResult {
                +RagiumItems.MORTAR
                count = 2
            }
            time /= 2
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
        RagiumRecipeBuilders.mixing {
            itemIngredient { +holderSet(Tags.Items.GRAVELS) }
            fluidIngredient {
                +waterSet()
                amount /= 4
            }
            itemResult { +Items.FLINT }
            time /= 4
        }.save(exporter)
    }

    override fun getName(): String = "Chemical Recipes"
}
