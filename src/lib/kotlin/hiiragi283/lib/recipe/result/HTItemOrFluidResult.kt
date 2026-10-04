package hiiragi283.lib.recipe.result

import com.mojang.serialization.MapCodec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.serialization.codec.HTCodecs
import hiiragi283.lib.serialization.network.HTStreamCodecs
import hiiragi283.lib.util.Ior
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.Identifier

/**
 * アイテムまたは液体の完成品を保持するクラスです。
 * @author Hiiragi Tsubasa
 * @since 26.1.8
 */
@JvmRecord
data class HTItemOrFluidResult(val content: Ior<HTItemResult, HTFluidResult>) : HTRecipeResult<HTItemAndFluidStack> {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTItemOrFluidResult> = HTCodecs.ior(
            HTItemResult.CODEC.fieldOf(HTConstants.ITEM_RESULT),
            HTFluidResult.CODEC.fieldOf(HTConstants.FLUID_RESULT)
        ).xmap(::HTItemOrFluidResult, HTItemOrFluidResult::content)

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTItemOrFluidResult> = HTStreamCodecs
            .ior(HTItemResult.STREAM_CODEC, HTFluidResult.STREAM_CODEC)
            .map(::HTItemOrFluidResult, HTItemOrFluidResult::content)
    }

    override fun getId(): Identifier? = content.fold(
        HTItemResult::getId,
        HTFluidResult::getId
    ) { item: HTItemResult, fluid: HTFluidResult -> item.getId() ?: fluid.getId() }

    override fun create(): HTItemAndFluidStack {
        val (item: HTItemResult?, fluid: HTFluidResult?) = content.toPair()
        return HTItemAndFluidStack(item.createOrEmpty(), fluid.createOrEmpty())
    }
}
