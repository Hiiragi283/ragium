package hiiragi283.ragium.impl.recipe.result

import hiiragi283.ragium.api.recipe.result.HTChemicalResult
import hiiragi283.ragium.api.registry.HTKeyOrTagEntry
import hiiragi283.ragium.api.registry.HTKeyOrTagHelper
import hiiragi283.ragium.api.serialization.codec.BiCodec
import hiiragi283.ragium.api.serialization.codec.BiCodecs
import io.netty.buffer.ByteBuf
import mekanism.api.MekanismAPI
import mekanism.api.chemical.Chemical
import mekanism.api.chemical.ChemicalStack
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.resources.ResourceLocation

class HTChemicalResultImpl(private val entry: HTKeyOrTagEntry<Chemical>, private val amount: Long) : HTChemicalResult {
    companion object {
        @JvmField
        val CODEC: BiCodec<ByteBuf, HTChemicalResultImpl> = BiCodec.composite(
            HTKeyOrTagHelper.INSTANCE
                .codec(MekanismAPI.CHEMICAL_REGISTRY_NAME)
                .fieldOf("id"),
            HTChemicalResultImpl::entry,
            BiCodecs.POSITIVE_LONG.fieldOf("amount"),
            HTChemicalResultImpl::amount,
            ::HTChemicalResultImpl,
        )
    }

    override val id: ResourceLocation = entry.getId()

    override fun getStackResult(provider: HolderLookup.Provider?): Result<ChemicalStack> = entry
        .getFirstHolder(provider)
        .mapCatching { holder: Holder<Chemical> ->
            val stack = ChemicalStack(holder, amount)
            check(!stack.isEmpty) { "Empty chemical stack is not valid for recipe result" }
            stack
        }

    override fun copyWithAmount(amount: Long): HTChemicalResult = HTChemicalResultImpl(this.entry, amount)
}
