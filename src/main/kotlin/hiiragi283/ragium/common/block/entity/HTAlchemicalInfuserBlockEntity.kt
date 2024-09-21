package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.inventory.*
import hiiragi283.ragium.common.machine.HTBlockPredicate
import hiiragi283.ragium.common.machine.HTMultiblockBuilder
import hiiragi283.ragium.common.recipe.HTAlchemyRecipe
import hiiragi283.ragium.common.screen.HTAlchemicalInfuserHandler
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.recipe.RecipeEntry
import net.minecraft.screen.NamedScreenHandlerFactory
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerContext
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.ItemActionResult
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import kotlin.jvm.optionals.getOrNull

class HTAlchemicalInfuserBlockEntity(pos: BlockPos, state: BlockState) :
    HTBaseBlockEntity(RagiumBlockEntityTypes.ALCHEMICAL_INFUSER, pos, state),
    HTDelegatedInventory,
    HTMultiblockController,
    NamedScreenHandlerFactory {
    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hit: BlockHitResult,
    ): ActionResult = onUseController(state, world, pos, player)

    fun processRecipe(stack: ItemStack, world: World): ItemActionResult {
        if (stack.isOf(Items.BLAZE_ROD)) {
            val input = HTAlchemyRecipe.Input(
                getStack(0),
                getStack(1),
                getStack(2),
                getStack(3),
            )
            val recipe: HTAlchemyRecipe =
                world.recipeManager
                    .getFirstMatch(HTAlchemyRecipe.Type, input, world)
                    .map(RecipeEntry<HTAlchemyRecipe>::value)
                    .getOrNull() ?: return ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION
            if (!canAcceptOutput(input, world, recipe)) return ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION
            modifyOutput(input, world, recipe)
            decrementInput(0, recipe)
            decrementInput(1, recipe)
            decrementInput(2, recipe)
            decrementInput(3, recipe)
            return ItemActionResult.success(world.isClient)
        }
        return ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION
    }

    private fun canAcceptOutput(input: HTAlchemyRecipe.Input, world: World, recipe: HTAlchemyRecipe): Boolean {
        val stackOut: ItemStack = recipe.craft(input, world.registryManager)
        val stackIn: ItemStack = getStack(4)
        return when {
            stackIn.isEmpty -> true
            ItemStack.areItemsAndComponentsEqual(stackOut, stackIn) -> true
            else -> false
        }
    }

    private fun modifyOutput(input: HTAlchemyRecipe.Input, world: World, recipe: HTAlchemyRecipe) {
        parent.modifyStack(4) { stackIn: ItemStack ->
            val result: ItemStack = recipe.craft(input, world.registryManager)
            when {
                !canAcceptOutput(input, world, recipe) -> null
                stackIn.isEmpty -> result
                ItemStack.areItemsAndComponentsEqual(stackIn, result) -> stackIn.apply { count += result.count }
                else -> null
            } ?: stackIn
        }
    }

    private fun decrementInput(slot: Int, recipe: HTAlchemyRecipe) {
        val delCount: Int = recipe.getInput(slot)?.count ?: return
        getStack(slot).count -= delCount
    }

    //    HTDelegatedInventory    //

    override val parent: HTSidedInventory = HTSidedStorageBuilder(5)
        .set(0, HTStorageIO.INPUT, HTStorageSide.ANY)
        .set(1, HTStorageIO.INPUT, HTStorageSide.ANY)
        .set(2, HTStorageIO.INPUT, HTStorageSide.ANY)
        .set(3, HTStorageIO.INPUT, HTStorageSide.ANY)
        .set(4, HTStorageIO.OUTPUT, HTStorageSide.ANY)
        .buildSided()

    override fun markDirty() {
        super<HTBaseBlockEntity>.markDirty()
    }

    //    HTMultiblockController    //

    override var showPreview: Boolean = false

    override fun onValid(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
    ) {
    }

    override fun buildMultiblock(builder: HTMultiblockBuilder): HTMultiblockBuilder = builder
        // tiles
        .add(-2, -1, -2, HTBlockPredicate.block(Blocks.DEEPSLATE_TILES))
        .add(-2, -1, 2, HTBlockPredicate.block(Blocks.DEEPSLATE_TILES))
        .add(2, -1, -2, HTBlockPredicate.block(Blocks.DEEPSLATE_TILES))
        .add(2, -1, 2, HTBlockPredicate.block(Blocks.DEEPSLATE_TILES))
        // slabs
        .add(-2, 0, -2, HTBlockPredicate.block(Blocks.DEEPSLATE_TILE_SLAB))
        .add(-2, 0, 2, HTBlockPredicate.block(Blocks.DEEPSLATE_TILE_SLAB))
        .add(2, 0, -2, HTBlockPredicate.block(Blocks.DEEPSLATE_TILE_SLAB))
        .add(2, 0, 2, HTBlockPredicate.block(Blocks.DEEPSLATE_TILE_SLAB))
        .addLayer(-1..1, -1, -2..-2, HTBlockPredicate.block(Blocks.DEEPSLATE_TILE_SLAB))
        .addLayer(-1..1, -1, 2..2, HTBlockPredicate.block(Blocks.DEEPSLATE_TILE_SLAB))
        .addLayer(-2..-2, -1, -1..1, HTBlockPredicate.block(Blocks.DEEPSLATE_TILE_SLAB))
        .addLayer(2..2, -1, -1..1, HTBlockPredicate.block(Blocks.DEEPSLATE_TILE_SLAB))
        // obsidian
        .add(-1, -1, -1, HTBlockPredicate.block(Blocks.OBSIDIAN))
        .add(-1, -1, 1, HTBlockPredicate.block(Blocks.OBSIDIAN))
        .add(1, -1, -1, HTBlockPredicate.block(Blocks.OBSIDIAN))
        .add(1, -1, 1, HTBlockPredicate.block(Blocks.OBSIDIAN))
        // crying
        .add(-1, -1, 0, HTBlockPredicate.block(Blocks.CRYING_OBSIDIAN))
        .add(0, -1, -1, HTBlockPredicate.block(Blocks.CRYING_OBSIDIAN))
        .add(0, -1, 0, HTBlockPredicate.block(Blocks.CRYING_OBSIDIAN))
        .add(0, -1, 1, HTBlockPredicate.block(Blocks.CRYING_OBSIDIAN))
        .add(1, -1, 0, HTBlockPredicate.block(Blocks.CRYING_OBSIDIAN))

    //    NamedScreenHandlerFactory    //

    override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity): ScreenHandler =
        HTAlchemicalInfuserHandler(syncId, playerInventory, ScreenHandlerContext.create(world, pos))

    override fun getDisplayName(): Text = Text.literal("Alchemical Infuser")
}
