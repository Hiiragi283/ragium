package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.common.recipe.HTPotionDropRecipe
import hiiragi283.ragium.impl.data.recipe.HTCombineRecipeBuilder
import net.minecraft.core.Holder
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.Potion
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
        brewing(
            itemCreator.fromItem(Items.GOLDEN_CARROT),
            Potions.NIGHT_VISION,
        )
        brewing(
            itemCreator.fromTagKey(Tags.Items.GEMS_AMETHYST),
            Potions.INVISIBILITY,
        )
        brewing(
            itemCreator.fromItem(Items.RABBIT_FOOT),
            Potions.LEAPING,
        )
        brewing(
            itemCreator.fromItem(Items.MAGMA_CREAM),
            Potions.FIRE_RESISTANCE,
        )
        brewing(
            itemCreator.fromItem(Items.SUGAR),
            Potions.SWIFTNESS,
        )
        brewing(
            itemCreator.fromTagKey(Tags.Items.INGOTS_IRON),
            Potions.SLOWNESS,
        )
        brewing(
            itemCreator.fromItem(Items.TURTLE_SCUTE),
            Potions.TURTLE_MASTER,
        )
        brewing(
            itemCreator.fromItem(Items.PUFFERFISH),
            Potions.WATER_BREATHING,
        )
        brewing(
            itemCreator.fromItem(Items.GLISTERING_MELON_SLICE),
            Potions.HEALING,
        )
        brewing(
            itemCreator.fromItem(Items.FERMENTED_SPIDER_EYE),
            Potions.HARMING,
        )
        brewing(
            itemCreator.fromItem(Items.SPIDER_EYE),
            Potions.POISON,
        )
        brewing(
            itemCreator.fromItem(Items.GHAST_TEAR),
            Potions.REGENERATION,
        )
        brewing(
            itemCreator.fromItem(Items.BLAZE_POWDER),
            Potions.STRENGTH,
        )
        brewing(
            itemCreator.fromItem(Items.BONE),
            Potions.WEAKNESS,
        )
        brewing(
            itemCreator.fromTagKey(Tags.Items.GEMS_EMERALD),
            Potions.LUCK,
        )
        brewing(
            itemCreator.fromItem(Items.PHANTOM_MEMBRANE),
            Potions.SLOW_FALLING,
        )
        brewing(
            itemCreator.fromItem(Items.WIND_CHARGE),
            Potions.WIND_CHARGED,
        )
        brewing(
            itemCreator.fromItem(Items.COBWEB),
            Potions.WEAVING,
        )
        brewing(
            itemCreator.fromTagKey(Tags.Items.STORAGE_BLOCKS_SLIME),
            Potions.OOZING,
        )
        brewing(
            itemCreator.fromTagKey(Tags.Items.STONES),
            Potions.INFESTED,
        )
    }

    @JvmStatic
    private fun customEffects() {
        brewing(itemCreator.fromItem(Items.GOLDEN_APPLE)) {
            add(MobEffectInstance(MobEffects.DIG_SPEED, 3 * 60 * 20, 0))
        }

        mapOf(
            Items.POISONOUS_POTATO to MobEffects.CONFUSION,
            Items.ROTTEN_FLESH to MobEffects.HUNGER,
            Items.WITHER_ROSE to MobEffects.WITHER,
            Items.SHULKER_SHELL to MobEffects.LEVITATION,
        ).forEach { (item: Item, effect: Holder<MobEffect>) ->
            brewing(itemCreator.fromItem(item)) { add(MobEffectInstance(effect, 45 * 20, 0)) }
        }
    }

    @JvmStatic
    private fun brewing(
        right: HTItemIngredient,
        potion: Holder<Potion>,
        left: HTItemIngredient = itemCreator.fromTagKey(Tags.Items.CROPS_NETHER_WART),
    ) {
        HTCombineRecipeBuilder
            .brewing(
                left,
                right,
                potion,
            ).save(output)
    }

    @JvmStatic
    private fun brewing(
        right: HTItemIngredient,
        left: HTItemIngredient = itemCreator.fromTagKey(Tags.Items.CROPS_NETHER_WART),
        builderAction: MutableList<MobEffectInstance>.() -> Unit,
    ) {
        HTCombineRecipeBuilder
            .brewing(
                left,
                right,
                builderAction,
            ).save(output)
    }
}
