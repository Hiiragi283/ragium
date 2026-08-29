package hiiragi283.ragium.data.recipe

import hiiragi283.core.api.data.recipe.HTRecipeProvider
import hiiragi283.core.api.tag.CommonTagPrefixes
import hiiragi283.core.api.tag.HiiragiCoreTags
import hiiragi283.core.common.material.CommonMaterialKeys
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.core.setup.HCEnchantments
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.data.recipe.HTEnchantingRecipeBuilder
import java.util.concurrent.CompletableFuture
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.Enchantments
import net.neoforged.neoforge.common.Tags

class RagiumArcaneRecipeProvider(packOutput: PackOutput, future: CompletableFuture<HolderLookup.Provider>) : HTRecipeProvider(packOutput, future, RagiumAPI.MOD_ID) {
    private val enchLookup: HolderLookup.RegistryLookup<Enchantment> by lazy { registries.lookupOrThrow(Registries.ENCHANTMENT) }

    override fun buildRecipes() {
        enchanting()
        matter()
    }

    override fun getName(): String = "Arcane Recipes"

    //    Enchanting    //

    fun enchanting() {
        armor()
        melee()
        tool()
        bow()
        fishingRod()
        trident()
        crossBow()
        mace()

        HTEnchantingRecipeBuilder.create {
            ingredient { +Tags.Items.PUMPKINS_CARVED }
            +enchLookup.getOrThrow(Enchantments.BINDING_CURSE)
        }.save(exporter)
        HTEnchantingRecipeBuilder.create {
            ingredient { +Tags.Items.COBBLESTONES }
            +enchLookup.getOrThrow(Enchantments.VANISHING_CURSE)
        }.save(exporter)
        HTEnchantingRecipeBuilder.create {
            ingredient { +Tags.Items.NETHER_STARS }
            +enchLookup.getOrThrow(Enchantments.MENDING)
        }.save(exporter)

        hiiragiCore()
    }

    private fun armor() {
        HTEnchantingRecipeBuilder.create {
            ingredient {
                +tag(CommonTagPrefixes.INGOT, CommonMaterialKeys.STEEL)
                count = 64
            }
            +enchLookup.getOrThrow(Enchantments.PROTECTION)
        }.save(exporter)
        HTEnchantingRecipeBuilder.create {
            ingredient {
                +Items.MAGMA_CREAM
                count = 64
            }
            +enchLookup.getOrThrow(Enchantments.FIRE_PROTECTION)
        }.save(exporter)
        HTEnchantingRecipeBuilder.create {
            ingredient {
                +Tags.Items.FEATHERS
                count = 64
            }
            +enchLookup.getOrThrow(Enchantments.FEATHER_FALLING)
        }.save(exporter)
        HTEnchantingRecipeBuilder.create {
            ingredient {
                +tag(CommonTagPrefixes.INGOT, VanillaMaterialKeys.IRON)
                count = 64
            }
            +enchLookup.getOrThrow(Enchantments.PROJECTILE_PROTECTION)
        }.save(exporter)

        HTEnchantingRecipeBuilder.create {
            ingredient {
                +Items.PUFFERFISH
                count = 64
            }
            +enchLookup.getOrThrow(Enchantments.RESPIRATION)
        }.save(exporter)
        HTEnchantingRecipeBuilder.create {
            ingredient {
                +Items.TROPICAL_FISH
                count = 16
            }
            +enchLookup.getOrThrow(Enchantments.AQUA_AFFINITY)
        }.save(exporter)

        HTEnchantingRecipeBuilder.create {
            ingredient { +Tags.Items.CROPS_CACTUS }
            +enchLookup.getOrThrow(Enchantments.THORNS)
        }.save(exporter)

        HTEnchantingRecipeBuilder.create {
            ingredient {
                +HiiragiCoreTags.Items.PLASTICS
                count = 64
            }
            +enchLookup.getOrThrow(Enchantments.DEPTH_STRIDER)
        }.save(exporter)
        HTEnchantingRecipeBuilder.create {
            ingredient {
                +Items.PACKED_ICE
                count = 64
            }
            +enchLookup.getOrThrow(Enchantments.FROST_WALKER)
        }.save(exporter)

        HTEnchantingRecipeBuilder.create {
            ingredient {
                +ItemTags.SOUL_FIRE_BASE_BLOCKS
                count = 64
            }
            +enchLookup.getOrThrow(Enchantments.SOUL_SPEED)
        }.save(exporter)
        HTEnchantingRecipeBuilder.create {
            ingredient {
                +tag(CommonTagPrefixes.DUST, VanillaMaterialKeys.ECHO)
                count = 16
            }
            +enchLookup.getOrThrow(Enchantments.SWIFT_SNEAK)
        }.save(exporter)
    }

    private fun melee() {
        HTEnchantingRecipeBuilder.create {
            ingredient {
                +tag(CommonTagPrefixes.GEM, VanillaMaterialKeys.QUARTZ)
                count = 64
            }
            +enchLookup.getOrThrow(Enchantments.SHARPNESS)
        }.save(exporter)
        HTEnchantingRecipeBuilder.create {
            ingredient {
                +tag(CommonTagPrefixes.DUST, CommonMaterialKeys.SALT)
                count = 64
            }
            +enchLookup.getOrThrow(Enchantments.SMITE)
        }.save(exporter)
        HTEnchantingRecipeBuilder.create {
            ingredient {
                +tag(CommonTagPrefixes.DUST, CommonMaterialKeys.SULFUR)
                count = 64
            }
            +enchLookup.getOrThrow(Enchantments.BANE_OF_ARTHROPODS)
        }.save(exporter)
        HTEnchantingRecipeBuilder.create {
            ingredient {
                +Items.PISTON
                count = 16
            }
            +enchLookup.getOrThrow(Enchantments.KNOCKBACK)
        }.save(exporter)
        HTEnchantingRecipeBuilder.create {
            ingredient {
                +Items.BLAZE_POWDER
                count = 32
            }
            +enchLookup.getOrThrow(Enchantments.FIRE_ASPECT)
        }.save(exporter)
        HTEnchantingRecipeBuilder.create {
            ingredient {
                +tag(CommonTagPrefixes.GEM, VanillaMaterialKeys.EMERALD)
                count = 64
            }
            +enchLookup.getOrThrow(Enchantments.LOOTING)
        }.save(exporter)
        HTEnchantingRecipeBuilder.create {
            ingredient {
                +tag(CommonTagPrefixes.PLATE, VanillaMaterialKeys.IRON)
                count = 64
            }
            +enchLookup.getOrThrow(Enchantments.SWEEPING_EDGE)
        }.save(exporter)
    }

    private fun tool() {
        HTEnchantingRecipeBuilder.create {
            ingredient {
                +tag(CommonTagPrefixes.DUST, VanillaMaterialKeys.REDSTONE)
                count = 64
            }
            +enchLookup.getOrThrow(Enchantments.EFFICIENCY)
        }.save(exporter)
        HTEnchantingRecipeBuilder.create {
            ingredient {
                +Tags.Items.STRINGS
                count = 64
            }
            +enchLookup.getOrThrow(Enchantments.SILK_TOUCH)
        }.save(exporter)
        HTEnchantingRecipeBuilder.create {
            ingredient {
                +Tags.Items.OBSIDIANS
                count = 64
            }
            +enchLookup.getOrThrow(Enchantments.UNBREAKING)
        }.save(exporter)
        HTEnchantingRecipeBuilder.create {
            ingredient {
                +tag(CommonTagPrefixes.GEM, VanillaMaterialKeys.LAPIS)
                count = 64
            }
            +enchLookup.getOrThrow(Enchantments.FORTUNE)
        }.save(exporter)
    }

    private fun bow() {
        HTEnchantingRecipeBuilder.create {
            ingredient {
                +tag(CommonTagPrefixes.GEM, VanillaMaterialKeys.AMETHYST)
                count = 64
            }
            +enchLookup.getOrThrow(Enchantments.POWER)
        }.save(exporter)
        HTEnchantingRecipeBuilder.create {
            ingredient {
                +Items.STICKY_PISTON
                count = 16
            }
            +enchLookup.getOrThrow(Enchantments.PUNCH)
        }.save(exporter)
        HTEnchantingRecipeBuilder.create {
            ingredient {
                +Tags.Items.RODS_BLAZE
                count = 16
            }
            +enchLookup.getOrThrow(Enchantments.FLAME)
        }.save(exporter)
        HTEnchantingRecipeBuilder.create {
            ingredient {
                +tag(CommonTagPrefixes.INGOT, CommonMaterialKeys.IRIDIUM)
            }
            +enchLookup.getOrThrow(Enchantments.INFINITY)
        }.save(exporter)
    }

    private fun fishingRod() {
        HTEnchantingRecipeBuilder.create {
            ingredient {
                +Items.PRISMARINE_SHARD
                count = 64
            }
            +enchLookup.getOrThrow(Enchantments.LUCK_OF_THE_SEA)
        }.save(exporter)
        HTEnchantingRecipeBuilder.create {
            ingredient {
                +tag(CommonTagPrefixes.GEM, VanillaMaterialKeys.PRISMARINE)
                count = 64
            }
            +enchLookup.getOrThrow(Enchantments.LURE)
        }.save(exporter)
    }

    private fun trident() {
        HTEnchantingRecipeBuilder.create {
            ingredient {
                +Items.LEAD
                count = 64
            }
            +enchLookup.getOrThrow(Enchantments.LOYALTY)
        }.save(exporter)
        HTEnchantingRecipeBuilder.create {
            ingredient {
                +Items.NAUTILUS_SHELL
                count = 32
            }
            +enchLookup.getOrThrow(Enchantments.IMPALING)
        }.save(exporter)
        HTEnchantingRecipeBuilder.create {
            ingredient { +Items.HEART_OF_THE_SEA }
            +enchLookup.getOrThrow(Enchantments.RIPTIDE)
        }.save(exporter)
        HTEnchantingRecipeBuilder.create {
            ingredient {
                +Items.LIGHTNING_ROD
                count = 16
            }
            +enchLookup.getOrThrow(Enchantments.CHANNELING)
        }.save(exporter)
    }

    private fun crossBow() {}

    private fun mace() {}

    private fun hiiragiCore() {
        // Sword
        HTEnchantingRecipeBuilder.create {
            ingredient { +Items.OMINOUS_BOTTLE }
            +enchLookup.getOrThrow(HCEnchantments.HAMMER_OF_JUSTICE)
        }.save(exporter)
        HTEnchantingRecipeBuilder.create {
            ingredient {
                +tag(CommonTagPrefixes.GEM, VanillaMaterialKeys.ECHO)
                count = 64
            }
            +enchLookup.getOrThrow(HCEnchantments.NOISE_CANCELING)
        }.save(exporter)
        HTEnchantingRecipeBuilder.create {
            ingredient {
                +tag(CommonTagPrefixes.DUST, CommonMaterialKeys.IRIDIUM)
                count = 16
            }
            +enchLookup.getOrThrow(HCEnchantments.PURIFICATION)
        }.save(exporter)
        // Armor
        HTEnchantingRecipeBuilder.create {
            ingredient {
                +tag(CommonTagPrefixes.STORAGE_BLOCK, VanillaMaterialKeys.ECHO)
                count = 16
            }
            +enchLookup.getOrThrow(HCEnchantments.SONIC_PROTECTION)
        }.save(exporter)
    }

    //    Matter    //

    private fun matter() {}
}
