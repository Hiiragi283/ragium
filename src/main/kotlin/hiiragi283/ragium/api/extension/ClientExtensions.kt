package hiiragi283.ragium.api.extension

import hiiragi283.ragium.api.world.HTEnergyNetwork
import net.minecraft.client.Minecraft
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.Level

//    Minecraft    //

fun Minecraft.getEnergyNetworkMap(): Map<ResourceKey<Level>, HTEnergyNetwork> = singleplayerServer?.networkMap ?: mapOf()
