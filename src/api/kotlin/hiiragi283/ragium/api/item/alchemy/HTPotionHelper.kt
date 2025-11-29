package hiiragi283.ragium.api.item.alchemy

import hiiragi283.ragium.api.item.createItemStack
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponents
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.level.ItemLike
import java.util.Optional

object HTPotionHelper {
    @JvmStatic
    fun isEmpty(contents: PotionContents): Boolean = contents.allEffects.none()

    @JvmStatic
    fun contents(potion: Holder<Potion>): PotionContents = PotionContents(potion)

    @JvmStatic
    fun contents(potion: Holder<Potion>?, customColor: Int?, instances: List<HTMobEffectInstance>): PotionContents = PotionContents(
        Optional.ofNullable(potion),
        Optional.ofNullable(customColor),
        instances.map(HTMobEffectInstance::toMutable),
    )

    //    ItemStack    //

    @JvmStatic
    fun createPotion(item: ItemLike, potion: Holder<Potion>, count: Int = 1): ItemStack = createPotion(item, contents(potion), count)

    @JvmStatic
    fun createPotion(item: ItemLike, contents: HTPotionContents, count: Int = 1): ItemStack =
        createPotion(item, contents.toVanilla(), count)

    @JvmStatic
    fun createPotion(item: ItemLike, contents: PotionContents, count: Int = 1): ItemStack =
        createItemStack(item, DataComponents.POTION_CONTENTS, contents, count)
}
