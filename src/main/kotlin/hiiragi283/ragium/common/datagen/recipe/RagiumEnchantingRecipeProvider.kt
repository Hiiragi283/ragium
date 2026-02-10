package hiiragi283.ragium.common.datagen.recipe

import hiiragi283.core.api.data.recipe.HTRecipeProviderContext
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.ragium.common.data.recipe.HTSingleRecipeBuilder
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.Enchantments
import net.neoforged.neoforge.common.Tags

object RagiumEnchantingRecipeProvider : HTRecipeProviderContext() {
    override lateinit var output: RecipeOutput
        private set

    private lateinit var enchLookup: HolderLookup.RegistryLookup<Enchantment>

    fun buildRecipes(provider: HolderLookup.Provider, output: RecipeOutput) {
        this.enchLookup = provider.lookupOrThrow(Registries.ENCHANTMENT)
        this.output = output

        armor()
        melee()
        tool()
        bow()
        lure()
        trident()
        crossBow()
        mace()

        HTSingleRecipeBuilder.enchanting(output) {
            ingredient = inputCreator.create(Tags.Items.PUMPKINS_CARVED)
            result = enchLookup.getOrThrow(Enchantments.BINDING_CURSE)
        }
        HTSingleRecipeBuilder.enchanting(output) {
            ingredient = inputCreator.create(Tags.Items.COBBLESTONES)
            result = enchLookup.getOrThrow(Enchantments.VANISHING_CURSE)
        }
        HTSingleRecipeBuilder.enchanting(output) {
            ingredient = inputCreator.create(Tags.Items.NETHER_STARS)
            result = enchLookup.getOrThrow(Enchantments.MENDING)
        }
    }

    @JvmStatic
    private fun armor() {
        HTSingleRecipeBuilder.enchanting(output) {
            ingredient = inputCreator.create(CommonTagPrefixes.INGOT, CommonMaterialKeys.STEEL, 64)
            result = enchLookup.getOrThrow(Enchantments.PROTECTION)
        }
        HTSingleRecipeBuilder.enchanting(output) {
            ingredient = inputCreator.create(Items.MAGMA_CREAM, 64)
            result = enchLookup.getOrThrow(Enchantments.FIRE_PROTECTION)
        }
        HTSingleRecipeBuilder.enchanting(output) {
            ingredient = inputCreator.create(Tags.Items.FEATHERS, 64)
            result = enchLookup.getOrThrow(Enchantments.FEATHER_FALLING)
        }
        HTSingleRecipeBuilder.enchanting(output) {
            ingredient = inputCreator.create(CommonTagPrefixes.INGOT, VanillaMaterialKeys.IRON, 64)
            result = enchLookup.getOrThrow(Enchantments.PROJECTILE_PROTECTION)
        }

        HTSingleRecipeBuilder.enchanting(output) {
            ingredient = inputCreator.create(Items.PUFFERFISH, 64)
            result = enchLookup.getOrThrow(Enchantments.RESPIRATION)
        }
        HTSingleRecipeBuilder.enchanting(output) {
            ingredient = inputCreator.create(Items.TROPICAL_FISH, 16)
            result = enchLookup.getOrThrow(Enchantments.AQUA_AFFINITY)
        }

        HTSingleRecipeBuilder.enchanting(output) {
            ingredient = inputCreator.create(Tags.Items.CROPS_CACTUS)
            result = enchLookup.getOrThrow(Enchantments.THORNS)
        }

        HTSingleRecipeBuilder.enchanting(output) {
            ingredient = inputCreator.create(CommonTagPrefixes.PLATE, CommonMaterialKeys.PLASTIC, 64)
            result = enchLookup.getOrThrow(Enchantments.DEPTH_STRIDER)
        }
        HTSingleRecipeBuilder.enchanting(output) {
            ingredient = inputCreator.create(Items.PACKED_ICE, 64)
            result = enchLookup.getOrThrow(Enchantments.FROST_WALKER)
        }

        HTSingleRecipeBuilder.enchanting(output) {
            ingredient = inputCreator.create(ItemTags.SOUL_FIRE_BASE_BLOCKS, 64)
            result = enchLookup.getOrThrow(Enchantments.SOUL_SPEED)
        }
        HTSingleRecipeBuilder.enchanting(output) {
            ingredient = inputCreator.create(CommonTagPrefixes.GEM, VanillaMaterialKeys.ECHO, 16)
            result = enchLookup.getOrThrow(Enchantments.SWIFT_SNEAK)
        }
    }

    @JvmStatic
    private fun melee() {
        HTSingleRecipeBuilder.enchanting(output) {
            ingredient = inputCreator.create(CommonTagPrefixes.GEM, VanillaMaterialKeys.QUARTZ, 64)
            result = enchLookup.getOrThrow(Enchantments.SHARPNESS)
        }
        HTSingleRecipeBuilder.enchanting(output) {
            ingredient = inputCreator.create(CommonTagPrefixes.DUST, CommonMaterialKeys.SALT, 64)
            result = enchLookup.getOrThrow(Enchantments.SMITE)
        }
        HTSingleRecipeBuilder.enchanting(output) {
            ingredient = inputCreator.create(CommonTagPrefixes.DUST, CommonMaterialKeys.SULFUR, 64)
            result = enchLookup.getOrThrow(Enchantments.BANE_OF_ARTHROPODS)
        }
        HTSingleRecipeBuilder.enchanting(output) {
            ingredient = inputCreator.create(Items.PISTON, 16)
            result = enchLookup.getOrThrow(Enchantments.KNOCKBACK)
        }
        HTSingleRecipeBuilder.enchanting(output) {
            ingredient = inputCreator.create(Items.BLAZE_POWDER, 32)
            result = enchLookup.getOrThrow(Enchantments.FIRE_ASPECT)
        }
        HTSingleRecipeBuilder.enchanting(output) {
            ingredient = inputCreator.create(CommonTagPrefixes.GEM, VanillaMaterialKeys.EMERALD, 64)
            result = enchLookup.getOrThrow(Enchantments.LOOTING)
        }
        HTSingleRecipeBuilder.enchanting(output) {
            ingredient = inputCreator.create(CommonTagPrefixes.PLATE, VanillaMaterialKeys.IRON, 64)
            result = enchLookup.getOrThrow(Enchantments.SWEEPING_EDGE)
        }
    }

    @JvmStatic
    private fun tool() {
        HTSingleRecipeBuilder.enchanting(output) {
            ingredient = inputCreator.create(CommonTagPrefixes.DUST, VanillaMaterialKeys.REDSTONE, 64)
            result = enchLookup.getOrThrow(Enchantments.EFFICIENCY)
        }
        HTSingleRecipeBuilder.enchanting(output) {
            ingredient = inputCreator.create(Tags.Items.STRINGS, 64)
            result = enchLookup.getOrThrow(Enchantments.SILK_TOUCH)
        }
        HTSingleRecipeBuilder.enchanting(output) {
            ingredient = inputCreator.create(Tags.Items.OBSIDIANS, 64)
            result = enchLookup.getOrThrow(Enchantments.UNBREAKING)
        }
        HTSingleRecipeBuilder.enchanting(output) {
            ingredient = inputCreator.create(CommonTagPrefixes.GEM, VanillaMaterialKeys.LAPIS, 64)
            result = enchLookup.getOrThrow(Enchantments.FORTUNE)
        }
    }

    @JvmStatic
    private fun bow() {
        HTSingleRecipeBuilder.enchanting(output) {
            ingredient = inputCreator.create(CommonTagPrefixes.GEM, VanillaMaterialKeys.AMETHYST, 64)
            result = enchLookup.getOrThrow(Enchantments.POWER)
        }
        HTSingleRecipeBuilder.enchanting(output) {
            ingredient = inputCreator.create(Items.STICKY_PISTON, 16)
            result = enchLookup.getOrThrow(Enchantments.PUNCH)
        }
        HTSingleRecipeBuilder.enchanting(output) {
            ingredient = inputCreator.create(Tags.Items.RODS_BLAZE, 16)
            result = enchLookup.getOrThrow(Enchantments.FLAME)
        }
        HTSingleRecipeBuilder.enchanting(output) {
            ingredient = inputCreator.create(CommonTagPrefixes.INGOT, CommonMaterialKeys.IRIDIUM)
            result = enchLookup.getOrThrow(Enchantments.INFINITY)
        }
    }

    @JvmStatic
    private fun lure() {
        HTSingleRecipeBuilder.enchanting(output) {
            ingredient = inputCreator.create(Items.PRISMARINE_SHARD, 64)
            result = enchLookup.getOrThrow(Enchantments.LUCK_OF_THE_SEA)
        }
        HTSingleRecipeBuilder.enchanting(output) {
            ingredient = inputCreator.create(CommonTagPrefixes.GEM, VanillaMaterialKeys.PRISMARINE, 64)
            result = enchLookup.getOrThrow(Enchantments.LURE)
        }
    }

    @JvmStatic
    private fun trident() {
        HTSingleRecipeBuilder.enchanting(output) {
            ingredient = inputCreator.create(Items.LEAD, 64)
            result = enchLookup.getOrThrow(Enchantments.LOYALTY)
        }
        HTSingleRecipeBuilder.enchanting(output) {
            ingredient = inputCreator.create(Items.NAUTILUS_SHELL, 32)
            result = enchLookup.getOrThrow(Enchantments.IMPALING)
        }
        HTSingleRecipeBuilder.enchanting(output) {
            ingredient = inputCreator.create(Items.HEART_OF_THE_SEA)
            result = enchLookup.getOrThrow(Enchantments.RIPTIDE)
        }
        HTSingleRecipeBuilder.enchanting(output) {
            ingredient = inputCreator.create(Items.LIGHTNING_ROD, 16)
            result = enchLookup.getOrThrow(Enchantments.CHANNELING)
        }
    }

    @JvmStatic
    private fun crossBow() {}

    @JvmStatic
    private fun mace() {}
}
