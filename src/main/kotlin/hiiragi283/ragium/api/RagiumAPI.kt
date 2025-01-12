package hiiragi283.ragium.api

import hiiragi283.ragium.api.content.HTFluidContent
import hiiragi283.ragium.api.extension.collectEntrypoints
import hiiragi283.ragium.api.extension.isClientEnv
import hiiragi283.ragium.api.machine.HTMachineKey
import hiiragi283.ragium.api.machine.HTMachineRegistry
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.material.HTMaterialRegistry
import hiiragi283.ragium.api.util.DelegatedLogger
import hiiragi283.ragium.common.advancement.HTDrankFluidCriterion
import hiiragi283.ragium.common.advancement.HTInteractMachineCriterion
import hiiragi283.ragium.common.internal.InternalRagiumAPI
import net.minecraft.advancement.AdvancementCriterion
import net.minecraft.fluid.Fluid
import net.minecraft.item.ItemStack
import net.minecraft.registry.Registries
import net.minecraft.registry.entry.RegistryEntry
import net.minecraft.registry.entry.RegistryEntryList
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier
import org.slf4j.Logger

/**
 * RagiumのAPI
 */
@Suppress("DEPRECATION")
interface RagiumAPI {
    companion object {
        private val logger: Logger by DelegatedLogger()

        const val MOD_ID = "ragium"
        const val MOD_NAME = "Ragium"

        /**
         * 名前空間が`ragium`となる[Identifier]を返します。
         */
        @JvmStatic
        fun id(path: String): Identifier = Identifier.of(MOD_ID, path)

        /**
         * [RagiumAPI]の単一のインスタンスを返します。
         */
        @JvmStatic
        fun getInstance(): RagiumAPI = InternalRagiumAPI

        /**
         * [RagiumPlugin]の一覧です。
         */
        @JvmStatic
        val plugins: List<RagiumPlugin> by lazy {
            logger.info("=== Loaded Ragium Plugins ===")
            buildList {
                addAll(collectEntrypoints<RagiumPlugin>(RagiumPlugin.SERVER_KEY))
                if (isClientEnv()) {
                    addAll(collectEntrypoints<RagiumPlugin>(RagiumPlugin.CLIENT_KEY))
                }
            }.sortedWith(compareBy(RagiumPlugin::priority).thenBy { it::class.java.canonicalName })
                .filter(RagiumPlugin::shouldLoad)
                .onEach { plugin: RagiumPlugin ->
                    logger.info("- Priority : ${plugin.priority} ... ${plugin.javaClass.canonicalName}")
                }.apply { logger.info("=============================") }
        }
    }

    /**
     * Ragiumのコンフィグです。
     */
    val config: RagiumConfig

    /**
     * 機械レジストリのインスタンスです。
     */
    val machineRegistry: HTMachineRegistry

    /**
     * 素材レジストリのインスタンスです。
     */
    val materialRegistry: HTMaterialRegistry

    /**
     * 機械ブロックを右クリックした時に呼び出される進捗の条件を返します。
     * @param key 対象の機械
     * @param minTier 条件を満たしうる最低の[HTMachineTier]
     */
    fun createInteractMachineCriterion(
        key: HTMachineKey,
        minTier: HTMachineTier,
    ): AdvancementCriterion<HTInteractMachineCriterion.Condition>

    /**
     * 液体キューブを飲んだ時に呼び出される進捗の条件を返します。
     * @param fluid 条件に一致する液体
     */
    fun createFluidDrinkCriterion(fluid: Fluid): AdvancementCriterion<HTDrankFluidCriterion.Condition> =
        createFluidDrinkCriterion(RegistryEntryList.of(fluid.registryEntry))

    /**
     * 液体キューブを飲んだ時に呼び出される進捗の条件を返します。
     * @param tagKey 条件に一致する液体のタグ
     */
    fun createFluidDrinkCriterion(tagKey: TagKey<Fluid>): AdvancementCriterion<HTDrankFluidCriterion.Condition> =
        createFluidDrinkCriterion(Registries.FLUID.getOrCreateEntryList(tagKey))

    /**
     * 液体キューブを飲んだ時に呼び出される進捗の条件を返します。
     * @param entryList 条件に一致する液体の一覧
     */
    fun createFluidDrinkCriterion(entryList: RegistryEntryList<Fluid>): AdvancementCriterion<HTDrankFluidCriterion.Condition>

    /**
     * 指定した[content]で満たされた液体キューブの[ItemStack]を返します。
     */
    fun createFilledCube(content: HTFluidContent, count: Int = 1): ItemStack = createFilledCube(content.get(), count)

    /**
     * 指定した[fluid]で満たされた液体キューブの[ItemStack]を返します。
     */
    fun createFilledCube(fluid: Fluid, count: Int = 1): ItemStack = createFilledCube(fluid.registryEntry, count)

    /**
     * 指定した[entry]で満たされた液体キューブの[ItemStack]を返します。
     */
    fun createFilledCube(entry: RegistryEntry<Fluid>, count: Int = 1): ItemStack
}
