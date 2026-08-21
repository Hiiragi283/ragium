package hiiragi283.lib.integration.jei

import hiiragi283.lib.resource.toId
import mezz.jei.api.IModPlugin
import net.minecraft.resources.Identifier

/**
 * Hiiragi Seriesで使用される[IModPlugin]の抽象クラスです。
 *
 * 参照 : [Mekanism - MekanismJEI](https://github.com/mekanism/Mekanism/blob/26.1/src/main/java/mekanism/client/recipe_viewer/jei/MekanismJEI.java)
 * @param modId 対象のMOD ID
 * @author Hiiragi Tsubasa
 * @since 26.1.0
 */
abstract class HTJeiPlugin(protected val modId: String) : IModPlugin {
    final override fun getPluginUid(): Identifier = modId.toId("jei_plugin")
}
