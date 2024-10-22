package hiiragi283.ragium.api

import hiiragi283.ragium.api.widget.HTFluidWidget
import net.fabricmc.api.EnvType
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.fluid.Fluid

interface RagiumEnvironmentBridge {
    companion object {
        const val KEY = "ragium.bridge"

        fun getInstance(): RagiumEnvironmentBridge =
            FabricLoader.getInstance().getEntrypoints(KEY, RagiumEnvironmentBridge::class.java).first {
                it.environment == FabricLoader.getInstance().environmentType
            }
    }

    val environment: EnvType

    fun createFluidWidget(fluid: Fluid): HTFluidWidget

    fun createFluidWidget(fluid: Fluid, amount: Long): HTFluidWidget
}
