package hiiragi283.ragium.api.item.component

import com.mojang.datafixers.util.Either
import net.minecraft.core.Holder
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionContents

//    PotionContents    //

fun PotionContents.unwrap(): Either<Holder<Potion>, Iterable<MobEffectInstance>> = this
    .potion()
    .map { Either.left<Holder<Potion>, Iterable<MobEffectInstance>>(it) }
    .orElseGet { Either.right(this.allEffects) }
