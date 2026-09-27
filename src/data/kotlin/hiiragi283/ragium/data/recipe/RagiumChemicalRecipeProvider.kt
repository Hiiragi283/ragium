package hiiragi283.ragium.data.recipe

import hiiragi283.lib.data.recipe.HTRecipeProvider
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
    }

    private fun washing() {
        // Sand + Water -> SiO2 + Borax
        RagiumRecipeBuilders.washing {
            itemIngredient {
                +holderSet(Tags.Items.SANDS_COLORLESS)
                count = 4
            }
            fluidIngredient { +waterSet() }
            result {
                +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Gem.QUARTZ)
                count = 3
            }
            result { +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Mineral.BORAX) }
            recipeId suffix "_from_sands"
        }.save(exporter)
        // Red Sand + Water -> SiO2 + Bauxite
        RagiumRecipeBuilders.washing {
            itemIngredient {
                +holderSet(Tags.Items.SANDS_RED)
                count = 4
            }
            fluidIngredient { +waterSet() }
            result {
                +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Gem.QUARTZ)
                count = 3
            }
            result { +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Mineral.BAUXITE) }
            recipeId suffix "_from_red_sands"
        }.save(exporter)
        // Blue Ice + HF (aq) -> Packed Ice + Cryolite
        RagiumRecipeBuilders.washing {
            itemIngredient { items { +Items.BLUE_ICE } }
            fluidIngredient { +holderSet(RagiumFluids.HYDROFLUORIC_ACID) }
            result {
                +Items.PACKED_ICE
                count = 6
            }
            result { +RagiumItems.getOrThrow(HTItemPart.GEM, RagiumMaterial.Gem.CRYOLITE) }
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
        // Raw XX -> XX Dust
        RagiumRecipeBuilders.washing {
            itemIngredient { +holderSet(CommonTagPrefixes.RAW_MATERIALS, RagiumMaterial.Metal.COPPER) }
            fluidIngredient {
                +holderSet(RagiumFluids.SULFURIC_ACID)
                amount /= 4
            }
            result {
                +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Metal.COPPER)
                count = 3
            }
            recipeId suffix "_from_raw"
        }.save(exporter)
        RagiumRecipeBuilders.washing {
            itemIngredient { +holderSet(CommonTagPrefixes.RAW_MATERIALS, RagiumMaterial.Metal.IRON) }
            fluidIngredient {
                +holderSet(RagiumFluids.SULFURIC_ACID)
                amount /= 4
            }
            result {
                +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Metal.IRON)
                count = 2
            }
            recipeId suffix "_from_raw"
        }.save(exporter)
        RagiumRecipeBuilders.washing {
            itemIngredient { +holderSet(CommonTagPrefixes.RAW_MATERIALS, RagiumMaterial.Metal.GOLD) }
            fluidIngredient {
                +holderSet(RagiumFluids.NITRIC_ACID)
                amount /= 4
            }
            result {
                +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Metal.GOLD)
                count = 2
            }
            recipeId suffix "_from_raw"
        }.save(exporter)
    }

    override fun getName(): String = "Chemical Recipes"
}
