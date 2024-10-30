package hiiragi283.ragium.api

import hiiragi283.ragium.api.extension.getEnvType
import hiiragi283.ragium.api.fluid.HTMachineFluidStorage
import hiiragi283.ragium.api.widget.HTFluidWidget
import net.fabricmc.api.EnvType
import net.fabricmc.loader.api.FabricLoader

interface RagiumEnvironmentBridge {
    companion object {
        const val KEY = "ragium.bridge"

        fun getInstance(): RagiumEnvironmentBridge =
            FabricLoader.getInstance().getEntrypoints(KEY, RagiumEnvironmentBridge::class.java).first {
                it.environment == getEnvType()
            }
    }

    val environment: EnvType

    fun createFluidWidget(storage: HTMachineFluidStorage, index: Int): HTFluidWidget
}
