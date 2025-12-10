package hiiragi283.ragium.data.server.recipe

import hiiragi283.ragium.api.data.recipe.HTRecipeProvider
import hiiragi283.ragium.api.material.HTMaterialLike
import hiiragi283.ragium.api.material.prefix.HTPrefixLike
import hiiragi283.ragium.api.registry.HTFluidHolderLike
import hiiragi283.ragium.api.stack.toImmutableOrThrow
import hiiragi283.ragium.common.block.HTImitationSpawnerBlock
import hiiragi283.ragium.common.data.recipe.HTComplexRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTShapedRecipeBuilder
import hiiragi283.ragium.common.data.recipe.HTSimulatingRecipeBuilder
import hiiragi283.ragium.common.material.CommonMaterialPrefixes
import hiiragi283.ragium.common.material.RagiumMaterialKeys
import hiiragi283.ragium.common.material.VanillaMaterialKeys
import hiiragi283.ragium.setup.RagiumBlocks
import hiiragi283.ragium.setup.RagiumFluidContents
import hiiragi283.ragium.setup.RagiumItems
import net.minecraft.core.HolderSet
import net.minecraft.core.registries.Registries
import net.minecraft.tags.ItemTags
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.neoforged.neoforge.common.Tags

@Suppress("DEPRECATION")
object RagiumSimulatingRecipeProvider : HTRecipeProvider.Direct() {
    @JvmStatic
    private fun block(vararg blocks: Block): HolderSet<Block> = HolderSet.direct(Block::builtInRegistryHolder, *blocks)

    @JvmStatic
    private fun block(prefix: HTPrefixLike, material: HTMaterialLike): HolderSet<Block> =
        provider.lookupOrThrow(Registries.BLOCK).getOrThrow(prefix.createTagKey(Registries.BLOCK, material))

    @JvmStatic
    private fun entity(vararg entityTypes: EntityType<*>): HolderSet<EntityType<*>> =
        HolderSet.direct(EntityType<*>::builtInRegistryHolder, *entityTypes)

    override fun buildRecipeInternal() {
        // Amethyst
        HTSimulatingRecipeBuilder
            .block(
                null,
                block(Blocks.BUDDING_AMETHYST),
            ).setResult(resultHelper.item(Items.AMETHYST_SHARD, 4))
            .save(output)
        // Quartz
        HTSimulatingRecipeBuilder
            .block(
                null,
                block(RagiumBlocks.BUDDING_QUARTZ.get()),
            ).setResult(resultHelper.item(Items.QUARTZ, 4))
            .save(output)
        // Echo Shard
        HTSimulatingRecipeBuilder
            .block(
                itemCreator.fromTagKey(CommonMaterialPrefixes.GEM, VanillaMaterialKeys.AMETHYST),
                block(Blocks.SCULK_SHRIEKER),
            ).setResult(resultHelper.item(Items.ECHO_SHARD))
            .save(output)
        // Imitation Spawner
        HTShapedRecipeBuilder
            .create(RagiumBlocks.IMITATION_SPAWNER)
            .pattern(
                " AA",
                "ABA",
                "AA ",
            ).define('A', RagiumBlocks.getMetalBars(RagiumMaterialKeys.DEEP_STEEL))
            .define('B', CommonMaterialPrefixes.STORAGE_BLOCK, RagiumMaterialKeys.ELDRITCH_PEARL)
            .save(output)

        mobExtracting()
    }

    @JvmStatic
    fun mobExtracting() {
        // Armadillo Scute
        HTSimulatingRecipeBuilder
            .entity(
                null,
                entity(EntityType.ARMADILLO),
            ).setResult(resultHelper.item(Items.ARMADILLO_SCUTE))
            .save(output)
        // Honeycomb
        HTSimulatingRecipeBuilder
            .block(
                null,
                block(Blocks.BEE_NEST),
            ).setResult(resultHelper.item(Items.HONEYCOMB))
            .save(output)
        // Honey Bottle
        HTSimulatingRecipeBuilder
            .block(
                itemCreator.fromItem(Items.GLASS_BOTTLE),
                block(Blocks.BEE_NEST),
            ).setResult(resultHelper.item(Items.HONEY_BOTTLE))
            .save(output)
        // Egg
        HTSimulatingRecipeBuilder
            .entity(
                itemCreator.fromTagKey(Tags.Items.SEEDS),
                entity(EntityType.CHICKEN),
            ).setResult(resultHelper.item(Items.EGG))
            .save(output)
        // Milk from Cow
        HTSimulatingRecipeBuilder
            .entity(
                null,
                entity(EntityType.COW),
            ).setResult(resultHelper.fluid(HTFluidHolderLike.MILK, 250))
            .saveSuffixed(output, "_from_cow")
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
        HTSimulatingRecipeBuilder
            .block(
                itemCreator.fromItem(Items.GLASS_BOTTLE),
                block(Blocks.DRAGON_HEAD),
            ).setResult(resultHelper.item(Items.DRAGON_BREATH))
            .save(output)
        // Frog Lights
        // Milk from Goat
        HTSimulatingRecipeBuilder
            .entity(
                null,
                entity(EntityType.GOAT),
            ).setResult(resultHelper.fluid(HTFluidHolderLike.MILK, 500))
            .saveSuffixed(output, "_from_goat")
        // Poppy
        HTSimulatingRecipeBuilder
            .entity(
                itemCreator.fromTagKey(Tags.Items.FERTILIZERS),
                entity(EntityType.IRON_GOLEM),
            ).setResult(resultHelper.item(Items.POPPY))
            .save(output)
        // Mushroom Stew from Mooshroom
        HTSimulatingRecipeBuilder
            .entity(
                null,
                entity(EntityType.MOOSHROOM),
            ).setResult(resultHelper.fluid(RagiumFluidContents.MUSHROOM_STEW, 500))
            .save(output)
        // Ancient Debris
        HTSimulatingRecipeBuilder
            .entity(
                itemCreator.fromItem(Items.NETHER_BRICKS, 64),
                entity(EntityType.PIGLIN_BRUTE),
            ).setResult(resultHelper.item(Items.ANCIENT_DEBRIS))
            .save(output)
        // Wool
        HTSimulatingRecipeBuilder
            .entity(
                null,
                entity(EntityType.SHEEP),
            ).setResult(resultHelper.item(Items.WHITE_WOOL))
            .save(output)
        // Turtle Scute
        HTSimulatingRecipeBuilder
            .entity(
                itemCreator.fromItem(Items.SEAGRASS, 8),
                entity(EntityType.TURTLE),
            ).setResult(resultHelper.item(Items.TURTLE_SCUTE))
            .save(output)
        // Resonant Debris
        HTSimulatingRecipeBuilder
            .entity(
                itemCreator.fromItem(Items.DEEPSLATE, 8),
                entity(EntityType.WARDEN),
            ).setResult(resultHelper.item(RagiumBlocks.RESONANT_DEBRIS))
            .save(output)
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

        HTSimulatingRecipeBuilder
            .block(
                itemCreator.fromItem(RagiumItems.WITHER_DOLl),
                block(CommonMaterialPrefixes.STORAGE_BLOCK, RagiumMaterialKeys.NIGHT_METAL),
            ).setResult(resultHelper.item(RagiumItems.WITHER_STAR))
            .save(output)

        HTComplexRecipeBuilder
            .mixing()
            .addIngredient(itemCreator.fromItem(RagiumItems.WITHER_STAR))
            .addIngredient(itemCreator.fromItem(Items.GHAST_TEAR, 16))
            .addIngredient(itemCreator.fromTagKey(CommonMaterialPrefixes.SCRAP, VanillaMaterialKeys.NETHERITE, 16))
            .addIngredient(itemCreator.fromTagKey(CommonMaterialPrefixes.GEM, VanillaMaterialKeys.QUARTZ, 64))
            .addIngredient(fluidCreator.fromHolder(RagiumFluidContents.ELDRITCH_FLUX, 8000))
            .setResult(resultHelper.item(Items.NETHER_STAR))
            .save(output)

        HTShapedRecipeBuilder(HTImitationSpawnerBlock.createStack(EntityType.WITHER).toImmutableOrThrow())
            .cross8()
            .define('A', RagiumItems.IRIDESCENT_POWDER)
            .define('B', Items.WITHER_ROSE)
            .define('C', RagiumBlocks.IMITATION_SPAWNER)
            .saveSuffixed(output, "/wither")

        HTSimulatingRecipeBuilder
            .entity(
                itemCreator.fromItem(RagiumItems.WITHER_DOLl),
                entity(EntityType.WITHER),
            ).setResult(resultHelper.item(Items.NETHER_STAR))
            .save(output)
    }
}
