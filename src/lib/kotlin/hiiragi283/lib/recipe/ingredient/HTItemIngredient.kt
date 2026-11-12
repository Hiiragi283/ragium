package hiiragi283.lib.recipe.ingredient

import com.mojang.serialization.Codec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.recipe.display.HTDisplayableContent
import hiiragi283.lib.recipe.display.HTSizedSlotDisplay
import hiiragi283.lib.serialization.codec.HTCodecs
import net.minecraft.core.TypedInstance
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.display.SlotDisplay
import net.neoforged.neoforge.common.util.NeoForgeExtraCodecs

/**
 * [Item]向けの[HTIngredient]の実装クラスです。
 *
 * 参照 : [Mekanism - ItemStackIngredient](https://github.com/mekanism/Mekanism/blob/26.3/src/api/java/mekanism/api/recipes/ingredients/ItemStackIngredient.java)
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
@JvmRecord
data class HTItemIngredient(val unsized: Ingredient, val count: Int) :
    HTIngredient<Item, ItemInstance>,
    HTDisplayableContent {
    companion object {
        @JvmField
        val CODEC: Codec<HTItemIngredient> = HTCodecs.record { instance ->
            instance.group(
                NeoForgeExtraCodecs.aliasedFieldOf(
                    Ingredient.CODEC,
                    HTConstants.ITEMS,
                    HTConstants.INGREDIENT,
                ).forGetter(HTItemIngredient::unsized),
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

    override fun testOnlyType(instance: TypedInstance<Item>): Boolean = when {
        unsized.isSimple -> unsized.acceptsItem(instance.typeHolder())
        else -> HTIngredientHelper.unwrap(instance).let(unsized::test)
    }

    override fun getMatchingStack(instance: ItemInstance): ItemInstance = when (test(instance)) {
        true -> HTIngredientHelper.copyWithCount(instance, count)
        false -> ItemStack.EMPTY
    }

    override fun display(): SlotDisplay = HTSizedSlotDisplay(unsized.display(), count)
}
