package hiiragi283.ragium.api.recipe

import com.mojang.serialization.MapCodec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.data.buildDataPatch
import hiiragi283.lib.item.ItemStack
import hiiragi283.lib.item.component.buildItemEnchantments
import hiiragi283.lib.recipe.HTRecipeType
import hiiragi283.lib.recipe.HTSerializableRecipe
import hiiragi283.lib.recipe.base.HTDoubleItemToItemRecipe
import hiiragi283.lib.recipe.base.HTProgressData
import hiiragi283.lib.recipe.base.HTProgressRecipe
import hiiragi283.lib.recipe.ingredient.HTItemIngredient
import hiiragi283.lib.serialization.codec.HTCodecs
import hiiragi283.lib.serialization.network.HTStreamCodecs
import hiiragi283.lib.util.Either
import hiiragi283.lib.util.unwrap
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.ItemEnchantments

@JvmRecord
data class RTEnchantingRecipe(
    val ingredient: HTItemIngredient,
    val result: EnchantmentResult,
    override val progressData: HTProgressData
) : HTDoubleItemToItemRecipe,
    HTProgressRecipe.Simple<RecipeInput>,
    HTSerializableRecipe<RecipeInput> {
    companion object {
        @JvmField
        val BOOK_INGREDIENT = HTItemIngredient(Ingredient.of(Items.BOOK), 1)

        @JvmField
        val CODEC: MapCodec<RTEnchantingRecipe> = HTCodecs.recordMap { instance ->
            instance.group(
                HTItemIngredient.CODEC.fieldOf(HTConstants.INGREDIENT).forGetter(RTEnchantingRecipe::ingredient),
                EnchantmentResult.CODEC.fieldOf(HTConstants.RESULT).forGetter(RTEnchantingRecipe::result),
                HTProgressData.CODEC.forGetter(RTEnchantingRecipe::progressData)
            ).apply(instance, ::RTEnchantingRecipe)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, RTEnchantingRecipe> = StreamCodec.composite(
            HTItemIngredient.STREAM_CODEC,
            RTEnchantingRecipe::ingredient,
            EnchantmentResult.STREAM_CODEC,
            RTEnchantingRecipe::result,
            HTProgressData.STREAM_CODEC,
            RTEnchantingRecipe::progressData,
            ::RTEnchantingRecipe
        )

        @JvmField
        val SERIALIZER: RecipeSerializer<RTEnchantingRecipe> = RecipeSerializer(CODEC, STREAM_CODEC)
    }

    override fun test(first: ItemInstance, second: ItemInstance): Boolean =
        BOOK_INGREDIENT.test(first) && ingredient.test(second)

    override fun apply(first: ItemInstance, second: ItemInstance): ItemStack = result.create()

    override fun getRequiredAmount(first: ItemInstance, second: ItemInstance): Pair<Int, Int> =
        BOOK_INGREDIENT.getRequiredAmount(first) to ingredient.getRequiredAmount(second)

    override fun getSerializer(): RecipeSerializer<RTEnchantingRecipe> = RagiumRecipeSerializers.ENCHANTING

    override fun getType(): HTRecipeType<RTEnchantingRecipe> = RagiumRecipeTypes.ENCHANTING

    //    EnchantmentResult    //

    @JvmRecord
    data class EnchantmentResult(val content: Either<Holder<Enchantment>, ItemEnchantments>) {
        companion object {
            @JvmField
            val CODEC: MapCodec<EnchantmentResult> = HTCodecs.mapEither(
                HTCodecs.holder(Registries.ENCHANTMENT).fieldOf(HTConstants.ID),
                ItemEnchantments.CODEC.fieldOf("enchantments")
            ).xmap(::EnchantmentResult, EnchantmentResult::content)

            @JvmField
            val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, EnchantmentResult> = HTStreamCodecs
                .either(HTStreamCodecs.holder(Registries.ENCHANTMENT), ItemEnchantments.STREAM_CODEC)
                .map(::EnchantmentResult, EnchantmentResult::content)
        }

        fun create(): ItemStack = content
            .mapLeft { holder: Holder<Enchantment> -> buildItemEnchantments { set(holder, holder.value().maxLevel) } }
            .unwrap()
            .let { buildDataPatch { set(DataComponents.STORED_ENCHANTMENTS, it) } }
            .let { ItemStack(Items.ENCHANTED_BOOK, 1, it) }
    }
}
