package hiiragi283.ragium.common.util

import hiiragi283.ragium.api.item.createItemStack
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponents
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.level.ItemLike
import java.util.Optional

object HTPotionHelper {
    @JvmStatic
    fun content(potion: Holder<Potion>): PotionContents = PotionContents(potion)

    @JvmStatic
    inline fun content(builderAction: MutableList<MobEffectInstance>.() -> Unit): PotionContents = PotionContents(
        Optional.empty(),
        Optional.empty(),
        buildList(builderAction),
    )

    //    ItemStack    //

    @JvmStatic
    fun createPotion(item: ItemLike, potion: Holder<Potion>, count: Int = 1): ItemStack = createPotion(item, content(potion), count)

    @JvmStatic
    fun createPotion(item: ItemLike, builderAction: MutableList<MobEffectInstance>.() -> Unit, count: Int = 1): ItemStack =
        createPotion(item, content(builderAction), count)

    @JvmStatic
    fun createPotion(item: ItemLike, contents: PotionContents, count: Int = 1): ItemStack =
        createItemStack(item, DataComponents.POTION_CONTENTS, contents, count)
}
