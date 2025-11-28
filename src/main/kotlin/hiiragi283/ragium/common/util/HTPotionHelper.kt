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
    fun isEmpty(contents: PotionContents): Boolean = contents.allEffects.none()
    
    @JvmStatic
    fun content(potion: Holder<Potion>): PotionContents = PotionContents(potion)

    @JvmStatic
    fun content(instances: List<MobEffectInstance>): PotionContents = PotionContents(
        Optional.empty(),
        Optional.empty(),
        instances,
    )

    //    ItemStack    //

    @JvmStatic
    fun createPotion(item: ItemLike, potion: Holder<Potion>, count: Int = 1): ItemStack = createPotion(item, content(potion), count)

    @JvmStatic
    fun createPotion(item: ItemLike, instances: List<MobEffectInstance>, count: Int = 1): ItemStack =
        createPotion(item, content(instances), count)

    @JvmStatic
    fun createPotion(item: ItemLike, contents: PotionContents, count: Int = 1): ItemStack =
        createItemStack(item, DataComponents.POTION_CONTENTS, contents, count)
}
