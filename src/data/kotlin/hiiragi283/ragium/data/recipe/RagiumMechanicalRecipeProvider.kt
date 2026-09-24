package hiiragi283.ragium.data.recipe

import hiiragi283.lib.color.HTColoredCollection
import hiiragi283.lib.color.VanillaColoredCollections
import hiiragi283.lib.data.recipe.HTRecipeProvider
import hiiragi283.lib.data.recipe.ingredient.HTItemIngredientBuilder
import hiiragi283.lib.registry.HTSimpleDeferredItem
import hiiragi283.lib.resource.debugPath
import hiiragi283.lib.resource.vanillaId
import hiiragi283.lib.tag.CommonTagPrefixes
import hiiragi283.lib.tag.HTCommonTags
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.recipe.builder.RagiumRecipeBuilders
import hiiragi283.ragium.api.material.HTItemPart
import hiiragi283.ragium.api.material.RagiumMaterial
import hiiragi283.ragium.api.tag.HTMachineType
import hiiragi283.ragium.api.tag.RagiumTags
import hiiragi283.ragium.common.block.RagiumBlocks
import hiiragi283.ragium.common.fluid.RagiumFluids
import hiiragi283.ragium.common.item.RagiumItems
import hiiragi283.ragium.common.material.RagiumMaterialHelper
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.tags.BlockTags
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.material.Fluids
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.crafting.BlockTagIngredient
import net.neoforged.neoforge.common.crafting.DifferenceIngredient
import java.util.concurrent.CompletableFuture

class RagiumMechanicalRecipeProvider(packOutput: PackOutput, future: CompletableFuture<HolderLookup.Provider>) :
    HTRecipeProvider(packOutput, future, RagiumAPI.MOD_ID) {
    override fun exportValues() {
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
            ingredient { items { +Items.BLACKSTONE } }
            extra {
                +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Metal.GOLD)
                count = 8
            }
            result { +Items.GILDED_BLACKSTONE }
        }.save(exporter)
        // Dirt + Leaves -> Podzol
        RagiumRecipeBuilders.assembling {
            ingredient { items { +Items.DIRT } }
            extra {
                +holderSet(ItemTags.LEAVES)
                count = 8
            }
            result { +Items.PODZOL }
        }.save(exporter)
        // Dirt + Mushroom -> Mycelium
        RagiumRecipeBuilders.assembling {
            ingredient { items { +Items.DIRT } }
            extra { +holderSet(Tags.Items.MUSHROOMS) }
            result { +Items.MYCELIUM }
        }.save(exporter)
        // Crimson Nylium
        RagiumRecipeBuilders.assembling {
            ingredient { +holderSet(Tags.Items.NETHERRACKS) }
            extra { items { +Items.CRIMSON_FUNGUS } }
            result { +Items.CRIMSON_NYLIUM }
        }.save(exporter)
        // Warped Nylium
        RagiumRecipeBuilders.assembling {
            ingredient { +holderSet(Tags.Items.NETHERRACKS) }
            extra { items { +Items.WARPED_FUNGUS } }
            result { +Items.WARPED_NYLIUM }
        }.save(exporter)
        // String + Sticky -> Cobweb
        RagiumRecipeBuilders.assembling {
            ingredient {
                +holderSet(Tags.Items.STRINGS)
                count = 5
            }
            extra { +holderSet(HTCommonTags.Items.STICKY_BALLS) }
            result { +Items.COBWEB }
        }.save(exporter)

        // XX Ingot + XX Nugget -> XX Chain
        setOf(
            RagiumMaterial.Metal.COPPER to Items.COPPER_CHAIN.unaffected(),
            RagiumMaterial.Metal.IRON to Items.IRON_CHAIN
        ).forEach { (metal: RagiumMaterial.Metal, chain: Item) ->
            RagiumRecipeBuilders.assembling {
                ingredient { +holderSet(CommonTagPrefixes.INGOT, metal) }
                extra {
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
            RagiumMaterial.Metal.COPPER to Items.COPPER_LANTERN.unaffected(),
            RagiumMaterial.Metal.IRON to Items.LANTERN
        ).forEach { (metal: RagiumMaterial.Metal, lantern: Item) ->
            RagiumRecipeBuilders.assembling {
                ingredient { +holderSet(CommonTagPrefixes.INGOT, metal) }
                extra { items { +Items.TORCH } }
                result {
                    +lantern
                    count = 2
                }
            }.save(exporter)
        }
        RagiumRecipeBuilders.assembling {
            ingredient { +holderSet(CommonTagPrefixes.INGOT, RagiumMaterial.Metal.IRON) }
            extra { items { +Items.SOUL_TORCH } }
            result {
                +Items.SOUL_LANTERN
                count = 2
            }
        }.save(exporter)
        // Repeater
        RagiumRecipeBuilders.assembling {
            ingredient { +holderSet(Tags.Items.STONES) }
            extra {
                items { +Items.REDSTONE_TORCH }
                count = 2
            }
            extra {
                +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Mineral.REDSTONE)
            }
            result {
                +Items.REPEATER
                count = 2
            }
        }.save(exporter)
        // Comparator
        RagiumRecipeBuilders.assembling {
            ingredient { +holderSet(Tags.Items.STONES) }
            extra {
                items { +Items.REDSTONE_TORCH }
                count = 3
            }
            extra {
                +dustOrGem(RagiumMaterial.Gem.QUARTZ)
            }
            result {
                +Items.COMPARATOR
                count = 2
            }
        }.save(exporter)
        // Dispenser
        RagiumRecipeBuilders.assembling {
            ingredient { items { +Items.DROPPER } }
            extra { +holderSet(Tags.Items.TOOLS_BOW) }
            result { +Items.DISPENSER }
        }.save(exporter)
        RagiumRecipeBuilders.assembling {
            ingredient { items { +Items.DROPPER } }
            extra {
                +holderSet(Tags.Items.RODS_WOODEN)
                count = 3
            }
            extra {
                +holderSet(Tags.Items.STRINGS)
                count = 3
            }
            result { +Items.DISPENSER }
            recipeId suffix "_stackable"
        }.save(exporter)
        // Rail
        RagiumRecipeBuilders.assembling {
            ingredient {
                +holderSet(CommonTagPrefixes.INGOT, RagiumMaterial.Metal.IRON)
                count = 6
            }
            extra { +holderSet(Tags.Items.RODS_WOODEN) }
            result {
                +Items.RAIL
                count = 24
            }
        }.save(exporter)
        RagiumRecipeBuilders.assembling {
            ingredient {
                +holderSet(CommonTagPrefixes.INGOT, RagiumMaterial.Metal.GOLD)
                count = 6
            }
            extra { +holderSet(Tags.Items.RODS_WOODEN) }
            extra { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Mineral.REDSTONE) }
            result {
                +Items.POWERED_RAIL
                count = 9
            }
        }.save(exporter)
        RagiumRecipeBuilders.assembling {
            ingredient {
                +holderSet(CommonTagPrefixes.INGOT, RagiumMaterial.Metal.IRON)
                count = 6
            }
            extra { +BlockTagIngredient(BlockTags.STONE_PRESSURE_PLATES) }
            extra { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Mineral.REDSTONE) }
            result {
                +Items.DETECTOR_RAIL
                count = 9
            }
        }.save(exporter)
        RagiumRecipeBuilders.assembling {
            ingredient {
                +holderSet(CommonTagPrefixes.INGOT, RagiumMaterial.Metal.IRON)
                count = 6
            }
            extra { +holderSet(Tags.Items.RODS_WOODEN) }
            extra {
                items { +Items.REDSTONE_TORCH }
                count = 2
            }
            result {
                +Items.ACTIVATOR_RAIL
                count = 9
            }
        }.save(exporter)
        // TNT
        RagiumRecipeBuilders.assembling {
            ingredient {
                +holderSet(Tags.Items.SANDS)
                count = 4
            }
            extra {
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
            ingredient { items { +Items.SKELETON_SKULL } }
            extra {
                items { +Items.ROTTEN_FLESH }
                count = 8
            }
            result { +Items.ZOMBIE_HEAD }
        }.save(exporter)
        RagiumRecipeBuilders.assembling {
            ingredient { items { +Items.SKELETON_SKULL } }
            extra {
                +holderSet(Tags.Items.GUNPOWDERS)
                count = 8
            }
            result { +Items.CREEPER_HEAD }
        }.save(exporter)
        RagiumRecipeBuilders.assembling {
            ingredient { items { +Items.SKELETON_SKULL } }
            extra {
                items { +Items.PORKCHOP }
                count = 8
            }
            result { +Items.PIGLIN_HEAD }
        }.save(exporter)

        // Gold Block + Apple -> Enchanted Golden Apple
        RagiumRecipeBuilders.assembling {
            ingredient { items { +Items.APPLE } }
            extra {
                +holderSet(CommonTagPrefixes.STORAGE_BLOCK, RagiumMaterial.Metal.GOLD)
                count = 8
            }
            result { +Items.ENCHANTED_GOLDEN_APPLE }
            time *= 8
        }.save(exporter)

        // Machine Casing
        RagiumRecipeBuilders.assembling {
            ingredient { items { +RagiumItems.getParts(HTMachineType.MECHANICAL) } }
            extra { items { +Items.MAGMA_BLOCK } }
            result { +RagiumItems.getParts(HTMachineType.HEAT) }
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
        for (color: DyeColor in DyeColor.entries) {
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
        for (fuel: RagiumMaterial.Fuel in RagiumMaterial.Fuel.entries) {
            val baseItem: HTSimpleDeferredItem = RagiumMaterialHelper.getFuelBase(fuel)
            RagiumRecipeBuilders.compressing {
                ingredient { +holderSet(CommonTagPrefixes.DUST, fuel) }
                result { +baseItem }
                recipeId suffix "_from_dust"
                condition { itemTagPresent(CommonTagPrefixes.DUST, fuel) }
            }.save(exporter)
        }
        setOf(
            RagiumMaterial.Other.WOOD to RagiumItems.PARTICLE_BOARD,
            RagiumMaterial.Other.PAPER to HTSimpleDeferredItem(vanillaId("paper"))
        ).forEach { (material: RagiumMaterial, item: HTSimpleDeferredItem) ->
            RagiumRecipeBuilders.compressing {
                ingredient {
                    +holderSet(CommonTagPrefixes.DUST, material)
                    count = 2
                }
                result { +item }
            }.save(exporter)
        }

        // 4x Blaze Powder -> Blaze Rod
        RagiumRecipeBuilders.compressing {
            ingredient {
                items { +Items.BLAZE_POWDER }
                count = 4
            }
            result { +Items.BLAZE_ROD }
        }.save(exporter)
        // 6x Wind Charge -> Breeze Rod
        RagiumRecipeBuilders.compressing {
            ingredient {
                items { +Items.WIND_CHARGE }
                count = 6
            }
            result { +Items.BREEZE_ROD }
        }.save(exporter)
    }

    private fun crushing() {
        crushMaterial()
        crushWood()
        crushOre()
        dyes()

        // XX Block -> XX
        setOf(
            Items.AMETHYST_BLOCK to Items.AMETHYST_SHARD,
            Items.BRICKS to Items.BRICK,
            Items.CLAY to Items.CLAY_BALL,
            Items.DRIPSTONE_BLOCK to Items.POINTED_DRIPSTONE,
            Items.GLOWSTONE to Items.GLOWSTONE_DUST,
            Items.HONEYCOMB_BLOCK to Items.HONEYCOMB,
            Items.MAGMA_BLOCK to Items.MAGMA_CREAM,
            Items.NETHER_BRICKS to Items.NETHER_BRICK,
            Items.PRISMARINE to Items.PRISMARINE_SHARD,
            Items.PURPUR_BLOCK to Items.POPPED_CHORUS_FRUIT,
            Items.SNOW_BLOCK to Items.SNOWBALL,
            RagiumBlocks.ECHO_BLOCK to Items.ECHO_SHARD,
            RagiumBlocks.FLUORITE_BLOCK to RagiumItems.getOrThrow(HTItemPart.GEM, RagiumMaterial.Gem.FLUORITE),
            RagiumBlocks.CRYOLITE_BLOCK to RagiumItems.getOrThrow(HTItemPart.GEM, RagiumMaterial.Gem.CRYOLITE)
        ).forEach { (block: ItemLike, base: ItemLike) ->
            RagiumRecipeBuilders.crushing {
                ingredient { items { +block.asItem() } }
                result {
                    +base.asItem()
                    count = 4
                }
            }.save(exporter)
        }
        setOf(
            RagiumTags.BlockItem.QUARTZ_BLOCKS.item to Items.QUARTZ
        ).forEach { (block: TagKey<Item>, base: Item) ->
            RagiumRecipeBuilders.crushing {
                ingredient { +holderSet(block) }
                result {
                    +base
                    count = 4
                }
            }.save(exporter)
        }
        // Sandstone -> 4x Sand + Niter
        RagiumRecipeBuilders.crushing {
            ingredient { +holderSet(Tags.Items.SANDSTONE_UNCOLORED_BLOCKS) }
            result {
                +Items.SAND
                count = 4
            }
            result {
                +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Mineral.NITER)
            }
        }.save(exporter)
        // Red Sandstone -> 4x Red Sand + Bauxite
        RagiumRecipeBuilders.crushing {
            ingredient { +holderSet(Tags.Items.SANDSTONE_RED_BLOCKS) }
            result {
                +Items.RED_SAND
                count = 4
            }
            result {
                +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Mineral.BAUXITE)
            }
        }.save(exporter)
        // Blue Ice -> 9x Packed Ice + Cryolite
        RagiumRecipeBuilders.crushing {
            ingredient { items { +Items.BLUE_ICE } }
            result {
                +Items.PACKED_ICE
                count = 9
            }
            result {
                +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Gem.CRYOLITE)
            }
        }.save(exporter)

        // Book -> 3x Paper Pulp
        RagiumRecipeBuilders.crushing {
            ingredient { items { +Items.BOOK } }
            result {
                +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Other.PAPER)
                count = 3
            }
            recipeId suffix "_from_book"
        }.save(exporter)
        // Blaze Rod -> 4x Blaze Powder
        RagiumRecipeBuilders.crushing {
            ingredient { +holderSet(Tags.Items.RODS_BLAZE) }
            result {
                +Items.BLAZE_POWDER
                count = 4
            }
        }.save(exporter)
        // Breeze Rod -> 6x Wind Charge
        RagiumRecipeBuilders.crushing {
            ingredient { +holderSet(Tags.Items.RODS_BREEZE) }
            result {
                +Items.WIND_CHARGE
                count = 6
            }
        }.save(exporter)
    }

    private fun crushMaterial() {
        // XX Dust
        RagiumRecipeBuilders.crushing {
            ingredient { items { +Items.COAL } }
            result { +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Fuel.COAL) }
        }.save(exporter)
        RagiumRecipeBuilders.crushing {
            ingredient { items { +Items.CHARCOAL } }
            result { +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Fuel.CHARCOAL) }
        }.save(exporter)
        RagiumRecipeBuilders.crushing {
            ingredient { +holderSet(RagiumTags.Items.COKES) }
            result { +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Other.CARBON) }
        }.save(exporter)

        for (gem: RagiumMaterial.Gem in RagiumMaterial.Gem.entries) {
            val dust: HTSimpleDeferredItem = RagiumItems.MATERIAL_ITEMS[HTItemPart.DUST, gem] ?: continue
            RagiumRecipeBuilders.crushing {
                ingredient { +holderSet(CommonTagPrefixes.GEM, gem) }
                result { +dust }
                recipeId suffix "_from_gem"
            }.save(exporter)
        }

        for (metal: RagiumMaterial.Metal in RagiumMaterial.Metal.entries) {
            val dust: HTSimpleDeferredItem = RagiumItems.MATERIAL_ITEMS[HTItemPart.DUST, metal] ?: continue
            RagiumRecipeBuilders.crushing {
                ingredient { +holderSet(CommonTagPrefixes.INGOT, metal) }
                result { +dust }
                recipeId suffix "_from_ingot"
            }.save(exporter)
        }

        RagiumRecipeBuilders.crushing {
            ingredient { +holderSet(Tags.Items.GLASS_BLOCKS) }
            result { +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Other.GLASS) }
            recipeId suffix "_from_block"
        }.save(exporter)
        RagiumRecipeBuilders.crushing {
            ingredient {
                +holderSet(Tags.Items.GLASS_PANES)
                count = 8
            }
            result {
                +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Other.GLASS)
                count = 3
            }
            recipeId suffix "_from_pane"
        }.save(exporter)
        RagiumRecipeBuilders.crushing {
            ingredient { +holderSet(Tags.Items.OBSIDIANS_NORMAL) }
            result { +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Other.OBSIDIAN) }
        }.save(exporter)
        // Gear -> Dust
        for (material: RagiumMaterial in RagiumMaterial.entries) {
            val dust: HTSimpleDeferredItem = RagiumItems.MATERIAL_ITEMS[HTItemPart.DUST, material] ?: continue
            if (RagiumItems.MATERIAL_ITEMS.contains(HTItemPart.GEAR, material)) {
                RagiumRecipeBuilders.crushing {
                    ingredient { +holderSet(CommonTagPrefixes.GEAR, material) }
                    result {
                        +dust
                        count = 4
                    }
                    recipeId suffix "_from_gear"
                }.save(exporter)
            }
        }
    }

    private fun crushWood() {
        val woodPulp: HTSimpleDeferredItem = RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Other.WOOD)
        setOf(
            Triple(ItemTags.PLANKS, 1, 1),
            Triple(ItemTags.WOODEN_BUTTONS, 1, 1),
            Triple(ItemTags.WOODEN_DOORS, 1, 2),
            Triple(ItemTags.WOODEN_STAIRS, 2, 3),
            Triple(ItemTags.WOODEN_SLABS, 2, 1),
            Triple(ItemTags.WOODEN_PRESSURE_PLATES, 1, 2),
            Triple(ItemTags.WOODEN_SHELVES, 1, 6),
            Triple(ItemTags.LOGS, 1, 6),
            Triple(ItemTags.WOODEN_TRAPDOORS, 3, 1),
            Triple(ItemTags.SIGNS, 1, 2),
            Triple(ItemTags.HANGING_SIGNS, 1, 6),
            Triple(ItemTags.CHEST_BOATS, 1, 13),
            Triple(Tags.Items.BARRELS_WOODEN, 1, 7),
            Triple(Tags.Items.CHESTS_WOODEN, 1, 8),
            Triple(Tags.Items.FENCE_GATES_WOODEN, 1, 4),
            Triple(Tags.Items.FENCES_WOODEN, 1, 1),
            Triple(Tags.Items.RODS_WOODEN, 2, 1),
            Triple(CommonTagPrefixes.GEAR.itemTagKey(RagiumMaterial.Other.WOOD), 4, 1)
        ).forEach { (input: TagKey<Item>, inputCount: Int, outputCount: Int) ->
            RagiumRecipeBuilders.crushing {
                ingredient {
                    +holderSet(input)
                    count = inputCount
                }
                result {
                    +woodPulp
                    count = outputCount
                }
                recipeId suffix "_from_${input.location().debugPath}"
            }.save(exporter)
        }

        RagiumRecipeBuilders.crushing {
            ingredient {
                +DifferenceIngredient.of(
                    Ingredient.of(holderSet(ItemTags.BOATS)),
                    Ingredient.of(holderSet(ItemTags.CHEST_BOATS))
                )
            }
            result {
                +woodPulp
                count = 5
            }
            recipeId suffix "_from_boats"
        }.save(exporter)
    }

    private fun crushOre() {
        // XX Ore -> XX Dust
        RagiumRecipeBuilders.crushing {
            ingredient { +holderSet(CommonTagPrefixes.ORE, RagiumMaterial.Fuel.COAL) }
            result {
                +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Fuel.COAL)
                count = 2
            }
            result {
                +RagiumItems.getOrThrow(HTItemPart.TINY, RagiumMaterial.Fuel.COAL)
                count = 3
            }
            recipeId suffix "_from_ore"
        }.save(exporter)
        RagiumRecipeBuilders.crushing {
            ingredient { +holderSet(CommonTagPrefixes.ORE, RagiumMaterial.Mineral.REDSTONE) }
            result {
                +Items.REDSTONE
                count = 6
            }
            result {
                +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Mineral.RAGINITE)
            }
            recipeId suffix "_from_ore"
        }.save(exporter)

        RagiumRecipeBuilders.crushing {
            ingredient { +holderSet(CommonTagPrefixes.ORE, RagiumMaterial.Gem.LAPIS) }
            result {
                +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Gem.LAPIS)
                count = 6
            }
            recipeId suffix "_from_ore"
        }.save(exporter)
        RagiumRecipeBuilders.crushing {
            ingredient { +holderSet(CommonTagPrefixes.ORE, RagiumMaterial.Gem.QUARTZ) }
            result {
                +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Gem.QUARTZ)
                count = 4
            }
            result {
                +Items.GOLD_NUGGET
            }
            recipeId suffix "_from_ore"
        }.save(exporter)
        RagiumRecipeBuilders.crushing {
            ingredient { +holderSet(CommonTagPrefixes.ORE, RagiumMaterial.Gem.DIAMOND) }
            result {
                +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Gem.DIAMOND)
                count = 2
            }
            recipeId suffix "_from_ore"
        }.save(exporter)
        RagiumRecipeBuilders.crushing {
            ingredient { +holderSet(CommonTagPrefixes.ORE, RagiumMaterial.Gem.EMERALD) }
            result {
                +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Gem.EMERALD)
                count = 2
            }
            recipeId suffix "_from_ore"
        }.save(exporter)

        RagiumRecipeBuilders.crushing {
            ingredient { +holderSet(CommonTagPrefixes.ORE, RagiumMaterial.Metal.COPPER) }
            result {
                +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Metal.COPPER)
                count = 3
            }
            result {
                +Items.GOLD_NUGGET
                count = 3
            }
            recipeId suffix "_from_ore"
        }.save(exporter)
        RagiumRecipeBuilders.crushing {
            ingredient { +holderSet(CommonTagPrefixes.ORE, RagiumMaterial.Metal.IRON) }
            result {
                +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Metal.IRON)
                count = 2
            }
            result { +Items.FLINT }
            recipeId suffix "_from_ore"
        }.save(exporter)
        RagiumRecipeBuilders.crushing {
            ingredient { +holderSet(CommonTagPrefixes.ORE, RagiumMaterial.Metal.GOLD) }
            result {
                +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Metal.GOLD)
                count = 2
            }
            recipeId suffix "_from_ore"
        }.save(exporter)
        // Raw XX -> XX Dust
        for (metal: RagiumMaterial.Metal in RagiumMaterial.Metal.entries) {
            if (!metal.hasRawVariant) continue
            val dust: HTSimpleDeferredItem = RagiumItems.MATERIAL_ITEMS[HTItemPart.DUST, metal] ?: continue
            RagiumRecipeBuilders.crushing {
                ingredient {
                    +holderSet(CommonTagPrefixes.RAW_MATERIALS, metal)
                    count = 3
                }
                result {
                    +dust
                    count = 4
                }
                recipeId suffix "_from_raw"
                condition { itemTagPresent(CommonTagPrefixes.RAW_MATERIALS, metal) }
            }.save(exporter)
        }

        // Ragium
        for (mineral: RagiumMaterial.Mineral in setOf(RagiumMaterial.Mineral.SULFUR, RagiumMaterial.Mineral.NITER)) {
            RagiumRecipeBuilders.crushing {
                ingredient { +holderSet(CommonTagPrefixes.ORE, mineral) }
                result {
                    +RagiumItems.getOrThrow(HTItemPart.DUST, mineral)
                    count = 6
                }
                recipeId suffix "_from_ore"
            }.save(exporter)
        }
    }

    private fun dyes() {
        // Single
        val single = HTColoredCollection<(HTItemIngredientBuilder.() -> Unit)?>(
            white = {
                items {
                    +Items.BONE_MEAL
                    +Items.LILY_OF_THE_VALLEY
                }
            },
            orange = {
                items {
                    +Items.ORANGE_TULIP
                    +Items.TORCHFLOWER
                    +Items.OPEN_EYEBLOSSOM
                }
            },
            magenta = { items { +Items.ALLIUM } },
            lightBlue = { items { +Items.BLUE_ORCHID } },
            yellow = {
                items {
                    +Items.DANDELION
                    +Items.GOLDEN_DANDELION
                    +Items.WILDFLOWERS
                }
            },
            lime = { items { +Items.SEA_PICKLE } },
            pink = {
                items {
                    +Items.CACTUS_FLOWER
                    +Items.PINK_PETALS
                    +Items.PINK_TULIP
                }
            },
            gray = { items { +Items.CLOSED_EYEBLOSSOM } },
            lightGray = {
                items {
                    +Items.AZURE_BLUET
                    +Items.OXEYE_DAISY
                    +Items.WHITE_TULIP
                }
            },
            cyan = null,
            purple = null,
            blue = { items { +Items.CORNFLOWER } },
            brown = { items { +Items.COCOA_BEANS } },
            green = { items { +Items.CACTUS } },
            red = {
                items {
                    +Items.POPPY
                    +Items.BEETROOT
                }
            },
            black = {
                items {
                    +Items.INK_SAC
                    +Items.WITHER_ROSE
                }
            }
        )
        for (color: DyeColor in DyeColor.entries) {
            val builder: HTItemIngredientBuilder.() -> Unit = single[color] ?: continue
            RagiumRecipeBuilders.crushing {
                ingredient(builder)
                result {
                    +VanillaColoredCollections.DYE[color]
                    count = 2
                }
            }.save(exporter)
        }
        // Double
        RagiumRecipeBuilders.crushing {
            ingredient { items { +Items.ROSE_BUSH } }
            result {
                +Items.RED_DYE
                count = 4
            }
            recipeId prefix "double_"
        }.save(exporter)
        RagiumRecipeBuilders.crushing {
            ingredient { items { +Items.SUNFLOWER } }
            result {
                +Items.YELLOW_DYE
                count = 4
            }
            recipeId prefix "double_"
        }.save(exporter)
        RagiumRecipeBuilders.crushing {
            ingredient { items { +Items.PITCHER_PLANT } }
            result {
                +Items.CYAN_DYE
                count = 4
            }
            recipeId prefix "double_"
        }.save(exporter)
        RagiumRecipeBuilders.crushing {
            ingredient { items { +Items.LILAC } }
            result {
                +Items.PINK_DYE
                count = 4
            }
            recipeId prefix "double_"
        }.save(exporter)
    }

    private fun cutting() {
        // Sapling -> Stick
        RagiumRecipeBuilders.cutting {
            ingredient { +holderSet(ItemTags.SAPLINGS) }
            result { +Items.STICK }
            recipeId suffix "_from_saplings"
        }.save(exporter)
        // Book -> Leather + 3x Paper
        RagiumRecipeBuilders.cutting {
            ingredient { items { +Items.BOOK } }
            result {
                +Items.PAPER
                count = 3
            }
            result { +Items.LEATHER }
            recipeId suffix "_from_book"
        }.save(exporter)
        // Melon -> Sliced Melon
        RagiumRecipeBuilders.cutting {
            ingredient { items { +Items.MELON } }
            result {
                +Items.MELON_SLICE
                count = 9
            }
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
            recipeId replace id("honey_from_bottle")
        }.save(exporter)
        // Wet Sponge -> Sponge + Water
        RagiumRecipeBuilders.draining {
            ingredient { items { +Items.WET_SPONGE } }
            itemResult { +Items.SPONGE }
            fluidResult { +Fluids.WATER }
        }.save(exporter)

        // Honeycomb -> Beeswax + Honey
        RagiumRecipeBuilders.draining {
            ingredient { items { +Items.HONEYCOMB } }
            itemResult { +RagiumItems.BEESWAX }
            fluidResult { +RagiumFluids.HONEY }
            recipeId replace id("honey_from_comb")
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
        for (color: DyeColor in DyeColor.entries) {
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
