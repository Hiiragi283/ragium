package hiiragi283.ragium.common.util

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.data.HTTagBuilder
import hiiragi283.ragium.api.data.recipe.HTShapedRecipeBuilder
import hiiragi283.ragium.api.extension.asItemHolder
import hiiragi283.ragium.api.extension.blockId
import hiiragi283.ragium.api.extension.idOrThrow
import hiiragi283.ragium.api.extension.vanillaId
import hiiragi283.ragium.api.registry.HTBlockRegister
import hiiragi283.ragium.api.registry.HTBlockSet
import hiiragi283.ragium.api.registry.HTItemRegister
import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.BlockTags
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.CraftingBookCategory
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.StonecutterRecipe
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.SlabBlock
import net.minecraft.world.level.block.StairBlock
import net.minecraft.world.level.block.WallBlock
import net.minecraft.world.level.block.state.BlockBehaviour
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.client.model.generators.BlockStateProvider
import net.neoforged.neoforge.client.model.generators.ItemModelProvider
import net.neoforged.neoforge.common.data.LanguageProvider
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredItem

class HTBuildingBlockSets(
    name: String,
    properties: BlockBehaviour.Properties,
    factory: (BlockBehaviour.Properties) -> Block = ::Block,
    private val prefix: String = name,
) : HTBlockSet {
    private val blockRegister = HTBlockRegister(RagiumAPI.MOD_ID)
    private val itemRegister = HTItemRegister(RagiumAPI.MOD_ID)

    val base: DeferredBlock<*> = blockRegister.registerBlock(
        name,
        factory,
        properties,
    )

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

    //    HTBlockSet    //

    override val blockHolders: List<DeferredBlock<*>> = blockRegister.entries

    override val itemHolders: List<DeferredItem<*>> = blockHolders.map(itemRegister::registerSimpleBlockItem)

    override fun init(eventBus: IEventBus) {
        blockRegister.register(eventBus)
        itemRegister.register(eventBus)
    }

    override fun appendBlockTags(builder: HTTagBuilder<Block>, mineableTag: TagKey<Block>) {
        blockHolders.forEach { builder.add(mineableTag, it) }
        builder.add(BlockTags.STAIRS, stairs)
        builder.add(BlockTags.SLABS, slab)
        builder.add(BlockTags.WALLS, wall)
    }

    override fun appendItemTags(builder: HTTagBuilder.ItemTag) {
        builder.copyFromBlock(BlockTags.STAIRS, ItemTags.STAIRS)
        builder.copyFromBlock(BlockTags.SLABS, ItemTags.SLABS)
        builder.copyFromBlock(BlockTags.WALLS, ItemTags.WALLS)
    }

    override fun addRecipes(output: RecipeOutput, holderLookup: HolderLookup.Provider) {
        // Base -> Slab
        HTShapedRecipeBuilder(slab, 6)
            .pattern("AAA")
            .define('A', base)
            .save(output)

        output.accept(
            slab.asItemHolder().idOrThrow.withPrefix("stonecutting/"),
            StonecutterRecipe(
                "",
                Ingredient.of(base),
                slab.toStack(2),
            ),
            null,
        )
        // Base -> Stairs
        HTShapedRecipeBuilder(stairs, 4, CraftingBookCategory.BUILDING)
            .pattern("A  ")
            .pattern("AA ")
            .pattern("AAA")
            .define('A', base)
            .save(output)

        output.accept(
            stairs.id.withPrefix("stonecutting/"),
            StonecutterRecipe(
                "",
                Ingredient.of(base),
                ItemStack(stairs),
            ),
            null,
        )
        // Base -> Wall
        HTShapedRecipeBuilder(wall, 4, CraftingBookCategory.BUILDING)
            .pattern("AAA")
            .pattern("AAA")
            .define('A', base)
            .save(output)

        output.accept(
            wall.id.withPrefix("stonecutting/"),
            StonecutterRecipe(
                "",
                Ingredient.of(base),
                ItemStack(wall),
            ),
            null,
        )
    }

    override fun addBlockStates(provider: BlockStateProvider) {
        val texId: ResourceLocation = base.blockId
        provider.simpleBlock(base.get())
        provider.stairsBlock(stairs.get(), texId)
        provider.slabBlock(slab.get(), texId, texId)
        provider.wallBlock(wall.get(), texId)
    }

    override fun addItemModels(provider: ItemModelProvider) {
        val texId: ResourceLocation = base.blockId
        provider.simpleBlockItem(base.id)
        provider.simpleBlockItem(stairs.id)
        provider.simpleBlockItem(slab.id)

        provider
            .withExistingParent("${prefix}_wall", vanillaId("block/wall_inventory"))
            .texture("wall", texId)
    }

    override fun addTranslationEn(name: String, provider: LanguageProvider) {
        provider.addBlock(base, name)
        provider.addBlock(stairs, "$name Stairs")
        provider.addBlock(slab, "$name Slab")
        provider.addBlock(wall, "$name Wall")
    }

    override fun addTranslationJp(name: String, provider: LanguageProvider) {
        provider.addBlock(base, name)
        provider.addBlock(stairs, "${name}の階段")
        provider.addBlock(slab, "${name}のハーフブロック")
        provider.addBlock(wall, "${name}の壁")
    }
}
