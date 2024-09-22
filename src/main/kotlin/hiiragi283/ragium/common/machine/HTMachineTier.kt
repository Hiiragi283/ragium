package hiiragi283.ragium.common.machine

import com.mojang.serialization.Codec
import hiiragi283.ragium.common.Ragium
import hiiragi283.ragium.common.init.RagiumEnergyProviders
import io.netty.buffer.ByteBuf
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.data.client.ModelIds
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.util.Identifier
import net.minecraft.util.StringIdentifiable
import net.minecraft.util.function.ValueLists
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import team.reborn.energy.api.EnergyStorage
import java.util.function.IntFunction

enum class HTMachineTier(val casingTex: Identifier, val base: Block, val condition: (World, BlockPos) -> Boolean = Condition.NONE) :
    StringIdentifiable {
    NONE(Ragium.id("block/ragi_alloy_block"), Blocks.SMOOTH_STONE),
    HEAT(Ragium.id("block/ragi_alloy_block"), Blocks.BRICKS, Condition.HEAT),
    ELECTRIC(Ragium.id("block/ragi_steel_block"), Blocks.POLISHED_BLACKSTONE_BRICKS, Condition.ELECTRIC),
    CHEMICAL(Ragium.id("block/refined_ragi_steel_block"), Blocks.END_STONE_BRICKS, Condition.ELECTRIC),
    // ALCHEMICAL(Ragium.id("block/refined_ragi_steel_block"), Blocks.CRYING_OBSIDIAN),
    ;

    companion object {
        @JvmField
        val CODEC: Codec<HTMachineTier> = StringIdentifiable.createCodec(HTMachineTier::values)

        @JvmField
        val INT_FUNCTION: IntFunction<HTMachineTier> =
            ValueLists.createIdToValueFunction(
                HTMachineTier::ordinal,
                entries.toTypedArray(),
                ValueLists.OutOfBoundsHandling.WRAP,
            )

        @JvmField
        val PACKET_CODEC: PacketCodec<ByteBuf, HTMachineTier> =
            PacketCodecs.indexed(INT_FUNCTION, HTMachineTier::ordinal)
    }

    val baseTex: Identifier = ModelIds.getBlockModelId(base)

    /*fun canProcess(world: World, pos: BlockPos): Boolean = condition
        .or { world1: World, pos1: BlockPos ->
            Direction.entries.any {
                world1.getBlockState(pos1.offset(it)).isOf(RagiumBlocks.CREATIVE_SOURCE)
            }
        }.test(world, pos)*/

    //    StringIdentifiable    //

    override fun asString(): String = name.lowercase()

    //    Condition    //

    private object Condition {
        @JvmField
        val NONE: (World, BlockPos) -> Boolean = { _: World, _: BlockPos -> false }

        @JvmField
        val HEAT: (World, BlockPos) -> Boolean = { world: World, pos: BlockPos ->
            val downPos: BlockPos = pos.down()
            RagiumEnergyProviders.HEAT.find(
                world,
                downPos,
                world.getBlockState(downPos),
                world.getBlockEntity(downPos),
                Direction.UP,
            ) ?: false
        }

        /*@JvmField
        val KINETIC: (World, BlockPos) -> Boolean = { world: World, pos: BlockPos ->
            RagiumEnergyProviders.KINETIC.createCacheOrNull(world, pos)?.find(null) ?: false
            // (world.getBlockEntity(pos) as? HTSingleMachineBlockEntity)?.receivingPower ?: false
        }*/

        @JvmField
        val ELECTRIC: (World, BlockPos) -> Boolean = { world: World, pos: BlockPos ->
            EnergyStorage.SIDED
                .find(world, pos, world.getBlockState(pos), world.getBlockEntity(pos), null)
                ?.amount
                ?.let { it > -32 * 200 }
                ?: false
        }
    }
}
