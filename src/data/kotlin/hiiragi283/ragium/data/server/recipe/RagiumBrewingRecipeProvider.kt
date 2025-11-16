package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.common.recipe.HTPotionDropRecipe
import hiiragi283.ragium.impl.data.recipe.HTBrewingRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTMixingRecipeBuilder
import hiiragi283.ragium.setup.RagiumFluidContents
import net.minecraft.core.Holder
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.Potions
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.neoforged.neoforge.common.Tags

object RagiumBrewingRecipeProvider : HTRecipeProvider.Direct() {
    override fun buildRecipeInternal() {
        // Awkward Water
        HTMixingRecipeBuilder
            .create()
            .addIngredient(itemCreator.fromTagKey(Tags.Items.CROPS_NETHER_WART))
            .addIngredient(fluidCreator.water(1000))
            .setResult(resultHelper.fluid(RagiumFluidContents.AWKWARD_WATER, 1000))
            .save(output)
        // Potion Drop -> Potion
        save(
            RagiumAPI.id("shapeless/potion"),
            HTPotionDropRecipe(CraftingBookCategory.MISC),
        )

        potions()
        customEffects()
    }

    @JvmStatic
    private fun potions() {
        HTBrewingRecipeBuilder
            .create(
                itemCreator.fromItem(Items.GOLDEN_CARROT),
                Potions.NIGHT_VISION,
            ).save(output)
        HTBrewingRecipeBuilder
            .create(
                itemCreator.fromTagKey(Tags.Items.GEMS_AMETHYST),
                Potions.INVISIBILITY,
            ).save(output)
        HTBrewingRecipeBuilder
            .create(
                itemCreator.fromItem(Items.RABBIT_FOOT),
                Potions.LEAPING,
            ).save(output)
        HTBrewingRecipeBuilder
            .create(
                itemCreator.fromItem(Items.MAGMA_CREAM),
                Potions.FIRE_RESISTANCE,
            ).save(output)
        HTBrewingRecipeBuilder
            .create(
                itemCreator.fromItem(Items.SUGAR),
                Potions.SWIFTNESS,
            ).save(output)
        HTBrewingRecipeBuilder
            .create(
                itemCreator.fromTagKey(Tags.Items.INGOTS_IRON),
                Potions.SLOWNESS,
            ).save(output)
        HTBrewingRecipeBuilder
            .create(
                itemCreator.fromItem(Items.TURTLE_SCUTE),
                Potions.TURTLE_MASTER,
            ).save(output)
        HTBrewingRecipeBuilder
            .create(
                itemCreator.fromItem(Items.PUFFERFISH),
                Potions.WATER_BREATHING,
            ).save(output)
        HTBrewingRecipeBuilder
            .create(
                itemCreator.fromItem(Items.GLISTERING_MELON_SLICE),
                Potions.HEALING,
            ).save(output)
        HTBrewingRecipeBuilder
            .create(
                itemCreator.fromItem(Items.FERMENTED_SPIDER_EYE),
                Potions.HARMING,
            ).save(output)
        HTBrewingRecipeBuilder
            .create(
                itemCreator.fromItem(Items.SPIDER_EYE),
                Potions.POISON,
            ).save(output)
        HTBrewingRecipeBuilder
            .create(
                itemCreator.fromItem(Items.GHAST_TEAR),
                Potions.REGENERATION,
            ).save(output)
        HTBrewingRecipeBuilder
            .create(
                itemCreator.fromItem(Items.BLAZE_POWDER),
                Potions.STRENGTH,
            ).save(output)
        HTBrewingRecipeBuilder
            .create(
                itemCreator.fromItem(Items.BONE),
                Potions.WEAKNESS,
            ).save(output)
        HTBrewingRecipeBuilder
            .create(
                itemCreator.fromTagKey(Tags.Items.GEMS_EMERALD),
                Potions.LUCK,
            ).save(output)
        HTBrewingRecipeBuilder
            .create(
                itemCreator.fromItem(Items.PHANTOM_MEMBRANE),
                Potions.SLOW_FALLING,
            ).save(output)
        HTBrewingRecipeBuilder
            .create(
                itemCreator.fromItem(Items.WIND_CHARGE),
                Potions.WIND_CHARGED,
            ).save(output)
        HTBrewingRecipeBuilder
            .create(
                itemCreator.fromItem(Items.COBWEB),
                Potions.WEAVING,
            ).save(output)
        HTBrewingRecipeBuilder
            .create(
                itemCreator.fromTagKey(Tags.Items.STORAGE_BLOCKS_SLIME),
                Potions.OOZING,
            ).save(output)
        HTBrewingRecipeBuilder
            .create(
                itemCreator.fromTagKey(Tags.Items.STONES),
                Potions.INFESTED,
            ).save(output)
    }

    @JvmStatic
    private fun customEffects() {
        HTBrewingRecipeBuilder
            .create(itemCreator.fromItem(Items.GOLDEN_APPLE)) {
                add(MobEffectInstance(MobEffects.DIG_SPEED, 3 * 60 * 20, 0))
            }.save(output)

        mapOf(
            Items.POISONOUS_POTATO to MobEffects.CONFUSION,
            Items.ROTTEN_FLESH to MobEffects.HUNGER,
            Items.WITHER_ROSE to MobEffects.WITHER,
            Items.SHULKER_SHELL to MobEffects.LEVITATION,
        ).forEach { (item: Item, effect: Holder<MobEffect>) ->
            HTBrewingRecipeBuilder
                .create(itemCreator.fromItem(item)) {
                    add(MobEffectInstance(effect, 45 * 20, 0))
                }.save(output)
        }
    }
}
