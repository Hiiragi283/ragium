package hiiragi283.ragium.data.recipe

import hiiragi283.lib.data.recipe.HTRecipeProvider
import hiiragi283.lib.data.recipe.builder.ingredient
import hiiragi283.lib.data.recipe.builder.result
import hiiragi283.lib.tag.CommonTagPrefixes
import hiiragi283.lib.tag.HTCommonTags
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.recipe.builder.RagiumRecipeBuilders
import hiiragi283.ragium.api.material.RagiumMaterial
import hiiragi283.ragium.api.tag.RagiumTags
import hiiragi283.ragium.common.fluid.RagiumFluids
import hiiragi283.ragium.common.item.RagiumItems
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.enchantment.Enchantments
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.crafting.CompoundIngredient
import java.util.concurrent.CompletableFuture

class RagiumArcaneRecipeProvider(packOutput: PackOutput, future: CompletableFuture<HolderLookup.Provider>) :
    HTRecipeProvider(packOutput, future, RagiumAPI.MOD_ID) {
    override fun exportValues() {
        enchanting()
        transforming()
    }

    private fun enchanting() {
        // Armor
        RagiumRecipeBuilders.enchanting {
            ingredient {
                +holderSet(CommonTagPrefixes.INGOT, RagiumMaterial.Metal.IRON)
                count = 64
            }
            result { +registries.getOrThrow(Enchantments.PROTECTION) }
        }.save(exporter)
        RagiumRecipeBuilders.enchanting {
            ingredient {
                +holderSet(Tags.Items.BRICKS)
                count = 64
            }
            result { +registries.getOrThrow(Enchantments.FIRE_PROTECTION) }
        }.save(exporter)
        RagiumRecipeBuilders.enchanting {
            ingredient {
                +holderSet(Tags.Items.FEATHERS)
                count = 64
            }
            result { +registries.getOrThrow(Enchantments.FEATHER_FALLING) }
        }.save(exporter)
        RagiumRecipeBuilders.enchanting {
            ingredient {
                +holderSet(Tags.Items.OBSIDIANS_NORMAL)
                count = 32
            }
            result { +registries.getOrThrow(Enchantments.BLAST_PROTECTION) }
        }.save(exporter)

        // Helmet
        RagiumRecipeBuilders.enchanting {
            ingredient {
                items { +Items.GLASS_BOTTLE }
                count = 64
            }
            result { +registries.getOrThrow(Enchantments.RESPIRATION) }
        }.save(exporter)
        RagiumRecipeBuilders.enchanting {
            ingredient {
                +holderSet(CommonTagPrefixes.INGOT, RagiumMaterial.Metal.COPPER)
                count = 64
            }
            result { +registries.getOrThrow(Enchantments.AQUA_AFFINITY) }
        }.save(exporter)
        // Chestplate
        RagiumRecipeBuilders.enchanting {
            ingredient {
                +CompoundIngredient.of(
                    Ingredient.of(holderSet(Tags.Items.CROPS_CACTUS)),
                    Ingredient.of(Items.POINTED_DRIPSTONE),
                )
                count = 64
            }
            result { +registries.getOrThrow(Enchantments.THORNS) }
        }.save(exporter)
        // Boots
        RagiumRecipeBuilders.enchanting {
            ingredient {
                +holderSet(Tags.Items.GEMS_PRISMARINE)
                count = 32
            }
            result { +registries.getOrThrow(Enchantments.DEPTH_STRIDER) }
        }.save(exporter)
        RagiumRecipeBuilders.enchanting {
            ingredient {
                items { +Items.PACKED_ICE }
                count = 64
            }
            result { +registries.getOrThrow(Enchantments.FROST_WALKER) }
        }.save(exporter)
        // Leggings
        RagiumRecipeBuilders.enchanting {
            ingredient {
                +holderSet(ItemTags.SOUL_FIRE_BASE_BLOCKS)
                count = 64
            }
            result { +registries.getOrThrow(Enchantments.SOUL_SPEED) }
        }.save(exporter)
        RagiumRecipeBuilders.enchanting {
            ingredient {
                items { +Items.SCULK }
                count = 64
            }
            result { +registries.getOrThrow(Enchantments.SWIFT_SNEAK) }
        }.save(exporter)
        // Melee
        RagiumRecipeBuilders.enchanting {
            ingredient {
                +holderSet(CommonTagPrefixes.GEM, RagiumMaterial.Gem.QUARTZ)
                count = 64
            }
            result { +registries.getOrThrow(Enchantments.SHARPNESS) }
        }.save(exporter)
        RagiumRecipeBuilders.enchanting {
            ingredient {
                +holderSet(CommonTagPrefixes.INGOT, RagiumMaterial.Metal.SILVER)
                count = 32
            }
            result { +registries.getOrThrow(Enchantments.SMITE) }
        }.save(exporter)
        RagiumRecipeBuilders.enchanting {
            ingredient {
                +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Chemical.SULFUR)
                count = 64
            }
            result { +registries.getOrThrow(Enchantments.BANE_OF_ARTHROPODS) }
        }.save(exporter)
        RagiumRecipeBuilders.enchanting {
            ingredient {
                items { +Items.PISTON }
                count = 16
            }
            result { +registries.getOrThrow(Enchantments.KNOCKBACK) }
        }.save(exporter)
        RagiumRecipeBuilders.enchanting {
            ingredient {
                items { +Items.FIRE_CHARGE }
                count = 16
            }
            result { +registries.getOrThrow(Enchantments.FIRE_ASPECT) }
        }.save(exporter)
        RagiumRecipeBuilders.enchanting {
            ingredient {
                +holderSet(CommonTagPrefixes.GEM, RagiumMaterial.Gem.EMERALD)
                count = 64
            }
            result { +registries.getOrThrow(Enchantments.LOOTING) }
        }.save(exporter)
        RagiumRecipeBuilders.enchanting {
            ingredient {
                +holderSet(HTCommonTags.Items.PLASTICS)
                count = 32
            }
            result { +registries.getOrThrow(Enchantments.SWEEPING_EDGE) }
        }.save(exporter)
        // Mining Tool
        RagiumRecipeBuilders.enchanting {
            ingredient {
                +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Other.REDSTONE)
                count = 64
            }
            result { +registries.getOrThrow(Enchantments.EFFICIENCY) }
        }.save(exporter)
        RagiumRecipeBuilders.enchanting {
            ingredient {
                items { +Items.PAPER }
                count = 64
            }
            result { +registries.getOrThrow(Enchantments.SILK_TOUCH) }
        }.save(exporter)
        RagiumRecipeBuilders.enchanting {
            ingredient {
                +holderSet(CommonTagPrefixes.GEM, RagiumMaterial.Gem.LAPIS)
                count = 64
            }
            result { +registries.getOrThrow(Enchantments.FORTUNE) }
        }.save(exporter)
        // Bow
        RagiumRecipeBuilders.enchanting {
            ingredient {
                +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Chemical.SILICA)
                count = 64
            }
            result { +registries.getOrThrow(Enchantments.POWER) }
        }.save(exporter)
        RagiumRecipeBuilders.enchanting {
            ingredient {
                +holderSet(Tags.Items.STRINGS)
                count = 16
            }
            result { +registries.getOrThrow(Enchantments.PUNCH) }
        }.save(exporter)
        RagiumRecipeBuilders.enchanting {
            ingredient {
                +holderSet(Tags.Items.RODS_BLAZE)
                count = 8
            }
            result { +registries.getOrThrow(Enchantments.FLAME) }
        }.save(exporter)
        RagiumRecipeBuilders.enchanting {
            ingredient {
                items { +Items.SKELETON_SKULL }
                count = 4
            }
            result { +registries.getOrThrow(Enchantments.INFINITY) }
        }.save(exporter)
        // Fishing Rod
        // Trident
        // Crossbow
        // Mace
        // Other
        RagiumRecipeBuilders.enchanting {
            ingredient {
                +holderSet(CommonTagPrefixes.GEM, RagiumMaterial.Gem.DIAMOND)
                count = 16
            }
            result { +registries.getOrThrow(Enchantments.UNBREAKING) }
        }.save(exporter)
        RagiumRecipeBuilders.enchanting {
            ingredient { +holderSet(Tags.Items.NETHER_STARS) }
            result { +registries.getOrThrow(Enchantments.MENDING) }
        }.save(exporter)
        // Curse
        RagiumRecipeBuilders.enchanting {
            ingredient { +holderSet(Tags.Items.SLIME_BALLS) }
            result { +registries.getOrThrow(Enchantments.BINDING_CURSE) }
        }.save(exporter)
        RagiumRecipeBuilders.enchanting {
            ingredient { +holderSet(Tags.Items.ENDER_PEARLS) }
            result { +registries.getOrThrow(Enchantments.VANISHING_CURSE) }
        }.save(exporter)

        // Protection + Unbreaking
        RagiumRecipeBuilders.enchanting {
            ingredient {
                +holderSet(CommonTagPrefixes.STORAGE_BLOCK, RagiumMaterial.Metal.IRON)
                count = 16
            }
            result {
                enchantedBook {
                    set(registries.getOrThrow(Enchantments.PROTECTION), 4)
                    set(registries.getOrThrow(Enchantments.UNBREAKING), 3)
                }
            }
            recipeId replace id("armor_sets")
        }.save(exporter)
        // Sharpness + Unbreaking
        RagiumRecipeBuilders.enchanting {
            ingredient {
                +holderSet(RagiumTags.BlockItems.QUARTZ_BLOCKS.item)
                count = 16
            }
            result {
                enchantedBook {
                    set(registries.getOrThrow(Enchantments.SHARPNESS), 5)
                    set(registries.getOrThrow(Enchantments.UNBREAKING), 3)
                }
            }
            recipeId replace id("sword_sets")
        }.save(exporter)
        // Efficiency + Unbreaking
        RagiumRecipeBuilders.enchanting {
            ingredient {
                +holderSet(CommonTagPrefixes.STORAGE_BLOCK, RagiumMaterial.Gem.DIAMOND)
                count = 4
            }
            result {
                enchantedBook {
                    set(registries.getOrThrow(Enchantments.EFFICIENCY), 5)
                    set(registries.getOrThrow(Enchantments.UNBREAKING), 3)
                }
            }
            recipeId replace id("mining_sets")
        }.save(exporter)
    }

    private fun transforming() {
        // Stone -> Infested Stone
        setOf(
            Items.STONE to Items.INFESTED_STONE,
            Items.COBBLESTONE to Items.INFESTED_COBBLESTONE,
            Items.STONE_BRICKS to Items.INFESTED_STONE_BRICKS,
            Items.MOSSY_STONE_BRICKS to Items.INFESTED_MOSSY_STONE_BRICKS,
            Items.CRACKED_STONE_BRICKS to Items.INFESTED_CRACKED_STONE_BRICKS,
            Items.CHISELED_STONE_BRICKS to Items.INFESTED_CHISELED_STONE_BRICKS,
            Items.DEEPSLATE to Items.INFESTED_DEEPSLATE,
        ).forEach { (base: Item, infested: Item) ->
            RagiumRecipeBuilders.transforming {
                itemIngredient { items { +base } }
                fluidIngredient {
                    +holderSet(RagiumFluids.ENCHANTED_EXPERIENCE)
                    amount /= 8
                }
                result { +infested }
            }.save(exporter)
        }
        // Glass -> Quartz
        RagiumRecipeBuilders.transforming {
            itemIngredient { +holderSet(Tags.Items.GLASS_BLOCKS_CHEAP) }
            fluidIngredient {
                +holderSet(RagiumFluids.ENCHANTED_EXPERIENCE)
                amount /= 8
            }
            result { +Items.QUARTZ }
        }.save(exporter)
        // Snowball -> Wind Charge
        RagiumRecipeBuilders.transforming {
            itemIngredient { items { +Items.SNOWBALL } }
            fluidIngredient {
                +holderSet(RagiumFluids.ENCHANTED_EXPERIENCE)
                amount /= 8
            }
            result { +Items.WIND_CHARGE }
        }.save(exporter)

        // Small Flower -> Chorus Flower
        RagiumRecipeBuilders.transforming {
            itemIngredient { +holderSet(Tags.Items.FLOWERS_SMALL) }
            fluidIngredient {
                +holderSet(RagiumFluids.ENCHANTED_EXPERIENCE)
                amount /= 4
            }
            result { +Items.CHORUS_FLOWER }
        }.save(exporter)
        // Shroomlight -> Glowstone Dust
        RagiumRecipeBuilders.transforming {
            itemIngredient { items { +Items.SHROOMLIGHT } }
            fluidIngredient {
                +holderSet(RagiumFluids.ENCHANTED_EXPERIENCE)
                amount /= 4
            }
            result { +Items.GLOWSTONE }
        }.save(exporter)
        // Honey Bottle -> Exp Bottle
        RagiumRecipeBuilders.transforming {
            itemIngredient { +holderSet(Tags.Items.DRINKS_HONEY) }
            fluidIngredient {
                +holderSet(RagiumFluids.ENCHANTED_EXPERIENCE)
                amount /= 4
            }
            result { +Items.EXPERIENCE_BOTTLE }
        }.save(exporter)
        // TODO: Ender Pearl

        // Amethyst Block -> Budding Amethyst
        RagiumRecipeBuilders.transforming {
            itemIngredient { items { +Items.AMETHYST_BLOCK } }
            fluidIngredient {
                +holderSet(RagiumFluids.ENCHANTED_EXPERIENCE)
                amount /= 2
            }
            result { +Items.BUDDING_AMETHYST }
        }.save(exporter)
        // Obsidian -> Crying Obsidian
        RagiumRecipeBuilders.transforming {
            itemIngredient { +holderSet(Tags.Items.OBSIDIANS_NORMAL) }
            fluidIngredient {
                +holderSet(RagiumFluids.ENCHANTED_EXPERIENCE)
                amount /= 2
            }
            result { +Items.CRYING_OBSIDIAN }
        }.save(exporter)
        // Skull -> Wither Skull
        RagiumRecipeBuilders.transforming {
            itemIngredient { items { +Items.SKELETON_SKULL } }
            fluidIngredient {
                +holderSet(RagiumFluids.ENCHANTED_EXPERIENCE)
                amount /= 2
            }
            result { +Items.WITHER_SKELETON_SKULL }
        }.save(exporter)

        // Player Head -> Dragon Head
        RagiumRecipeBuilders.transforming {
            itemIngredient { items { +Items.PLAYER_HEAD } }
            fluidIngredient { +holderSet(RagiumFluids.ENCHANTED_EXPERIENCE) }
            result { +Items.DRAGON_HEAD }
        }.save(exporter)
        // Diamond Block -> Netherite Smithing Template
        RagiumRecipeBuilders.transforming {
            itemIngredient { +holderSet(CommonTagPrefixes.STORAGE_BLOCK, RagiumMaterial.Gem.DIAMOND) }
            fluidIngredient { +holderSet(RagiumFluids.ENCHANTED_EXPERIENCE) }
            result { +Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE }
        }.save(exporter)
        // Elder Heart -> Heart of the Sea
        RagiumRecipeBuilders.transforming {
            itemIngredient { items { +RagiumItems.ELDER_HEART } }
            fluidIngredient { +holderSet(RagiumFluids.ENCHANTED_EXPERIENCE) }
            result { +Items.HEART_OF_THE_SEA }
        }.save(exporter)
        // TODO: Heavy Core
        // Wither Star -> Nether Star
        RagiumRecipeBuilders.transforming {
            itemIngredient { items { +RagiumItems.WITHER_STAR } }
            fluidIngredient { +holderSet(RagiumFluids.ENCHANTED_EXPERIENCE) }
            result { +Items.NETHER_STAR }
        }.save(exporter)
    }

    override fun getName(): String = "Arcane Recipes"
}
