package hiiragi283.ragium.data.recipe

import hiiragi283.lib.data.recipe.HTRecipeProvider
import hiiragi283.lib.item.component.PotionContents
import hiiragi283.lib.recipe.ingredient.HTPotionFluidIngredient
import hiiragi283.lib.tag.CommonTagPrefixes
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.recipe.RagiumRecipeBuilders
import hiiragi283.ragium.api.material.RagiumMaterial
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.tags.TagKey
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.Potions
import net.minecraft.world.item.component.Consumable
import net.minecraft.world.item.component.Consumables
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect
import net.neoforged.neoforge.common.Tags

class RagiumBioRecipeProvider(packOutput: PackOutput, future: CompletableFuture<HolderLookup.Provider>) : HTRecipeProvider(packOutput, future, RagiumAPI.MOD_ID) {
    override fun buildRecipes() {
        brewing()
        planting()
    }

    private fun brewing() {
        // Poison + Spider Eye -> Fermented Spider Eye
        RagiumRecipeBuilders.freezing {
            itemIngredient { items { +Items.SPIDER_EYE } }
            fluidIngredient {
                +HTPotionFluidIngredient(Potions.POISON)
                amount = 250
            }
            result { +Items.FERMENTED_SPIDER_EYE }
        }
        // Poison + Potato -> Poisonous Potato
        RagiumRecipeBuilders.freezing {
            itemIngredient { +holderSet(Tags.Items.CROPS_POTATO) }
            fluidIngredient {
                +HTPotionFluidIngredient(Potions.POISON)
                amount = 250
            }
            result { +Items.POISONOUS_POTATO }
        }

        // Haste
        // Nausea
        // Resistance
        // Blindness
        // Hunger
        RagiumRecipeBuilders.brewing {
            itemIngredient { items { +Items.ROTTEN_FLESH } }
            fluidIngredient { +HTPotionFluidIngredient(Potions.THICK) }
            result { +PotionContents(customEffects = listOf(MobEffectInstance(MobEffects.HUNGER, 900)), customName = "hunger") }
            recipeId replace "potion/hunger"
        }.save(exporter)
        // Wither
        // Health Boost
        // Absorption
        // Saturation
        // Glowing
        // Levitation
        // Luck
        // Unluck
        // Darkness
        RagiumRecipeBuilders.brewing {
            itemIngredient { +holderSet(CommonTagPrefixes.GEM, RagiumMaterial.Gem.ECHO) }
            fluidIngredient { +HTPotionFluidIngredient(Potions.THICK) }
            result { +PotionContents(customEffects = listOf(MobEffectInstance(MobEffects.DARKNESS, 900)), customName = "darkness") }
            recipeId replace "potion/darkness"
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
            recipeId replace "potion/golden_apple"
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
            recipeId replace "potion/enchanted_golden_apple"
        }.save(exporter)
    }

    private fun extractEffects(consumable: Consumable): List<MobEffectInstance> = consumable
        .onConsumeEffects
        .filterIsInstance<ApplyStatusEffectsConsumeEffect>()
        .flatMap(ApplyStatusEffectsConsumeEffect::effects)

    private fun planting() {
        // Plant with seed
        setOf(
            Triple(Tags.Items.SEEDS_BEETROOT, Items.BEETROOT, Items.BEETROOT_SEEDS),
            Triple(Tags.Items.SEEDS_MELON, Items.MELON, Items.MELON_SEEDS),
            Triple(Tags.Items.SEEDS_PITCHER_PLANT, Items.PITCHER_PLANT, Items.PITCHER_POD),
            Triple(Tags.Items.SEEDS_PUMPKIN, Items.PUMPKIN, Items.PUMPKIN_SEEDS),
            Triple(Tags.Items.SEEDS_TORCHFLOWER, Items.TORCHFLOWER, Items.TORCHFLOWER_SEEDS),
            Triple(Tags.Items.SEEDS_WHEAT, Items.WHEAT, Items.WHEAT_SEEDS),
        ).forEach { (seedIn: TagKey<Item>, crop: Item, seedOut: Item) ->
            RagiumRecipeBuilders.planting {
                ingredient {
                    +holderSet(seedIn)
                    count = 8
                }
                primary {
                    +crop
                    count = 12
                }
                secondary {
                    +seedOut
                    count = 12
                }
            }.save(exporter)
        }
        // Plant without seed
        val crops: Set<Item> = setOf(Items.CARROT, Items.POTATO, Items.SWEET_BERRIES, Items.GLOW_BERRIES, Items.APPLE, Items.COCOA_BEANS, Items.NETHER_WART, Items.BROWN_MUSHROOM, Items.RED_MUSHROOM)
        for (crop: Item in crops) {
            RagiumRecipeBuilders.planting {
                ingredient {
                    items { +crop }
                    count = 8
                }
                primary {
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
                primary {
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
                primary {
                    +log
                    count = 48
                }
                secondary {
                    +sapling
                    count = 12
                }
            }.save(exporter)
        }
    }

    override fun getName(): String = "Bio Recipes"
}
