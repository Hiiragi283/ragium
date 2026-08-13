package hiiragi283.ragium.data.recipe

import hiiragi283.lib.data.recipe.HTRecipeProvider
import hiiragi283.lib.tag.CommonTagPrefixes
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.recipe.RagiumRecipeBuilders
import hiiragi283.ragium.api.tag.HTItemPart
import hiiragi283.ragium.api.tag.HTMaterial
import hiiragi283.ragium.api.tag.RagiumTags
import hiiragi283.ragium.item.RagiumItems
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

class RagiumMechanicalRecipeProvider(packOutput: PackOutput, future: CompletableFuture<HolderLookup.Provider>) : HTRecipeProvider(packOutput, future, RagiumAPI.MOD_ID) {
    override fun buildRecipes() {
        assembling()
        crushing()
    }

    private fun assembling() {
        // Blackstone + Gold -> Gilded Blackstone
        RagiumRecipeBuilders.assembling {
            primary { items { +Items.BLACKSTONE } }
            secondary {
                +holderSet(CommonTagPrefixes.DUST, HTMaterial.Metal.GOLD)
                count = 8
            }
            result { +Items.GILDED_BLACKSTONE }
        }.save(exporter)
        // Dirt + Leaves -> Podzol
        RagiumRecipeBuilders.assembling {
            primary { items { +Items.DIRT } }
            secondary {
                +holderSet(ItemTags.LEAVES)
                count = 8
            }
            result { +Items.PODZOL }
        }.save(exporter)
        // Dirt + Mushroom -> Mycelium
        RagiumRecipeBuilders.assembling {
            primary { items { +Items.DIRT } }
            secondary { +holderSet(Tags.Items.MUSHROOMS) }
            result { +Items.MYCELIUM }
        }.save(exporter)
        // Crimson Nylium
        RagiumRecipeBuilders.assembling {
            primary { +holderSet(Tags.Items.NETHERRACKS) }
            secondary { items { +Items.CRIMSON_FUNGUS } }
            result { +Items.CRIMSON_NYLIUM }
        }.save(exporter)
        // Warped Nylium
        RagiumRecipeBuilders.assembling {
            primary { +holderSet(Tags.Items.NETHERRACKS) }
            secondary { items { +Items.WARPED_FUNGUS } }
            result { +Items.WARPED_NYLIUM }
        }.save(exporter)
        // String + Sticky -> Cobweb
        RagiumRecipeBuilders.assembling {
            primary {
                +holderSet(Tags.Items.STRINGS)
                count = 5
            }
            secondary { +holderSet(RagiumTags.Items.STICKY_BALLS) }
            result { +Items.COBWEB }
        }.save(exporter)

        // Iron Ingot + Iron Nugget -> Chain
        RagiumRecipeBuilders.assembling {
            primary { +holderSet(CommonTagPrefixes.INGOT, HTMaterial.Metal.IRON) }
            secondary {
                +holderSet(CommonTagPrefixes.NUGGET, HTMaterial.Metal.IRON)
                count = 3
            }
            result {
                +Items.IRON_CHAIN
                count = 3
            }
        }.save(exporter)
        // Iron Ingot + Torch -> Lantern
        RagiumRecipeBuilders.assembling {
            primary { +holderSet(CommonTagPrefixes.INGOT, HTMaterial.Metal.IRON) }
            secondary { items { +Items.TORCH } }
            result {
                +Items.LANTERN
                count = 2
            }
        }.save(exporter)
        RagiumRecipeBuilders.assembling {
            primary { +holderSet(CommonTagPrefixes.INGOT, HTMaterial.Metal.IRON) }
            secondary { items { +Items.SOUL_TORCH } }
            result {
                +Items.SOUL_LANTERN
                count = 2
            }
        }.save(exporter)
        // Iron Ingot + Chest -> Hopper
        RagiumRecipeBuilders.assembling {
            primary {
                +holderSet(CommonTagPrefixes.INGOT, HTMaterial.Metal.IRON)
                count = 5
            }
            secondary { +holderSet(Tags.Items.CHESTS_WOODEN) }
            result { +Items.HOPPER }
        }.save(exporter)
        // Dropper + Bow -> Dispenser
        RagiumRecipeBuilders.assembling {
            primary { items { +Items.DROPPER } }
            secondary { +holderSet(Tags.Items.TOOLS_BOW) }
            result { +Items.DISPENSER }
        }.save(exporter)
        // TNT
        RagiumRecipeBuilders.assembling {
            primary {
                +holderSet(Tags.Items.SANDS)
                count = 4
            }
            secondary {
                +holderSet(Tags.Items.GUNPOWDERS)
                count = 5
            }
            result {
                +Items.TNT
                count = 2
            }
        }.save(exporter)

        // Leather + Iron Nugget -> Saddle
        RagiumRecipeBuilders.assembling {
            primary {
                +holderSet(Tags.Items.LEATHERS)
                count = 5
            }
            secondary {
                +holderSet(CommonTagPrefixes.NUGGET, HTMaterial.Metal.IRON)
                count = 2
            }
            result { +Items.SADDLE }
        }.save(exporter)
        // Head
        RagiumRecipeBuilders.assembling {
            primary { items { +Items.SKELETON_SKULL } }
            secondary {
                items { +Items.ROTTEN_FLESH }
                count = 8
            }
            result { +Items.ZOMBIE_HEAD }
        }.save(exporter)
        RagiumRecipeBuilders.assembling {
            primary { items { +Items.SKELETON_SKULL } }
            secondary {
                +holderSet(Tags.Items.GUNPOWDERS)
                count = 8
            }
            result { +Items.CREEPER_HEAD }
        }.save(exporter)
        RagiumRecipeBuilders.assembling {
            primary { items { +Items.SKELETON_SKULL } }
            secondary {
                items { +Items.PORKCHOP }
                count = 8
            }
            result { +Items.PIGLIN_HEAD }
        }.save(exporter)
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
