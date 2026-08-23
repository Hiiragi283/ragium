package hiiragi283.lib.recipe.ingredient

import com.mojang.serialization.Codec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.serialization.codec.HTCodecs
import hiiragi283.lib.util.Either
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.util.context.ContextMap
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.display.DisplayContentsFactory
import net.neoforged.neoforge.common.util.NeoForgeExtraCodecs

typealias HTCatalystOrIngredient = Either<Ingredient, HTItemIngredient>

/**
 * [Item]向けの[HTIngredient]の実装クラスです。
 *
 * 参照 : [Mekanism - ItemStackIngredient](https://github.com/mekanism/Mekanism/blob/26.1/src/api/java/mekanism/api/recipes/ingredients/ItemStackIngredient.java)
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
@JvmRecord
data class HTItemIngredient(val unsized: Ingredient, val count: Int) :
    HTIngredient<ItemInstance>,
    HTStackPreview<ItemStack> {
    companion object {
        @JvmField
        val CODEC: Codec<HTItemIngredient> = HTCodecs.record { instance ->
            instance.group(
                NeoForgeExtraCodecs.aliasedFieldOf(Ingredient.CODEC, HTConstants.ITEMS, HTConstants.INGREDIENT).forGetter(HTItemIngredient::unsized),
                HTCodecs.POSITIVE_INT.fieldOf(HTConstants.COUNT).forGetter(HTItemIngredient::count),
            ).apply(instance, ::HTItemIngredient)
        }

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTItemIngredient> = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC,
            HTItemIngredient::unsized,
            ByteBufCodecs.VAR_INT,
            HTItemIngredient::count,
            ::HTItemIngredient,
        )
    }

    override fun test(instance: ItemInstance): Boolean = testOnlyType(instance) && instance.count() >= count

    override fun testOnlyType(instance: ItemInstance): Boolean = HTIngredientHelper.unwrap(instance).let(unsized::test)

    override fun getRequiredAmount(instance: ItemInstance): Int = when (testOnlyType(instance)) {
        true -> count
        false -> 0
    }

    override fun getPreviewStacks(contextMap: ContextMap): List<ItemStack> = unsized
        .display()
        .resolve(contextMap, DisplayContentsFactory.ForStacks { it.copyWithCount(count) })
        .toList()
}
