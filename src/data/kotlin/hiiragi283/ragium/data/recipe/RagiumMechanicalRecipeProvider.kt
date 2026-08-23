package hiiragi283.ragium.data.recipe

import hiiragi283.lib.color.HTDefaultColor
import hiiragi283.lib.color.VanillaColoredCollections
import hiiragi283.lib.data.recipe.HTRecipeProvider
import hiiragi283.lib.material.HTMaterial
import hiiragi283.lib.material.HTMaterialCategory
import hiiragi283.lib.material.VanillaMaterials
import hiiragi283.lib.registry.HTSimpleDeferredItem
import hiiragi283.lib.tag.CommonTagPrefixes
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.recipe.RagiumRecipeBuilders
import hiiragi283.ragium.api.tag.HTItemPart
import hiiragi283.ragium.api.tag.RagiumTags
import hiiragi283.ragium.item.RagiumItems
import hiiragi283.ragium.material.RagiumMaterialHelper
import hiiragi283.ragium.material.RagiumMaterials
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags

class RagiumMechanicalRecipeProvider(packOutput: PackOutput, future: CompletableFuture<HolderLookup.Provider>) : HTRecipeProvider(packOutput, future, RagiumAPI.MOD_ID) {
    override fun buildRecipes() {
        assembling()
        compressing()
        crushing()
        cutting()
    }

    private fun assembling() {
        // Blackstone + Gold -> Gilded Blackstone
        RagiumRecipeBuilders.assembling {
            primary { items { +Items.BLACKSTONE } }
            secondary {
                +holderSet(CommonTagPrefixes.DUST, VanillaMaterials.GOLD)
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

        // XX Ingot + XX Nugget -> XX Chain
        setOf(
            VanillaMaterials.COPPER to Items.COPPER_CHAIN.unaffected(),
            VanillaMaterials.IRON to Items.IRON_CHAIN,
        ).forEach { (metal: VanillaMaterials, chain: Item) ->
            RagiumRecipeBuilders.assembling {
                primary { +holderSet(CommonTagPrefixes.INGOT, metal) }
                secondary {
                    +holderSet(CommonTagPrefixes.NUGGET, metal)
                    count = 3
                }
                result {
                    +chain
                    count = 3
                }
            }.save(exporter)
        }
        // XX Ingot + Torch -> XX Lantern
        setOf(
            VanillaMaterials.COPPER to Items.COPPER_LANTERN.unaffected(),
            VanillaMaterials.IRON to Items.LANTERN,
        ).forEach { (metal: VanillaMaterials, lantern: Item) ->
            RagiumRecipeBuilders.assembling {
                primary { +holderSet(CommonTagPrefixes.INGOT, metal) }
                secondary { items { +Items.TORCH } }
                result {
                    +lantern
                    count = 2
                }
            }.save(exporter)
        }
        RagiumRecipeBuilders.assembling {
            primary { +holderSet(CommonTagPrefixes.INGOT, VanillaMaterials.IRON) }
            secondary { items { +Items.SOUL_TORCH } }
            result {
                +Items.SOUL_LANTERN
                count = 2
            }
        }.save(exporter)
        // Iron Ingot + Chest -> Hopper
        RagiumRecipeBuilders.assembling {
            primary {
                +holderSet(CommonTagPrefixes.INGOT, VanillaMaterials.IRON)
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

    private fun compressing() {
        // Snow -> Snow Block
        RagiumRecipeBuilders.compressing {
            ingredient {
                items { +Items.SNOW }
                count = 8
            }
            result { +Items.SNOW_BLOCK }
        }.save(exporter)
        // Snow Block -> Ice
        RagiumRecipeBuilders.compressing {
            ingredient { items { +Items.SNOW_BLOCK } }
            result { +Items.ICE }
        }.save(exporter)
        // Ice -> Packed Ice
        RagiumRecipeBuilders.compressing {
            ingredient {
                items { +Items.ICE }
                count = 6
            }
            result { +Items.PACKED_ICE }
        }.save(exporter)
        // Packed Ice -> Blue Ice
        RagiumRecipeBuilders.compressing {
            ingredient {
                items { +Items.PACKED_ICE }
                count = 6
            }
            result { +Items.BLUE_ICE }
        }.save(exporter)

        // Mud -> Clay
        RagiumRecipeBuilders.compressing {
            ingredient { items { +Items.MUD } }
            result { +Items.CLAY }
        }.save(exporter)
        // Moss Carpet -> Moss
        RagiumRecipeBuilders.compressing {
            ingredient {
                items { +Items.MOSS_CARPET }
                count = 3
            }
            result {
                +Items.MOSS_BLOCK
                count = 2
            }
            recipeId suffix "_from_carpet"
        }.save(exporter)
        RagiumRecipeBuilders.compressing {
            ingredient {
                items { +Items.PALE_MOSS_CARPET }
                count = 3
            }
            result {
                +Items.PALE_MOSS_BLOCK
                count = 2
            }
            recipeId suffix "_from_carpet"
        }.save(exporter)
        // Sculk Vein -> Sculk
        RagiumRecipeBuilders.compressing {
            ingredient {
                items { +Items.SCULK_VEIN }
                count = 8
            }
            result { +Items.SCULK }
        }.save(exporter)

        // XX Carpet -> XX Wool
        for (color: HTDefaultColor in HTDefaultColor.entries) {
            RagiumRecipeBuilders.compressing {
                ingredient {
                    items { +VanillaColoredCollections.CARPET[color] }
                    count = 3
                }
                result {
                    +VanillaColoredCollections.WOOL[color]
                    count = 2
                }
                recipeId suffix "_from_carpet"
            }.save(exporter)
        }

        // XX Dust -> XX
        for (fuel: HTMaterial in RagiumMaterialHelper.MANAGER[HTMaterialCategory.FUEL]) {
            val baseItem: HTSimpleDeferredItem = RagiumMaterialHelper.getFuelBase(fuel) ?: continue
            RagiumRecipeBuilders.compressing {
                ingredient { +holderSet(CommonTagPrefixes.DUST, fuel) }
                result { +baseItem }
                recipeId suffix "_from_dust"
            }.save(exporter)
        }

        RagiumRecipeBuilders.compressing {
            ingredient {
                +holderSet(CommonTagPrefixes.DUST, VanillaMaterials.WOOD)
                count = 2
            }
            result { +RagiumItems.PARTICLE_BOARD }
        }.save(exporter)
    }

    private fun crushing() {
        // XX Dust
        for (fuel: HTMaterial in RagiumMaterialHelper.MANAGER[HTMaterialCategory.FUEL]) {
            val baseItem: HTSimpleDeferredItem = RagiumMaterialHelper.getFuelBase(fuel) ?: continue
            RagiumRecipeBuilders.crushing {
                ingredient { items { +baseItem } }
                primary { +RagiumItems.getOrThrow(HTItemPart.DUST, fuel) }
            }.save(exporter)
        }

        for (gem: HTMaterial in RagiumMaterialHelper.MANAGER[HTMaterialCategory.GEM]) {
            RagiumRecipeBuilders.crushing {
                ingredient { +holderSet(CommonTagPrefixes.GEM, gem) }
                primary { +RagiumItems.getOrThrow(HTItemPart.DUST, gem) }
                recipeId suffix "_from_gem"
            }.save(exporter)
        }

        for (metal: HTMaterial in RagiumMaterialHelper.MANAGER[HTMaterialCategory.METAL]) {
            RagiumRecipeBuilders.crushing {
                ingredient { +holderSet(CommonTagPrefixes.INGOT, metal) }
                primary { +RagiumItems.getOrThrow(HTItemPart.DUST, metal) }
                recipeId suffix "_from_ingot"
            }.save(exporter)
        }

        RagiumRecipeBuilders.crushing {
            ingredient { +holderSet(ItemTags.PLANKS) }
            primary { +RagiumItems.getOrThrow(HTItemPart.DUST, VanillaMaterials.WOOD) }
            recipeId suffix "_from_planks"
        }.save(exporter)
        RagiumRecipeBuilders.crushing {
            ingredient { +holderSet(Tags.Items.GLASS_BLOCKS) }
            primary { +RagiumItems.getOrThrow(HTItemPart.DUST, VanillaMaterials.GLASS) }
            recipeId suffix "_from_block"
        }.save(exporter)
        RagiumRecipeBuilders.crushing {
            ingredient {
                +holderSet(Tags.Items.GLASS_PANES)
                count = 8
            }
            primary {
                +RagiumItems.getOrThrow(HTItemPart.DUST, VanillaMaterials.GLASS)
                count = 3
            }
            recipeId suffix "_from_pane"
        }.save(exporter)
        RagiumRecipeBuilders.crushing {
            ingredient { +holderSet(Tags.Items.OBSIDIANS_NORMAL) }
            primary { +RagiumItems.getOrThrow(HTItemPart.DUST, VanillaMaterials.OBSIDIAN) }
        }.save(exporter)

        // XX Ore -> XX Dust
        RagiumRecipeBuilders.crushing {
            ingredient { +holderSet(CommonTagPrefixes.ORE, VanillaMaterials.COAL) }
            primary {
                +RagiumItems.getOrThrow(HTItemPart.DUST, VanillaMaterials.COAL)
                count = 2
            }
            secondary {
                +RagiumItems.getOrThrow(HTItemPart.TINY, VanillaMaterials.COAL)
                count = 3
            }
            recipeId suffix "_from_ore"
        }.save(exporter)
        RagiumRecipeBuilders.crushing {
            ingredient { +holderSet(CommonTagPrefixes.ORE, VanillaMaterials.REDSTONE) }
            primary {
                +Items.REDSTONE
                count = 6
            }
            secondary {
                +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterials.RAGINITE)
            }
            recipeId suffix "_from_ore"
        }.save(exporter)

        RagiumRecipeBuilders.crushing {
            ingredient { +holderSet(CommonTagPrefixes.ORE, VanillaMaterials.LAPIS) }
            primary {
                +RagiumItems.getOrThrow(HTItemPart.DUST, VanillaMaterials.LAPIS)
                count = 6
            }
            recipeId suffix "_from_ore"
        }.save(exporter)
        RagiumRecipeBuilders.crushing {
            ingredient { +holderSet(CommonTagPrefixes.ORE, VanillaMaterials.QUARTZ) }
            primary {
                +RagiumItems.getOrThrow(HTItemPart.DUST, VanillaMaterials.QUARTZ)
                count = 4
            }
            secondary {
                +Items.GOLD_NUGGET
            }
            recipeId suffix "_from_ore"
        }.save(exporter)
        RagiumRecipeBuilders.crushing {
            ingredient { +holderSet(CommonTagPrefixes.ORE, VanillaMaterials.DIAMOND) }
            primary {
                +RagiumItems.getOrThrow(HTItemPart.DUST, VanillaMaterials.DIAMOND)
                count = 2
            }
            recipeId suffix "_from_ore"
        }.save(exporter)
        RagiumRecipeBuilders.crushing {
            ingredient { +holderSet(CommonTagPrefixes.ORE, VanillaMaterials.EMERALD) }
            primary {
                +RagiumItems.getOrThrow(HTItemPart.DUST, VanillaMaterials.EMERALD)
                count = 2
            }
            recipeId suffix "_from_ore"
        }.save(exporter)

        RagiumRecipeBuilders.crushing {
            ingredient { +holderSet(CommonTagPrefixes.ORE, VanillaMaterials.COPPER) }
            primary {
                +RagiumItems.getOrThrow(HTItemPart.DUST, VanillaMaterials.COPPER)
                count = 3
            }
            secondary {
                +Items.GOLD_NUGGET
                count = 3
            }
            recipeId suffix "_from_ore"
        }.save(exporter)
        RagiumRecipeBuilders.crushing {
            ingredient { +holderSet(CommonTagPrefixes.ORE, VanillaMaterials.IRON) }
            primary {
                +RagiumItems.getOrThrow(HTItemPart.DUST, VanillaMaterials.IRON)
                count = 2
            }
            secondary { +Items.FLINT }
            recipeId suffix "_from_ore"
        }.save(exporter)
        RagiumRecipeBuilders.crushing {
            ingredient { +holderSet(CommonTagPrefixes.ORE, VanillaMaterials.GOLD) }
            primary {
                +RagiumItems.getOrThrow(HTItemPart.DUST, VanillaMaterials.GOLD)
                count = 2
            }
            recipeId suffix "_from_ore"
        }.save(exporter)
        // Raw XX -> XX Dust
        for (metal: HTMaterial in setOf(VanillaMaterials.COPPER, VanillaMaterials.IRON, VanillaMaterials.GOLD)) {
            RagiumRecipeBuilders.crushing {
                ingredient {
                    +holderSet(CommonTagPrefixes.RAW_MATERIALS, metal)
                    count = 3
                }
                primary {
                    +RagiumItems.getOrThrow(HTItemPart.DUST, metal)
                    count = 4
                }
                recipeId suffix "_from_raw"
            }.save(exporter)
        }

        // Book -> 3x Paper Pulp
        RagiumRecipeBuilders.crushing {
            ingredient { items { +Items.BOOK } }
            primary {
                +RagiumItems.getOrThrow(HTItemPart.DUST, VanillaMaterials.PAPER)
                count = 3
            }
            recipeId suffix "_from_book"
        }.save(exporter)
    }

    private fun cutting() {
        // Sapling -> Stick
        RagiumRecipeBuilders.cutting {
            ingredient { +holderSet(ItemTags.SAPLINGS) }
            primary { +Items.STICK }
            recipeId suffix "_from_saplings"
        }.save(exporter)
        // Book -> Leather + 3x Paper
        RagiumRecipeBuilders.cutting {
            ingredient { items { +Items.BOOK } }
            primary {
                +Items.PAPER
                count = 3
            }
            secondary { +Items.LEATHER }
            recipeId suffix "_from_book"
        }.save(exporter)
    }

    override fun getName(): String = "Mechanical Recipes"
}
