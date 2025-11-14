package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.api.recipe.ingredient.HTEntityTypeIngredient
import hiiragi283.ragium.api.registry.HTFluidContent
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.common.material.VanillaMaterialKeys
import hiiragi283.ragium.impl.data.recipe.HTItemWithCatalystRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTShapedRecipeBuilder
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.core.HolderGetter
import net.minecraft.core.registries.Registries
import net.minecraft.tags.ItemTags
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.common.Tags

object RagiumSimulatingRecipeProvider : HTRecipeProvider.Direct() {
    private val entityLookup: HolderGetter<EntityType<*>> by lazy { provider.lookupOrThrow(Registries.ENTITY_TYPE) }

    override fun buildRecipeInternal() {
        // Amethyst
        HTItemWithCatalystRecipeBuilder
            .simulating(
                null,
                itemCreator.fromItem(Items.BUDDING_AMETHYST),
                resultHelper.item(Items.AMETHYST_SHARD, 4),
            ).save(output)
        // Echo Shard
        HTItemWithCatalystRecipeBuilder
            .simulating(
                itemCreator.fromTagKey(CommonMaterialPrefixes.GEM, VanillaMaterialKeys.AMETHYST),
                itemCreator.fromItem(Items.SCULK_CATALYST),
                resultHelper.item(Items.ECHO_SHARD),
            ).save(output)

        mobExtracting()
    }

    @JvmStatic
    fun mobExtracting() {
        // Armadillo Scute
        HTItemWithCatalystRecipeBuilder
            .simulating(
                null,
                itemCreator.fromVanilla(HTEntityTypeIngredient.of(EntityType.ARMADILLO)),
                resultHelper.item(Items.ARMADILLO_SCUTE),
            ).save(output)
        // Honeycomb
        HTItemWithCatalystRecipeBuilder
            .simulating(
                null,
                itemCreator.fromItem(Items.BEE_NEST),
                resultHelper.item(Items.HONEYCOMB),
            ).save(output)
        // Honey Bottle
        HTItemWithCatalystRecipeBuilder
            .simulating(
                itemCreator.fromItem(Items.GLASS_BOTTLE),
                itemCreator.fromItem(Items.BEE_NEST),
                resultHelper.item(Items.HONEY_BOTTLE),
            ).save(output)
        // Egg
        HTItemWithCatalystRecipeBuilder
            .simulating(
                itemCreator.fromTagKey(Tags.Items.SEEDS),
                itemCreator.fromVanilla(HTEntityTypeIngredient.of(EntityType.CHICKEN)),
                resultHelper.item(Items.EGG),
            ).save(output)
        // Milk from Cow
        HTItemWithCatalystRecipeBuilder
            .simulating(
                null,
                itemCreator.fromVanilla(HTEntityTypeIngredient.of(EntityType.COW)),
                null,
                resultHelper.fluid(HTFluidContent.MILK, 250),
            ).saveSuffixed(output, "_from_cow")
        // Heart of the Sea
        HTItemWithCatalystRecipeBuilder
            .simulating(
                itemCreator.fromItem(RagiumItems.ELDER_HEART),
                itemCreator.fromVanilla(HTEntityTypeIngredient.of(EntityType.ELDER_GUARDIAN)),
                resultHelper.item(Items.HEART_OF_THE_SEA),
            ).save(output)
        // Dragon Breath
        HTItemWithCatalystRecipeBuilder
            .simulating(
                itemCreator.fromItem(Items.GLASS_BOTTLE),
                itemCreator.fromVanilla(
                    Ingredient.of(Items.DRAGON_HEAD),
                    HTEntityTypeIngredient.of(EntityType.ENDER_DRAGON),
                ),
                resultHelper.item(Items.DRAGON_BREATH),
            ).save(output)
        // Undying Totem
        // Frog Lights
        // Glow Ink Sac
        HTItemWithCatalystRecipeBuilder
            .simulating(
                null,
                itemCreator.fromVanilla(HTEntityTypeIngredient.of(EntityType.GLOW_SQUID)),
                resultHelper.item(Items.GLOW_INK_SAC),
            ).save(output)
        // Milk from Goat
        HTItemWithCatalystRecipeBuilder
            .simulating(
                null,
                itemCreator.fromVanilla(HTEntityTypeIngredient.of(EntityType.GOAT)),
                null,
                resultHelper.fluid(HTFluidContent.MILK, 500),
            ).saveSuffixed(output, "_from_goat")
        // Poppy
        HTItemWithCatalystRecipeBuilder
            .simulating(
                itemCreator.fromTagKey(Tags.Items.FERTILIZERS),
                itemCreator.fromVanilla(HTEntityTypeIngredient.of(EntityType.IRON_GOLEM)),
                resultHelper.item(Items.POPPY),
            ).save(output)
        // Mushroom Stew from Mooshroom
        HTItemWithCatalystRecipeBuilder
            .simulating(
                null,
                itemCreator.fromVanilla(HTEntityTypeIngredient.of(EntityType.MOOSHROOM)),
                null,
                resultHelper.fluid(RagiumFluidContents.MUSHROOM_STEW, 500),
            ).save(output)
        // Ancient Debris
        HTItemWithCatalystRecipeBuilder
            .simulating(
                itemCreator.fromItem(Items.NETHER_BRICKS, 64),
                itemCreator.fromVanilla(HTEntityTypeIngredient.of(EntityType.PIGLIN_BRUTE)),
                resultHelper.item(Items.ANCIENT_DEBRIS),
            ).save(output)
        // Wool
        HTItemWithCatalystRecipeBuilder
            .simulating(
                null,
                itemCreator.fromVanilla(HTEntityTypeIngredient.of(EntityType.SHEEP)),
                resultHelper.item(Items.WHITE_WOOL),
            ).save(output)
        // Ink Sac
        HTItemWithCatalystRecipeBuilder
            .simulating(
                null,
                itemCreator.fromVanilla(HTEntityTypeIngredient.of(EntityType.SQUID)),
                resultHelper.item(Items.INK_SAC),
            ).save(output)
        // Turtle Scute
        HTItemWithCatalystRecipeBuilder
            .simulating(
                itemCreator.fromItem(Items.SEAGRASS, 8),
                itemCreator.fromVanilla(HTEntityTypeIngredient.of(EntityType.TURTLE)),
                resultHelper.item(Items.TURTLE_SCUTE),
            ).save(output)
        // Resonant Debris
        HTItemWithCatalystRecipeBuilder
            .simulating(
                itemCreator.fromItem(Items.DEEPSLATE, 8),
                itemCreator.fromVanilla(
                    HTEntityTypeIngredient.of(entityLookup.getOrThrow(RagiumModTags.EntityTypes.GENERATE_RESONANT_DEBRIS)),
                ),
                resultHelper.item(RagiumBlocks.RESONANT_DEBRIS),
            ).save(output)
        // Nether Star
        HTShapedRecipeBuilder
            .misc(RagiumItems.WITHER_DOLl)
            .pattern(
                "AAA",
                "BBB",
                " B ",
            ).define('A', Items.WITHER_SKELETON_SKULL)
            .define('B', ItemTags.SOUL_FIRE_BASE_BLOCKS)
            .save(output)

        HTItemWithCatalystRecipeBuilder
            .simulating(
                itemCreator.fromItem(RagiumItems.WITHER_DOLl),
                itemCreator.fromTagKey(CommonMaterialPrefixes.STORAGE_BLOCK, RagiumMaterialKeys.IRIDESCENTIUM),
                resultHelper.item(Tags.Items.NETHER_STARS),
            ).save(output)
    }
}
