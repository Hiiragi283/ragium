package hiiragi283.ragium.api.item.component

import hiiragi283.ragium.api.util.Ior
import hiiragi283.ragium.api.util.toIor
import net.minecraft.core.Holder
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionContents
import kotlin.jvm.optionals.getOrNull

//    PotionContents    //

fun PotionContents.unwrap(): Ior<List<MobEffectInstance>, Holder<Potion>>? =
    (this.customEffects().takeUnless(List<MobEffectInstance>::isEmpty) to this.potion().getOrNull()).toIor()
