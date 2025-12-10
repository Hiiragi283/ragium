package hiiragi283.ragium.data.server.bootstrap

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumPlatform
import hiiragi283.ragium.api.data.HTBrewingRecipeData
import hiiragi283.ragium.api.data.recipe.ingredient.HTItemIngredientCreator
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.registry.createKey
import hiiragi283.ragium.api.registry.idOrThrow
import hiiragi283.ragium.api.util.wrapOptional
import hiiragi283.ragium.common.data.brewing.HTEffectBrewingRecipeData
import hiiragi283.ragium.common.data.brewing.HTPotionBrewingRecipeData
import net.minecraft.core.Holder
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.Potions
import net.neoforged.neoforge.common.Tags

object RagiumBrewingRecipeDataProvider : RegistrySetBuilder.RegistryBootstrap<HTBrewingRecipeData> {
    val itemCreator: HTItemIngredientCreator = RagiumPlatform.INSTANCE.itemCreator()

    private lateinit var context: BootstrapContext<HTBrewingRecipeData>

    override fun run(context: BootstrapContext<HTBrewingRecipeData>) {
        this.context = context
        // Potion
        register(itemCreator.fromItem(Items.GOLDEN_CARROT), Potions.NIGHT_VISION, Potions.LONG_NIGHT_VISION)
        register(itemCreator.fromTagKey(Tags.Items.GEMS_AMETHYST), Potions.INVISIBILITY, Potions.LONG_INVISIBILITY)
        register(itemCreator.fromItem(Items.RABBIT_FOOT), Potions.LEAPING, Potions.LONG_LEAPING, Potions.STRONG_LEAPING)
        register(itemCreator.fromItem(Items.MAGMA_CREAM), Potions.FIRE_RESISTANCE, Potions.LONG_FIRE_RESISTANCE)
        register(itemCreator.fromItem(Items.SUGAR), Potions.SWIFTNESS, Potions.LONG_SWIFTNESS, Potions.STRONG_SWIFTNESS)
        register(itemCreator.fromTagKey(Tags.Items.INGOTS_IRON), Potions.SLOWNESS, Potions.LONG_SLOWNESS, Potions.STRONG_SLOWNESS)
        register(itemCreator.fromItem(Items.TURTLE_SCUTE), Potions.TURTLE_MASTER, Potions.LONG_TURTLE_MASTER, Potions.STRONG_TURTLE_MASTER)
        register(itemCreator.fromItem(Items.PUFFERFISH), Potions.WATER_BREATHING, Potions.LONG_WATER_BREATHING)
        register(itemCreator.fromItem(Items.GLISTERING_MELON_SLICE), Potions.HEALING, null, Potions.STRONG_HEALING)
        register(itemCreator.fromItem(Items.FERMENTED_SPIDER_EYE), Potions.HARMING, null, Potions.STRONG_HARMING)
        register(itemCreator.fromItem(Items.SPIDER_EYE), Potions.POISON, Potions.LONG_POISON, Potions.STRONG_POISON)
        register(itemCreator.fromItem(Items.GHAST_TEAR), Potions.REGENERATION, Potions.LONG_REGENERATION, Potions.STRONG_REGENERATION)
        register(itemCreator.fromItem(Items.BLAZE_POWDER), Potions.STRENGTH, Potions.LONG_STRENGTH, Potions.STRONG_STRENGTH)
        register(itemCreator.fromItem(Items.BONE), Potions.WEAKNESS, Potions.LONG_WEAKNESS)
        register(itemCreator.fromTagKey(Tags.Items.GEMS_EMERALD), Potions.LUCK)
        register(itemCreator.fromItem(Items.PHANTOM_MEMBRANE), Potions.SLOW_FALLING, Potions.LONG_SLOW_FALLING)

        register(itemCreator.fromItem(Items.WIND_CHARGE), Potions.WIND_CHARGED)
        register(itemCreator.fromItem(Items.COBWEB), Potions.WEAVING)
        register(itemCreator.fromTagKey(Tags.Items.STORAGE_BLOCKS_SLIME), Potions.OOZING)
        register(itemCreator.fromTagKey(Tags.Items.STONES), Potions.INFESTED)
        // Effect
        mapOf(
            Items.CAKE to MobEffects.SATURATION,
            Items.GLOWSTONE to MobEffects.GLOWING,
            Items.GOLDEN_PICKAXE to MobEffects.DIG_SPEED,
        ).forEach { (item: Item, effect: Holder<MobEffect>) ->
            register(itemCreator.fromItem(item), effect, HTEffectBrewingRecipeData::benefit)
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
            register(itemCreator.fromItem(item), effect, HTEffectBrewingRecipeData::harmful)
        }
    }

    @JvmStatic
    fun register(
        ingredient: HTItemIngredient,
        base: Holder<Potion>,
        long: Holder<Potion>? = null,
        strong: Holder<Potion>? = null,
    ) {
        context.register(
            RagiumAPI.BREWING_RECIPE_KEY.createKey(base.idOrThrow.withPrefix("potion/")),
            HTPotionBrewingRecipeData(
                ingredient,
                base,
                long.wrapOptional(),
                strong.wrapOptional(),
            ),
        )
    }

    @JvmStatic
    fun register(
        ingredient: HTItemIngredient,
        effect: Holder<MobEffect>,
        factory: (HTItemIngredient, Holder<MobEffect>) -> HTEffectBrewingRecipeData,
    ) {
        context.register(
            RagiumAPI.BREWING_RECIPE_KEY.createKey(effect.idOrThrow.withPrefix("effect/")),
            factory(ingredient, effect),
        )
    }
}
