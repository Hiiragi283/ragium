package hiiragi283.ragium.common.block

import net.minecraft.block.Block
import net.minecraft.block.BlockEntityProvider
import net.minecraft.block.BlockState
import net.minecraft.screen.NamedScreenHandlerFactory
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

abstract class HTBlockWithEntity(settings: Settings) : Block(settings), BlockEntityProvider {

    override fun createScreenHandlerFactory(
        state: BlockState,
        world: World,
        pos: BlockPos,
    ): NamedScreenHandlerFactory? = world.getBlockEntity(pos) as? NamedScreenHandlerFactory

}