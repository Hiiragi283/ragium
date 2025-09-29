package hiiragi283.ragium.impl.data.recipe

import hiiragi283.ragium.api.codec.BiCodec
import hiiragi283.ragium.api.data.recipe.HTResultHelper
import hiiragi283.ragium.api.recipe.result.HTFluidResult
import hiiragi283.ragium.api.recipe.result.HTItemResult
import hiiragi283.ragium.api.registry.HTKeyOrTagHelper
import hiiragi283.ragium.impl.recipe.result.HTFluidResultImpl
import hiiragi283.ragium.impl.recipe.result.HTItemResultImpl
import hiiragi283.ragium.impl.recipe.result.HTRecipeResultBase
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.material.Fluid

class HTResultHelperImpl : HTResultHelper {
    companion object {
        @JvmStatic
        private val ITEM_CODEC: BiCodec<RegistryFriendlyByteBuf, HTItemResult> = HTRecipeResultBase
            .createCodec(
                Registries.ITEM,
                BiCodec.intRange(1, 99).optionalOrElseField("count", 1),
                ::HTItemResultImpl,
            ).let(BiCodec.Companion::downCast)

        @JvmStatic
        private val FLUID_CODEC: BiCodec<RegistryFriendlyByteBuf, HTFluidResult> = HTRecipeResultBase
            .createCodec(
                Registries.FLUID,
                BiCodec.INT.fieldOf("amount"),
                ::HTFluidResultImpl,
            ).let(BiCodec.Companion::downCast)
    }

    override fun itemCodec(): BiCodec<RegistryFriendlyByteBuf, HTItemResult> = ITEM_CODEC

    override fun fluidCodec(): BiCodec<RegistryFriendlyByteBuf, HTFluidResult> = FLUID_CODEC

    override fun item(id: ResourceLocation, count: Int, component: DataComponentPatch): HTItemResult =
        HTItemResultImpl(HTKeyOrTagHelper.INSTANCE.create(Registries.ITEM, id), count, component)

    override fun item(tagKey: TagKey<Item>, count: Int): HTItemResult =
        HTItemResultImpl(HTKeyOrTagHelper.INSTANCE.create(tagKey), count, DataComponentPatch.EMPTY)

    override fun fluid(id: ResourceLocation, amount: Int, component: DataComponentPatch): HTFluidResult =
        HTFluidResultImpl(HTKeyOrTagHelper.INSTANCE.create(Registries.FLUID, id), amount, component)

    override fun fluid(tagKey: TagKey<Fluid>, amount: Int): HTFluidResult =
        HTFluidResultImpl(HTKeyOrTagHelper.INSTANCE.create(tagKey), amount, DataComponentPatch.EMPTY)
}
