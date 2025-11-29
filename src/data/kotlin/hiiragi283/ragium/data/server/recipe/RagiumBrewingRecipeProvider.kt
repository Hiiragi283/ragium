package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.common.recipe.crafting.HTPotionDropRecipe
import net.minecraft.core.Holder
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.Potions
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.neoforged.neoforge.common.Tags

object RagiumBrewingRecipeProvider : HTRecipeProvider.Direct() {
    override fun buildRecipeInternal() {
        // Potion Drop -> Potion
        save(
            RagiumAPI.id("shapeless", "potion"),
            HTPotionDropRecipe(CraftingBookCategory.MISC),
        )

        potions()
        customEffects()
    }

    @JvmStatic
    private fun potions() {
        brewing(itemCreator.fromItem(Items.GOLDEN_CARROT), Potions.NIGHT_VISION, Potions.LONG_NIGHT_VISION, null)
        brewing(itemCreator.fromTagKey(Tags.Items.GEMS_AMETHYST), Potions.INVISIBILITY, Potions.LONG_INVISIBILITY, null)
        brewing(itemCreator.fromItem(Items.RABBIT_FOOT), Potions.LEAPING, Potions.LONG_LEAPING, Potions.STRONG_LEAPING)
        brewing(itemCreator.fromItem(Items.MAGMA_CREAM), Potions.FIRE_RESISTANCE, Potions.LONG_FIRE_RESISTANCE, null)
        brewing(itemCreator.fromItem(Items.SUGAR), Potions.SWIFTNESS, Potions.LONG_SWIFTNESS, Potions.STRONG_SWIFTNESS)
        brewing(itemCreator.fromTagKey(Tags.Items.INGOTS_IRON), Potions.SLOWNESS, Potions.LONG_SLOWNESS, Potions.STRONG_SLOWNESS)
        brewing(itemCreator.fromItem(Items.TURTLE_SCUTE), Potions.TURTLE_MASTER, Potions.LONG_TURTLE_MASTER, Potions.STRONG_TURTLE_MASTER)
        brewing(itemCreator.fromItem(Items.PUFFERFISH), Potions.WATER_BREATHING, Potions.LONG_WATER_BREATHING, null)
        brewing(itemCreator.fromItem(Items.GLISTERING_MELON_SLICE), Potions.HEALING, null, Potions.STRONG_HEALING)
        brewing(itemCreator.fromItem(Items.FERMENTED_SPIDER_EYE), Potions.HARMING, null, Potions.STRONG_HARMING)
        brewing(itemCreator.fromItem(Items.SPIDER_EYE), Potions.POISON, Potions.LONG_POISON, Potions.STRONG_POISON)
        brewing(itemCreator.fromItem(Items.GHAST_TEAR), Potions.REGENERATION, Potions.LONG_REGENERATION, Potions.STRONG_REGENERATION)
        brewing(itemCreator.fromItem(Items.BLAZE_POWDER), Potions.STRENGTH, Potions.LONG_STRENGTH, Potions.STRONG_STRENGTH)
        brewing(itemCreator.fromItem(Items.BONE), Potions.WEAKNESS, Potions.LONG_WEAKNESS, null)
        brewing(itemCreator.fromTagKey(Tags.Items.GEMS_EMERALD), Potions.LUCK)
        brewing(itemCreator.fromItem(Items.PHANTOM_MEMBRANE), Potions.SLOW_FALLING, Potions.LONG_SLOW_FALLING, null)

        brewing(itemCreator.fromItem(Items.WIND_CHARGE), Potions.WIND_CHARGED)
        brewing(itemCreator.fromItem(Items.COBWEB), Potions.WEAVING)
        brewing(itemCreator.fromTagKey(Tags.Items.STORAGE_BLOCKS_SLIME), Potions.OOZING)
        brewing(itemCreator.fromTagKey(Tags.Items.STONES), Potions.INFESTED)
    }

    @JvmStatic
    private fun customEffects() {
        // Dig
        mapOf(
            Items.CAKE to MobEffects.SATURATION,
            Items.GLOWSTONE to MobEffects.GLOWING,
            Items.GOLDEN_PICKAXE to MobEffects.DIG_SPEED,
        ).forEach { (item: Item, effect: Holder<MobEffect>) ->
            benefitBrewing(itemCreator.fromItem(item), effect)
        }

        mapOf(
            Items.ECHO_SHARD to MobEffects.DARKNESS,
            Items.POISONOUS_POTATO to MobEffects.CONFUSION,
            Items.ROTTEN_FLESH to MobEffects.HUNGER,
            Items.SHULKER_SHELL to MobEffects.LEVITATION,
            Items.STONE_PICKAXE to MobEffects.DIG_SLOWDOWN,
            Items.TINTED_GLASS to MobEffects.BLINDNESS,
            Items.WITHER_ROSE to MobEffects.WITHER,
        ).forEach { (item: Item, effect: Holder<MobEffect>) ->
            harmfulBrewing(itemCreator.fromItem(item), effect)
        }
    }
}
