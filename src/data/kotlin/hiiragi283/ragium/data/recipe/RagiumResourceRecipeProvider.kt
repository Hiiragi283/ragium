package hiiragi283.ragium.data.recipe

import hiiragi283.lib.data.recipe.HTRecipeProvider
import hiiragi283.lib.data.recipe.builder.VanillaRecipeBuilders
import hiiragi283.lib.data.recipe.builder.ingredient
import hiiragi283.lib.data.recipe.builder.result
import hiiragi283.lib.item.component.PotionContents
import hiiragi283.lib.recipe.ingredient.HTPotionFluidIngredient
import hiiragi283.lib.tag.CommonTagPrefixes
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.recipe.builder.RagiumRecipeBuilders
import hiiragi283.ragium.api.material.RagiumMaterial
import hiiragi283.ragium.api.tag.RagiumTags
import hiiragi283.ragium.common.fluid.RagiumFluids
import hiiragi283.ragium.common.item.RagiumItems
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.tags.TagKey
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.Potions
import net.minecraft.world.item.component.Consumable
import net.minecraft.world.item.component.Consumables
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect
import net.minecraft.world.level.biome.Biomes
import net.neoforged.neoforge.common.Tags
import java.util.concurrent.CompletableFuture

class RagiumResourceRecipeProvider(packOutput: PackOutput, future: CompletableFuture<HolderLookup.Provider>) :
    HTRecipeProvider(packOutput, future, RagiumAPI.MOD_ID) {
    override fun exportValues() {
        brewing()
        planting()
        resourcePumping()

        honey()
        bioFuel()
    }

    override fun getName(): String = "Bio Recipes"

    private fun brewing() {
        // Splash Bottle
        VanillaRecipeBuilders.shapeless {
            ingredient { +holderSet(Tags.Items.GUNPOWDERS) }
            repeat(4) { ingredient { items { +Items.GLASS_BOTTLE } } }
            result {
                +RagiumItems.SPLASH_BOTTLE
                count = 4
            }
        }.save(exporter)
        // Lingering Bottle
        VanillaRecipeBuilders.shapeless {
            ingredient { items { +Items.DRAGON_BREATH } }
            repeat(4) { ingredient { items { +Items.GLASS_BOTTLE } } }
            result {
                +RagiumItems.LINGERING_BOTTLE
                count = 4
            }
        }.save(exporter)

        // Haste
        // Nausea
        RagiumRecipeBuilders.brewing {
            itemIngredient { items { +Items.PUFFERFISH } }
            fluidIngredient { +HTPotionFluidIngredient(Potions.THICK) }
            result {
                +PotionContents(
                    customEffects = listOf(MobEffectInstance(MobEffects.NAUSEA, 900)),
                    customName = "nausea",
                )
            }
            recipeId replace id("potion/nausea")
        }.save(exporter)
        // Resistance
        // Blindness
        RagiumRecipeBuilders.brewing {
            itemIngredient { +holderSet(Tags.Items.GLASS_BLOCKS_TINTED) }
            fluidIngredient { +HTPotionFluidIngredient(Potions.THICK) }
            result {
                +PotionContents(
                    customEffects = listOf(MobEffectInstance(MobEffects.BLINDNESS, 900)),
                    customName = "blindness",
                )
            }
            recipeId replace id("potion/blindness")
        }.save(exporter)
        // Hunger
        RagiumRecipeBuilders.brewing {
            itemIngredient { items { +Items.ROTTEN_FLESH } }
            fluidIngredient { +HTPotionFluidIngredient(Potions.THICK) }
            result {
                +PotionContents(
                    customEffects = listOf(MobEffectInstance(MobEffects.HUNGER, 900)),
                    customName = "hunger",
                )
            }
            recipeId replace id("potion/hunger")
        }.save(exporter)
        // Wither
        RagiumRecipeBuilders.brewing {
            itemIngredient {
                items {
                    +Items.WITHER_ROSE
                    +Items.WITHER_SKELETON_SKULL
                }
            }
            fluidIngredient { +HTPotionFluidIngredient(Potions.THICK) }
            result {
                +PotionContents(
                    customEffects = listOf(MobEffectInstance(MobEffects.WITHER, 900)),
                    customName = "wither",
                )
            }
            recipeId replace id("potion/wither")
        }.save(exporter)
        // Health Boost
        // Absorption
        // Saturation
        // Glowing
        // Levitation
        // Luck
        // Unluck
        // Darkness
        RagiumRecipeBuilders.brewing {
            itemIngredient { +holderSet(CommonTagPrefixes.DUST, RagiumMaterial.Gem.ECHO) }
            fluidIngredient { +HTPotionFluidIngredient(Potions.THICK) }
            result {
                +PotionContents(
                    customEffects = listOf(MobEffectInstance(MobEffects.DARKNESS, 900)),
                    customName = "darkness",
                )
            }
            recipeId replace id("potion/darkness")
        }.save(exporter)

        // Golden Apple
        RagiumRecipeBuilders.brewing {
            itemIngredient { items { +Items.GOLDEN_APPLE } }
            fluidIngredient { +HTPotionFluidIngredient(Potions.THICK) }
            result {
                +PotionContents(
                    customColor = 0xff9900,
                    customEffects = extractEffects(Consumables.GOLDEN_APPLE),
                    customName = "golden_apple",
                )
            }
            recipeId replace id("potion/golden_apple")
        }.save(exporter)
        RagiumRecipeBuilders.brewing {
            itemIngredient { items { +Items.ENCHANTED_GOLDEN_APPLE } }
            fluidIngredient { +HTPotionFluidIngredient(Potions.THICK) }
            result {
                +PotionContents(
                    customColor = 0xff9900,
                    customEffects = extractEffects(Consumables.ENCHANTED_GOLDEN_APPLE),
                    customName = "enchanted_golden_apple",
                )
            }
            recipeId replace id("potion/enchanted_golden_apple")
        }.save(exporter)
    }

    private fun extractEffects(consumable: Consumable): List<MobEffectInstance> = consumable
        .onConsumeEffects
        .filterIsInstance<ApplyStatusEffectsConsumeEffect>()
        .flatMap(ApplyStatusEffectsConsumeEffect::effects)

    private fun planting() {
        // Plant with seed
        setOf(
            Pair(Tags.Items.SEEDS_BEETROOT, Items.BEETROOT),
            Pair(Tags.Items.SEEDS_MELON, Items.MELON),
            Pair(Tags.Items.SEEDS_PITCHER_PLANT, Items.PITCHER_PLANT),
            Pair(Tags.Items.SEEDS_PUMPKIN, Items.PUMPKIN),
            Pair(Tags.Items.SEEDS_TORCHFLOWER, Items.TORCHFLOWER),
            Pair(Tags.Items.SEEDS_WHEAT, Items.WHEAT),
        ).forEach { (seedIn: TagKey<Item>, crop: Item) ->
            RagiumRecipeBuilders.planting {
                ingredient {
                    +holderSet(seedIn)
                    count = 8
                }
                result {
                    +crop
                    count = 12
                }
            }.save(exporter)
        }
        // Plant without seed
        val crops: Set<Item> = setOf(
            Items.CARROT,
            Items.POTATO,
            Items.SWEET_BERRIES,
            Items.GLOW_BERRIES,
            Items.APPLE,
            Items.COCOA_BEANS,
            Items.NETHER_WART,
            Items.BROWN_MUSHROOM,
            Items.RED_MUSHROOM,
        )
        for (crop: Item in crops) {
            RagiumRecipeBuilders.planting {
                ingredient {
                    items { +crop }
                    count = 8
                }
                result {
                    +crop
                    count = 16
                }
            }.save(exporter)
        }
        // Cactus, Sugar Cane
        for (crop: Item in listOf(Items.CACTUS, Items.SUGAR_CANE)) {
            RagiumRecipeBuilders.planting {
                ingredient {
                    items { +crop }
                    count = 8
                }
                result {
                    +crop
                    count = 24
                }
            }.save(exporter)
        }
        // Trees
        setOf(
            Items.OAK_SAPLING to Items.OAK_LOG,
            Items.SPRUCE_SAPLING to Items.SPRUCE_LOG,
            Items.BIRCH_SAPLING to Items.BIRCH_LOG,
            Items.JUNGLE_SAPLING to Items.JUNGLE_LOG,
            Items.ACACIA_SAPLING to Items.ACACIA_LOG,
            Items.DARK_OAK_SAPLING to Items.DARK_OAK_LOG,
            Items.CHERRY_SAPLING to Items.CHERRY_LOG,
            Items.MANGROVE_PROPAGULE to Items.MANGROVE_LOG,
            Items.PALE_OAK_SAPLING to Items.PALE_OAK_LOG,
            Items.CRIMSON_FUNGUS to Items.CRIMSON_STEM,
            Items.WARPED_FUNGUS to Items.WARPED_STEM,
            Items.CHORUS_FLOWER to Items.CHORUS_FRUIT,
        ).forEach { (sapling: Item, log: Item) ->
            RagiumRecipeBuilders.planting {
                ingredient {
                    items { +sapling }
                    count = 8
                }
                result {
                    +log
                    count = 48
                }
            }.save(exporter)
        }
    }

    private fun resourcePumping() {
        // XX -> Water
        RagiumRecipeBuilders.resourcePumping {
            biomes { +holderSet(Tags.Biomes.IS_OCEAN) }
            result {
                water()
                amount *= 4
            }
            recipeId suffix "_at_ocean"
        }.save(exporter)
        RagiumRecipeBuilders.resourcePumping {
            biomes { +holderSet(Tags.Biomes.IS_RIVER) }
            result {
                water()
                amount *= 2
            }
            recipeId suffix "_at_river"
        }.save(exporter)
        RagiumRecipeBuilders.resourcePumping {
            biomes { +holderSet(Tags.Biomes.IS_WET) }
            result { water() }
            recipeId suffix "_at_wet_biomes"
        }.save(exporter)

        // Nether -> Lava
        RagiumRecipeBuilders.resourcePumping {
            biomes { +holderSet(Tags.Biomes.IS_NETHER) }
            result {
                lava()
                amount /= 4
            }
            recipeId suffix "_at_nether"
        }.save(exporter)
        // Soul Sand Valley -> Crude Oil
        RagiumRecipeBuilders.resourcePumping {
            biomes { biomes { +registries.getOrThrow(Biomes.SOUL_SAND_VALLEY) } }
            result {
                +RagiumFluids.CRUDE_OIL
                amount /= 4
            }
            recipeId suffix "_at_soul_sand_valley"
        }.save(exporter)
    }

    private fun honey() {
        // Block -> Liquid
        RagiumRecipeBuilders.melting {
            ingredient { items { +Items.HONEY_BLOCK } }
            result { +RagiumFluids.HONEY }
            recipeId suffix "_from_block"
        }.save(exporter)
        // Liquid -> Block
        RagiumRecipeBuilders.freezing {
            ingredient { +holderSet(RagiumFluids.HONEY) }
            catalyst { items { +RagiumItems.BLOCK_MOLD } }
            result { +Items.HONEY_BLOCK }
        }.save(exporter)

        // Honey Bottle -> Glass Bottle + Liquid
        RagiumRecipeBuilders.extracting {
            ingredient { +holderSet(Tags.Items.DRINKS_HONEY) }
            itemResult { +Items.GLASS_BOTTLE }
            fluidResult {
                +RagiumFluids.HONEY
                amount = 250
            }
            recipeId replace id("honey_from_bottle")
        }.save(exporter)
        // Glass Bottle + Liquid -> Honey Bottle
        RagiumRecipeBuilders.filling {
            itemIngredient { items { +Items.GLASS_BOTTLE } }
            fluidIngredient {
                +holderSet(RagiumFluids.HONEY)
                amount = 250
            }
            result { +Items.HONEY_BOTTLE }
        }.save(exporter)

        // Honeycomb -> Beeswax + Liquid
        RagiumRecipeBuilders.extracting {
            ingredient { items { +Items.HONEYCOMB } }
            itemResult { +RagiumItems.BEESWAX }
            fluidResult {
                +RagiumFluids.HONEY
                amount = 250
            }
            recipeId replace id("honey_from_comb")
        }.save(exporter)
        // Candle
        VanillaRecipeBuilders.shaped {
            +"A"
            +"B"
            define('A') { +holderSet(Tags.Items.STRINGS) }
            define('B') { items { +RagiumItems.BEESWAX } }
            result { +Items.CANDLE }
            category = RecipeCategory.DECORATIONS
        }.save(exporter)
    }

    private fun bioFuel() {
        // Seed -> Plant Oil + Bio Chaff
        RagiumRecipeBuilders.extracting {
            ingredient {
                +holderSet(Tags.Items.SEEDS)
                count *= 8
            }
            itemResult { +RagiumItems.BIO_CHAFF }
            fluidResult { +RagiumFluids.PLANT_OIL }
        }.save(exporter)
        // Bio Chaff -> Dirt
        RagiumRecipeBuilders.compressing {
            ingredient {
                items { +RagiumItems.BIO_CHAFF }
                count *= 4
            }
            result { +Items.DIRT }
            recipeId suffix "_from_chaff"
        }.save(exporter)

        // Water + Potato / Sugarcane -> Alcohol
        RagiumRecipeBuilders.brewing {
            itemIngredient { +holderSet(setOf(Tags.Items.CROPS_POTATO, Tags.Items.CROPS_SUGAR_CANE)) }
            fluidIngredient {
                +waterSet()
                amount /= 4
            }
            result {
                +RagiumFluids.ALCOHOL
                amount /= 4
            }
            recipeId prefix "bio_"
        }.save(exporter)

        // Plant Oil + R-OH + NaOH -> Fuel + Glycerol
        RagiumRecipeBuilders.fluidReacting {
            ingredient { +holderSet(RagiumFluids.PLANT_OIL) }
            ingredient {
                +holderSet(RagiumTags.Fluids.ALCOHOLS)
                amount *= 6
            }
            fluidResult {
                +RagiumFluids.FUEL
                amount *= 6
            }
            recipeId prefix "bio_"
        }.save(exporter)
    }
}
