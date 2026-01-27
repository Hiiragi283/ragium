package hiiragi283.ragium.data.server.recipe

import hiiragi283.core.api.data.recipe.HTSubRecipeProvider
import hiiragi283.core.api.tag.CommonTagPrefixes
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
        HTSimulatingRecipeBuilder.block(output) {
            catalyst = block(Blocks.BUDDING_AMETHYST)
            result += resultCreator.material(CommonTagPrefixes.GEM, VanillaMaterialKeys.AMETHYST, 4)
        }
        // Echo Shard
        HTSimulatingRecipeBuilder.block(output) {
            ingredient = inputCreator.create(CommonTagPrefixes.GEM, VanillaMaterialKeys.AMETHYST)
            catalyst = block(Blocks.SCULK_SHRIEKER)
            result += resultCreator.material(CommonTagPrefixes.GEM, VanillaMaterialKeys.ECHO)
        }

        mobExtracting()
    }

    @JvmStatic
    private fun mobExtracting() {
        // Armadillo Scute
        HTSimulatingRecipeBuilder.entity(output) {
            catalyst = entity(EntityType.ARMADILLO)
            result += resultCreator.create(Items.ARMADILLO_SCUTE)
        }
        // Honeycomb
        HTSimulatingRecipeBuilder.block(output) {
            catalyst = block(Blocks.BEE_NEST)
            result += resultCreator.create(Items.HONEYCOMB)
        }
        // Honey Bottle
        HTSimulatingRecipeBuilder.block(output) {
            ingredient = inputCreator.create(Items.GLASS_BOTTLE)
            catalyst = block(Blocks.BEE_NEST)
            result += resultCreator.create(Items.HONEY_BOTTLE)
        }
        // Egg
        HTSimulatingRecipeBuilder.entity(output) {
            ingredient = inputCreator.create(Tags.Items.SEEDS)
            catalyst = entity(EntityType.CHICKEN)
            result += resultCreator.create(Items.EGG)
        }
        // Milk from Cow
        HTSimulatingRecipeBuilder.entity(output) {
            catalyst = entity(EntityType.COW)
            result += resultCreator.milk(250)
            recipeId suffix "_from_cow"
        }
        // Heart of the Sea
        // Dragon Breath
        HTSimulatingRecipeBuilder.block(output) {
            ingredient = inputCreator.create(Items.GLASS_BOTTLE)
            catalyst = block(Blocks.DRAGON_HEAD)
            result += resultCreator.create(Items.DRAGON_BREATH)
        }
        // Frog Lights
        // Milk from Goat
        HTSimulatingRecipeBuilder.entity(output) {
            catalyst = entity(EntityType.GOAT)
            result += resultCreator.milk(500)
            recipeId suffix "_from_goat"
        }
        // Poppy
        HTSimulatingRecipeBuilder.entity(output) {
            ingredient = inputCreator.create(Tags.Items.FERTILIZERS)
            catalyst = entity(EntityType.IRON_GOLEM)
            result += resultCreator.create(Items.POPPY)
        }
        // Mushroom Stew from Mooshroom
        HTSimulatingRecipeBuilder.entity(output) {
            catalyst = entity(EntityType.MOOSHROOM)
            result += resultCreator.create(HCFluids.MUSHROOM_STEW, 500)
        }
        // Ancient Debris
        HTSimulatingRecipeBuilder.entity(output) {
            ingredient = inputCreator.create(Items.NETHER_BRICKS, 64)
            catalyst = entity(EntityType.PIGLIN_BRUTE)
            result += resultCreator.create(Items.ANCIENT_DEBRIS)
        }
        // Wool
        HTSimulatingRecipeBuilder.entity(output) {
            catalyst = entity(EntityType.SHEEP)
            result += resultCreator.create(Items.WHITE_WOOL)
        }
        // Turtle Scute
        HTSimulatingRecipeBuilder.entity(output) {
            ingredient = inputCreator.create(Items.SEAGRASS, 8)
            catalyst = entity(EntityType.TURTLE)
            result += resultCreator.create(Items.TURTLE_SCUTE)
        }
        // Resonant Debris

        // Nether Star
        HTSimulatingRecipeBuilder.block(output) {
            ingredient = inputCreator.create(HCItems.WITHER_DOLL)
            catalyst = block(Tags.Blocks.OBSIDIANS_CRYING)
            result += resultCreator.create(HCItems.WITHER_STAR)
        }
    }
}
