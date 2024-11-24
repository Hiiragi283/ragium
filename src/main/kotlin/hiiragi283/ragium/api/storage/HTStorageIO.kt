package hiiragi283.ragium.api.storage

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant
import net.fabricmc.fabric.api.transfer.v1.fluid.base.SingleFluidStorage
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant
import net.fabricmc.fabric.api.transfer.v1.item.base.SingleItemStorage
import net.fabricmc.fabric.api.transfer.v1.storage.Storage
import net.fabricmc.fabric.api.transfer.v1.storage.base.FilteringStorage

enum class HTStorageIO(val canInsert: Boolean, val canExtract: Boolean) {
    INPUT(true, false),
    OUTPUT(false, true),
    GENERIC(true, true),
    INTERNAL(false, false),
    ;

    fun <T : Any> wrapStorage(storage: Storage<T>): Storage<T> = when (this) {
        INPUT -> FilteringStorage.insertOnlyOf(storage)
        OUTPUT -> FilteringStorage.extractOnlyOf(storage)
        GENERIC -> storage
        INTERNAL -> FilteringStorage.readOnlyOf(storage)
    }

    fun createItemStorage(capacity: Long): SingleItemStorage = when (this) {
        INPUT -> object : SingleItemStorage() {
            override fun getCapacity(variant: ItemVariant): Long = capacity

            override fun canExtract(variant: ItemVariant): Boolean = false

            override fun supportsExtraction(): Boolean = false
        }
        OUTPUT -> object : SingleItemStorage() {
            override fun getCapacity(variant: ItemVariant): Long = capacity

            override fun canInsert(variant: ItemVariant): Boolean = false

            override fun supportsInsertion(): Boolean = false
        }
        GENERIC -> object : SingleItemStorage() {
            override fun getCapacity(variant: ItemVariant): Long = capacity
        }
        INTERNAL -> object : SingleItemStorage() {
            override fun getCapacity(variant: ItemVariant): Long = capacity

            override fun canExtract(variant: ItemVariant): Boolean = false

            override fun canInsert(variant: ItemVariant): Boolean = false

            override fun supportsExtraction(): Boolean = false

            override fun supportsInsertion(): Boolean = false
        }
    }

    fun createFluidStorage(capacity: Long): SingleFluidStorage = when (this) {
        INPUT ->
            object : SingleFluidStorage() {
                override fun getCapacity(variant: FluidVariant): Long = capacity

                override fun canExtract(variant: FluidVariant): Boolean = false

                override fun supportsExtraction(): Boolean = false
            }
        OUTPUT ->
            object : SingleFluidStorage() {
                override fun getCapacity(variant: FluidVariant): Long = capacity

                override fun canInsert(variant: FluidVariant): Boolean = false

                override fun supportsInsertion(): Boolean = false
            }
        GENERIC ->
            object : SingleFluidStorage() {
                override fun getCapacity(variant: FluidVariant): Long = capacity
            }
        INTERNAL ->
            object : SingleFluidStorage() {
                override fun getCapacity(variant: FluidVariant): Long = capacity

                override fun canExtract(variant: FluidVariant): Boolean = false

                override fun canInsert(variant: FluidVariant): Boolean = false

                override fun supportsExtraction(): Boolean = false

                override fun supportsInsertion(): Boolean = false
            }
    }
}
