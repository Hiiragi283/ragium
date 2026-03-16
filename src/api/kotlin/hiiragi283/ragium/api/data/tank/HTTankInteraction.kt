package hiiragi283.ragium.api.data.tank

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import hiiragi283.core.api.function.identity
import hiiragi283.ragium.api.RagiumRegistries
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack

interface HTTankInteraction {
    val amount: Int

    fun canEmptyContainer(container: ItemStack): Boolean

    fun emptyContainer(container: ItemStack): Pair<ItemStack, FluidStack>

    fun canFillContainer(container: ItemStack, fluidStack: FluidStack): Boolean

    fun fillContainer(container: ItemStack, fluidStack: FluidStack): ItemStack

    interface Serializable : HTTankInteraction {
        companion object {
            @JvmField
            val CODEC: Codec<Serializable> =
                RagiumRegistries.TANK_INTERACTION_TYPE.byNameCodec().dispatch(Serializable::type, identity())
        }

        fun type(): MapCodec<out Serializable>
    }
}
