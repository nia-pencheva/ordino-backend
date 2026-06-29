package com.ordino.domain.warehouse.warehouse_batches.service;

import java.util.List;

import com.ordino.domain.warehouse.warehouse_batches.model.dto.MarkQuantityUsedRequestDTO;
import com.ordino.domain.warehouse.warehouse_batches.model.dto.WriteOffQuantityRequestDTO;
import com.ordino.domain.warehouse.warehouse_batches.model.dto.batch.WarehouseBatchResponseDTO;
import com.ordino.domain.warehouse.warehouse_batches.model.dto.batches_page.WarehouseBatchesOfProductPageResponseDTO;
import com.ordino.domain.warehouse.warehouse_batches.model.dto.products_page.WarehouseBatchesProductsPageResponseDTO;
import com.ordino.domain.warehouse.warehouse_batches.model.entity.WarehouseBatch;

public interface WarehouseBatchService {
    WarehouseBatchesProductsPageResponseDTO getProducts(Integer page, Integer pageSize);

    WarehouseBatchesOfProductPageResponseDTO getBatchesOfProduct(Long warehouseProductId, Integer page, Integer pageSize);

    WarehouseBatchResponseDTO getBatch(Long warehouseProductId, Long warehouseBatchId);

    String markQuantityUsed(Long id, MarkQuantityUsedRequestDTO dto);

    String writeOffQuantity(Long id, WriteOffQuantityRequestDTO dto);

    List<WarehouseBatch> getExpiringProductBatches(Integer daysBeforeExpiry);
}
