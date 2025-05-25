package hiiragi283.ragium.common.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import hiiragi283.ragium.api.data.interaction.HTBlockAction
import hiiragi283.ragium.api.extension.listOf
import hiiragi283.ragium.api.recipe.HTBlockInteractingRecipe
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import net.minecraft.core.HolderSet
import net.minecraft.core.RegistryCodecs
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState

class HTBlockInteractingRecipeImpl(
    override val ingredient: Ingredient,
    val blocks: HolderSet<Block>,
    override val actions: List<HTBlockAction>,
) : HTBlockInteractingRecipe {
    companion object {
        @JvmField
        val CODEC: MapCodec<HTBlockInteractingRecipeImpl> = RecordCodecBuilder.mapCodec { instance ->
            instance
                .group(
                    Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(HTBlockInteractingRecipeImpl::ingredient),
                    RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("predicate").forGetter(HTBlockInteractingRecipeImpl::blocks),
                    HTBlockAction.CODEC
                        .listOf(1, Int.MAX_VALUE)
                        .fieldOf("actions")
                        .forGetter(HTBlockInteractingRecipeImpl::actions),
                ).apply(instance, ::HTBlockInteractingRecipeImpl)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTBlockInteractingRecipeImpl> = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC,
            HTBlockInteractingRecipeImpl::ingredient,
            ByteBufCodecs.holderSet(Registries.BLOCK),
            HTBlockInteractingRecipeImpl::blocks,
            HTBlockAction.STREAM_CODEC.listOf(),
            HTBlockInteractingRecipeImpl::actions,
            ::HTBlockInteractingRecipeImpl,
        )
    }

    override fun matchState(state: BlockState): Boolean = state.`is`(blocks)

    override fun getSerializer(): RecipeSerializer<*> = RagiumRecipeSerializers.BLOCK_INTERACTING.get()
}
