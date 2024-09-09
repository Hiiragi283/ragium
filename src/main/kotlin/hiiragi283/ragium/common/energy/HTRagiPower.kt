package hiiragi283.ragium.common.energy

import hiiragi283.ragium.common.Ragium
import hiiragi283.ragium.common.init.RagiumComponentTypes
import io.netty.buffer.ByteBuf
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup
import net.fabricmc.fabric.api.lookup.v1.item.ItemApiLookup
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.item.ItemStack
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.state.property.EnumProperty
import net.minecraft.util.StringIdentifiable
import net.minecraft.util.function.ValueLists
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import java.util.function.IntFunction

enum class HTRagiPower : StringIdentifiable {
    NONE,
    STEAM,
    KINETIC,
    ELECTRIC,
    ATOMIC,
    ;

    companion object {
        @JvmField
        val PROPERTY: EnumProperty<HTRagiPower> = EnumProperty.of("ragi_power", HTRagiPower::class.java)

        @JvmField
        val CODEC: StringIdentifiable.EnumCodec<HTRagiPower> = StringIdentifiable.createCodec(HTRagiPower::values)

        @JvmField
        val INT_FUNCTION: IntFunction<HTRagiPower> = ValueLists.createIdToValueFunction(
            HTRagiPower::ordinal, entries.toTypedArray(), ValueLists.OutOfBoundsHandling.WRAP
        )

        @JvmField
        val PACKET_CODEC: PacketCodec<ByteBuf, HTRagiPower> = PacketCodecs.indexed(INT_FUNCTION, HTRagiPower::ordinal)

        @JvmField
        val SIDED_LOOKUP: BlockApiLookup<HTRagiPower, Direction?> =
            BlockApiLookup.get(
                Ragium.id("ragi_power_sided"),
                HTRagiPower::class.java,
                Direction::class.java
            )

        @JvmField
        val ITEM_LOOKUP: ItemApiLookup<HTRagiPower, ContainerItemContext> =
            ItemApiLookup.get(
                Ragium.id("ragi_power"),
                HTRagiPower::class.java,
                ContainerItemContext::class.java
            )

        init {
            SIDED_LOOKUP.registerFallback { world: World, pos: BlockPos, state: BlockState, blockEntity: BlockEntity?, context: Direction? ->
                (blockEntity as? HTRagiPowerProvider)
                    ?.getPower(world, pos, state, context)
                    ?: (state.block as? HTRagiPowerProvider)
                        ?.getPower(world, pos, state, context)
            }

            ITEM_LOOKUP.registerFallback { stack: ItemStack, context: ContainerItemContext ->
                stack.get(RagiumComponentTypes.POWER)
            }
        }
    }

    val tier: Int = ordinal

    val back: HTRagiPower
        get() = when (this) {
            NONE -> NONE
            else -> entries[ordinal - 1]
        }

    val next: HTRagiPower
        get() = when (this) {
            ATOMIC -> ATOMIC
            else -> entries[ordinal + 1]
        }

    //    StringIdentifiable    //

    override fun asString(): String = tier.toString()

}