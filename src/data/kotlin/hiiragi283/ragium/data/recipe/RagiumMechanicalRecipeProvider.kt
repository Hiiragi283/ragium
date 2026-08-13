package hiiragi283.ragium.data.recipe

import hiiragi283.lib.data.recipe.HTRecipeProvider
import hiiragi283.lib.tag.CommonTagPrefixes
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.recipe.RagiumRecipeBuilders
import hiiragi283.ragium.api.tag.HTItemPart
import hiiragi283.ragium.api.tag.HTMaterial
import hiiragi283.ragium.item.RagiumItems
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

class RagiumMechanicalRecipeProvider(packOutput: PackOutput, future: CompletableFuture<HolderLookup.Provider>) : HTRecipeProvider(packOutput, future, RagiumAPI.MOD_ID) {
    override fun buildRecipes() {
        crushing()
    }

    private fun crushing() {
        // XX Dust
        RagiumRecipeBuilders.crushing {
            ingredient { items { +Items.COAL } }
            primary { +RagiumItems.getOrThrow(HTItemPart.DUST, HTMaterial.Fuel.COAL) }
        }.save(exporter)
        RagiumRecipeBuilders.crushing {
            ingredient { items { +Items.CHARCOAL } }
            primary { +RagiumItems.getOrThrow(HTItemPart.DUST, HTMaterial.Fuel.CHARCOAL) }
        }.save(exporter)
        RagiumRecipeBuilders.crushing {
            ingredient { items { +RagiumItems.COAL_COKE } }
            primary { +RagiumItems.getOrThrow(HTItemPart.DUST, HTMaterial.Fuel.COAL_COKE) }
        }.save(exporter)

        for (gem: HTMaterial.Gem in HTMaterial.Gem.entries) {
            RagiumRecipeBuilders.crushing {
                ingredient { +holderSet(CommonTagPrefixes.GEM, gem) }
                primary { +RagiumItems.getOrThrow(HTItemPart.DUST, gem) }
                recipeId suffix "_from_gem"
            }.save(exporter)
        }

        for (metal: HTMaterial.Metal in HTMaterial.Metal.entries) {
            RagiumRecipeBuilders.crushing {
                ingredient { +holderSet(CommonTagPrefixes.INGOT, metal) }
                primary { +RagiumItems.getOrThrow(HTItemPart.DUST, metal) }
                recipeId suffix "_from_ingot"
            }.save(exporter)
        }

        RagiumRecipeBuilders.crushing {
            ingredient { +holderSet(ItemTags.PLANKS) }
            primary { +RagiumItems.getOrThrow(HTItemPart.DUST, HTMaterial.Other.WOOD) }
            recipeId suffix "_from_planks"
        }.save(exporter)
        RagiumRecipeBuilders.crushing {
            ingredient { +holderSet(Tags.Items.GLASS_BLOCKS) }
            primary { +RagiumItems.getOrThrow(HTItemPart.DUST, HTMaterial.Other.GLASS) }
            recipeId suffix "_from_block"
        }.save(exporter)
        RagiumRecipeBuilders.crushing {
            ingredient {
                +holderSet(Tags.Items.GLASS_PANES)
                count = 8
            }
            primary {
                +RagiumItems.getOrThrow(HTItemPart.DUST, HTMaterial.Other.GLASS)
                count = 3
            }
            recipeId suffix "_from_pane"
        }.save(exporter)
        RagiumRecipeBuilders.crushing {
            ingredient { +holderSet(Tags.Items.OBSIDIANS_NORMAL) }
            primary { +RagiumItems.getOrThrow(HTItemPart.DUST, HTMaterial.Other.OBSIDIAN) }
        }.save(exporter)
    }

    override fun getName(): String = "Mechanical Recipes"
}
