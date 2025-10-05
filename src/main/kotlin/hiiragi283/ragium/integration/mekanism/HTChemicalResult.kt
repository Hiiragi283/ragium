package hiiragi283.ragium.integration.mekanism

import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.codec.BiCodec
import hiiragi283.ragium.api.codec.BiCodecs
import hiiragi283.ragium.api.extension.filterNot
import hiiragi283.ragium.api.recipe.result.HTRecipeResult
import hiiragi283.ragium.api.registry.HTKeyOrTagEntry
import hiiragi283.ragium.api.registry.HTKeyOrTagHelper
import io.netty.buffer.ByteBuf
import mekanism.api.MekanismAPI
import mekanism.api.chemical.Chemical
import mekanism.api.chemical.ChemicalStack
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.resources.ResourceLocation

class HTChemicalResult(private val entry: HTKeyOrTagEntry<Chemical>, private val amount: Long) : HTRecipeResult<ChemicalStack> {
    companion object {
        @JvmField
        val CODEC: BiCodec<ByteBuf, HTChemicalResult> = BiCodec.composite(
            HTKeyOrTagHelper.INSTANCE.codec(MekanismAPI.CHEMICAL_REGISTRY_NAME).fieldOf("id"),
            HTChemicalResult::entry,
            BiCodecs.POSITIVE_LONG.fieldOf("amount"),
            HTChemicalResult::amount,
            ::HTChemicalResult,
        )
    }

    override val id: ResourceLocation = entry.getId()

    override fun getStackResult(provider: HolderLookup.Provider?): DataResult<ChemicalStack> = entry
        .getFirstHolder(provider)
        .map { holder: Holder<Chemical> -> ChemicalStack(holder, amount) }
        .filterNot(ChemicalStack::isEmpty, "Empty chemical stack is not valid for recipe result")
}
