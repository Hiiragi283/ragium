package hiiragi283.ragium.common.recipe

import com.mojang.serialization.Codec
import hiiragi283.ragium.common.block.entity.HTBurningBoxBlockEntity
import hiiragi283.ragium.common.util.getOrNull
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.state.property.EnumProperty
import net.minecraft.util.StringIdentifiable
import net.minecraft.util.function.ValueLists
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import java.util.function.BiPredicate
import java.util.function.IntFunction

enum class HTMachineTier(private val condition: BiPredicate<World, BlockPos> = Condition.NONE) : StringIdentifiable {
    NONE,
    HEAT(Condition.HEAT),
    KINETIC,
    ELECTRIC,
    ATOMIC,
    ;

    companion object {
        @JvmField
        val PROPERTY: EnumProperty<HTMachineTier> = EnumProperty.of("tier", HTMachineTier::class.java)

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

        /*@JvmField
        val SIDED_LOOKUP: BlockApiLookup<HTMachineTier, Direction?> =
            BlockApiLookup.get(
                Ragium.id("ragi_power_sided"),
                HTMachineTier::class.java,
                Direction::class.java
            )

        @JvmField
        val ITEM_LOOKUP: ItemApiLookup<HTMachineTier, ContainerItemContext> =
            ItemApiLookup.get(
                Ragium.id("ragi_power"),
                HTMachineTier::class.java,
                ContainerItemContext::class.java
            )
         */
    }

    val tier: Int = ordinal

    val back: HTMachineTier
        get() =
            when (this) {
                NONE -> NONE
                else -> entries[ordinal - 1]
            }

    val next: HTMachineTier
        get() =
            when (this) {
                ATOMIC -> ATOMIC
                else -> entries[ordinal + 1]
            }

    fun canProcess(world: World, pos: BlockPos): Boolean = condition
        .or { world1: World, pos1: BlockPos -> world1.getBlockState(pos1).getOrNull(PROPERTY) == this }
        .test(world, pos)

    //    StringIdentifiable    //

    override fun asString(): String = tier.toString()

    //    Condition    //

    private object Condition {
        @JvmField
        val NONE: BiPredicate<World, BlockPos> = BiPredicate { _: World, _: BlockPos -> false }

        @JvmField
        val HEAT: BiPredicate<World, BlockPos> =
            BiPredicate { world: World, pos: BlockPos ->
                (world.getBlockEntity(pos.down()) as? HTBurningBoxBlockEntity)?.isBurning ?: false
            }
    }
}
