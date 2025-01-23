package hiiragi283.ragium.api.extension

import net.neoforged.fml.ModList

//    ModList    //

fun isModLoaded(modid: String): Boolean = ModList.get().isLoaded(modid)
