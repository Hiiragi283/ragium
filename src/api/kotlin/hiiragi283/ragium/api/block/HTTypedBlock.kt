package hiiragi283.ragium.api.block

import hiiragi283.ragium.api.block.attribute.HTBlockAttribute
import hiiragi283.ragium.api.block.attribute.HTDirectionalBlockAttribute
import hiiragi283.ragium.api.block.attribute.getAllAttributes
import hiiragi283.ragium.api.block.attribute.getAttribute
import hiiragi283.ragium.api.block.type.HTBlockType
import net.minecraft.core.BlockPos
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Mirror
import net.minecraft.world.level.block.Rotation
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.Property
import java.util.function.UnaryOperator

/**
 * @see mekanism.common.block.prefab.BlockBase
 */
open class HTTypedBlock<TYPE : HTBlockType>(protected val type: TYPE, properties: Properties) :
    Block(hack(type, properties)),
    HTBlockWithType {
    companion object {
        @JvmStatic
        private lateinit var cacheType: HTBlockType

        @JvmStatic
        private fun <TYPE : HTBlockType> hack(type: TYPE, properties: Properties): Properties {
            cacheType = type
            return properties
        }
    }

    constructor(type: TYPE, operator: UnaryOperator<Properties>) : this(type, operator.apply(Properties.of().requiresCorrectToolForDrops()))

    init {
        registerDefaultState(stateDefinition.any())
    }

    @Suppress("USELESS_ELVIS")
    override fun type(): HTBlockType = this.type ?: cacheType

    final override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        super.createBlockStateDefinition(builder)
        val properties: List<Property<*>> = buildList {
            for (attribute: HTBlockAttribute in this@HTTypedBlock.getAllAttributes()) {
                if (attribute is HTDirectionalBlockAttribute) {
                    attribute.buildBlockState(this@HTTypedBlock, this)
                }
            }
        }
        if (!properties.isEmpty()) {
            builder.add(*properties.toTypedArray())
        }
    }

    final override fun getStateForPlacement(context: BlockPlaceContext): BlockState? {
        var state: BlockState? = super.getStateForPlacement(context)
        for (attribute: HTBlockAttribute in this@HTTypedBlock.getAllAttributes()) {
            if (attribute is HTDirectionalBlockAttribute) {
                state = attribute.getStateForPlacement(this, context)
            }
        }
        return state
    }

    final override fun rotate(
        state: BlockState,
        level: LevelAccessor,
        pos: BlockPos,
        direction: Rotation,
    ): BlockState {
        val attribute: HTDirectionalBlockAttribute =
            state.getAttribute<HTDirectionalBlockAttribute>() ?: return super.rotate(state, level, pos, direction)
        return attribute.setDirection(state, direction.rotate(attribute.getDirection(state)))
    }

    final override fun mirror(state: BlockState, mirror: Mirror): BlockState {
        val attribute: HTDirectionalBlockAttribute = state.getAttribute<HTDirectionalBlockAttribute>() ?: return super.mirror(state, mirror)
        return attribute.setDirection(state, mirror.mirror(attribute.getDirection(state)))
    }
}
