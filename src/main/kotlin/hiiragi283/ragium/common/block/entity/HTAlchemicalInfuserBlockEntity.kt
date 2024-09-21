package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.inventory.*
import hiiragi283.ragium.common.machine.HTBlockPredicate
import hiiragi283.ragium.common.machine.HTMultiblockBuilder
import hiiragi283.ragium.common.screen.HTAlchemicalInfuserHandler
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.NamedScreenHandlerFactory
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerContext
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

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
