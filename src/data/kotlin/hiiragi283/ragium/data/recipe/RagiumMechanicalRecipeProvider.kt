package hiiragi283.ragium.data.recipe

import hiiragi283.lib.collection.base
import hiiragi283.lib.collection.get
import hiiragi283.lib.data.recipe.HTRecipeProvider
import hiiragi283.lib.data.recipe.builder.ingredient
import hiiragi283.lib.data.recipe.builder.result
import hiiragi283.lib.data.recipe.ingredient.HTItemIngredientBuilder
import hiiragi283.lib.registry.HTSimpleDeferredItem
import hiiragi283.lib.resource.debugPath
import hiiragi283.lib.tag.CommonTagPrefixes
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.recipe.builder.RagiumRecipeBuilders
import hiiragi283.ragium.api.material.HTItemPart
import hiiragi283.ragium.api.material.RagiumMaterial
import hiiragi283.ragium.api.tag.RagiumTags
import hiiragi283.ragium.common.block.RagiumBlocks
import hiiragi283.ragium.common.fluid.RagiumFluids
import hiiragi283.ragium.common.item.RagiumItems
import hiiragi283.ragium.common.material.RagiumMaterialHelper
import net.minecraft.references.ItemIds
import net.minecraft.tags.BlockItemTagId
import net.minecraft.tags.BlockTags
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.ColorCollection
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.crafting.BlockTagIngredient
import net.neoforged.neoforge.common.crafting.CompoundIngredient
import net.neoforged.neoforge.common.crafting.DifferenceIngredient
import java.util.Optional

data object RagiumMechanicalRecipeProvider : HTRecipeProvider(RagiumAPI.MOD_ID) {
    override fun register() {
        assembling()
        centrifuging()
        compressing()
        crushing()
        cutting()
        pressing()

        filling()
    }

    private fun assembling() {
        // Blackstone + Gold -> Gilded Blackstone
        RagiumRecipeBuilders.assembling {
            ingredient { items { +Items.BLACKSTONE } }
            ingredient {
                +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Metal.GOLD)
                count = 8
            }
            result { +Items.GILDED_BLACKSTONE }
        }.save(context)
        // Dirt + Leaves -> Podzol
        RagiumRecipeBuilders.assembling {
            ingredient { items { +Items.DIRT } }
            ingredient {
                +holderSet(ItemTags.LEAVES)
                count = 8
            }
            result { +Items.PODZOL }
        }.save(context)
        // Dirt + Mushroom -> Mycelium
        RagiumRecipeBuilders.assembling {
            ingredient { items { +Items.DIRT } }
            ingredient { +holderSet(Tags.Items.MUSHROOMS) }
            result { +Items.MYCELIUM }
        }.save(context)
        // Crimson Nylium
        RagiumRecipeBuilders.assembling {
            ingredient { +itemSet(Tags.BlockItems.NETHERRACKS) }
            ingredient { items { +Items.CRIMSON_FUNGUS } }
            result { +Items.CRIMSON_NYLIUM }
        }.save(context)
        // Warped Nylium
        RagiumRecipeBuilders.assembling {
            ingredient { +itemSet(Tags.BlockItems.NETHERRACKS) }
            ingredient { items { +Items.WARPED_FUNGUS } }
            result { +Items.WARPED_NYLIUM }
        }.save(context)
        // String + Slime Ball -> Cobweb
        RagiumRecipeBuilders.assembling {
            ingredient {
                +holderSet(Tags.Items.STRINGS)
                count = 5
            }
            ingredient { +holderSet(Tags.Items.SLIME_BALLS) }
            result { +Items.COBWEB }
        }.save(context)

        // XX Ingot + XX Nugget -> XX Chain
        setOf(
            RagiumMaterial.Metal.COPPER to Items.COPPER_CHAIN.base(),
            RagiumMaterial.Metal.IRON to Items.IRON_CHAIN
        ).forEach { (metal: RagiumMaterial.Metal, chain: Item) ->
            RagiumRecipeBuilders.assembling {
                ingredient { +holderSet(CommonTagPrefixes.INGOT, metal) }
                ingredient {
                    +holderSet(CommonTagPrefixes.NUGGET, metal)
                    count = 3
                }
                result {
                    +chain
                    count = 3
                }
            }.save(context)
        }
        // XX Ingot + Torch -> XX Lantern
        setOf(
            RagiumMaterial.Metal.COPPER to Items.COPPER_LANTERN.base(),
            RagiumMaterial.Metal.IRON to Items.LANTERN
        ).forEach { (metal: RagiumMaterial.Metal, lantern: Item) ->
            RagiumRecipeBuilders.assembling {
                ingredient { +holderSet(CommonTagPrefixes.INGOT, metal) }
                ingredient { items { +Items.TORCH } }
                result {
                    +lantern
                    count = 2
                }
            }.save(context)
        }
        RagiumRecipeBuilders.assembling {
            ingredient { +holderSet(CommonTagPrefixes.INGOT, RagiumMaterial.Metal.IRON) }
            ingredient { items { +Items.SOUL_TORCH } }
            result {
                +Items.SOUL_LANTERN
                count = 2
            }
        }.save(context)
        // Repeater
        RagiumRecipeBuilders.assembling {
            ingredient { +itemSet(Tags.BlockItems.STONES) }
            ingredient {
                items { +Items.REDSTONE_TORCH }
                count = 2
            }
            ingredient {
                +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Mineral.REDSTONE)
            }
            result {
                +Items.REPEATER
                count = 2
            }
        }.save(context)
        // Comparator
        RagiumRecipeBuilders.assembling {
            ingredient { +itemSet(Tags.BlockItems.STONES) }
            ingredient {
                items { +Items.REDSTONE_TORCH }
                count = 3
            }
            ingredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Chemicals.SILICA) }
            result {
                +Items.COMPARATOR
                count = 2
            }
        }.save(context)
        // Dispenser
        RagiumRecipeBuilders.assembling {
            ingredient { items { +Items.DROPPER } }
            ingredient { +holderSet(Tags.Items.TOOLS_BOW) }
            result { +Items.DISPENSER }
        }.save(context)
        RagiumRecipeBuilders.assembling {
            ingredient { items { +Items.DROPPER } }
            ingredient {
                +holderSet(Tags.Items.RODS_WOODEN)
                count = 3
            }
            ingredient {
                +holderSet(Tags.Items.STRINGS)
                count = 3
            }
            result { +Items.DISPENSER }
            recipeId suffix "_stackable"
        }.save(context)
        // Rail
        RagiumRecipeBuilders.assembling {
            ingredient {
                +holderSet(CommonTagPrefixes.INGOT, RagiumMaterial.Metal.IRON)
                count = 6
            }
            ingredient { +holderSet(Tags.Items.RODS_WOODEN) }
            result {
                +Items.RAIL
                count = 24
            }
        }.save(context)
        RagiumRecipeBuilders.assembling {
            ingredient {
                +holderSet(CommonTagPrefixes.INGOT, RagiumMaterial.Metal.GOLD)
                count = 6
            }
            ingredient { +holderSet(Tags.Items.RODS_WOODEN) }
            ingredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Mineral.REDSTONE) }
            result {
                +Items.POWERED_RAIL
                count = 9
            }
        }.save(context)
        RagiumRecipeBuilders.assembling {
            ingredient {
                +holderSet(CommonTagPrefixes.INGOT, RagiumMaterial.Metal.IRON)
                count = 6
            }
            ingredient { +BlockTagIngredient(BlockTags.STONE_PRESSURE_PLATES) }
            ingredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Mineral.REDSTONE) }
            result {
                +Items.DETECTOR_RAIL
                count = 9
            }
        }.save(context)
        RagiumRecipeBuilders.assembling {
            ingredient {
                +holderSet(CommonTagPrefixes.INGOT, RagiumMaterial.Metal.IRON)
                count = 6
            }
            ingredient { +holderSet(Tags.Items.RODS_WOODEN) }
            ingredient {
                items { +Items.REDSTONE_TORCH }
                count = 2
            }
            result {
                +Items.ACTIVATOR_RAIL
                count = 9
            }
        }.save(context)
        // TNT
        RagiumRecipeBuilders.assembling {
            ingredient {
                +itemSet(Tags.BlockItems.SANDS)
                count = 4
            }
            ingredient {
                +holderSet(Tags.Items.GUNPOWDERS)
                count = 5
            }
            result {
                +Items.TNT
                count = 2
            }
        }.save(context)
        // Head
        RagiumRecipeBuilders.assembling {
            ingredient { items { +Items.SKELETON_SKULL } }
            ingredient {
                items { +Items.ROTTEN_FLESH }
                count = 8
            }
            result { +Items.ZOMBIE_HEAD }
        }.save(context)
        RagiumRecipeBuilders.assembling {
            ingredient { items { +Items.SKELETON_SKULL } }
            ingredient {
                +holderSet(Tags.Items.GUNPOWDERS)
                count = 8
            }
            result { +Items.CREEPER_HEAD }
        }.save(context)
        RagiumRecipeBuilders.assembling {
            ingredient { items { +Items.SKELETON_SKULL } }
            ingredient {
                items { +Items.PORKCHOP }
                count = 8
            }
            result { +Items.PIGLIN_HEAD }
        }.save(context)

        // Gold Block + Apple -> Enchanted Golden Apple
        RagiumRecipeBuilders.assembling {
            ingredient { items { +Items.APPLE } }
            ingredient {
                +holderSet(CommonTagPrefixes.STORAGE_BLOCK, RagiumMaterial.Metal.GOLD)
                count = 8
            }
            result { +Items.ENCHANTED_GOLDEN_APPLE }
            time *= 8
        }.save(context)
    }

    private fun centrifuging() {
        // Honey Bottle -> Glass Bottle + Honey
        RagiumRecipeBuilders.centrifuging {
            ingredient { +holderSet(Tags.Items.DRINKS_HONEY) }
            itemResult { +Items.GLASS_BOTTLE }
            fluidResult {
                +RagiumFluids.HONEY
                amount = 250
            }
            recipeId replace id("honey_from_bottle")
        }.save(context)
        // Wet Sponge -> Sponge + Water
        RagiumRecipeBuilders.centrifuging {
            ingredient { items { +Items.WET_SPONGE } }
            itemResult { +Items.SPONGE }
            fluidResult { water() }
        }.save(context)

        // Honeycomb -> Beeswax + Honey
        RagiumRecipeBuilders.centrifuging {
            ingredient { items { +Items.HONEYCOMB } }
            itemResult { +RagiumItems.BEESWAX }
            fluidResult {
                +RagiumFluids.HONEY
                amount = 250
            }
            recipeId replace id("honey_from_comb")
        }.save(context)
    }

    private fun compressing() {
        // Snow -> Snow Block
        RagiumRecipeBuilders.compressing {
            ingredient {
                items { +Items.SNOW }
                count = 8
            }
            result { +Items.SNOW_BLOCK }
        }.save(context)
        // Snow Block -> Ice
        RagiumRecipeBuilders.compressing {
            ingredient { items { +Items.SNOW_BLOCK } }
            result { +Items.ICE }
        }.save(context)
        // Ice -> Packed Ice
        RagiumRecipeBuilders.compressing {
            ingredient {
                items { +Items.ICE }
                count = 6
            }
            result { +Items.PACKED_ICE }
        }.save(context)
        // Packed Ice -> Blue Ice
        RagiumRecipeBuilders.compressing {
            ingredient {
                items { +Items.PACKED_ICE }
                count = 6
            }
            result { +Items.BLUE_ICE }
        }.save(context)

        // Mud -> Clay
        RagiumRecipeBuilders.compressing {
            ingredient { items { +Items.MUD } }
            result { +Items.CLAY }
        }.save(context)
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
        }.save(context)
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
        }.save(context)
        // Sculk Vein -> Sculk
        RagiumRecipeBuilders.compressing {
            ingredient {
                items { +Items.SCULK_VEIN }
                count = 8
            }
            result { +Items.SCULK }
        }.save(context)

        // XX Carpet -> XX Wool
        for (color: DyeColor in DyeColor.entries) {
            RagiumRecipeBuilders.compressing {
                ingredient {
                    items { +Items.CARPET[color] }
                    count = 3
                }
                result {
                    +Items.WOOL[color]
                    count = 2
                }
                recipeId suffix "_from_carpet"
            }.save(context)
        }

        // XX Dust -> XX
        for (fuel: RagiumMaterial.Fuel in RagiumMaterial.Fuel.entries) {
            val baseItem: HTSimpleDeferredItem = RagiumMaterialHelper.getBase(fuel)
            RagiumRecipeBuilders.compressing {
                ingredient { +holderSet(CommonTagPrefixes.DUST, fuel) }
                result { +baseItem }
                recipeId suffix "_from_dust"
                condition { itemTagPresent(CommonTagPrefixes.DUST, fuel) }
            }.save(context)
        }
        setOf(
            RagiumMaterial.Other.WOOD to RagiumItems.PARTICLE_BOARD,
            RagiumMaterial.Other.PAPER to HTSimpleDeferredItem(ItemIds.PAPER)
        ).forEach { (material: RagiumMaterial, item: HTSimpleDeferredItem) ->
            RagiumRecipeBuilders.compressing {
                ingredient {
                    +holderSet(CommonTagPrefixes.DUST, material)
                    count = 2
                }
                result { +item }
            }.save(context)
        }
        RagiumRecipeBuilders.compressing {
            ingredient {
                +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Chemicals.CARBON)
                count = 64
            }
            result { +Items.DIAMOND }
            time *= 4
            recipeId suffix "_from_carbon"
        }.save(context)

        // 4x Blaze Powder -> Blaze Rod
        RagiumRecipeBuilders.compressing {
            ingredient {
                items { +Items.BLAZE_POWDER }
                count = 4
            }
            result { +Items.BLAZE_ROD }
        }.save(context)
        // 6x Wind Charge -> Breeze Rod
        RagiumRecipeBuilders.compressing {
            ingredient {
                items { +Items.WIND_CHARGE }
                count = 6
            }
            result { +Items.BREEZE_ROD }
        }.save(context)
    }

    private fun crushing() {
        crushMaterial()
        crushWood()
        dyes()

        // XX Block -> XX
        setOf(
            Tags.BlockItems.SANDSTONE_UNCOLORED_BLOCKS to Items.SAND,
            Tags.BlockItems.SANDSTONE_RED_BLOCKS to Items.RED_SAND
        ).forEach { (block: BlockItemTagId, base: Item) ->
            RagiumRecipeBuilders.crushing {
                ingredient { +itemSet(block) }
                result {
                    +base
                    count = 4
                }
            }.save(context)
        }

        // Stone -> Cobblestone
        RagiumRecipeBuilders.crushing {
            ingredient {
                +CompoundIngredient.of(
                    Ingredient.of(Items.STONE),
                    Ingredient.of(holderSet(ItemTags.STONE_BRICKS))
                )
            }
            result { +Items.COBBLESTONE }
            time /= 2
            recipeId suffix "_from_stones"
        }.save(context)
        RagiumRecipeBuilders.crushing {
            ingredient {
                items {
                    +Items.DEEPSLATE
                    +Items.POLISHED_DEEPSLATE
                    +Items.DEEPSLATE_BRICKS
                    +Items.CRACKED_DEEPSLATE_BRICKS
                    +Items.DEEPSLATE_TILES
                    +Items.CRACKED_DEEPSLATE_TILES
                    +Items.CHISELED_DEEPSLATE
                }
            }
            result { +Items.COBBLED_DEEPSLATE }
            time /= 2
            recipeId suffix "_from_stones"
        }
        // Cobblestone -> Gravel
        RagiumRecipeBuilders.crushing {
            ingredient {
                +DifferenceIngredient.of(
                    Ingredient.of(itemSet(Tags.BlockItems.COBBLESTONES)),
                    Ingredient.of(itemSet(Tags.BlockItems.COBBLESTONES_INFESTED))
                )
            }
            result { +Items.GRAVEL }
            time /= 2
            recipeId suffix "_from_cobblestones"
        }
        // Gravel -> Sand + Flint
        RagiumRecipeBuilders.crushing {
            ingredient { +itemSet(Tags.BlockItems.GRAVELS) }
            result { +Items.SAND }
            time /= 2
            recipeId suffix "_from_gravel"
        }.save(context)

        // Book -> 3x Paper Pulp
        RagiumRecipeBuilders.crushing {
            ingredient { items { +Items.BOOK } }
            result {
                +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Other.PAPER)
                count = 3
            }
            recipeId suffix "_from_book"
        }.save(context)
        // Blaze Rod -> 4x Blaze Powder
        RagiumRecipeBuilders.crushing {
            ingredient { +holderSet(Tags.Items.RODS_BLAZE) }
            result {
                +Items.BLAZE_POWDER
                count = 4
            }
        }.save(context)
        // Breeze Rod -> 6x Wind Charge
        RagiumRecipeBuilders.crushing {
            ingredient { +holderSet(Tags.Items.RODS_BREEZE) }
            result {
                +Items.WIND_CHARGE
                count = 6
            }
        }.save(context)
    }

    private fun crushMaterial() {
        // XX Dust
        RagiumRecipeBuilders.crushing {
            ingredient { items { +Items.COAL } }
            result { +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Fuel.COAL) }
        }.save(context)
        RagiumRecipeBuilders.crushing {
            ingredient { items { +Items.CHARCOAL } }
            result { +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Fuel.CHARCOAL) }
        }.save(context)
        RagiumRecipeBuilders.crushing {
            ingredient { +holderSet(RagiumTags.Items.COKES) }
            result { +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Chemicals.CARBON) }
        }.save(context)

        RagiumRecipeBuilders.crushing {
            ingredient {
                items {
                    +Items.CALCITE
                    +Items.DRIPSTONE_BLOCK
                }
            }
            result { +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Chemicals.LIME) }
        }.save(context)
        RagiumRecipeBuilders.crushing {
            ingredient {
                items { +Items.POINTED_DRIPSTONE }
                count = 4
            }
            result { +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Chemicals.LIME) }
            recipeId suffix "_from_pointed"
        }.save(context)

        for (gem: RagiumMaterial.Gem in RagiumMaterial.Gem.entries) {
            val dust: HTSimpleDeferredItem = RagiumItems.MATERIAL_ITEMS[HTItemPart.DUST, gem] ?: continue
            RagiumRecipeBuilders.crushing {
                ingredient { +holderSet(CommonTagPrefixes.GEM, gem) }
                result { +dust }
                recipeId suffix "_from_gem"
            }.save(context)
        }

        for (metalLike: RagiumMaterial.MetalLike in RagiumMaterial.MetalLike.entries) {
            val dust: HTSimpleDeferredItem = RagiumItems.MATERIAL_ITEMS[HTItemPart.DUST, metalLike] ?: continue
            RagiumRecipeBuilders.crushing {
                ingredient { +holderSet(CommonTagPrefixes.INGOT, metalLike) }
                result { +dust }
                recipeId suffix "_from_ingot"
            }.save(context)
        }

        val silicaDust: HTSimpleDeferredItem = RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Chemicals.SILICA)
        RagiumRecipeBuilders.crushing {
            ingredient { +holderSet(CommonTagPrefixes.GEM, RagiumMaterial.Gem.QUARTZ) }
            result { +silicaDust }
            recipeId suffix "_from_quartz"
        }.save(context)
        RagiumRecipeBuilders.crushing {
            ingredient { +holderSet(RagiumTags.BlockItems.QUARTZ_BLOCKS.item) }
            result {
                +silicaDust
                count = 4
            }
            recipeId suffix "_from_quartz_block"
        }.save(context)
        RagiumRecipeBuilders.crushing {
            ingredient {
                +holderSet(CommonTagPrefixes.GEM, RagiumMaterial.Gem.AMETHYST)
                count = 4
            }
            result { +silicaDust }
            recipeId suffix "_from_amethyst"
        }.save(context)
        RagiumRecipeBuilders.crushing {
            ingredient { items { +Items.AMETHYST_BLOCK } }
            result { +silicaDust }
            recipeId suffix "_from_amethyst_block"
        }.save(context)
        RagiumRecipeBuilders.crushing {
            ingredient { +itemSet(Tags.BlockItems.OBSIDIANS_NORMAL) }
            result { +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Other.OBSIDIAN) }
        }.save(context)
        /*RagiumRecipeBuilders.crushing {
            ingredient { +holderSet(Tags.Items.OBSIDIANS_CRYING) }
            result { +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Other.OBSIDIAN) }
            result { +RagiumItems.ELDRITCH_TEAR }
            recipeId suffix "_from_crying"
        }.save(context)*/
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
                }.save(context)
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
            Triple(Tags.BlockItems.BARRELS_WOODEN.item(), 1, 7),
            Triple(Tags.BlockItems.CHESTS_WOODEN.item(), 1, 8),
            Triple(Tags.BlockItems.FENCE_GATES_WOODEN.item(), 1, 4),
            Triple(Tags.BlockItems.FENCES_WOODEN.item(), 1, 1),
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
            }.save(context)
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
        }.save(context)
    }

    private fun dyes() {
        // Single
        val single = ColorCollection<Optional<HTItemIngredientBuilder.() -> Unit>>(
            Optional.of {
                items {
                    +Items.BONE_MEAL
                    +Items.LILY_OF_THE_VALLEY
                }
            },
            Optional.of {
                items {
                    +Items.ORANGE_TULIP
                    +Items.TORCHFLOWER
                    +Items.OPEN_EYEBLOSSOM
                }
            },
            Optional.of { items { +Items.ALLIUM } },
            Optional.of { items { +Items.BLUE_ORCHID } },
            Optional.of {
                items {
                    +Items.DANDELION
                    +Items.GOLDEN_DANDELION
                    +Items.WILDFLOWERS
                }
            },
            Optional.of { items { +Items.SEA_PICKLE } },
            Optional.of {
                items {
                    +Items.CACTUS_FLOWER
                    +Items.PINK_PETALS
                    +Items.PINK_TULIP
                }
            },
            Optional.of { items { +Items.CLOSED_EYEBLOSSOM } },
            Optional.of {
                items {
                    +Items.AZURE_BLUET
                    +Items.OXEYE_DAISY
                    +Items.WHITE_TULIP
                }
            },
            Optional.empty(),
            Optional.empty(),
            Optional.of { items { +Items.CORNFLOWER } },
            Optional.of { items { +Items.COCOA_BEANS } },
            Optional.of { items { +Items.CACTUS } },
            Optional.of {
                items {
                    +Items.POPPY
                    +Items.BEETROOT
                }
            },
            Optional.of {
                items {
                    +Items.INK_SAC
                    +Items.WITHER_ROSE
                }
            }
        )
        for (color: DyeColor in DyeColor.entries) {
            single[color].ifPresent { builder: HTItemIngredientBuilder.() -> Unit ->
                RagiumRecipeBuilders.crushing {
                    ingredient(builder)
                    result {
                        +Items.DYE[color]
                        count = 2
                    }
                }.save(context)
            }
        }
        // Double
        RagiumRecipeBuilders.crushing {
            ingredient { items { +Items.ROSE_BUSH } }
            result {
                +Items.DYE.red()
                count = 4
            }
            recipeId prefix "double_"
        }.save(context)
        RagiumRecipeBuilders.crushing {
            ingredient { items { +Items.SUNFLOWER } }
            result {
                +Items.DYE.yellow()
                count = 4
            }
            recipeId prefix "double_"
        }.save(context)
        RagiumRecipeBuilders.crushing {
            ingredient { items { +Items.PITCHER_PLANT } }
            result {
                +Items.DYE.cyan()
                count = 4
            }
            recipeId prefix "double_"
        }.save(context)
        RagiumRecipeBuilders.crushing {
            ingredient { items { +Items.LILAC } }
            result {
                +Items.DYE.pink()
                count = 4
            }
            recipeId prefix "double_"
        }.save(context)
    }

    private fun cutting() {
        // Sapling -> Stick
        RagiumRecipeBuilders.cutting {
            ingredient { +holderSet(ItemTags.SAPLINGS) }
            result { +Items.STICK }
            recipeId suffix "_from_saplings"
        }.save(context)
        // Book -> Leather + 3x Paper
        RagiumRecipeBuilders.cutting {
            ingredient { items { +Items.BOOK } }
            result {
                +Items.PAPER
                count = 3
            }
            result { +Items.LEATHER }
            recipeId suffix "_from_book"
        }.save(context)

        // Lens
        RagiumRecipeBuilders.cutting {
            ingredient { +holderSet(RagiumTags.BlockItems.QUARTZ_BLOCKS.item) }
            result { +RagiumItems.QUARTZ_LENS }
            result { +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Chemicals.SILICA) }
        }.save(context)
        RagiumRecipeBuilders.cutting {
            ingredient { +holderSet(CommonTagPrefixes.STORAGE_BLOCK, RagiumMaterial.Gem.DIAMOND) }
            result { +RagiumItems.DIAMOND_LENS }
            result {
                +RagiumItems.getOrThrow(HTItemPart.DUST, RagiumMaterial.Gem.DIAMOND)
                count = 3
            }
        }.save(context)

        RagiumRecipeBuilders.cutting {
            ingredient { +holderSet(Tags.Items.NETHER_STARS) }
            result { +RagiumItems.NETHER_STAR_LENS }
        }.save(context)
    }

    private fun pressing() {
        val unpacking: Ingredient = Ingredient.of(RagiumItems.UNPACKING_DIE)
        // Melon -> Sliced Melon
        RagiumRecipeBuilders.pressing {
            ingredient { items { +Items.MELON } }
            +unpacking
            result {
                +Items.MELON_SLICE
                count = 9
            }
        }.save(context)

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
            RagiumBlocks.FLUORITE_BLOCK to RagiumItems.getOrThrow(HTItemPart.GEM, RagiumMaterial.Gem.FLUORITE)
            // RagiumBlocks.CRYOLITE_BLOCK to RagiumItems.getOrThrow(HTItemPart.GEM, RagiumMaterial.Gem.CRYOLITE)
        ).forEach { (block: ItemLike, base: ItemLike) ->
            RagiumRecipeBuilders.pressing {
                ingredient { items { +block.asItem() } }
                +unpacking
                result {
                    +base.asItem()
                    count = 4
                }
            }.save(context)
        }

        setOf(
            RagiumTags.BlockItems.QUARTZ_BLOCKS.item to Items.QUARTZ
        ).forEach { (block: TagKey<Item>, base: Item) ->
            RagiumRecipeBuilders.pressing {
                ingredient { +holderSet(block) }
                +unpacking
                result {
                    +base
                    count = 4
                }
            }.save(context)
        }
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
        }.save(context)
        // Dirt + Water -> Mud
        RagiumRecipeBuilders.filling {
            itemIngredient { items { +Items.DIRT } }
            fluidIngredient {
                +waterSet()
                amount = 250
            }
            result { +Items.MUD }
        }.save(context)

        // XX Concrete Powder + Water -> XX Concrete
        for (color: DyeColor in DyeColor.entries) {
            RagiumRecipeBuilders.filling {
                itemIngredient { items { +Items.CONCRETE_POWDER[color] } }
                fluidIngredient {
                    +waterSet()
                    amount = 10
                }
                result { +Items.CONCRETE[color] }
                recipeId suffix "_from_powder"
            }.save(context)
        }
    }
}
