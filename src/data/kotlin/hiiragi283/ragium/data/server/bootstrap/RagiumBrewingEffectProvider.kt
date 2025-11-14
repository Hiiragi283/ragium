package hiiragi283.ragium.data.server.bootstrap

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumPlatform
import hiiragi283.ragium.api.data.recipe.ingredient.HTItemIngredientCreator
import hiiragi283.ragium.api.data.registry.HTBrewingEffect
import hiiragi283.ragium.api.item.alchemy.HTMobEffectInstance
import hiiragi283.ragium.api.recipe.ingredient.HTItemIngredient
import hiiragi283.ragium.api.registry.createKey
import hiiragi283.ragium.api.registry.idOrThrow
import net.minecraft.core.Holder
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.Potions
import net.neoforged.neoforge.common.Tags

object RagiumBrewingEffectProvider : RegistrySetBuilder.RegistryBootstrap<HTBrewingEffect> {
    private lateinit var itemCreator: HTItemIngredientCreator

    override fun run(context: BootstrapContext<HTBrewingEffect>) {
        itemCreator = RagiumPlatform.INSTANCE.createItemCreator(context.lookup(Registries.ITEM))
        // Potions
        register(context, itemCreator.fromItem(Items.GOLDEN_CARROT), Potions.NIGHT_VISION)
        register(context, itemCreator.fromTagKey(Tags.Items.GEMS_AMETHYST), Potions.INVISIBILITY)
        register(context, itemCreator.fromItem(Items.RABBIT_FOOT), Potions.LEAPING)
        register(context, itemCreator.fromItem(Items.MAGMA_CREAM), Potions.FIRE_RESISTANCE)
        register(context, itemCreator.fromItem(Items.SUGAR), Potions.SWIFTNESS)
        register(context, itemCreator.fromTagKey(Tags.Items.INGOTS_IRON), Potions.SLOWNESS)
        register(context, itemCreator.fromItem(Items.TURTLE_SCUTE), Potions.TURTLE_MASTER)
        register(context, itemCreator.fromItem(Items.PUFFERFISH), Potions.WATER_BREATHING)
        register(context, itemCreator.fromItem(Items.GLISTERING_MELON_SLICE), Potions.HEALING)
        register(context, itemCreator.fromItem(Items.FERMENTED_SPIDER_EYE), Potions.HARMING)
        register(context, itemCreator.fromItem(Items.SPIDER_EYE), Potions.POISON)
        register(context, itemCreator.fromItem(Items.GHAST_TEAR), Potions.REGENERATION)
        register(context, itemCreator.fromItem(Items.BLAZE_POWDER), Potions.STRENGTH)
        register(context, itemCreator.fromItem(Items.BONE), Potions.WEAKNESS)
        register(context, itemCreator.fromTagKey(Tags.Items.GEMS_EMERALD), Potions.LUCK)
        register(context, itemCreator.fromItem(Items.PHANTOM_MEMBRANE), Potions.SLOW_FALLING)
        register(context, itemCreator.fromItem(Items.WIND_CHARGE), Potions.WIND_CHARGED)
        register(context, itemCreator.fromItem(Items.COBWEB), Potions.WEAVING)
        register(context, itemCreator.fromTagKey(Tags.Items.STORAGE_BLOCKS_SLIME), Potions.OOZING)
        register(context, itemCreator.fromTagKey(Tags.Items.STONES), Potions.INFESTED)

        // Custom - Vanilla
        register(context, itemCreator.fromItem(Items.GOLDEN_APPLE)) {
            add(HTMobEffectInstance(MobEffects.DIG_SPEED, 3 * 60 * 20, 0))
        }

        mapOf(
            Items.POISONOUS_POTATO to MobEffects.CONFUSION,
            Items.ROTTEN_FLESH to MobEffects.HUNGER,
            Items.WITHER_ROSE to MobEffects.WITHER,
            Items.SHULKER_SHELL to MobEffects.LEVITATION,
        ).forEach { (item: Item, effect: Holder<MobEffect>) ->
            register(context, itemCreator.fromItem(item)) { add(HTMobEffectInstance(effect, 45 * 20, 0)) }
        }
    }

    @JvmStatic
    private fun register(context: BootstrapContext<HTBrewingEffect>, ingredient: HTItemIngredient, potion: Holder<Potion>) {
        context.register(
            RagiumAPI.BREWING_EFFECT_KEY.createKey(potion.idOrThrow),
            HTBrewingEffect(ingredient, potion),
        )
    }

    @JvmStatic
    private fun register(
        context: BootstrapContext<HTBrewingEffect>,
        ingredient: HTItemIngredient,
        builderAction: MutableList<HTMobEffectInstance>.() -> Unit,
    ) {
        val effect = HTBrewingEffect(ingredient, builderAction)
        val firstEffect: Holder<MobEffect> = effect.getFirstEffect()?.effect ?: return
        context.register(RagiumAPI.BREWING_EFFECT_KEY.createKey(firstEffect.idOrThrow), effect)
    }
}
