package hiiragi283.ragium.data.recipe

import hiiragi283.lib.data.recipe.HTRecipeProvider
import hiiragi283.lib.data.recipe.builder.ingredient
import hiiragi283.lib.data.recipe.builder.result
import hiiragi283.lib.tag.CommonTagPrefixes
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.recipe.builder.RagiumRecipeBuilders
import hiiragi283.ragium.api.material.RagiumMaterial
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
                ingredient { items { +base } }
                result { +infested }
                energy = 10 * 20 * 10
            }.save(exporter)
        }
        // Glass -> Quartz
        RagiumRecipeBuilders.transforming {
            ingredient { +holderSet(Tags.Items.GLASS_BLOCKS_CHEAP) }
            result { +Items.QUARTZ }
            energy = 10 * 20 * 10
        }.save(exporter)
        // Small Flower -> Chorus Flower
        RagiumRecipeBuilders.transforming {
            ingredient { +holderSet(Tags.Items.FLOWERS_SMALL) }
            result { +Items.CHORUS_FLOWER }
            energy = 10 * 20 * 10
        }.save(exporter)
        // Snowball -> Wind Charge
        RagiumRecipeBuilders.transforming {
            ingredient { items { +Items.SNOWBALL } }
            result { +Items.WIND_CHARGE }
            energy = 10 * 20 * 10
        }.save(exporter)

        // Shroomlight -> Glowstone Dust
        RagiumRecipeBuilders.transforming {
            ingredient { items { +Items.SHROOMLIGHT } }
            result { +Items.GLOWSTONE }
            energy = 15 * 20 * 10
        }.save(exporter)
        // Honey Bottle -> Exp Bottle
        RagiumRecipeBuilders.transforming {
            ingredient { +holderSet(Tags.Items.DRINKS_HONEY) }
            result { +Items.EXPERIENCE_BOTTLE }
            energy = 15 * 20 * 10
        }.save(exporter)
        // TODO: Ender Pearl

        // Amethyst Block -> Budding Amethyst
        RagiumRecipeBuilders.transforming {
            ingredient { items { +Items.AMETHYST_BLOCK } }
            result { +Items.BUDDING_AMETHYST }
            energy = 20 * 20 * 10
        }.save(exporter)
        // Obsidian -> Crying Obsidian
        RagiumRecipeBuilders.transforming {
            ingredient { +holderSet(Tags.Items.OBSIDIANS_NORMAL) }
            result { +Items.CRYING_OBSIDIAN }
            energy = 20 * 20 * 10
        }.save(exporter)
        // Skull -> Wither Skull
        RagiumRecipeBuilders.transforming {
            ingredient { items { +Items.SKELETON_SKULL } }
            result { +Items.WITHER_SKELETON_SKULL }
            energy = 20 * 20 * 10
        }.save(exporter)
        // Player Head -> Dragon Head
        RagiumRecipeBuilders.transforming {
            ingredient { items { +Items.PLAYER_HEAD } }
            result { +Items.DRAGON_HEAD }
            energy = 20 * 20 * 10
        }.save(exporter)
        // Diamond Block -> Netherite Smithing Template
        RagiumRecipeBuilders.transforming {
            ingredient { +holderSet(CommonTagPrefixes.STORAGE_BLOCK, RagiumMaterial.Gem.DIAMOND) }
            result { +Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE }
            energy = 20 * 20 * 10
        }.save(exporter)

        // Elder Heart -> Heart of the Sea
        RagiumRecipeBuilders.transforming {
            ingredient { items { +RagiumItems.ELDER_HEART } }
            result { +Items.HEART_OF_THE_SEA }
            energy = 30 * 20 * 10
        }.save(exporter)
        // TODO: Heavy Core
        // Wither Star -> Nether Star
        RagiumRecipeBuilders.transforming {
            ingredient { items { +RagiumItems.WITHER_STAR } }
            result { +Items.NETHER_STAR }
            energy = 30 * 20 * 20
        }.save(exporter)
    }

    override fun getName(): String = "Arcane Recipes"
}
