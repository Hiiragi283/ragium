package hiiragi283.lib.recipe.ingredient

import com.mojang.serialization.MapCodec
import hiiragi283.lib.HTConstants
import hiiragi283.lib.recipe.display.HTDisplayableContent
import hiiragi283.lib.util.fold
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.display.SlotDisplay
import java.util.Optional
import kotlin.jvm.optionals.getOrDefault

/**
 * 消費されない[Item]向けの[HTIngredient]の実装クラスです。
 *
 * @author Hiiragi Tsubasa
 * @since 26.1.8
 */
@JvmRecord
data class HTItemCatalyst(val content: Optional<Ingredient>) :
    HTIngredient<Item, ItemInstance>,
    HTDisplayableContent {
    companion object {
        @JvmField
        val EMPTY = HTItemCatalyst(Optional.empty())

        @JvmField
        val CODEC: MapCodec<HTItemCatalyst> = Ingredient.CODEC
            .optionalFieldOf(HTConstants.CATALYST)
            .xmap(::HTItemCatalyst, HTItemCatalyst::content)

        @JvmField
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, HTItemCatalyst> =
            Ingredient.OPTIONAL_CONTENTS_STREAM_CODEC.map(::HTItemCatalyst, HTItemCatalyst::content)
    }

    constructor(ingredient: Ingredient) : this(Optional.of(ingredient))

    override fun test(instance: ItemInstance): Boolean = testOnlyType(instance)

    override fun testOnlyType(instance: ItemInstance): Boolean =
        content.fold({ HTIngredientHelper.isEmpty(instance) }, { it.test(HTIngredientHelper.unwrap(instance)) })

    override fun getMatchingStack(instance: ItemInstance): ItemInstance = ItemStack.EMPTY

    override fun display(): SlotDisplay = content.map(Ingredient::display).getOrDefault(SlotDisplay.Empty.INSTANCE)
}
