package hiiragi283.lib.fluid

import java.util.Optional
import java.util.function.Supplier
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.item.Item
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.Fluid
import net.minecraft.world.level.material.FluidState
import net.minecraft.world.phys.Vec3
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape
import net.neoforged.neoforge.common.SoundActions
import net.neoforged.neoforge.fluids.FluidType

/**
 * 設置不可能な液体を表す[Fluid]の拡張クラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
class HTVirtualFluid(private val typeGetter: Supplier<out FluidType>, private val bucketGetter: Supplier<out Item>) : Fluid() {
    override fun getBucket(): Item = bucketGetter.get()

    override fun canBeReplacedWith(
        state: FluidState,
        level: BlockGetter,
        pos: BlockPos,
        fluid: Fluid,
        direction: Direction,
    ): Boolean = true

    override fun getFlow(blockReader: BlockGetter, pos: BlockPos, fluidState: FluidState): Vec3 = Vec3.ZERO

    override fun getTickDelay(level: LevelReader): Int = 0

    override fun getExplosionResistance(): Float = 0f

    override fun getHeight(state: FluidState, level: BlockGetter, pos: BlockPos): Float = 0f

    override fun getOwnHeight(state: FluidState): Float = 0f

    override fun createLegacyBlock(state: FluidState): BlockState = Blocks.AIR.defaultBlockState()

    override fun isSource(state: FluidState): Boolean = true

    override fun getAmount(state: FluidState): Int = 0

    override fun isSame(fluid: Fluid): Boolean = fluid == this

    override fun getShape(state: FluidState, level: BlockGetter, pos: BlockPos): VoxelShape = Shapes.empty()

    override fun getFluidType(): FluidType = typeGetter.get()

    override fun getPickupSound(): Optional<SoundEvent> = Optional.ofNullable(fluidType.getSound(SoundActions.BUCKET_FILL))
}
