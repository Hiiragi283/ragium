package hiiragi283.ragium.api.block.entity

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.enchantment.HTEnchantmentEntry
import hiiragi283.ragium.api.enchantment.HTEnchantmentHolder
import hiiragi283.ragium.api.extension.enchLookup
import hiiragi283.ragium.api.registry.HTDeferredBlockEntityType
import hiiragi283.ragium.api.storage.HTStorageIO
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponentMap
import net.minecraft.core.component.DataComponents
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.resources.RegistryOps
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.ItemEnchantments
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.common.util.TriState
import net.neoforged.neoforge.energy.IEnergyStorage

/**
 * エンチャント可能な[HTTickAwareBlockEntity]
 */
abstract class HTMachineBlockEntity(type: HTDeferredBlockEntityType<*>, pos: BlockPos, state: BlockState) :
    HTTickAwareBlockEntity(type, pos, state),
    HTEnchantmentHolder {
    //    Save & Load    //

    override fun writeNbt(nbt: CompoundTag, registryOps: RegistryOps<Tag>) {
        if (!itemEnchantments.isEmpty) {
            ItemEnchantments.CODEC
                .encodeStart(registryOps, itemEnchantments)
                .ifSuccess { nbt.put(ENCH_KEY, it) }
        }
    }

    override fun readNbt(nbt: CompoundTag, registryOps: RegistryOps<Tag>) {
        ItemEnchantments.CODEC
            .parse(registryOps, nbt.get(ENCH_KEY))
            .result()
            .orElse(ItemEnchantments.EMPTY)
            .let(::loadEnchantment)
    }

    override fun applyImplicitComponents(componentInput: DataComponentInput) {
        itemEnchantments = componentInput.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY)
        loadEnchantment(itemEnchantments)
    }

    override fun collectImplicitComponents(components: DataComponentMap.Builder) {
        components.set(DataComponents.ENCHANTMENTS, itemEnchantments)
    }

    //    Ticking    //

    override fun onServerTick(level: Level, pos: BlockPos, state: BlockState): TriState {
        val network: IEnergyStorage = this.network ?: return TriState.FALSE
        return onServerTick(level, pos, state, network)
    }

    /**
     * [IEnergyStorage]を引数に加えた[onServerTick]の拡張メソッド
     */
    protected abstract fun onServerTick(
        level: Level,
        pos: BlockPos,
        state: BlockState,
        network: IEnergyStorage,
    ): TriState

    //    HTEnchantmentHolder    //

    protected var itemEnchantments: ItemEnchantments = ItemEnchantments.EMPTY
        private set

    /**
     * エンチャントが更新された時に呼び出されます。
     */
    protected open fun loadEnchantment(newEnchantments: ItemEnchantments) {
        this.itemEnchantments = newEnchantments
    }

    override fun getEnchLevel(key: ResourceKey<Enchantment>): Int {
        val lookup: HolderLookup.RegistryLookup<Enchantment> = level?.registryAccess()?.enchLookup() ?: return 0
        return lookup.get(key).map(itemEnchantments::getLevel).orElse(0)
    }

    override fun getEnchEntries(): Iterable<HTEnchantmentEntry> = itemEnchantments.entrySet().map(::HTEnchantmentEntry)

    //    HTHandlerBlockEntity    //

    protected var network: IEnergyStorage? = null

    override fun afterLevelInit(level: Level) {
        network = RagiumAPI
            .getInstance()
            .getEnergyNetworkManager()
            .getNetwork(level)
            ?.let(HTStorageIO.INPUT::wrapEnergyStorage)
    }

    override fun getEnergyStorage(direction: Direction?): IEnergyStorage? = network
}
