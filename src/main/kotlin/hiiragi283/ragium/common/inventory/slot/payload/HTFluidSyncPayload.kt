package hiiragi283.ragium.common.inventory.slot.payload

import hiiragi283.ragium.api.inventory.container.HTSyncableMenu
import hiiragi283.ragium.api.inventory.slot.payload.HTSyncablePayload
import hiiragi283.ragium.api.stack.ImmutableFluidStack
import hiiragi283.ragium.api.util.wrapOptional
import hiiragi283.ragium.common.inventory.slot.HTFluidSyncSlot
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import java.util.Optional
import kotlin.jvm.optionals.getOrNull

/**
 * @see mekanism.common.network.to_client.container.property.FluidStackPropertyData
 */
@JvmRecord
data class HTFluidSyncPayload(val stack: Optional<ImmutableFluidStack>) : HTSyncablePayload {
    companion object {
        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTFluidSyncPayload> =
            ImmutableFluidStack.OPTIONAL_CODEC.streamCodec
                .map(::HTFluidSyncPayload, HTFluidSyncPayload::stack)
    }

    constructor(stack: ImmutableFluidStack?) : this(stack.wrapOptional())

    override fun type(): StreamCodec<RegistryFriendlyByteBuf, HTFluidSyncPayload> = STREAM_CODEC

    @Suppress("UNCHECKED_CAST")
    override fun setValue(menu: HTSyncableMenu, index: Int) {
        (menu.getTrackedSlot(index) as? HTFluidSyncSlot)?.setStack(this.stack.getOrNull())
    }
}
