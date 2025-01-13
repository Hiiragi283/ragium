package hiiragi283.ragium.api

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.content.HTFluidContent
import hiiragi283.ragium.api.machine.HTMachineRegistry
import hiiragi283.ragium.api.material.HTMaterialRegistry
import net.minecraft.core.Holder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.material.Fluid
import org.slf4j.Logger

/**
 * RagiumのAPI
 */
@Suppress("DEPRECATION")
interface RagiumAPI {
    companion object {
        private val logger: Logger = LogUtils.getLogger()

        const val MOD_ID = "ragium"
        const val MOD_NAME = "Ragium"

        /**
         * 名前空間が`ragium`となる[Identifier]を返します。
         */
        @JvmStatic
        fun id(path: String): ResourceLocation = ResourceLocation.fromNamespaceAndPath(MOD_ID, path)

        /**
         * [RagiumAPI]の単一のインスタンスを返します。
         */
        @JvmStatic
        fun getInstance(): RagiumAPI = TODO()

        /**
         * [RagiumPlugin]の一覧です。
         */
        @JvmStatic
        val plugins: List<RagiumPlugin> by lazy {
            logger.info("=== Loaded Ragium Plugins ===")
            buildList<RagiumPlugin> {}
                .sortedWith(compareBy(RagiumPlugin::priority).thenBy { it::class.java.canonicalName })
                .filter(RagiumPlugin::shouldLoad)
                .onEach { plugin: RagiumPlugin ->
                    logger.info("- Priority : ${plugin.priority} ... ${plugin.javaClass.canonicalName}")
                }.apply { logger.info("=============================") }
        }
    }

    /**
     * 機械レジストリのインスタンスです。
     */
    val machineRegistry: HTMachineRegistry

    /**
     * 素材レジストリのインスタンスです。
     */
    val materialRegistry: HTMaterialRegistry

    /**
     * 指定した[content]で満たされた液体キューブの[ItemStack]を返します。
     */
    fun createFilledCube(
        content: HTFluidContent,
        count: Int = 1,
    ): ItemStack = createFilledCube(content.get(), count)

    /**
     * 指定した[fluid]で満たされた液体キューブの[ItemStack]を返します。
     */
    fun createFilledCube(
        fluid: Fluid,
        count: Int = 1,
    ): ItemStack = createFilledCube(fluid.builtInRegistryHolder(), count)

    /**
     * 指定した[entry]で満たされた液体キューブの[ItemStack]を返します。
     */
    fun createFilledCube(
        entry: Holder<Fluid>,
        count: Int = 1,
    ): ItemStack
}
