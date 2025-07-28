package hiiragi283.ragium

import com.almostreliable.unified.api.AlmostUnified
import com.google.common.collect.Multimap
import com.google.common.collect.Table
import com.mojang.authlib.GameProfile
import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.addon.RagiumAddon
import hiiragi283.ragium.api.extension.asItemHolder
import hiiragi283.ragium.api.extension.createItemStack
import hiiragi283.ragium.api.inventory.HTMenuDefinition
import hiiragi283.ragium.api.item.HTFoodBuilder
import hiiragi283.ragium.api.recipe.HTBlockInteractingRecipe
import hiiragi283.ragium.api.recipe.HTItemOutput
import hiiragi283.ragium.api.recipe.HTTransmuteRecipe
import hiiragi283.ragium.api.recipe.HTUniversalRecipe
import hiiragi283.ragium.api.storage.HTStorageIO
import hiiragi283.ragium.api.storage.energy.HTEnergyNetworkManager
import hiiragi283.ragium.api.util.HTMultiMap
import hiiragi283.ragium.api.util.HTTable
import hiiragi283.ragium.api.util.RagiumConst
import hiiragi283.ragium.common.recipe.HTInfusingRecipe
import hiiragi283.ragium.common.storage.energy.HTEnergyNetworkManagerImpl
import hiiragi283.ragium.common.storage.energy.HTLimitedEnergyStorage
import hiiragi283.ragium.setup.RagiumItems
import hiiragi283.ragium.setup.RagiumRecipeSerializers
import hiiragi283.ragium.setup.RagiumRecipeTypes
import hiiragi283.ragium.util.HTAddonCollector
import hiiragi283.ragium.util.HTWrappedMultiMap
import hiiragi283.ragium.util.HTWrappedTable
import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponents
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.MinecraftServer
import net.minecraft.tags.TagKey
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.inventory.SimpleContainerData
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.fml.LogicalSide
import net.neoforged.fml.ModList
import net.neoforged.fml.util.thread.EffectiveSide
import net.neoforged.neoforge.energy.IEnergyStorage
import net.neoforged.neoforge.items.ItemStackHandler
import net.neoforged.neoforge.server.ServerLifecycleHooks
import org.slf4j.Logger
import java.util.UUID

class InternalRagiumAPI : RagiumAPI {
    companion object {
        @JvmStatic
        private val LOGGER: Logger = LogUtils.getLogger()
    }

    //    Addon    //

    private lateinit var addonCache: List<RagiumAddon>

    override fun getAddons(): List<RagiumAddon> {
        if (!::addonCache.isInitialized) {
            LOGGER.info("Collecting addons for Ragium...")
            addonCache = HTAddonCollector
                .collectInstances<RagiumAddon>()
                .onEach { addon: RagiumAddon ->
                    LOGGER.info("Loaded addon from ${addon::class.qualifiedName}!")
                }
        }
        return addonCache
    }

    //    Item    //

    override fun createSoda(instances: List<MobEffectInstance>, count: Int): ItemStack =
        createItemStack(RagiumItems.ICE_CREAM_SODA, count) {
            set(DataComponents.FOOD, HTFoodBuilder.create { instances.forEach(this::addEffect) })
        }

    //    Server    //

    override fun getRandomGameProfile(): GameProfile = GameProfile(UUID.randomUUID(), "[${RagiumAPI.MOD_NAME}]")

    override fun getCurrentServer(): MinecraftServer? = ServerLifecycleHooks.getCurrentServer()

    override fun getCurrentSide(): LogicalSide = EffectiveSide.get()

    override fun getEnergyNetworkManager(): HTEnergyNetworkManager = HTEnergyNetworkManagerImpl

    //    Platform    //

    override fun <K : Any, V : Any> createMultiMap(multimap: Multimap<K, V>): HTMultiMap.Mutable<K, V> = HTWrappedMultiMap.Mutable(multimap)

    override fun <R : Any, C : Any, V : Any> createTable(table: Table<R, C, V>): HTTable.Mutable<R, C, V> = HTWrappedTable.Mutable(table)

    override fun wrapEnergyStorage(storageIO: HTStorageIO, storage: IEnergyStorage): IEnergyStorage =
        HTLimitedEnergyStorage(storageIO, storage)

    override fun getBlockInteractingRecipeType(): RecipeType<HTBlockInteractingRecipe> = RagiumRecipeTypes.BLOCK_INTERACTING.get()

    override fun getTransmuteRecipeSerializer(): RecipeSerializer<HTTransmuteRecipe> = RagiumRecipeSerializers.TRANSMUTE.get()

    override fun createInfusingRecipe(ingredient: Ingredient, output: HTItemOutput, cost: Float): HTUniversalRecipe =
        HTInfusingRecipe(ingredient, output, cost)

    override fun createEmptyMenuDefinition(size: Int): HTMenuDefinition = HTMenuDefinition(
        ItemStackHandler(size),
        ItemStackHandler(4),
        SimpleContainerData(2),
    )

    override fun unifyItemFromId(holder: Holder<Item>, id: ResourceLocation): Holder<Item> {
        if (ModList.get().isLoaded(RagiumConst.ALMOST)) {
            return AlmostUnified.INSTANCE.getVariantItemTarget(holder.value())?.asItemHolder() ?: holder
        }
        return holder
    }

    override fun unifyItemFromTag(holder: Holder<Item>, tagKey: TagKey<Item>): Holder<Item> {
        if (ModList.get().isLoaded(RagiumConst.ALMOST)) {
            return AlmostUnified.INSTANCE.getTagTargetItem(tagKey)?.asItemHolder() ?: holder
        }
        return holder
    }
}
