package hiiragi283.ragium.api.block

import hiiragi283.ragium.api.RagiumAPI
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

/**
 * ブロックの回転処理を行います。
 */
fun interface HTBlockRotationHandler {
    fun rotate(state: BlockState, direction: Direction): BlockState

    companion object {
        @JvmField
        val LOOKUP: BlockApiLookup<HTBlockRotationHandler, Void?> =
            BlockApiLookup.get(RagiumAPI.id("block_rotation"), HTBlockRotationHandler::class.java, Void::class.java)

        init {
            LOOKUP.registerFallback { _: World, _: BlockPos, state: BlockState, _: BlockEntity?, _: Void? ->
                when {
                    state.contains(Properties.FACING) ->
                        HTBlockRotationHandler { stateIn: BlockState, direction: Direction ->
                            stateIn.with(Properties.FACING, direction)
                        }

                    state.contains(Properties.HOPPER_FACING) ->
                        HTBlockRotationHandler { stateIn: BlockState, direction: Direction ->
                            if (direction == Direction.UP) {
                                stateIn
                            } else {
                                stateIn.with(Properties.HOPPER_FACING, direction)
                            }
                        }

                    state.contains(Properties.HORIZONTAL_FACING) ->
                        HTBlockRotationHandler { stateIn: BlockState, direction: Direction ->
                            if (direction.axis == Direction.Axis.Y) {
                                stateIn
                            } else {
                                stateIn.with(Properties.HORIZONTAL_FACING, direction)
                            }
                        }

                    state.contains(Properties.AXIS) ->
                        HTBlockRotationHandler { stateIn: BlockState, direction: Direction ->
                            stateIn.with(Properties.AXIS, direction.axis)
                        }

                    state.contains(Properties.HORIZONTAL_AXIS) ->
                        HTBlockRotationHandler { stateIn: BlockState, direction: Direction ->
                            val axis: Direction.Axis = direction.axis
                            if (axis == Direction.Axis.Y) {
                                stateIn
                            } else {
                                stateIn.with(Properties.HORIZONTAL_AXIS, axis)
                            }
                        }

                    else -> null
                }
            }
        }
    }
}
