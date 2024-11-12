package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class HTAutoIlluminatorBlockEntity(pos: BlockPos, state: BlockState) :
    HTBlockEntityBase(RagiumBlockEntityTypes.AUTO_ILLUMINATOR, pos, state) {
    private val yRange: IntRange
        get() = (world?.bottomY ?: 0)..<pos.y

    override val tickRate: Int = 1

    override fun tickSecond(world: World, pos: BlockPos, state: BlockState) {
        for (x: Int in (pos.x - 64..pos.x + 64)) {
            for (z: Int in (pos.z - 64..pos.z + 64)) {
                for (y: Int in yRange) {
                    val posIn = BlockPos(x, y, z)
                    val stateIn: BlockState = world.getBlockState(posIn)
                    if (stateIn.isAir) {
                        if (!world.isSkyVisible(posIn) && world.getLightLevel(posIn) <= 7) {
                            world.setBlockState(posIn, Blocks.LIGHT.defaultState)
                            return
                        }
                    }
                }
            }
        }
        world.playSound(null, pos, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER, 10.0f, 0.5f)
        world.breakBlock(pos, true)
    }
}
