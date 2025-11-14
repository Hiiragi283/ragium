package hiiragi283.ragium.api

import com.google.gson.JsonObject
import hiiragi283.ragium.api.addon.RagiumAddon
import hiiragi283.ragium.api.material.HTMaterialDefinition
import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.prefix.HTMaterialPrefix
import hiiragi283.ragium.api.recipe.manager.HTMaterialRecipeManager
import hiiragi283.ragium.api.registry.RegistryKey
import hiiragi283.ragium.api.serialization.value.HTValueInput
import hiiragi283.ragium.api.serialization.value.HTValueOutput
import hiiragi283.ragium.api.storage.energy.HTEnergyBattery
import hiiragi283.ragium.api.storage.item.HTItemHandler
import net.minecraft.client.Minecraft
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.RegistryAccess
import net.minecraft.nbt.CompoundTag
import net.minecraft.resources.ResourceKey
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.level.Level
import net.neoforged.fml.loading.FMLEnvironment
import java.util.UUID
import kotlin.jvm.optionals.getOrNull

interface RagiumPlatform {
    companion object {
        @JvmField
        val INSTANCE: RagiumPlatform = RagiumAPI.getService()
    }

    //    Addon    //

    fun getAddons(): List<RagiumAddon>

    //    Item    //

    fun createSoda(potion: Holder<Potion>, count: Int = 1): ItemStack = createSoda(PotionContents(potion), count)

    fun createSoda(potion: PotionContents, count: Int = 1): ItemStack

    //    Material    //

    fun getMaterialDefinitions(): Map<HTMaterialKey, HTMaterialDefinition>

    fun getAllMaterials(): Set<HTMaterialKey> = getMaterialDefinitions().keys

    fun getMaterialDefinition(key: HTMaterialKey): HTMaterialDefinition = getMaterialDefinitions()[key] ?: HTMaterialDefinition.Empty

    fun getPrefix(name: String): HTMaterialPrefix?

    //    Recipe    //

    fun getMaterialRecipeManager(): HTMaterialRecipeManager

    //    Server    //

    fun getCurrentServer(): MinecraftServer?

    fun getLevel(key: ResourceKey<Level>): ServerLevel? = getCurrentServer()?.getLevel(key)

    fun getRegistryAccess(): RegistryAccess? = getCurrentServer()?.registryAccess() ?: when {
        FMLEnvironment.dist.isClient -> runCatching {
            Minecraft.getInstance().level?.registryAccess()
        }.getOrNull()
        else -> null
    }

    fun <T : Any> getLookup(lookup: HolderLookup.Provider?, registryKey: RegistryKey<T>): HolderLookup.RegistryLookup<T>? =
        (lookup ?: getRegistryAccess())?.lookup(registryKey)?.getOrNull()

    fun <T : Any> getHolder(lookup: HolderLookup.Provider?, key: ResourceKey<T>): Holder<T>? =
        (lookup ?: getRegistryAccess())?.holder(key)?.getOrNull()

    fun getPlayer(uuid: UUID?): ServerPlayer? {
        if (uuid == null) return null
        return getCurrentServer()?.playerList?.getPlayer(uuid)
    }

    fun getUniversalBundle(server: MinecraftServer, color: DyeColor): HTItemHandler

    /**
     * 指定した[level]からエネルギーのネットワークを返します。
     * @return 取得できなかった場合は`null`
     */
    fun getEnergyNetwork(level: Level?): HTEnergyBattery?

    //    Storage    //

    fun createValueInput(lookup: HolderLookup.Provider, jsonObject: JsonObject): HTValueInput

    fun createValueOutput(lookup: HolderLookup.Provider, jsonObject: JsonObject): HTValueOutput

    fun createValueInput(lookup: HolderLookup.Provider, compoundTag: CompoundTag): HTValueInput

    fun createValueOutput(lookup: HolderLookup.Provider, compoundTag: CompoundTag): HTValueOutput
}
