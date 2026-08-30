package hiiragi283.ragium.data.recipe

import hiiragi283.lib.color.HTDefaultColor
import hiiragi283.lib.color.VanillaColoredCollections
import hiiragi283.lib.data.recipe.HTRecipeProvider
import hiiragi283.lib.material.HTMaterialKey
import hiiragi283.lib.material.part.CommonParts
import hiiragi283.lib.tag.CommonTagPrefixes
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.recipe.RagiumRecipeBuilders
import hiiragi283.ragium.api.tag.RagiumTags
import hiiragi283.ragium.common.fluid.RagiumFluids
import hiiragi283.ragium.common.item.RagiumItems
import hiiragi283.ragium.common.material.VanillaMaterialKeys
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.material.Fluids
import net.neoforged.neoforge.common.Tags

class RagiumMechanicalRecipeProvider(packOutput: PackOutput, future: CompletableFuture<HolderLookup.Provider>) : HTRecipeProvider(packOutput, future, RagiumAPI.MOD_ID) {
    override fun buildRecipes() {
        assembling()
        compressing()
        crushing()
        cutting()
        draining()
        filling()
    }

    private fun assembling() {
        // Blackstone + Gold -> Gilded Blackstone
        RagiumRecipeBuilders.assembling {
            primary { items { +Items.BLACKSTONE } }
            secondary {
                +holderSet(CommonTagPrefixes.DUST, VanillaMaterialKeys.GOLD)
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
            VanillaMaterialKeys.COPPER to Items.COPPER_CHAIN.unaffected(),
            VanillaMaterialKeys.IRON to Items.IRON_CHAIN,
        ).forEach { (metal: HTMaterialKey, chain: Item) ->
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
            VanillaMaterialKeys.COPPER to Items.COPPER_LANTERN.unaffected(),
            VanillaMaterialKeys.IRON to Items.LANTERN,
        ).forEach { (metal: HTMaterialKey, lantern: Item) ->
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
            primary { +holderSet(CommonTagPrefixes.INGOT, VanillaMaterialKeys.IRON) }
            secondary { items { +Items.SOUL_TORCH } }
            result {
                +Items.SOUL_LANTERN
                count = 2
            }
        }.save(exporter)
        // Iron Ingot + Chest -> Hopper
        RagiumRecipeBuilders.assembling {
            primary {
                +holderSet(CommonTagPrefixes.INGOT, VanillaMaterialKeys.IRON)
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

        RagiumRecipeBuilders.compressing {
            ingredient {
                +holderSet(CommonTagPrefixes.DUST, VanillaMaterialKeys.WOOD)
                count = 2
            }
            result { +RagiumItems.PARTICLE_BOARD }
        }.save(exporter)
    }

    private fun crushing() {
        // XX Dust
        RagiumRecipeBuilders.crushing {
            ingredient { +holderSet(ItemTags.PLANKS) }
            primary { +(CommonParts.DUST to VanillaMaterialKeys.WOOD) }
            recipeId suffix "_from_planks"
        }.save(exporter)
        RagiumRecipeBuilders.crushing {
            ingredient { +holderSet(Tags.Items.GLASS_BLOCKS) }
            primary { +(CommonParts.DUST to VanillaMaterialKeys.GLASS) }
            recipeId suffix "_from_block"
        }.save(exporter)
        RagiumRecipeBuilders.crushing {
            ingredient {
                +holderSet(Tags.Items.GLASS_PANES)
                count = 8
            }
            primary {
                +(CommonParts.DUST to VanillaMaterialKeys.GLASS)
                count = 3
            }
            recipeId suffix "_from_pane"
        }.save(exporter)
        RagiumRecipeBuilders.crushing {
            ingredient { +holderSet(Tags.Items.OBSIDIANS_NORMAL) }
            primary { +(CommonParts.DUST to VanillaMaterialKeys.OBSIDIAN) }
        }.save(exporter)

        // XX Ore -> XX Dust
        RagiumRecipeBuilders.crushing {
            ingredient { +holderSet(CommonTagPrefixes.ORE, VanillaMaterialKeys.COAL) }
            primary {
                +(CommonParts.DUST to VanillaMaterialKeys.COAL)
                count = 2
            }
            secondary {
                +(CommonParts.TINY to VanillaMaterialKeys.COAL)
                count = 3
            }
            recipeId suffix "_from_ore"
        }.save(exporter)
        RagiumRecipeBuilders.crushing {
            ingredient { +holderSet(CommonTagPrefixes.ORE, VanillaMaterialKeys.REDSTONE) }
            primary {
                +Items.REDSTONE
                count = 6
            }
            recipeId suffix "_from_ore"
        }.save(exporter)

        RagiumRecipeBuilders.crushing {
            ingredient { +holderSet(CommonTagPrefixes.ORE, VanillaMaterialKeys.LAPIS) }
            primary {
                +(CommonParts.DUST to VanillaMaterialKeys.LAPIS)
                count = 6
            }
            recipeId suffix "_from_ore"
        }.save(exporter)
        RagiumRecipeBuilders.crushing {
            ingredient { +holderSet(CommonTagPrefixes.ORE, VanillaMaterialKeys.QUARTZ) }
            primary {
                +(CommonParts.DUST to VanillaMaterialKeys.QUARTZ)
                count = 4
            }
            secondary {
                +Items.GOLD_NUGGET
            }
            recipeId suffix "_from_ore"
        }.save(exporter)
        RagiumRecipeBuilders.crushing {
            ingredient { +holderSet(CommonTagPrefixes.ORE, VanillaMaterialKeys.DIAMOND) }
            primary {
                +(CommonParts.DUST to VanillaMaterialKeys.DIAMOND)
                count = 2
            }
            recipeId suffix "_from_ore"
        }.save(exporter)
        RagiumRecipeBuilders.crushing {
            ingredient { +holderSet(CommonTagPrefixes.ORE, VanillaMaterialKeys.EMERALD) }
            primary {
                +(CommonParts.DUST to VanillaMaterialKeys.EMERALD)
                count = 2
            }
            recipeId suffix "_from_ore"
        }.save(exporter)

        RagiumRecipeBuilders.crushing {
            ingredient { +holderSet(CommonTagPrefixes.ORE, VanillaMaterialKeys.COPPER) }
            primary {
                +(CommonParts.DUST to VanillaMaterialKeys.COPPER)
                count = 3
            }
            secondary {
                +Items.GOLD_NUGGET
                count = 3
            }
            recipeId suffix "_from_ore"
        }.save(exporter)
        RagiumRecipeBuilders.crushing {
            ingredient { +holderSet(CommonTagPrefixes.ORE, VanillaMaterialKeys.IRON) }
            primary {
                +(CommonParts.DUST to VanillaMaterialKeys.IRON)
                count = 2
            }
            secondary { +Items.FLINT }
            recipeId suffix "_from_ore"
        }.save(exporter)
        RagiumRecipeBuilders.crushing {
            ingredient { +holderSet(CommonTagPrefixes.ORE, VanillaMaterialKeys.GOLD) }
            primary {
                +(CommonParts.DUST to VanillaMaterialKeys.GOLD)
                count = 2
            }
            recipeId suffix "_from_ore"
        }.save(exporter)
        // Raw XX -> XX Dust
        for (metal: HTMaterialKey in setOf(VanillaMaterialKeys.COPPER, VanillaMaterialKeys.IRON, VanillaMaterialKeys.GOLD)) {
            RagiumRecipeBuilders.crushing {
                ingredient {
                    +holderSet(CommonTagPrefixes.RAW_MATERIALS, metal)
                    count = 3
                }
                primary {
                    +(CommonParts.DUST to metal)
                    count = 4
                }
                recipeId suffix "_from_raw"
            }.save(exporter)
        }

        // Book -> 3x Paper Pulp
        RagiumRecipeBuilders.crushing {
            ingredient { items { +Items.BOOK } }
            primary {
                +(CommonParts.DUST to VanillaMaterialKeys.PAPER)
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

    private fun draining() {
        // Honey Bottle -> Glass Bottle + Honey
        RagiumRecipeBuilders.draining {
            ingredient { +holderSet(Tags.Items.DRINKS_HONEY) }
            itemResult { +Items.GLASS_BOTTLE }
            fluidResult {
                +RagiumFluids.HONEY
                amount = 250
            }
            recipeId replace "honey_from_bottle"
        }.save(exporter)
        // Wet Sponge -> Sponge + Water
        RagiumRecipeBuilders.draining {
            ingredient { items { +Items.WET_SPONGE } }
            itemResult { +Items.SPONGE }
            fluidResult { +Fluids.WATER }
        }.save(exporter)
    }

    private fun filling() {
        // Honey Bottle <- Glass Bottle + Honey
        RagiumRecipeBuilders.filling {
            itemIngredient { items { +Items.GLASS_BOTTLE } }
            fluidIngredient {
                +holderSet(RagiumFluids.HONEY)
                amount = 250
            }
            result { +Items.HONEY_BOTTLE }
        }.save(exporter)
        // Dirt + Water -> Mud
        RagiumRecipeBuilders.filling {
            itemIngredient { items { +Items.DIRT } }
            fluidIngredient {
                +waterSet()
                amount = 250
            }
            result { +Items.MUD }
        }.save(exporter)

        // XX Concrete Powder + Water -> XX Concrete
        for (color: HTDefaultColor in HTDefaultColor.entries) {
            RagiumRecipeBuilders.filling {
                itemIngredient { items { +VanillaColoredCollections.CONCRETE_POWDER[color] } }
                fluidIngredient {
                    +waterSet()
                    amount = 10
                }
                result { +VanillaColoredCollections.CONCRETE[color] }
                recipeId suffix "_from_powder"
            }.save(exporter)
        }
    }

    override fun getName(): String = "Mechanical Recipes"
}
