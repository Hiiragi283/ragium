package hiiragi283.ragium.common.block.entity.storage

import hiiragi283.core.api.render.area.HTAreaDefinition
import hiiragi283.core.api.serialization.codec.VanillaBiCodecs
import hiiragi283.core.api.serialization.value.HTValueInput
import hiiragi283.core.api.serialization.value.HTValueOutput
import hiiragi283.core.api.serialization.value.read
import hiiragi283.core.api.serialization.value.write
import hiiragi283.core.api.storage.HTHandlerProvider
import hiiragi283.core.client.render.area.HTAreaRendererManager
import hiiragi283.core.common.block.entity.HTExtendedBlockEntity
import hiiragi283.core.common.capability.HTEnergyCapabilities
import hiiragi283.core.common.capability.HTFluidCapabilities
import hiiragi283.core.common.capability.HTItemCapabilities
import hiiragi283.ragium.api.RagiumConst
import hiiragi283.ragium.api.block.entity.HTTargetedBlockEntity
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.GlobalPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.capabilities.BlockCapabilityCache
import net.neoforged.neoforge.energy.IEnergyStorage
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.items.IItemHandler
import java.util.*

typealias CacheMap<T> = MutableMap<Direction?, BlockCapabilityCache<T, Direction?>>

class HTResonantInterfaceBlockEntity(pos: BlockPos, state: BlockState) :
    HTExtendedBlockEntity(RagiumBlockEntityTypes.RESONANT_INTERFACE, pos, state),
    HTAreaDefinition,
    HTHandlerProvider,
    HTTargetedBlockEntity {
    private val itemCache: CacheMap<IItemHandler> = EnumMap(Direction::class.java)
    private val fluidCache: CacheMap<IFluidHandler> = EnumMap(Direction::class.java)
    private val energyCache: CacheMap<IEnergyStorage> = EnumMap(Direction::class.java)

    override fun onRemove(level: Level, pos: BlockPos) {
        super.onRemove(level, pos)
        HTAreaRendererManager.removeArea(this)
    }

    //    HTAreaDefinition    //

    override fun getArea(): Set<BlockPos> = setOfNotNull(location)

    override fun getSource(): GlobalPos? {
        val level: Level = level ?: return null
        return GlobalPos(level.dimension(), blockPos)
    }

    override fun getColor(): Int = -1

    //    HTHandlerProvider    //

    override fun getItemHandler(direction: Direction?): IItemHandler? {
        val serverLevel: ServerLevel = getServerLevel() ?: return null
        val pos: BlockPos = location ?: return null
        return itemCache.computeIfAbsent(direction) { HTItemCapabilities.createCache(serverLevel, pos, it) }.capability
    }

    override fun getFluidHandler(direction: Direction?): IFluidHandler? {
        val serverLevel: ServerLevel = getServerLevel() ?: return null
        val pos: BlockPos = location ?: return null
        return fluidCache.computeIfAbsent(direction) { HTFluidCapabilities.createCache(serverLevel, pos, it) }.capability
    }

    override fun getEnergyStorage(direction: Direction?): IEnergyStorage? {
        val serverLevel: ServerLevel = getServerLevel() ?: return null
        val pos: BlockPos = location ?: return null
        return energyCache.computeIfAbsent(direction) { HTEnergyCapabilities.createCache(serverLevel, pos, it) }.capability
    }

    //    HTTargetedBlockEntity    //

    private var location: BlockPos? = null

    override fun writeValue(output: HTValueOutput) {
        super.writeValue(output)
        output.write(RagiumConst.LOCATION, VanillaBiCodecs.BLOCK_POS, location)
    }

    override fun readValue(input: HTValueInput) {
        super.readValue(input)
        location = input.read(RagiumConst.LOCATION, VanillaBiCodecs.BLOCK_POS)
    }

    override fun initReducedUpdateTag(output: HTValueOutput) {
        super.initReducedUpdateTag(output)
        output.write(RagiumConst.LOCATION, VanillaBiCodecs.BLOCK_POS, location)
    }

    override fun handleUpdateTag(input: HTValueInput) {
        super.handleUpdateTag(input)
        val oldLocation: BlockPos? = this.location
        val location: BlockPos? = input.read(RagiumConst.LOCATION, VanillaBiCodecs.BLOCK_POS)
        this.location = location
        if (oldLocation != null) {
            level
                ?.dimension()
                ?.let { GlobalPos(it, oldLocation) }
                ?.let(HTAreaRendererManager::removeArea)
        }
        if (location != null) {
            HTAreaRendererManager.addArea(this)
        }
    }

    override fun updateTarget(pos: GlobalPos) {
        if (level?.dimension() != pos.dimension()) return
        location = pos.pos()
        itemCache.clear()
        fluidCache.clear()
        energyCache.clear()
    }
}
