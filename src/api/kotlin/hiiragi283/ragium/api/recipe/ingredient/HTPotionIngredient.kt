package hiiragi283.ragium.api.recipe.ingredient

import hiiragi283.ragium.api.item.alchemy.HTPotionContents
import hiiragi283.ragium.api.item.alchemy.HTPotionHelper
import hiiragi283.ragium.api.registry.builtInRegistryHolder
import hiiragi283.ragium.api.serialization.codec.MapBiCodec
import hiiragi283.ragium.api.serialization.codec.VanillaBiCodecs
import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.crafting.ICustomIngredient
import net.neoforged.neoforge.common.crafting.IngredientType
import java.util.stream.Stream

/**
 * @see net.neoforged.neoforge.common.crafting.DataComponentIngredient
 */
data class HTPotionIngredient(val items: HolderSet<Item>, val contents: HTPotionContents) : ICustomIngredient {
    companion object {
        @JvmField
        val CODEC: MapBiCodec<RegistryFriendlyByteBuf, HTPotionIngredient> = MapBiCodec.composite(
            VanillaBiCodecs.holderSet(Registries.ITEM).fieldOf("items").forGetter(HTPotionIngredient::items),
            HTPotionContents.CODEC.fieldOf("contents").forGetter(HTPotionIngredient::contents),
            ::HTPotionIngredient,
        )

        @JvmStatic
        fun of(items: Collection<ItemLike>, contents: HTPotionContents): HTPotionIngredient = HTPotionIngredient(
            HolderSet.direct(ItemLike::builtInRegistryHolder, items),
            contents,
        )
    }

    private val stacks: List<ItemStack> = items
        .map { holder: Holder<Item> -> HTPotionHelper.createPotion(holder.value(), contents) }

    override fun test(stack: ItemStack): Boolean =
        stack.`is`(items) && contents.isOf(stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY))

    override fun getItems(): Stream<ItemStack> = stacks.stream()

    override fun isSimple(): Boolean = false

    override fun getType(): IngredientType<*> = RagiumIngredientTypes.POTION.get()
}
