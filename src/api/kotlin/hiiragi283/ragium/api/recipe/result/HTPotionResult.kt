package hiiragi283.ragium.api.recipe.result

import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.data.BiCodec
import hiiragi283.ragium.api.data.BiCodecs
import hiiragi283.ragium.api.extension.createPotionStack
import hiiragi283.ragium.api.extension.idOrThrow
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.alchemy.Potion

class HTPotionResult(val potion: Holder<Potion>) : HTRecipeResult<ItemStack> {
    companion object {
        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, HTPotionResult> =
            BiCodecs.holder(Registries.POTION).xmap(::HTPotionResult, HTPotionResult::potion)
    }

    override val id: ResourceLocation = potion.idOrThrow

    override fun getStackResult(provider: HolderLookup.Provider?): DataResult<ItemStack> = DataResult.success(createPotion())

    override fun getOrEmpty(provider: HolderLookup.Provider?): ItemStack = createPotion()

    private fun createPotion(): ItemStack = createPotionStack(potion)
}
