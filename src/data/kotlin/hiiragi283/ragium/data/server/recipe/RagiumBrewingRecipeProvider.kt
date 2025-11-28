package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import hiiragi283.ragium.common.material.VanillaMaterialKeys
import hiiragi283.ragium.common.recipe.HTPotionDropRecipe
import hiiragi283.ragium.common.util.HTPotionHelper
import hiiragi283.ragium.impl.data.recipe.HTCombineRecipeBuilder
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponents
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.item.alchemy.Potions
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.crafting.DataComponentIngredient

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

    //    Extensions    //

    // Potion
    @JvmStatic
    private fun dropIngredient(potion: Holder<Potion>): HTItemIngredient = itemCreator.fromVanilla(
        DataComponentIngredient.of(false, DataComponents.POTION_CONTENTS, PotionContents(potion), RagiumItems.POTION_DROP),
    )

    @JvmStatic
    private fun brewing(right: HTItemIngredient, potion: Holder<Potion>) {
        HTCombineRecipeBuilder
            .brewing(
                itemCreator.fromTagKey(Tags.Items.CROPS_NETHER_WART),
                right,
                potion,
            ).save(output)
    }

    @JvmStatic
    private fun brewing(
        right: HTItemIngredient,
        base: Holder<Potion>,
        long: Holder<Potion>?,
        strong: Holder<Potion>?,
    ) {
        // Base
        brewing(right, base)
        val drop: HTItemIngredient = dropIngredient(base)
        // Long
        if (long != null) {
            HTCombineRecipeBuilder
                .brewing(
                    itemCreator.fromTagKey(CommonMaterialPrefixes.DUST, VanillaMaterialKeys.REDSTONE),
                    drop,
                    long,
                ).save(output)
        }
        // Strong
        if (strong != null) {
            HTCombineRecipeBuilder
                .brewing(
                    itemCreator.fromTagKey(CommonMaterialPrefixes.DUST, VanillaMaterialKeys.GLOWSTONE),
                    drop,
                    strong,
                ).save(output)
        }
    }

    // MobEffectInstance
    @JvmStatic
    private fun dropIngredient(instance: MobEffectInstance): HTItemIngredient = itemCreator.fromVanilla(
        DataComponentIngredient.of(false, DataComponents.POTION_CONTENTS, HTPotionHelper.content(instance), RagiumItems.POTION_DROP),
    )

    @JvmStatic
    private fun brewing(
        right: HTItemIngredient,
        left: HTItemIngredient = itemCreator.fromTagKey(Tags.Items.CROPS_NETHER_WART),
        builderAction: MutableList<MobEffectInstance>.() -> Unit,
    ): HTCombineRecipeBuilder<PotionContents> = HTCombineRecipeBuilder
        .brewing(
            left,
            right,
            builderAction,
        )

    @JvmStatic
    private fun benefitBrewing(right: HTItemIngredient, effect: Holder<MobEffect>) {
        longAndStrongBrewing(right, effect, 3600, 9600, 1800)
    }

    @JvmStatic
    private fun harmfulBrewing(right: HTItemIngredient, effect: Holder<MobEffect>) {
        longAndStrongBrewing(right, effect, 900, 1800, 432)
    }

    @JvmStatic
    private fun longAndStrongBrewing(
        right: HTItemIngredient,
        effect: Holder<MobEffect>,
        baseTime: Int,
        longTime: Int,
        strongTime: Int,
    ) {
        val instance = MobEffectInstance(effect, baseTime)
        val drop: HTItemIngredient = dropIngredient(instance)
        // Base
        brewing(right) { add(instance) }.save(output)
        // Long
        brewing(
            itemCreator.fromTagKey(CommonMaterialPrefixes.DUST, VanillaMaterialKeys.REDSTONE),
            drop,
        ) { add(MobEffectInstance(effect, longTime)) }.savePrefixed(output, "long_")
        // Strong
        brewing(
            itemCreator.fromTagKey(CommonMaterialPrefixes.DUST, VanillaMaterialKeys.GLOWSTONE),
            drop,
        ) { add(MobEffectInstance(effect, strongTime, 1)) }.savePrefixed(output, "strong_")
    }
}
