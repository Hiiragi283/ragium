package hiiragi283.ragium.data.server.bootstrap

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.registry.HTBrewingEffect
import hiiragi283.ragium.api.item.component.HTPotionBuilder
import hiiragi283.ragium.api.registry.createKey
import hiiragi283.ragium.api.registry.idOrThrow
import net.minecraft.core.Holder
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.Potions
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.common.Tags

object RagiumBrewingEffectProvider : RegistrySetBuilder.RegistryBootstrap<HTBrewingEffect> {
    override fun run(context: BootstrapContext<HTBrewingEffect>) {
        // Potions
        register(context, Ingredient.of(Items.GOLDEN_CARROT), Potions.NIGHT_VISION)
        register(context, Ingredient.of(Tags.Items.GEMS_AMETHYST), Potions.INVISIBILITY)
        register(context, Ingredient.of(Items.RABBIT_FOOT), Potions.LEAPING)
        register(context, Ingredient.of(Items.MAGMA_CREAM), Potions.FIRE_RESISTANCE)
        register(context, Ingredient.of(Items.SUGAR), Potions.SWIFTNESS)
        register(context, Ingredient.of(Tags.Items.INGOTS_IRON), Potions.SLOWNESS)
        register(context, Ingredient.of(Items.TURTLE_SCUTE), Potions.TURTLE_MASTER)
        register(context, Ingredient.of(Items.PUFFERFISH), Potions.WATER_BREATHING)
        register(context, Ingredient.of(Items.GLISTERING_MELON_SLICE), Potions.HEALING)
        register(context, Ingredient.of(Items.FERMENTED_SPIDER_EYE), Potions.HARMING)
        register(context, Ingredient.of(Items.SPIDER_EYE), Potions.POISON)
        register(context, Ingredient.of(Items.GHAST_TEAR), Potions.REGENERATION)
        register(context, Ingredient.of(Items.BLAZE_POWDER), Potions.STRENGTH)
        register(context, Ingredient.of(Items.BONE), Potions.WEAKNESS)
        register(context, Ingredient.of(Tags.Items.GEMS_EMERALD), Potions.LUCK)
        register(context, Ingredient.of(Items.PHANTOM_MEMBRANE), Potions.SLOW_FALLING)
        register(context, Ingredient.of(Items.WIND_CHARGE), Potions.WIND_CHARGED)
        register(context, Ingredient.of(Items.COBWEB), Potions.WEAVING)
        register(context, Ingredient.of(Tags.Items.STORAGE_BLOCKS_SLIME), Potions.OOZING)
        register(context, Ingredient.of(Tags.Items.STONES), Potions.INFESTED)

        // Custom - Vanilla
        register(context, Ingredient.of(Items.GOLDEN_APPLE)) { addEffect(MobEffects.DIG_SPEED, 3 * 60 * 20, 0) }

        mapOf(
            Items.POISONOUS_POTATO to MobEffects.CONFUSION,
            Items.ROTTEN_FLESH to MobEffects.HUNGER,
            Items.WITHER_ROSE to MobEffects.WITHER,
            Items.SHULKER_SHELL to MobEffects.LEVITATION,
        ).forEach { (item: Item, effect: Holder<MobEffect>) ->
            register(context, Ingredient.of(item)) { addEffect(effect, 45 * 20, 0) }
        }
    }

    @JvmStatic
    private fun register(context: BootstrapContext<HTBrewingEffect>, ingredient: Ingredient, potion: Holder<Potion>) {
        context.register(
            RagiumAPI.BREWING_EFFECT_KEY.createKey(potion.idOrThrow),
            HTBrewingEffect(ingredient, potion),
        )
    }

    @JvmStatic
    private fun register(context: BootstrapContext<HTBrewingEffect>, ingredient: Ingredient, builderAction: HTPotionBuilder.() -> Unit) {
        val effect = HTBrewingEffect(ingredient, builderAction)
        val firstEffect: Holder<MobEffect> = effect.content.allEffects
            .first()
            .effect
        context.register(RagiumAPI.BREWING_EFFECT_KEY.createKey(firstEffect.idOrThrow), effect)
    }
}
