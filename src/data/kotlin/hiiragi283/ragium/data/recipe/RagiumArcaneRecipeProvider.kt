package hiiragi283.ragium.data.recipe

import hiiragi283.lib.data.recipe.HTRecipeProvider
import hiiragi283.lib.data.recipe.builder.ingredient
import hiiragi283.lib.data.recipe.builder.result
import hiiragi283.lib.tag.CommonTagPrefixes
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.recipe.builder.RagiumRecipeBuilders
import hiiragi283.ragium.api.material.RagiumMaterial
import hiiragi283.ragium.common.fluid.RagiumFluids
import hiiragi283.ragium.common.item.RagiumItems
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.enchantment.Enchantments
import net.neoforged.neoforge.common.Tags
import java.util.concurrent.CompletableFuture

class RagiumArcaneRecipeProvider(packOutput: PackOutput, future: CompletableFuture<HolderLookup.Provider>) :
    HTRecipeProvider(packOutput, future, RagiumAPI.MOD_ID) {
    override fun exportValues() {
        enchanting()
        transforming()
    }

    private fun enchanting() {
        // Iron -> Protection
        RagiumRecipeBuilders.enchanting {
            ingredient {
                +holderSet(CommonTagPrefixes.INGOT, RagiumMaterial.Metal.IRON)
                count = 64
            }
            result { +registries.getOrThrow(Enchantments.PROTECTION) }
        }.save(exporter)
        // Diamond -> Protection + Unbreaking
        RagiumRecipeBuilders.enchanting {
            ingredient {
                +holderSet(CommonTagPrefixes.GEM, RagiumMaterial.Gem.DIAMOND)
                count = 16
            }
            result {
                enchantedBook {
                    set(registries.getOrThrow(Enchantments.PROTECTION), 4)
                    set(registries.getOrThrow(Enchantments.UNBREAKING), 3)
                }
            }
            recipeId replace id("diamond_armor")
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
