package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.block.entity.HTBlockEntityBase
import hiiragi283.ragium.api.data.HTNbtCodecs
import hiiragi283.ragium.api.extension.dropStackAt
import hiiragi283.ragium.api.extension.ifServer
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumBlocks
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryWrapper
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import java.util.*

class HTAutoIlluminatorBlockEntity(pos: BlockPos, state: BlockState) :
    HTBlockEntityBase(RagiumBlockEntityTypes.AUTO_ILLUMINATOR, pos, state) {
    private var placer: UUID? = null
    private val yRange: IntRange
        get() = (world?.bottomY ?: 0)..<pos.y

    override fun writeNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        super.writeNbt(nbt, wrapperLookup)
        HTNbtCodecs.PLACER.writeTo(nbt, placer)
    }

    override fun readNbt(nbt: NbtCompound, wrapperLookup: RegistryWrapper.WrapperLookup) {
        super.readNbt(nbt, wrapperLookup)
        HTNbtCodecs.PLACER.readAndSetNullable(nbt, this::placer)
    }

    override fun onPlaced(
        world: World,
        pos: BlockPos,
        state: BlockState,
        placer: LivingEntity?,
        stack: ItemStack,
    ) {
        this.placer = placer?.uuid
    }

    override val tickRate: Int = 1

    override fun tickSecond(world: World, pos: BlockPos, state: BlockState) {
        val radius: Int = RagiumAPI
            .getInstance()
            .config.utility.autoIlluminatorRadius
        for (x: Int in (pos.x - radius..pos.x + radius)) {
            for (z: Int in (pos.z - radius..pos.z + radius)) {
                for (y: Int in yRange) {
                    val posIn = BlockPos(x, y, z)
                    if (world.isAir(posIn)) {
                        if (!world.isSkyVisible(posIn) && world.getLightLevel(posIn) <= 7) {
                            world.setBlockState(posIn, Blocks.LIGHT.defaultState)
                            return
                        }
                    }
                }
            }
        }
        world.ifServer {
            placer
                ?.let(this::getEntity)
                ?.let {
                    world.breakBlock(pos, false)
                    dropStackAt(it, RagiumBlocks.AUTO_ILLUMINATOR)
                    world.playSound(
                        null,
                        pos,
                        SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP,
                        SoundCategory.PLAYERS,
                        10.0f,
                        0.5f,
                    )
                }
        }
    }
}
