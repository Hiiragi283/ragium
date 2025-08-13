package hiiragi283.ragium.api.recipe.result

import com.mojang.datafixers.util.Either
import hiiragi283.ragium.api.data.BiCodec
import hiiragi283.ragium.api.data.BiCodecs
import net.minecraft.core.Holder
import net.minecraft.core.Registry
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

class HTItemResult(entry: Either<ResourceLocation, TagKey<Item>>, amount: Int, components: DataComponentPatch) :
    HTRecipeResult<Item, ItemStack>(
        entry,
        amount,
        components,
    ) {
    companion object {
        @JvmField
        val CODEC: BiCodec<RegistryFriendlyByteBuf, HTItemResult> = BiCodec.composite(
            BiCodecs.idOrTag(Registries.ITEM).fieldOf("id"),
            HTItemResult::entry,
            BiCodec.intRange(1, 99).optionalOrElseField("count", 1),
            HTItemResult::amount,
            BiCodecs.COMPONENT_PATCH.optionalFieldOf("components", DataComponentPatch.EMPTY),
            HTItemResult::components,
            ::HTItemResult,
        )
    }

    override val registry: Registry<Item> get() = BuiltInRegistries.ITEM

    override fun createStack(holder: Holder<Item>, amount: Int, components: DataComponentPatch): ItemStack =
        ItemStack(holder, amount, components)
}
