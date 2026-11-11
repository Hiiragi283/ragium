package hiiragi283.lib.recipe.result

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.recipe.display.displayOrEmpty
import hiiragi283.lib.serialization.codec.HTCodecs
import hiiragi283.lib.serialization.network.HTStreamCodecs
import hiiragi283.lib.util.Ior
import hiiragi283.lib.util.toPair
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.Identifier
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.display.SlotDisplay
import net.neoforged.neoforge.fluids.FluidStack
import java.util.Optional

/**
 * アイテムまたは液体の完成品を保持するクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.8
 */
@JvmRecord
data class HTItemOrFluidResult(val content: Ior<HTItemResult, HTFluidResult>) : HTRecipeResult<HTItemAndFluidStack> {
    companion object {
        @JvmField
        val MAP_CODEC: MapCodec<HTItemOrFluidResult> = HTCodecs.ior(
            HTItemResult.CODEC.fieldOf(HTConstants.ITEM),
            HTFluidResult.CODEC.fieldOf(HTConstants.FLUID),
        ).xmap(::HTItemOrFluidResult, HTItemOrFluidResult::content)

        @JvmField
        val CODEC: Codec<HTItemOrFluidResult> = Codec.lazyInitialized(MAP_CODEC::codec)

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTItemOrFluidResult> = HTStreamCodecs
            .ior(HTItemResult.STREAM_CODEC, HTFluidResult.STREAM_CODEC)
            .map(::HTItemOrFluidResult, HTItemOrFluidResult::content)
    }

    override fun getId(): Identifier? = content.fold(
        HTItemResult::getId,
        HTFluidResult::getId,
    ) { item: HTItemResult, fluid: HTFluidResult -> item.getId() ?: fluid.getId() }

    override fun create(): HTItemAndFluidStack = content
        .mapLeft(HTItemResult::create)
        .mapRight(HTFluidResult::create)
        .toPair(ItemStack::EMPTY, FluidStack::EMPTY)
        .let(::HTItemAndFluidStack)

    fun itemDisplay(): Optional<SlotDisplay> = content.getLeft().displayOrEmpty()

    fun fluidDisplay(): Optional<SlotDisplay> = content.getRight().displayOrEmpty()
}
