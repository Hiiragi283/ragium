package hiiragi283.ragium.api.recipe.result

import com.mojang.serialization.DataResult
import hiiragi283.ragium.api.data.BiCodec
import hiiragi283.ragium.api.data.BiCodecs
import hiiragi283.ragium.api.extension.idOrThrow
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.EnchantedBookItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.EnchantmentInstance

class HTEnchantedBookResult(val holder: Holder<Enchantment>, val level: Int) : HTRecipeResult<ItemStack> {
    companion object {
        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, HTEnchantedBookResult> = BiCodec.composite(
            BiCodecs.holder(Registries.ENCHANTMENT).fieldOf("enchantment"),
            HTEnchantedBookResult::holder,
            BiCodecs.POSITIVE_INT.fieldOf("level"),
            HTEnchantedBookResult::level,
            ::HTEnchantedBookResult,
        )
    }

    override val id: ResourceLocation = holder.idOrThrow

    override fun getStackResult(provider: HolderLookup.Provider?): DataResult<ItemStack> = DataResult.success(createBook())

    override fun getOrEmpty(provider: HolderLookup.Provider?): ItemStack = createBook()

    private fun createBook(): ItemStack = EnchantmentInstance(holder, level)
        .let(EnchantedBookItem::createForEnchantment)
}
