package hiiragi283.ragium.common.block

import com.mojang.serialization.MapCodec
import hiiragi283.ragium.api.RagiumDataMaps
import hiiragi283.ragium.api.data.HTTreeTap
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceKey
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.HorizontalDirectionalBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.pattern.BlockInWorld
import net.minecraft.world.level.block.state.properties.IntegerProperty
import net.minecraft.world.level.material.Fluid
import net.minecraft.world.level.pathfinder.PathComputationType
import net.neoforged.neoforge.fluids.CauldronFluidContent

class HTTreeTapBlock(properties: Properties) : HorizontalDirectionalBlock(properties) {
    override fun codec(): MapCodec<out HorizontalDirectionalBlock> = throw UnsupportedOperationException()

    init {
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH))
    }

    override fun randomTick(
        state: BlockState,
        level: ServerLevel,
        pos: BlockPos,
        random: RandomSource,
    ) {
        // 背面に原木があるかをチェック
        val front: Direction = state.getValue(FACING)
        val back: Direction = front.opposite
        val backStates: List<BlockInWorld> = listOf(
            pos.relative(back).above(),
            pos.relative(back),
            pos.relative(back).below(),
        ).map { posIn: BlockPos -> BlockInWorld(level, posIn, false) }
        // 液体を取得する
        var cauldron: CauldronFluidContent? = null
        for ((key: ResourceKey<Fluid>, treeTap: HTTreeTap) in BuiltInRegistries.FLUID.getDataMap(RagiumDataMaps.TREE_TAP)) {
            if (backStates.all(treeTap.predicate::matches)) {
                val fluid: Fluid = BuiltInRegistries.FLUID.get(key) ?: continue
                cauldron = CauldronFluidContent.getForFluid(fluid)
                if (cauldron != null) break
            }
        }
        if (cauldron == null) return
        val property: IntegerProperty? = cauldron.levelProperty
        // 下に大釜があるかチェック
        val belowPos: BlockPos = pos.below()
        val belowState: BlockState = level.getBlockState(belowPos)
        val belowCauldron: CauldronFluidContent = CauldronFluidContent.getForBlock(belowState.block) ?: return
        // 空の大釜の場合
        if (belowCauldron == CauldronFluidContent.getForBlock(Blocks.CAULDRON)) {
            val newState: BlockState = cauldron.block.defaultBlockState().apply {
                if (property != null) setValue(property, 1)
            }
            level.setBlockAndUpdate(belowPos, newState)
        } else if (belowCauldron == cauldron) {
            // すでに埋まっている大釜の場合
            val currentLevel: Int = belowCauldron.currentLevel(belowState)
            if (property == null) return
            if (currentLevel < belowCauldron.maxLevel) {
                level.setBlockAndUpdate(belowPos, belowState.setValue(property, currentLevel + 1))
            }
        }
    }

    override fun getStateForPlacement(context: BlockPlaceContext): BlockState? =
        defaultBlockState().setValue(FACING, context.horizontalDirection.opposite)

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(FACING)
    }

    override fun isPathfindable(state: BlockState, pathComputationType: PathComputationType): Boolean = false
}
