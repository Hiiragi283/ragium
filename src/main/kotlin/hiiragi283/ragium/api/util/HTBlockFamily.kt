package hiiragi283.ragium.api.util

import hiiragi283.ragium.api.data.recipe.HTShapedRecipeBuilder
import hiiragi283.ragium.api.extension.savePrefixed
import hiiragi283.ragium.api.extension.simpleBlockItem
import hiiragi283.ragium.data.server.RagiumRecipeProvider
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.SingleItemRecipeBuilder
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.BlockTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.SlabBlock
import net.minecraft.world.level.block.StairBlock
import net.minecraft.world.level.block.WallBlock
import net.minecraft.world.level.block.state.BlockBehaviour
import net.neoforged.neoforge.client.model.generators.BlockStateProvider
import net.neoforged.neoforge.client.model.generators.ItemModelProvider
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredRegister

class HTBlockFamily(
    blockRegister: DeferredRegister.Blocks,
    itemRegister: DeferredRegister.Items,
    val base: DeferredBlock<*>,
    properties: BlockBehaviour.Properties,
    private val prefix: String = base.id.path,
) : RagiumRecipeProvider.Child {
    val stairs: DeferredBlock<StairBlock> = blockRegister.registerBlock(
        "${prefix}_stairs",
        { properties: BlockBehaviour.Properties ->
            StairBlock(
                base.get().defaultBlockState(),
                properties,
            )
        },
        properties,
    )

    val slab: DeferredBlock<SlabBlock> = blockRegister.registerBlock(
        "${prefix}_slab",
        ::SlabBlock,
        properties,
    )

    val wall: DeferredBlock<WallBlock> = blockRegister.registerBlock(
        "${prefix}_wall",
        ::WallBlock,
        properties.forceSolidOn(),
    )

    val blocks: List<DeferredBlock<out Block>> = listOf(base, stairs, slab, wall)

    init {
        listOf(stairs, slab, wall).forEach(itemRegister::registerSimpleBlockItem)
    }

    //    Data Gen    //

    fun appendTags(mineable: TagKey<Block>, action: (TagKey<Block>, Holder<Block>) -> Unit) {
        blocks.forEach { action(mineable, it) }
        action(BlockTags.STAIRS, stairs)
        action(BlockTags.SLABS, slab)
        action(BlockTags.WALLS, wall)
    }

    fun generateStates(generator: BlockStateProvider) {
        val texId: ResourceLocation = base.id.withPrefix("block/")
        generator.simpleBlock(base.get())
        generator.stairsBlock(stairs.get(), texId)
        generator.slabBlock(slab.get(), texId, texId)
        generator.wallBlock(wall.get(), texId)
    }

    fun generateModels(provider: ItemModelProvider) {
        val texId: ResourceLocation = base.id.withPrefix("block/")

        provider.simpleBlockItem(base)
        provider.simpleBlockItem(stairs)
        provider.simpleBlockItem(slab)

        provider
            .withExistingParent("${prefix}_wall", ResourceLocation.withDefaultNamespace("block/wall_inventory"))
            .texture("wall", texId)
    }

    override fun buildRecipes(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        // Base -> Slab
        HTShapedRecipeBuilder(slab, 6, CraftingBookCategory.BUILDING)
            .pattern("AAA")
            .define('A', base)
            .save(output)

        SingleItemRecipeBuilder
            .stonecutting(Ingredient.of(base), RecipeCategory.BUILDING_BLOCKS, slab, 2)
            .unlockedBy("has_base", has(base))
            .savePrefixed(output)
        // Base -> Stairs
        HTShapedRecipeBuilder(stairs, 4, CraftingBookCategory.BUILDING)
            .pattern("A  ")
            .pattern("AA ")
            .pattern("AAA")
            .define('A', base)
            .save(output)

        SingleItemRecipeBuilder
            .stonecutting(Ingredient.of(base), RecipeCategory.BUILDING_BLOCKS, stairs)
            .unlockedBy("has_base", has(base))
            .savePrefixed(output)
        // Base -> Wall
        HTShapedRecipeBuilder(wall, 4, CraftingBookCategory.BUILDING)
            .pattern("AAA")
            .pattern("AAA")
            .define('A', base)
            .save(output)

        SingleItemRecipeBuilder
            .stonecutting(Ingredient.of(base), RecipeCategory.BUILDING_BLOCKS, wall)
            .unlockedBy("has_base", has(base))
            .savePrefixed(output)
    }
}
