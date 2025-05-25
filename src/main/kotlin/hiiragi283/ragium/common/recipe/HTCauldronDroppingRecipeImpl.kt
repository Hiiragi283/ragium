package hiiragi283.ragium.common.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.extension.streamCodec
import hiiragi283.ragium.api.recipe.HTCauldronDroppingRecipe
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.util.ExtraCodecs
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.level.material.Fluid

class HTCauldronDroppingRecipeImpl(
    override val fluid: Fluid,
    override val minLevel: Int,
    override val ingredient: Ingredient,
    private val result: ItemStack,
) : HTCauldronDroppingRecipe {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTCauldronDroppingRecipeImpl> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    BuiltInRegistries.FLUID
                        .byNameCodec()
                        .fieldOf("fluid")
                        .forGetter(HTCauldronDroppingRecipeImpl::fluid),
                    ExtraCodecs.POSITIVE_INT.fieldOf("min_level").forGetter(HTCauldronDroppingRecipeImpl::minLevel),
                    Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(HTCauldronDroppingRecipeImpl::ingredient),
                    ItemStack.STRICT_CODEC.fieldOf("result").forGetter(HTCauldronDroppingRecipeImpl::result),
                ).apply(instance, ::HTCauldronDroppingRecipeImpl)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTCauldronDroppingRecipeImpl> = StreamCodec.composite(
            BuiltInRegistries.FLUID.streamCodec(),
            HTCauldronDroppingRecipeImpl::fluid,
            ByteBufCodecs.VAR_INT,
            HTCauldronDroppingRecipeImpl::minLevel,
            Ingredient.CONTENTS_STREAM_CODEC,
            HTCauldronDroppingRecipeImpl::ingredient,
            ItemStack.STREAM_CODEC,
            HTCauldronDroppingRecipeImpl::result,
            ::HTCauldronDroppingRecipeImpl,
        )
    }

    override fun assemble(input: HTCauldronDroppingRecipe.Input, registries: HolderLookup.Provider): ItemStack = getResultItem(registries)

    override fun getResultItem(registries: HolderLookup.Provider): ItemStack = result.copy()

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.CAULDRON_DROP.get()
}
