package hiiragi283.ragium.api.storage.attachments

import hiiragi283.ragium.api.serialization.codec.BiCodec
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import net.minecraft.network.RegistryFriendlyByteBuf
import net.neoforged.neoforge.fluids.FluidStack
import java.util.Optional
import kotlin.jvm.optionals.getOrNull

/**
 * @see mekanism.common.attachments.containers.fluid.AttachedFluids
 */
@JvmRecord
data class HTAttachedFluids(override val containers: List<ImmutableFluidStack?>) :
    HTAttachedContainers<ImmutableFluidStack?, HTAttachedFluids> {
    companion object {
        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, HTAttachedFluids> = ImmutableFluidStack.CODEC
            .toOptional()
            .listOf()
            .xmap(
                { stacks: List<Optional<ImmutableFluidStack>> -> HTAttachedFluids(stacks.map(Optional<ImmutableFluidStack>::getOrNull)) },
                { attached: HTAttachedFluids -> attached.containers.map(Optional<ImmutableFluidStack>::ofNullable) },
            )

        @JvmField
        val EMPTY = HTAttachedFluids(listOf())

        @JvmStatic
        fun create(size: Int): HTAttachedFluids = HTAttachedFluids(List(size) { null })
    }

    override fun create(containers: List<ImmutableFluidStack?>): HTAttachedFluids = HTAttachedFluids(containers)

    override fun equals(other: Any?): Boolean {
        when {
            this === other -> return true
            other !is HTAttachedFluids -> return false
            else -> {
                val otherContainers: List<ImmutableFluidStack?> = other.containers
                return when {
                    containers.size != otherContainers.size -> {
                        false
                    }
                    else -> {
                        for (i: Int in containers.indices) {
                            val matches: Boolean = FluidStack.matches(
                                containers[i]?.unwrap() ?: FluidStack.EMPTY,
                                otherContainers[i]?.unwrap() ?: FluidStack.EMPTY,
                            )
                            if (!matches) return false
                        }
                        true
                    }
                }
            }
        }
    }

    override fun hashCode(): Int {
        var hash = 0
        for (stack: ImmutableFluidStack? in containers) {
            hash = hash * 31 + (stack?.hashCode() ?: 0)
        }
        return hash
    }
}
