package hiiragi283.ragium.api.item.alchemy

import hiiragi283.ragium.api.item.createItemStack
import hiiragi283.ragium.api.util.Ior
import hiiragi283.ragium.api.util.toIor
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponents
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.level.ItemLike
import java.util.Optional
import kotlin.jvm.optionals.getOrNull

object HTPotionHelper {
    @JvmStatic
    fun isEmpty(contents: PotionContents): Boolean = contents.allEffects.none()

    @JvmStatic
    fun contents(potion: Holder<Potion>): PotionContents = PotionContents(potion)

    @JvmStatic
    fun contents(vararg instances: HTMobEffectInstance): PotionContents = contents(listOf(*instances))

    @JvmStatic
    fun contents(instances: List<HTMobEffectInstance>): PotionContents = contents(null, null, instances)

    @JvmStatic
    fun contents(potion: Holder<Potion>?, customColor: Int?, instances: List<HTMobEffectInstance>): PotionContents = PotionContents(
        Optional.ofNullable(potion),
        Optional.ofNullable(customColor),
        instances.map(HTMobEffectInstance::toMutable),
    )

    @JvmStatic
    fun contents(potion: Holder<Potion>?, customColor: Int?, vararg instances: HTMobEffectInstance): PotionContents = PotionContents(
        Optional.ofNullable(potion),
        Optional.ofNullable(customColor),
        instances.map(HTMobEffectInstance::toMutable),
    )

    //    Ior    //

    @JvmStatic
    fun toContents(ior: Ior<Holder<Potion>, List<HTMobEffectInstance>>): PotionContents = ior.fold(
        ::PotionContents,
        { effects: List<HTMobEffectInstance> -> contents(effects) },
        { potion: Holder<Potion>, effects: List<HTMobEffectInstance> -> contents(potion, null, effects) },
    )

    @JvmStatic
    fun toIor(contents: PotionContents): Ior<Holder<Potion>, List<HTMobEffectInstance>> =
        (contents.potion().getOrNull() to contents.customEffects().map(::HTMobEffectInstance)).toIor() ?: Ior.Right(listOf())

    //    ItemStack    //

    @JvmStatic
    fun createPotion(item: ItemLike, potion: Holder<Potion>, count: Int = 1): ItemStack = createPotion(item, contents(potion), count)

    @JvmStatic
    fun createPotion(item: ItemLike, instances: List<HTMobEffectInstance>, count: Int = 1): ItemStack =
        createPotion(item, contents(instances), count)

    @JvmStatic
    fun createPotion(item: ItemLike, contents: PotionContents, count: Int = 1): ItemStack =
        createItemStack(item, DataComponents.POTION_CONTENTS, contents, count)
}
