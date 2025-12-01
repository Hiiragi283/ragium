package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.api.recipe.ingredient.HTEntityTypeIngredient
import hiiragi283.ragium.api.registry.HTFluidHolderLike
import hiiragi283.ragium.api.tag.RagiumModTags
import hiiragi283.ragium.common.HTMoldType
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.common.material.RagiumMoltenCrystalData
import hiiragi283.ragium.common.material.VanillaMaterialKeys
import hiiragi283.ragium.impl.data.recipe.HTComplexRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTItemWithCatalystRecipeBuilder
import hiiragi283.ragium.impl.data.recipe.HTShapedRecipeBuilder
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.core.HolderGetter
import net.minecraft.core.HolderSet
import net.minecraft.core.registries.Registries
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.common.Tags

object RagiumSimulatingRecipeProvider : HTRecipeProvider.Direct() {
    private val entityLookup: HolderGetter<EntityType<*>> by lazy { provider.lookupOrThrow(Registries.ENTITY_TYPE) }

    @Suppress("DEPRECATION")
    @JvmStatic
    private fun ingredient(vararg entityTypes: EntityType<*>): Ingredient =
        HTEntityTypeIngredient.of(HolderSet.direct(EntityType<*>::builtInRegistryHolder, *entityTypes))

    @JvmStatic
    private fun ingredient(tagKey: TagKey<EntityType<*>>): Ingredient = HTEntityTypeIngredient.of(entityLookup.getOrThrow(tagKey))

    override fun buildRecipeInternal() {
        // Amethyst
        HTItemWithCatalystRecipeBuilder
            .simulating(
                null,
                itemCreator.fromItem(Items.BUDDING_AMETHYST),
                resultHelper.item(Items.AMETHYST_SHARD, 4),
            ).save(output)
        // Quartz
        HTItemWithCatalystRecipeBuilder
            .simulating(
                null,
                itemCreator.fromItem(RagiumBlocks.BUDDING_QUARTZ),
                resultHelper.item(Items.QUARTZ, 4),
            ).save(output)
        // Echo Shard
        HTItemWithCatalystRecipeBuilder
            .simulating(
                itemCreator.fromTagKey(CommonMaterialPrefixes.GEM, VanillaMaterialKeys.AMETHYST),
                itemCreator.fromItem(Items.SCULK_CATALYST),
                resultHelper.item(Items.ECHO_SHARD),
            ).save(output)

        // Molten Crystals
        for (data: RagiumMoltenCrystalData in RagiumMoltenCrystalData.entries) {
            val base: TagKey<Item> = data.base ?: continue
            HTItemWithCatalystRecipeBuilder
                .simulating(
                    itemCreator.fromTagKey(base, 4),
                    itemCreator.fromItem(HTMoldType.GEM),
                    resultHelper.item(CommonMaterialPrefixes.GEM, data),
                ).save(output)
        }

        mobExtracting()
    }

    @JvmStatic
    fun mobExtracting() {
        // Armadillo Scute
        HTItemWithCatalystRecipeBuilder
            .simulating(
                null,
                itemCreator.fromVanilla(ingredient(EntityType.ARMADILLO)),
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
                itemCreator.fromVanilla(ingredient(EntityType.CHICKEN)),
                resultHelper.item(Items.EGG),
            ).save(output)
        // Milk from Cow
        HTItemWithCatalystRecipeBuilder
            .simulating(
                null,
                itemCreator.fromVanilla(ingredient(EntityType.COW)),
                null,
                resultHelper.fluid(HTFluidHolderLike.MILK, 250),
            ).saveSuffixed(output, "_from_cow")
        // Heart of the Sea
        HTComplexRecipeBuilder
            .mixing()
            .addIngredient(itemCreator.fromItem(RagiumItems.ELDER_HEART))
            .addIngredient(itemCreator.fromItem(Items.PRISMARINE_SHARD, 64))
            .addIngredient(itemCreator.fromTagKey(Tags.Items.GEMS_PRISMARINE, 64))
            .addIngredient(itemCreator.fromTagKey(CommonMaterialPrefixes.GEM, VanillaMaterialKeys.LAPIS, 64))
            .addIngredient(fluidCreator.water(8000))
            .setResult(resultHelper.item(Items.HEART_OF_THE_SEA))
            .save(output)
        // Dragon Breath
        HTItemWithCatalystRecipeBuilder
            .simulating(
                itemCreator.fromItem(Items.GLASS_BOTTLE),
                itemCreator.fromItem(Items.DRAGON_HEAD),
                resultHelper.item(Items.DRAGON_BREATH),
            ).save(output)
        // Frog Lights
        // Milk from Goat
        HTItemWithCatalystRecipeBuilder
            .simulating(
                null,
                itemCreator.fromVanilla(ingredient(EntityType.GOAT)),
                null,
                resultHelper.fluid(HTFluidHolderLike.MILK, 500),
            ).saveSuffixed(output, "_from_goat")
        // Poppy
        HTItemWithCatalystRecipeBuilder
            .simulating(
                itemCreator.fromTagKey(Tags.Items.FERTILIZERS),
                itemCreator.fromVanilla(ingredient(EntityType.IRON_GOLEM)),
                resultHelper.item(Items.POPPY),
            ).save(output)
        // Mushroom Stew from Mooshroom
        HTItemWithCatalystRecipeBuilder
            .simulating(
                null,
                itemCreator.fromVanilla(ingredient(EntityType.MOOSHROOM)),
                null,
                resultHelper.fluid(RagiumFluidContents.MUSHROOM_STEW, 500),
            ).save(output)
        // Ancient Debris
        HTItemWithCatalystRecipeBuilder
            .simulating(
                itemCreator.fromItem(Items.NETHER_BRICKS, 64),
                itemCreator.fromVanilla(ingredient(EntityType.PIGLIN_BRUTE)),
                resultHelper.item(Items.ANCIENT_DEBRIS),
            ).save(output)
        // Wool
        HTItemWithCatalystRecipeBuilder
            .simulating(
                null,
                itemCreator.fromVanilla(ingredient(EntityType.SHEEP)),
                resultHelper.item(Items.WHITE_WOOL),
            ).save(output)
        // Turtle Scute
        HTItemWithCatalystRecipeBuilder
            .simulating(
                itemCreator.fromItem(Items.SEAGRASS, 8),
                itemCreator.fromVanilla(ingredient(EntityType.TURTLE)),
                resultHelper.item(Items.TURTLE_SCUTE),
            ).save(output)
        // Resonant Debris
        HTItemWithCatalystRecipeBuilder
            .simulating(
                itemCreator.fromItem(Items.DEEPSLATE, 8),
                itemCreator.fromVanilla(ingredient(RagiumModTags.EntityTypes.GENERATE_RESONANT_DEBRIS)),
                resultHelper.item(RagiumBlocks.RESONANT_DEBRIS),
            ).save(output)
        // Nether Star
        HTShapedRecipeBuilder
            .create(RagiumItems.WITHER_DOLl)
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
                itemCreator.fromTagKey(CommonMaterialPrefixes.STORAGE_BLOCK, RagiumMaterialKeys.NIGHT_METAL),
                resultHelper.item(RagiumItems.WITHER_STAR),
            ).save(output)

        HTComplexRecipeBuilder
            .mixing()
            .addIngredient(itemCreator.fromItem(RagiumItems.WITHER_STAR))
            .addIngredient(itemCreator.fromItem(Items.GHAST_TEAR, 16))
            .addIngredient(itemCreator.fromTagKey(CommonMaterialPrefixes.SCRAP, VanillaMaterialKeys.NETHERITE, 16))
            .addIngredient(itemCreator.fromTagKey(CommonMaterialPrefixes.GEM, VanillaMaterialKeys.QUARTZ, 64))
            .addIngredient(fluidCreator.fromHolder(RagiumFluidContents.ELDRITCH_FLUX, 8000))
            .setResult(resultHelper.item(Items.NETHER_STAR))
            .save(output)

        HTItemWithCatalystRecipeBuilder
            .simulating(
                itemCreator.fromItem(RagiumItems.WITHER_DOLl),
                itemCreator.fromItem(RagiumItems.ETERNAL_COMPONENT),
                resultHelper.item(Items.NETHER_STAR),
            ).save(output)
    }
}
