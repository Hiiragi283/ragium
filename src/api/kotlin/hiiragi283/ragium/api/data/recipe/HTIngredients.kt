package hiiragi283.ragium.api.data.recipe

import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponents
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.ItemLike
import net.neoforged.neoforge.common.crafting.DataComponentIngredient

object HTIngredients {
    @JvmStatic
    fun potion(potion: Holder<Potion>, item: ItemLike = Items.POTION, vararg items: ItemLike = arrayOf()): Ingredient =
        potion(PotionContents(potion), item, *items)

    @JvmStatic
    fun potion(potion: PotionContents, item: ItemLike = Items.POTION, vararg items: ItemLike = arrayOf()): Ingredient =
        DataComponentIngredient.of(
            false,
            DataComponents.POTION_CONTENTS,
            potion,
            item,
            *items,
        )
}
