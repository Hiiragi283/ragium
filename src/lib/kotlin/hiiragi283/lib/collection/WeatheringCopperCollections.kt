package hiiragi283.lib.collection

import net.minecraft.world.level.block.WeatheringCopperCollection

fun <T : Any> WeatheringCopperCollection<T>.base(): T = this.weathering().unaffected()
