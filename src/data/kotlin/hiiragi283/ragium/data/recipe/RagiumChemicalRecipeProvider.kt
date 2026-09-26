package hiiragi283.ragium.data.recipe

import hiiragi283.lib.data.recipe.HTRecipeProvider
import hiiragi283.lib.tag.CommonTagPrefixes
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.recipe.builder.RagiumRecipeBuilders
import hiiragi283.ragium.api.material.RagiumMaterial
import hiiragi283.ragium.api.tag.RagiumTags
import hiiragi283.ragium.common.item.RagiumItems
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.world.item.Items
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
        
        // Cement + Water -> Mortar
        RagiumRecipeBuilders.bathing {
            itemIngredient { items { +RagiumItems.CEMENT } }
            fluidIngredient {
                +waterSet()
                amount /= 4
            }
            result { +RagiumItems.MORTAR }
        }.save(exporter)
    }

    override fun getName(): String = "Chemical Recipes"
}
