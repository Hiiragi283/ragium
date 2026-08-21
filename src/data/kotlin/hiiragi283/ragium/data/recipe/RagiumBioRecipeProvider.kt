package hiiragi283.ragium.data.recipe

import hiiragi283.lib.data.recipe.HTRecipeProvider
import hiiragi283.lib.item.component.PotionContents
import hiiragi283.lib.recipe.ingredient.HTPotionFluidIngredient
import hiiragi283.lib.tag.CommonTagPrefixes
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.recipe.RagiumRecipeBuilders
import hiiragi283.ragium.api.tag.HTMaterial
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.Potions
import net.minecraft.world.item.component.Consumable
import net.minecraft.world.item.component.Consumables
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect
import net.neoforged.neoforge.common.Tags

class RagiumBioRecipeProvider(packOutput: PackOutput, future: CompletableFuture<HolderLookup.Provider>) : HTRecipeProvider(packOutput, future, RagiumAPI.MOD_ID) {
    override fun buildRecipes() {
        brewing()
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
            fluidIngredient { +HTPotionFluidIngredient(Potions.MUNDANE) }
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
            itemIngredient { +holderSet(CommonTagPrefixes.GEM, HTMaterial.Gem.ECHO) }
            fluidIngredient { +HTPotionFluidIngredient(Potions.MUNDANE) }
            result { +PotionContents(customEffects = listOf(MobEffectInstance(MobEffects.DARKNESS, 900)), customName = "darkness") }
            recipeId replace "potion/darkness"
        }.save(exporter)

        // Golden Apple
        RagiumRecipeBuilders.brewing {
            itemIngredient { items { +Items.GOLDEN_APPLE } }
            fluidIngredient { +HTPotionFluidIngredient(Potions.MUNDANE) }
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
            fluidIngredient { +HTPotionFluidIngredient(Potions.MUNDANE) }
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

    override fun getName(): String = "Bio Recipes"
}
