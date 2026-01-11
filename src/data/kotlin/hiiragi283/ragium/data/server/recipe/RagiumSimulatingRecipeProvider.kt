package hiiragi283.ragium.data.server.recipe

import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.common.material.HCMaterialPrefixes
import hiiragi283.core.common.material.VanillaMaterialKeys
import hiiragi283.core.setup.HCFluids
import hiiragi283.core.setup.HCItems
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.common.data.recipe.HTSimulatingRecipeBuilder
import net.minecraft.core.HolderSet
import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.neoforged.neoforge.common.Tags

@Suppress("DEPRECATION")
object RagiumSimulatingRecipeProvider : HTSubRecipeProvider.Direct(RagiumAPI.MOD_ID) {
    @JvmStatic
    private fun block(vararg blocks: Block): HolderSet<Block> = HolderSet.direct(Block::builtInRegistryHolder, *blocks)

    @JvmStatic
    private fun block(tagKey: TagKey<Block>): HolderSet<Block> = provider.lookupOrThrow(Registries.BLOCK).getOrThrow(tagKey)

    @JvmStatic
    private fun entity(vararg entityTypes: EntityType<*>): HolderSet<EntityType<*>> =
        HolderSet.direct(EntityType<*>::builtInRegistryHolder, *entityTypes)

    override fun buildRecipeInternal() {
        // Amethyst
        HTSimulatingRecipeBuilder
            .block(
                null,
                block(Blocks.BUDDING_AMETHYST),
            ).setResult(itemResult.create(Items.AMETHYST_SHARD, 4))
            .save(output)
        // Echo Shard
        HTSimulatingRecipeBuilder
            .block(
                itemCreator.fromTagKey(HCMaterialPrefixes.GEM, VanillaMaterialKeys.AMETHYST),
                block(Blocks.SCULK_SHRIEKER),
            ).setResult(itemResult.create(Items.ECHO_SHARD))
            .save(output)

        mobExtracting()
    }

    @JvmStatic
    private fun mobExtracting() {
        // Armadillo Scute
        HTSimulatingRecipeBuilder
            .entity(
                null,
                entity(EntityType.ARMADILLO),
            ).setResult(itemResult.create(Items.ARMADILLO_SCUTE))
            .save(output)
        // Honeycomb
        HTSimulatingRecipeBuilder
            .block(
                null,
                block(Blocks.BEE_NEST),
            ).setResult(itemResult.create(Items.HONEYCOMB))
            .save(output)
        // Honey Bottle
        HTSimulatingRecipeBuilder
            .block(
                itemCreator.fromItem(Items.GLASS_BOTTLE),
                block(Blocks.BEE_NEST),
            ).setResult(itemResult.create(Items.HONEY_BOTTLE))
            .save(output)
        // Egg
        HTSimulatingRecipeBuilder
            .entity(
                itemCreator.fromTagKey(Tags.Items.SEEDS),
                entity(EntityType.CHICKEN),
            ).setResult(itemResult.create(Items.EGG))
            .save(output)
        // Milk from Cow
        HTSimulatingRecipeBuilder
            .entity(
                null,
                entity(EntityType.COW),
            ).setResult(fluidResult.milk(250))
            .saveSuffixed(output, "_from_cow")
        // Heart of the Sea
        // Dragon Breath
        HTSimulatingRecipeBuilder
            .block(
                itemCreator.fromItem(Items.GLASS_BOTTLE),
                block(Blocks.DRAGON_HEAD),
            ).setResult(itemResult.create(Items.DRAGON_BREATH))
            .save(output)
        // Frog Lights
        // Milk from Goat
        HTSimulatingRecipeBuilder
            .entity(
                null,
                entity(EntityType.GOAT),
            ).setResult(fluidResult.milk(500))
            .saveSuffixed(output, "_from_goat")
        // Poppy
        HTSimulatingRecipeBuilder
            .entity(
                itemCreator.fromTagKey(Tags.Items.FERTILIZERS),
                entity(EntityType.IRON_GOLEM),
            ).setResult(itemResult.create(Items.POPPY))
            .save(output)
        // Mushroom Stew from Mooshroom
        HTSimulatingRecipeBuilder
            .entity(
                null,
                entity(EntityType.MOOSHROOM),
            ).setResult(fluidResult.create(HCFluids.MUSHROOM_STEW, 500))
            .save(output)
        // Ancient Debris
        HTSimulatingRecipeBuilder
            .entity(
                itemCreator.fromItem(Items.NETHER_BRICKS, 64),
                entity(EntityType.PIGLIN_BRUTE),
            ).setResult(itemResult.create(Items.ANCIENT_DEBRIS))
            .save(output)
        // Wool
        HTSimulatingRecipeBuilder
            .entity(
                null,
                entity(EntityType.SHEEP),
            ).setResult(itemResult.create(Items.WHITE_WOOL))
            .save(output)
        // Turtle Scute
        HTSimulatingRecipeBuilder
            .entity(
                itemCreator.fromItem(Items.SEAGRASS, 8),
                entity(EntityType.TURTLE),
            ).setResult(itemResult.create(Items.TURTLE_SCUTE))
            .save(output)
        // Resonant Debris

        // Nether Star
        HTSimulatingRecipeBuilder
            .block(
                itemCreator.fromItem(HCItems.WITHER_DOLL),
                block(Tags.Blocks.OBSIDIANS_CRYING),
            ).setResult(itemResult.create(HCItems.WITHER_STAR))
            .save(output)
    }
}
