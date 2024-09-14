package hiiragi283.ragium.common.block

import net.minecraft.block.Block
import net.minecraft.block.BlockEntityProvider
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityTicker
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.screen.NamedScreenHandlerFactory
import net.minecraft.util.ActionResult
import net.minecraft.util.ItemScatterer
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

abstract class HTBlockWithEntity(settings: Settings) : Block(settings), BlockEntityProvider {

    override fun createScreenHandlerFactory(
        state: BlockState,
        world: World,
        pos: BlockPos,
    ): NamedScreenHandlerFactory? = world.getBlockEntity(pos) as? NamedScreenHandlerFactory

    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hit: BlockHitResult,
    ): ActionResult = when (world.isClient) {
        true -> ActionResult.SUCCESS
        false -> {
            player.openHandledScreen(state.createScreenHandlerFactory(world, pos))
            ActionResult.CONSUME
        }
    }

    override fun onStateReplaced(
        state: BlockState,
        world: World,
        pos: BlockPos,
        newState: BlockState,
        moved: Boolean,
    ) {
        ItemScatterer.onStateReplaced(state, newState, world, pos)
        super.onStateReplaced(state, world, pos, newState, moved)
    }

    //    Builder    //

    class Builder<B : BlockEntity> {
        lateinit var type: BlockEntityType<B>
            private set
        private var ticker: BlockEntityTicker<B>? = null

        fun type(type: BlockEntityType<B>): Builder<B> = apply { this.type = type }

        fun ticker(ticker: BlockEntityTicker<B>): Builder<B> = apply { this.ticker = ticker }

        fun build(settings: Settings = Settings.create()): HTBlockWithEntity = object : HTBlockWithEntity(settings) {
            override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity? =
                type.instantiate(pos, state)

            override fun <T : BlockEntity> getTicker(
                world: World,
                state: BlockState,
                type: BlockEntityType<T>,
            ): BlockEntityTicker<T>? = HTBlockEntityTicker.validateTicker(type, type, ticker)
        }
    }

}