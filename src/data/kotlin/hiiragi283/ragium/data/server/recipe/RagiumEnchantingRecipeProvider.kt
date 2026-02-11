package hiiragi283.ragium.data.server.recipe

import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.data.recipe.HTEnchantingRecipeBuilder
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.Enchantments
import net.neoforged.neoforge.common.Tags

object RagiumEnchantingRecipeProvider : HTSubRecipeProvider.Direct(RagiumAPI.MOD_ID) {
    private val enchLookup: HolderLookup.RegistryLookup<Enchantment> by lazy { provider.lookupOrThrow(Registries.ENCHANTMENT) }

    override fun buildRecipeInternal() {
        armor()
        melee()
        tool()
        bow()
        lure()
        trident()
        crossBow()
        mace()

        HTEnchantingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(Tags.Items.PUMPKINS_CARVED)
            result += enchLookup.getOrThrow(Enchantments.BINDING_CURSE)
        }
        HTEnchantingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(Tags.Items.COBBLESTONES)
            result += enchLookup.getOrThrow(Enchantments.VANISHING_CURSE)
        }
        HTEnchantingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(Tags.Items.NETHER_STARS)
            result += enchLookup.getOrThrow(Enchantments.MENDING)
        }
    }

    @JvmStatic
    private fun armor() {
        HTEnchantingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(CommonTagPrefixes.INGOT, CommonMaterialKeys.STEEL, 64)
            result += enchLookup.getOrThrow(Enchantments.PROTECTION)
        }
        HTEnchantingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(Items.MAGMA_CREAM, 64)
            result += enchLookup.getOrThrow(Enchantments.FIRE_PROTECTION)
        }
        HTEnchantingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(Tags.Items.FEATHERS, 64)
            result += enchLookup.getOrThrow(Enchantments.FEATHER_FALLING)
        }
        HTEnchantingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(CommonTagPrefixes.INGOT, VanillaMaterialKeys.IRON, 64)
            result += enchLookup.getOrThrow(Enchantments.PROJECTILE_PROTECTION)
        }

        HTEnchantingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(Items.PUFFERFISH, 64)
            result += enchLookup.getOrThrow(Enchantments.RESPIRATION)
        }
        HTEnchantingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(Items.TROPICAL_FISH, 16)
            result += enchLookup.getOrThrow(Enchantments.AQUA_AFFINITY)
        }

        HTEnchantingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(Tags.Items.CROPS_CACTUS)
            result += enchLookup.getOrThrow(Enchantments.THORNS)
        }

        HTEnchantingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(CommonTagPrefixes.PLATE, CommonMaterialKeys.PLASTIC, 64)
            result += enchLookup.getOrThrow(Enchantments.DEPTH_STRIDER)
        }
        HTEnchantingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(Items.PACKED_ICE, 64)
            result += enchLookup.getOrThrow(Enchantments.FROST_WALKER)
        }

        HTEnchantingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(ItemTags.SOUL_FIRE_BASE_BLOCKS, 64)
            result += enchLookup.getOrThrow(Enchantments.SOUL_SPEED)
        }
        HTEnchantingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(CommonTagPrefixes.GEM, VanillaMaterialKeys.ECHO, 16)
            result += enchLookup.getOrThrow(Enchantments.SWIFT_SNEAK)
        }
    }

    @JvmStatic
    private fun melee() {
        HTEnchantingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(CommonTagPrefixes.GEM, VanillaMaterialKeys.QUARTZ, 64)
            result += enchLookup.getOrThrow(Enchantments.SHARPNESS)
        }
        HTEnchantingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(CommonTagPrefixes.DUST, CommonMaterialKeys.SALT, 64)
            result += enchLookup.getOrThrow(Enchantments.SMITE)
        }
        HTEnchantingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(CommonTagPrefixes.DUST, CommonMaterialKeys.SULFUR, 64)
            result += enchLookup.getOrThrow(Enchantments.BANE_OF_ARTHROPODS)
        }
        HTEnchantingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(Items.PISTON, 16)
            result += enchLookup.getOrThrow(Enchantments.KNOCKBACK)
        }
        HTEnchantingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(Items.BLAZE_POWDER, 32)
            result += enchLookup.getOrThrow(Enchantments.FIRE_ASPECT)
        }
        HTEnchantingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(CommonTagPrefixes.GEM, VanillaMaterialKeys.EMERALD, 64)
            result += enchLookup.getOrThrow(Enchantments.LOOTING)
        }
        HTEnchantingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(CommonTagPrefixes.PLATE, VanillaMaterialKeys.IRON, 64)
            result += enchLookup.getOrThrow(Enchantments.SWEEPING_EDGE)
        }
    }

    @JvmStatic
    private fun tool() {
        HTEnchantingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(CommonTagPrefixes.DUST, VanillaMaterialKeys.REDSTONE, 64)
            result += enchLookup.getOrThrow(Enchantments.EFFICIENCY)
        }
        HTEnchantingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(Tags.Items.STRINGS, 64)
            result += enchLookup.getOrThrow(Enchantments.SILK_TOUCH)
        }
        HTEnchantingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(Tags.Items.OBSIDIANS, 64)
            result += enchLookup.getOrThrow(Enchantments.UNBREAKING)
        }
        HTEnchantingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(CommonTagPrefixes.GEM, VanillaMaterialKeys.LAPIS, 64)
            result += enchLookup.getOrThrow(Enchantments.FORTUNE)
        }
    }

    @JvmStatic
    private fun bow() {
        HTEnchantingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(CommonTagPrefixes.GEM, VanillaMaterialKeys.AMETHYST, 64)
            result += enchLookup.getOrThrow(Enchantments.POWER)
        }
        HTEnchantingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(Items.STICKY_PISTON, 16)
            result += enchLookup.getOrThrow(Enchantments.PUNCH)
        }
        HTEnchantingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(Tags.Items.RODS_BLAZE, 16)
            result += enchLookup.getOrThrow(Enchantments.FLAME)
        }
        HTEnchantingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(CommonTagPrefixes.INGOT, CommonMaterialKeys.IRIDIUM)
            result += enchLookup.getOrThrow(Enchantments.INFINITY)
        }
    }

    @JvmStatic
    private fun lure() {
        HTEnchantingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(Items.PRISMARINE_SHARD, 64)
            result += enchLookup.getOrThrow(Enchantments.LUCK_OF_THE_SEA)
        }
        HTEnchantingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(CommonTagPrefixes.GEM, VanillaMaterialKeys.PRISMARINE, 64)
            result += enchLookup.getOrThrow(Enchantments.LURE)
        }
    }

    @JvmStatic
    private fun trident() {
        HTEnchantingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(Items.LEAD, 64)
            result += enchLookup.getOrThrow(Enchantments.LOYALTY)
        }
        HTEnchantingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(Items.NAUTILUS_SHELL, 32)
            result += enchLookup.getOrThrow(Enchantments.IMPALING)
        }
        HTEnchantingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(Items.HEART_OF_THE_SEA)
            result += enchLookup.getOrThrow(Enchantments.RIPTIDE)
        }
        HTEnchantingRecipeBuilder.create(output) {
            ingredient = inputCreator.create(Items.LIGHTNING_ROD, 16)
            result += enchLookup.getOrThrow(Enchantments.CHANNELING)
        }
    }

    @JvmStatic
    private fun crossBow() {}

    @JvmStatic
    private fun mace() {}
}
